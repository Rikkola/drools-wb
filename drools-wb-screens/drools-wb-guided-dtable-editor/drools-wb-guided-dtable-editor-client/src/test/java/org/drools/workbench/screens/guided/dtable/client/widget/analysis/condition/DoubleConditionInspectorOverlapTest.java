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

import java.util.Arrays;
import java.util.Collection;

import org.drools.workbench.models.guided.dtable.shared.model.Pattern52;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import static java.lang.String.format;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(Parameterized.class)
public class DoubleConditionInspectorOverlapTest {

    private final Double value1;
    private final Double value2;
    private final String operator1;
    private final String operator2;
    private final boolean conflictExpected;

    @Test
    public void parametrizedConflictTest() {
        ComparableConditionInspector<Double> a = getCondition( value1, operator1 );
        ComparableConditionInspector<Double> b = getCondition( value2, operator2 );

        assertEquals( getAssertDescription( a, b, conflictExpected, "conflict" ), conflictExpected, a.conflicts( b ) );
        assertEquals( getAssertDescription( b, a, conflictExpected, "conflict" ), conflictExpected, b.conflicts( a ) );
    }

    @Test
    public void parametrizedOverlapTest() {
        ComparableConditionInspector<Double> a = getCondition( value1, operator1 );
        ComparableConditionInspector<Double> b = getCondition( value2, operator2 );

        assertEquals( getAssertDescription( a, b, !conflictExpected, "overlap" ), !conflictExpected, a.overlaps( b ) );
        assertEquals( getAssertDescription( b, a, !conflictExpected, "overlap" ), !conflictExpected, b.overlaps( a ) );
    }

    public DoubleConditionInspectorOverlapTest( String operator1,
                                                Double value1,
                                                String operator2,
                                                Double value2,
                                                boolean conflictExpected ) {
        this.value1 = value1;
        this.value2 = value2;
        this.operator1 = operator1;
        this.operator2 = operator2;
        this.conflictExpected = conflictExpected;
    }

    @Parameters
    public static Collection<Object[]> testData() {
        return Arrays.asList( new Object[][]{
                // matches and soundslike are probably not doable...
                // op1, val1, op2, val2, conflicts
                {"==", "a", "==", "a", false}
        } );
    }

    private ComparableConditionInspector<Double> getCondition( Double value,
                                                               String operator ) {
        return new ComparableConditionInspector<Double>( mock( Pattern52.class ), "value", value, operator );
    }

    private String getAssertDescription( ComparableConditionInspector<Double> a,
                                         ComparableConditionInspector<Double> b,
                                         boolean conflictExpected,
                                         String condition ) {
        return format( "Expected condition '%s' %sto %s with condition '%s':",
                       a.toHumanReadableString(),
                       conflictExpected ? "" : "not ",
                       condition,
                       b.toHumanReadableString() );
    }
}