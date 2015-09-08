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

import java.util.ArrayList;
import java.util.Collection;

import org.drools.workbench.screens.guided.dtable.analysis.cache.RowInspectorCache;
import org.drools.workbench.screens.guided.dtable.analysis.checks.base.Check;
import org.drools.workbench.screens.guided.dtable.analysis.checks.base.Checks;

public class ChecksGenerator {

    private final Checks checks;
    private final RowInspectorCache cache;

    public ChecksGenerator( final RowInspectorCache cache,
                            final Checks checks ) {
        this.cache = cache;
        this.checks = checks;
    }

    public void resetChecks() {
        for (RowInspector rowInspector : cache.all()) {
            checks.add( rowInspector );
        }
    }

    public Checks getChecks() {
        return checks;
    }

    public Collection<Check> getChecksWithIssues() {

        ArrayList<Check> result = new ArrayList<Check>();

        for (RowInspector rowInspector : cache.all()) {
            for (Check check : checks.get( rowInspector )) {
                if ( check.hasIssues() ) {
                    result.add( check );
                }
            }
        }

        return result;
    }

    public void update( final RowInspector oldRowInspector,
                        final RowInspector newRowInspector ) {
        checks.update( oldRowInspector,
                       newRowInspector );
    }
}
