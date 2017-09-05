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

import org.junit.Before;
import org.junit.Test;
import org.talend.dataquality.datamasking.Functions.GenerateFromListString;
import org.talend.dataquality.duplicating.RandomWrapper;

/**
 * created by jgonzalez on 29 juin 2015 Detailled comment
 *
 */
public class GenerateFromListStringTest {

    private String output;

    private GenerateFromListString gfls = new GenerateFromListString();

    @Before
    public void setUp() throws Exception {
        gfls.parse("Toto, Titi, Tata", false, new RandomWrapper(42)); //$NON-NLS-1$
    }

    @Test
    public void testGood() {
        output = gfls.generateMaskedRow(null);
        assertEquals(output, "Tata"); //$NON-NLS-1$
    }

    @Test
    public void testNull() {
        gfls.keepNull = true;
        output = gfls.generateMaskedRow(null);
        assertEquals(output, null);
    }
}
