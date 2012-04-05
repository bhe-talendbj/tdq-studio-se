// ============================================================================
//
// Copyright (C) 2006-2011 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.dq.analysis.explore;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.dataquality.analysis.ExecutionLanguage;
import org.talend.dataquality.indicators.PatternMatchingIndicator;
import org.talend.dataquality.indicators.columnset.ColumnsetPackage;
import orgomg.cwm.objectmodel.core.Expression;

/**
 * @author scorreia
 * 
 * This class explores the data that matched or did not match a pattern indicator.
 */
public class PatternExplorer extends DataExplorer {

    private String functionReturnValue = "";

    /**
     * DOC scorreia PatternExplorer constructor comment.
     * 
     * @param dbmsLang
     */
    public PatternExplorer() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.analysis.explore.IDataExplorer#getInvalidRowsStatement()
     */
    public String getInvalidRowsStatement() {
        String regexPatternString = dbmsLanguage.getRegexPatternString((PatternMatchingIndicator) this.indicator);
        String columnName = dbmsLanguage.quote(indicator.getAnalyzedElement().getName());
        String regexCmp = dbmsLanguage.regexNotLike(columnName, regexPatternString) + functionReturnValue;
        // add null as invalid rows
        String nullClause = dbmsLanguage.or() + columnName + dbmsLanguage.isNull();
        // mzhao TDQ-4967 add "(" and ")" for regex and null clause.
        String pattCondStr = "(" + regexCmp + nullClause + ")";//$NON-NLS-1$//$NON-NLS-2$ 
        return getRowsStatement(pattCondStr);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.analysis.explore.IDataExplorer#getValidRowsStatement()
     */
    public String getValidRowsStatement() {
        String regexPatternString = dbmsLanguage.getRegexPatternString((PatternMatchingIndicator) this.indicator);
        final String columnName = dbmsLanguage.quote(indicator.getAnalyzedElement().getName());
        String regexCmp = dbmsLanguage.regexLike(columnName, regexPatternString) + functionReturnValue;
        return getRowsStatement(regexCmp);
    }

    public Map<String, String> getQueryMap() {
        Map<String, String> map = new HashMap<String, String>();// MOD zshen feature 12919 adapt to pop-menu for Jave
        // MOD qiongli 2011-3-30,feature 19192,filter this menu and query for ColumnSet AllMatchIndicator
        if (ColumnsetPackage.eINSTANCE.getAllMatchIndicator().isSuperTypeOf(indicator.eClass())) {
            return map;
        }
        // engin on result page
        boolean isSqlEngine = ExecutionLanguage.SQL.equals(this.analysis.getParameters().getExecutionLanguage());
        // MOD gdbu 2011-12-5 TDQ-4087 get function name from sql sentence when use MSSQL
        EList<Expression> instantiatedExpressions = this.indicator.getInstantiatedExpressions();
        if (ConnectionHelper.isMssql((Connection) this.analysis.getContext().getConnection())
                && instantiatedExpressions.size() > 0) {
            Expression expression = instantiatedExpressions.get(0);
            String body = expression.getBody().toUpperCase();
            String functionName = body.split("WHEN").length > 1 ? body.split("WHEN")[1] : "";//$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
            functionReturnValue = functionName.split("\\(").length > 1 ? functionName.split("\\(")[1] : "";
            functionName = functionName.split("\\(").length > 1 ? functionName.split("\\(")[0] : "";//$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
            dbmsLanguage.setFunctionName(functionName);
            functionReturnValue = functionReturnValue.split("\\)").length > 1 ? functionReturnValue.split("\\)")[1] : "";
            functionReturnValue = functionReturnValue.split("THEN").length > 1 ? functionReturnValue.split("THEN")[0] : "";

            map.put(MENU_VIEW_INVALID_VALUES, isSqlEngine ? getComment(MENU_VIEW_INVALID_VALUES) + getInvalidValuesStatement()
                    : null);
            map.put(MENU_VIEW_VALID_VALUES, isSqlEngine ? getComment(MENU_VIEW_VALID_VALUES) + getValidValuesStatement() : null);
            map.put(MENU_VIEW_INVALID_ROWS, isSqlEngine ? getComment(MENU_VIEW_INVALID_ROWS) + getInvalidRowsStatement() : null);
            map.put(MENU_VIEW_VALID_ROWS, isSqlEngine ? getComment(MENU_VIEW_VALID_ROWS) + getValidRowsStatement() : null);
            return map;
        }
        // ~TDQ-4087

        // MOD zshen 10448 Add menus "view invalid values" and "view valid values" on pattern matching indicator
        map.put(MENU_VIEW_INVALID_VALUES, isSqlEngine ? getComment(MENU_VIEW_INVALID_VALUES) + getInvalidValuesStatement() : null);
        map.put(MENU_VIEW_VALID_VALUES, isSqlEngine ? getComment(MENU_VIEW_VALID_VALUES) + getValidValuesStatement() : null);
        // ~10448
        map.put(MENU_VIEW_INVALID_ROWS, isSqlEngine ? getComment(MENU_VIEW_INVALID_ROWS) + getInvalidRowsStatement() : null);
        map.put(MENU_VIEW_VALID_ROWS, isSqlEngine ? getComment(MENU_VIEW_VALID_ROWS) + getValidRowsStatement() : null);

        return map;
    }

    /**
     * 
     * DOC zshen Comment method "getValidValuesStatement".
     * 
     * @return SELECT statement for the invalid Value of select column
     */
    public String getInvalidValuesStatement() {
        String regexPatternString = dbmsLanguage.getRegexPatternString((PatternMatchingIndicator) this.indicator);
        String columnName = dbmsLanguage.quote(indicator.getAnalyzedElement().getName());
        String regexCmp = dbmsLanguage.regexNotLike(columnName, regexPatternString) + functionReturnValue;
        // add null as invalid rows
        String nullClause = dbmsLanguage.or() + columnName + dbmsLanguage.isNull();
        // mzhao TDQ-4967 add "(" and ")" for regex and null clause.
        String pattCondStr = "(" + regexCmp + nullClause + ")";//$NON-NLS-1$//$NON-NLS-2$ 
        return getRowsStatement(pattCondStr);
    }

    /**
     * 
     * DOC zshen Comment method "getValidValuesStatement".
     * 
     * @return SELECT statement for the valid Value of select column
     */
    public String getValidValuesStatement() {
        String regexPatternString = dbmsLanguage.getRegexPatternString((PatternMatchingIndicator) this.indicator);
        final String columnName = dbmsLanguage.quote(indicator.getAnalyzedElement().getName());
        String regexCmp = dbmsLanguage.regexLike(columnName, regexPatternString) + functionReturnValue;
        return getValuesStatement(columnName, regexCmp);
    }

}
