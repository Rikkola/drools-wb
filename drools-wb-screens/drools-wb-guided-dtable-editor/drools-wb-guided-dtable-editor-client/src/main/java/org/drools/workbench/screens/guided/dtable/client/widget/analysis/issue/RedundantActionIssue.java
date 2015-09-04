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

package org.drools.workbench.screens.guided.dtable.client.widget.analysis.issue;

import org.drools.workbench.screens.guided.dtable.analysis.action.ActionInspectorKey;
import org.drools.workbench.screens.guided.dtable.analysis.action.FactFieldColumnActionInspectorKey;
import org.drools.workbench.screens.guided.dtable.analysis.checks.DetectRedundantActionCheck;
import org.drools.workbench.screens.guided.dtable.client.widget.analysis.reporting.Issue;
import org.drools.workbench.screens.guided.dtable.client.widget.analysis.reporting.Severity;
import org.drools.workbench.screens.guided.dtable.client.resources.i18n.AnalysisConstants;

public class RedundantActionIssue
        extends Issue {

    public RedundantActionIssue( DetectRedundantActionCheck check ) {
        super( Severity.WARNING,
               getMessage( check.getKey() ),
               check.getIndex() );

        getExplanation()
                .addParagraph( AnalysisConstants.INSTANCE.RedundantActionsP1() )
                .startNote()
                .addParagraph( AnalysisConstants.INSTANCE.RedundantActionsNote1P1( check.getInspectorList().get( 0 ).toHumanReadableString(), check.getInspectorList().get( 1 ).toHumanReadableString() ) )
                .end();
    }

    private static String getMessage( ActionInspectorKey key ) {
        if ( key instanceof FactFieldColumnActionInspectorKey ) {
            return AnalysisConstants.INSTANCE.ValueForFactFieldIsSetTwice( ((FactFieldColumnActionInspectorKey) key).getBoundName(),
                                                                           ((FactFieldColumnActionInspectorKey) key).getFactField() );
        } else {
            return AnalysisConstants.INSTANCE.ValueForAnActionIsSetTwice();
        }
    }
}
