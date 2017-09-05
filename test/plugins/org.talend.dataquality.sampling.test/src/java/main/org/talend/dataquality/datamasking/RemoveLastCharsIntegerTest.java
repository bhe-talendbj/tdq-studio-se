// ============================================================================
//
// Copyright (C) 2006-2017 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.dataquality.datamasking;

import static org.junit.Assert.*;

import org.junit.Test;
import org.talend.dataquality.datamasking.Functions.RemoveLastCharsInteger;

/**
 * created by jgonzalez on 25 juin 2015 Detailled comment
 *
 */
public class RemoveLastCharsIntegerTest {

    private int input = 666;

    private int output;

    private RemoveLastCharsInteger rlci = new RemoveLastCharsInteger();

    @Test
    public void test() {
        rlci.integerParam = 2;
        output = rlci.generateMaskedRow(input);
        assertEquals(output, 6);
    }

    @Test
    public void testDummyGood() {
        rlci.integerParam = 10;
        output = rlci.generateMaskedRow(input);
        assertEquals(output, 0);
    }

}
