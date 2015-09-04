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

package org.drools.workbench.screens.guided.dtable.analysis;

import org.drools.workbench.models.guided.dtable.shared.model.Pattern52;
import org.drools.workbench.screens.guided.dtable.analysis.condition.NumericIntegerConditionInspector;
import org.drools.workbench.screens.guided.dtable.analysis.condition.StringConditionInspector;

public class TestUtil {

    public static NumericIntegerConditionInspector getNumericIntegerCondition( Pattern52 pattern,
                                                                               String factField,
                                                                               String operator,
                                                                               int value ) {
        return new NumericIntegerConditionInspector( pattern,
                                                     factField,
                                                     value,
                                                     operator );
    }

    public static StringConditionInspector getStringCondition( Pattern52 pattern,
                                                               String factField,
                                                               String operator,
                                                               String value ) {
        return new StringConditionInspector( pattern,
                                             factField,
                                             value,
                                             operator );
    }
}
