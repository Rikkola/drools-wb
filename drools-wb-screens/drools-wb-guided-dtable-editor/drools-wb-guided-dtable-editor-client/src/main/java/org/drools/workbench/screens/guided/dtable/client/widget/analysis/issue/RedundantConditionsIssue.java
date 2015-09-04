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

import org.drools.workbench.screens.guided.dtable.analysis.checks.DetectRedundantConditionsCheck;
import org.drools.workbench.screens.guided.dtable.client.widget.analysis.reporting.Issue;
import org.drools.workbench.screens.guided.dtable.client.widget.analysis.reporting.Severity;
import org.drools.workbench.screens.guided.dtable.client.resources.i18n.AnalysisConstants;

public class RedundantConditionsIssue
        extends Issue {

    public RedundantConditionsIssue( DetectRedundantConditionsCheck check ) {
        super( Severity.NOTE,
               AnalysisConstants.INSTANCE.RedundantConditionsTitle(),
               check.getIndex() );

        getExplanation()
                .startNote()
                .addParagraph( AnalysisConstants.INSTANCE.RedundantConditionsNote1P1( check.getKey().getPattern().getFactType(),
                                                                                      check.getKey().getFactField() ) )
                .addParagraph( AnalysisConstants.INSTANCE.RedundantConditionsNote1P2( check.getConditions().get( 0 ).toHumanReadableString(),
                                                                                      check.getConditions().get( 1 ).toHumanReadableString() ) )
                .end()
                .addParagraph( AnalysisConstants.INSTANCE.RedundantConditionsP1() );

    }
}
