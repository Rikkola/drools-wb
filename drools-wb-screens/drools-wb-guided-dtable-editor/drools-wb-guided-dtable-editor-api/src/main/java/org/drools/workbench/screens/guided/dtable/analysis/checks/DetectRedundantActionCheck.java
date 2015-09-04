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

import java.util.ArrayList;
import java.util.List;

import org.drools.workbench.screens.guided.dtable.analysis.RowInspector;
import org.drools.workbench.screens.guided.dtable.analysis.action.ActionInspector;
import org.drools.workbench.screens.guided.dtable.analysis.action.ActionInspectorKey;
import org.drools.workbench.screens.guided.dtable.analysis.checks.base.SingleCheck;

public class DetectRedundantActionCheck
        extends SingleCheck {

    private final List<ActionInspector> inspectorList = new ArrayList<ActionInspector>();
    private ActionInspectorKey key;

    public DetectRedundantActionCheck( final RowInspector rowInspector ) {
        super( rowInspector );
    }

    @Override
    public void check() {

        for (ActionInspectorKey key : rowInspector.getActions().keys()) {

            List<ActionInspector> actionInspectors = rowInspector.getActions().get( key );

            for (int i = 0; i < actionInspectors.size(); i++) {

                if ( actionInspectors.size() > i - 1 ) {
                    for (int j = i + 1; j < actionInspectors.size(); j++) {
                        if ( actionInspectors.get( i ).isRedundant( actionInspectors.get( j ) ) ) {
                            inspectorList.clear();
                            inspectorList.add( actionInspectors.get( i ) );
                            inspectorList.add( actionInspectors.get( j ) );
                            hasIssues = true;
                            this.key = key;
                            return;
                        }
                    }
                }
            }

        }
    }

    public ActionInspectorKey getKey() {
        return key;
    }

    public List<ActionInspector> getInspectorList() {
        return inspectorList;
    }
}
