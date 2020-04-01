/*
 * Copyright 2019 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.drools.workbench.screens.guided.dtable.backend.server.conversion;

import org.apache.poi.ss.usermodel.Workbook;
import org.drools.workbench.models.guided.dtable.backend.GuidedDTXMLPersistence;
import org.drools.workbench.models.guided.dtable.shared.model.GuidedDecisionTable52;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.drools.workbench.screens.guided.dtable.backend.server.util.TestUtil.loadResource;
import static org.junit.Assert.assertEquals;

public class XLSBuilderBRLTest
        extends TestBase {

    @BeforeClass
    public static void setUp() throws Exception {
        final String xml = loadResource(XLSBuilderAttributesNegateTest.class.getResourceAsStream("BRL.gdst"));

        final GuidedDecisionTable52 dtable = GuidedDTXMLPersistence.getInstance().unmarshal(xml);

        final XLSBuilder.BuildResult buildResult = new XLSBuilder(dtable, makeDMO()).build();
        final Workbook workbook = buildResult.getWorkbook();

        assertEquals(1, workbook.getNumberOfSheets());
        sheet = workbook.iterator().next();
    }

    @Test
    public void headers() {

        assertEquals("RuleSet", cell(1, 1).getStringCellValue());
        assertEquals("mortgages.mortgages", cell(1, 2).getStringCellValue());

        assertEquals("Import", cell(2, 1).getStringCellValue());
        assertEquals("", sheet.getRow(2).getCell(2).getStringCellValue());

        assertEquals("RuleTable BRL", cell(4, 1).getStringCellValue());
    }

    @Test
    public void columnTypes() {

        assertEquals("CONDITION", cell(5, 1).getStringCellValue());
        assertEquals("CONDITION", cell(5, 2).getStringCellValue());
        assertEquals("CONDITION", cell(5, 3).getStringCellValue());
        assertEquals("ACTION", cell(5, 4).getStringCellValue());
        assertEquals("ACTION", cell(5, 5).getStringCellValue());
        assertEquals("ACTION", cell(5, 6).getStringCellValue());
        assertNullCell(5, 7);
    }

    @Test
    public void patterns() {

        assertNullCell(6, 1);
        assertNullCell(6, 2);
        assertNullCell(6, 3);
        assertNullCell(6, 4);
    }

    @Test
    public void constraints() {

        assertEquals("LoanApplication( explanation != null )", cell(7, 1).getStringCellValue().trim());
        assertEquals("Applicant( age == $1 , approved == $2 )", cell(7, 2).getStringCellValue().trim());
        assertEquals("Applicant( age == $1 , approved == $2 )\n" +
                             "Applicant( age == $1 , approved == $3 )", cell(7, 3).getStringCellValue().trim());
        assertEquals("Applicant brlColumnFact0 = new Applicant();\n" +
                             "brlColumnFact0.setAge( $1 );\n" +
                             "brlColumnFact0.setApproved( $2 );\n" +
                             "insert( brlColumnFact0 );", cell(7, 4).getStringCellValue().trim());
        assertEquals("LoanApplication brlColumnFact1 = new LoanApplication();\n" +
                             "brlColumnFact1.setApproved( true );\n" +
                             "insert( brlColumnFact1 );", cell(7, 5).getStringCellValue().trim());
        assertEquals("log($1 + \" \" + $2);", cell(7, 6).getStringCellValue().trim());
        assertNullCell(7, 7);
    }

    @Test
    public void columnTitles() {

        assertEquals("Not null", cell(8, 1).getStringCellValue());
        assertEquals("Free form LHS", cell(8, 2).getStringCellValue());
        assertEquals("Order mixed on purpose", cell(8, 3).getStringCellValue());
        assertEquals("Something", cell(8, 4).getStringCellValue());
        assertEquals("Free form RHS", cell(8, 6).getStringCellValue());
        assertNullCell(8, 7);
    }

    @Test
    public void content() {

        assertEquals("X", cell(9, 1).getStringCellValue());
        assertEquals("12, false", cell(9, 2).getStringCellValue());
        assertEquals(", true, false", cell(9, 3).getStringCellValue());
        assertEquals("0, true", cell(9, 4).getStringCellValue());
        assertEquals("", cell(9, 5).getStringCellValue());
        assertEquals(", ", cell(9, 6).getStringCellValue());

        assertEquals("", cell(10, 1).getStringCellValue());
        assertEquals(", true", cell(10, 2).getStringCellValue());
        assertEquals("4444, false, true", cell(10, 3).getStringCellValue());
        assertEquals("121, true", cell(10, 4).getStringCellValue());
        assertEquals("X", cell(10, 5).getStringCellValue());
        assertEquals("\"log this\", \"and this\"", cell(10, 6).getStringCellValue());
    }
}