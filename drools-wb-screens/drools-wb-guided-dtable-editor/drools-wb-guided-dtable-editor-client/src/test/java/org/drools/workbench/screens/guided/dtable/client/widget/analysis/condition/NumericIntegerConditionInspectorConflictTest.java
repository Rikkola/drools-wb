/*
 * Copyright 2015 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.drools.workbench.screens.guided.dtable.client.widget.analysis.condition;

import static java.lang.String.format;

import org.drools.workbench.models.guided.dtable.shared.model.Pattern52;
import org.junit.Test;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collection;

@RunWith( Parameterized.class )
public class NumericIntegerConditionInspectorConflictTest {

    private final Integer value1;
    private final Integer value2;
    private final String operator1;
    private final String operator2;
    private final boolean conflictExpected;

    @Test
    public void parametrizedTest() {
        NumericIntegerConditionInspector a = getCondition( value1, operator1 );
        NumericIntegerConditionInspector b = getCondition( value2, operator2 );

        assertEquals( getAssertDescription(a, b, conflictExpected), conflictExpected, a.conflicts( b ) );
        assertEquals( getAssertDescription(b, a, conflictExpected), conflictExpected, b.conflicts( a ) );
    }

    public NumericIntegerConditionInspectorConflictTest( String operator1,
                                                         Integer value1,
                                                         String operator2,
                                                         Integer value2,
                                                         boolean conflictExpected) {
        this.value1 = value1;
        this.value2 = value2;
        this.operator1 = operator1;
        this.operator2 = operator2;
        this.conflictExpected = conflictExpected;
    }

    @Parameters
    public static Collection<Object[]> testData() {
        return Arrays.asList( new Object[][]{
            // 'in' and 'not in' are not doable here
            // op1, val1, op2, val2, conflicts
            { "==", 0, "==", 0, false },
            { "!=", 0, "!=", 0, false },
            { ">", 0, ">", 0, false },
            { ">=", 0, ">=", 0, false },
            { "<", 0, "<", 0, false },
            { "<=", 0, "<=", 0, false },

            { "==", 0, "!=", 1, false },
            { "==", 0, ">", -1, false },
            { "==", 0, ">", -10, false },
            { "==", 0, ">=", 0, false },
            { "==", 0, ">=", -10, false },
            { "==", 0, "<", 1, false },
            { "==", 0, "<", 10, false },
            { "==", 0, "<=", 0, false },
            { "==", 0, "<=", 10, false },

            { "==", 0, "==", 1, true },
            { "==", 0, "!=", 0, true },
            { "==", 0, ">", 0, true },
            { "==", 0, ">", 10, true },
            { "==", 0, ">=", 1, true },
            { "==", 0, ">=", 10, true },
            { "==", 0, "<", 0, true },
            { "==", 0, "<", -10, true },
            { "==", 0, "<=", -1, true },
            { "==", 0, "<=", -10, true },

            { "!=", 0, "!=", 1, false },
            { "!=", 0, ">", -1, false },
            { "!=", 0, ">=", 0, false },
            { "!=", 0, "<", 1, false },
            { "!=", 0, "<=", 0, false },

            { ">", 0, ">", 1, false },
            { ">", 0, ">=", 0, false },
            { ">", 0, "<", 2, false },
            { ">", 0, "<", 20, false },
            { ">", 0, "<=", 1, false },
            { ">", 0, "<=", 10, false },

            { ">", 0, "<", -1, true },
            { ">", 0, "<", 0, true },
            { ">", 0, "<", 1, true },
            { ">", 0, "<=", -2, true },
            { ">", 0, "<=", -1, true },
            { ">", 0, "<=", 0, true },

            { ">=", 0, ">=", 1, false },
            { ">=", 0, "<", 1, false },
            { ">=", 0, "<", 10, false },
            { ">=", 0, "<=", 0, false },
            { ">=", 0, "<=", 10, false },

            { ">=", 0, "<", -2, true },
            { ">=", 0, "<", -1, true },
            { ">=", 0, "<", 0, true },
            { ">=", 0, "<=", -3, true },
            { ">=", 0, "<=", -2, true },
            { ">=", 0, "<=", -1, true },

            { "<", 0, "<", 1, false },
            { "<", 0, "<=", 0, false },

            { "<=", 0, "<=", 1, false },
        } );
    }

    private String getAssertDescription( NumericIntegerConditionInspector a,
                                         NumericIntegerConditionInspector b,
                                         boolean conflictExpected ) {
        return format( "Expected condition '%s' %sto conflict with condition '%s':",
                       a.toHumanReadableString(),
                       conflictExpected ? "" : "not ",
                       b.toHumanReadableString() );
    }

    private NumericIntegerConditionInspector getCondition( Integer value,
                                                           String operator ) {
        return new NumericIntegerConditionInspector( mock( Pattern52.class ), "age", value, operator );
    }
}