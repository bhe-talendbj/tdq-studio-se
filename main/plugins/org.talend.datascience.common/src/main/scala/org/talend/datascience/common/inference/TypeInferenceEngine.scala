// ============================================================================
//
// Copyright (C) 2006-2015 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.datascience.common.inference

import scala.util.parsing.combinator.RegexParsers
import org.talend.dataquality.semantic.classifier.FieldClassifier
/**
 * Type inference engine to guess if a value belongs to a type according to predefined rules. <br> the infer engine can be extended.
 * @author mzhao
 */
object TypeInferenceEngine extends RegexParsers {
  private[inference] def intnumber: Parser[Int] = """^(\+|-)?\d+$""".r ^^ { _.toInt }
  //matches YYYY-MM-dd see: http://regexlib.com/REDetails.aspx?regexp_id=890
  private[inference] def date: Parser[String] = """(?<Year>(19|20)[0-9][0-9])-(?<Month>0[1-9]|1[0-2])-(?<Day>0[1-9]|[12][0-9]|3[01])""".r ^^ { _.toString() }
  //matches yyyy-MM-dd HH:mm:ss see: http://regexlib.com/REDetails.aspx?regexp_id=798
  private[inference] def datetime: Parser[String] = """^(19[0-9]{2}|[2-9][0-9]{3})-((0(1|3|5|7|8)|10|12)-(0[1-9]|1[0-9]|2[0-9]|3[0-1])|(0(4|6|9)|11)-(0[1-9]|1[0-9]|2[0-9]|30)|(02)-(0[1-9]|1[0-9]|2[0-9]))\x20(0[0-9]|1[0-9]|2[0-3])(:[0-5][0-9]){2}$""".r ^^ { _.toString() } //match dd/MM/YYYY see: http://regexlib.com/REDetails.aspx?regexp_id=250

  private[inference] def dnumber: Parser[Double] = """^[-+]?\d*\.?\d*$""".r ^^ { _.toDouble }
  private[inference] def factor: Parser[Double] = dnumber | "(" ~> expr <~ ")"
  private[inference] def term: Parser[Double] = factor ~ rep("*" ~ factor | "/" ~ factor) ^^ {
    case number ~ list => (number /: list) {
      case (x, "*" ~ y) => x * y
      case (x, "/" ~ y) => x / y
    }
  }
  private[inference] def expr: Parser[Double] = term ~ rep("+" ~ log(term)("Plus term") | "-" ~ log(term)("Minus term")) ^^ {
    case number ~ list => list.foldLeft(number) { // same as before, using alternate name for /:
      case (x, "+" ~ y) => x + y
      case (x, "-" ~ y) => x - y
    }
  }

  private[inference] def apply(input: String): Double = parseAll(expr, input) match {
    case Success(result, _) => result
    case failure: NoSuccess => Double.NaN
  }

  def applyWithoutCalculas(input: String): Double = parseAll(dnumber, input) match {
    case Success(result, _) => result
    case failure: NoSuccess => Double.NaN
  }

  /**
   * Get double value given a string value. Compute the expression if allow calculation parameter is set to true.
   * @author mzhao
   * @since 1.0
   * @param value String value to be converted to double.
   * @param allowCalculas Whether or not an expression need to be evaluated as double (in case of the expression exist)
   * @return converted double value , Doulbe.NaN if the string value cannot be converted as double.
   */
  def getDouble(value: String, allowCalculas: Boolean): Double = {
    var dvalue = Double.NaN
    if (allowCalculas) dvalue = apply(value)
    else dvalue = applyWithoutCalculas(value)

    if (Double.NaN == dvalue) {
      //TODO call elastic search to guess the value, e.g. one point five will give 1.5, 二百五十(in Chinese) will give 250
    }
    dvalue
  }

  /**
   * Whether or not the string value is integer.
   * @since 1.0
   * @author mzhao
   * @param value The string value to be asserted whether it is a integer or not.
   * @return true if it's a integer or false otherwise.
   */
  def isInteger(value: String): Boolean = {
    parseAll(intnumber, value) match {
      case Success(result, _) => true
      case failure: NoSuccess => false
    }
  }

  /**
   * Whether or not the string value is date type.
   * @since 1.0
   * @author mzhao
   * @param value The string value to be asserted whether it is a date type or not.
   * @return true if it's a date type or false otherwise.
   */
  def isDate(value: String): Boolean = {
    var isMatch = parseAll(date, value) match {
      case Success(result, _) => true
      case failure: NoSuccess => false
    }
    if (!isMatch) {
      isMatch = parseAll(datetime, value) match {
        case Success(result, _) => return true
        case failure: NoSuccess => false
      }
    }

    //Call semantic API to infer the data type.
    val typeWithSemantic = FieldClassifier.classify(value)
    val typeWithSemanticArray = typeWithSemantic.split("::");
    //TODO see if these "Data" and "Datetime" literal can be replace by enum name later from semantic API.
    if ("Date".equals(typeWithSemanticArray(0))||"Datetime".equals(typeWithSemanticArray(0))) {
      isMatch = true
    }
    isMatch
  }

}