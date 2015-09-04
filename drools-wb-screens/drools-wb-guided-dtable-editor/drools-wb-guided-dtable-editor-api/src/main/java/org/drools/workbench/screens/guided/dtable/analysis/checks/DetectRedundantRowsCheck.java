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

package org.drools.workbench.screens.guided.dtable.analysis.checks;

import org.drools.workbench.screens.guided.dtable.analysis.RowInspector;
import org.drools.workbench.screens.guided.dtable.analysis.checks.base.PairCheck;

public class DetectRedundantRowsCheck
        extends PairCheck {

    private boolean isRedundant;
    private boolean subsumes;

    public DetectRedundantRowsCheck( final RowInspector rowInspector,
                                     final RowInspector other ) {
        super( rowInspector,
               other );
    }

    @Override
    public void check() {
        if ( other.getActions().hasValues() ) {
            if ( rowInspector.isRedundant( other ) ) {
                hasIssues = true;
                isRedundant = true;
            } else if ( rowInspector.subsumes( other ) ) {
                hasIssues = true;
                subsumes = true;
            }
        }
    }

    public boolean isRedundant() {
        return isRedundant;
    }

    public boolean subsumes() {
        return subsumes;
    }
}
