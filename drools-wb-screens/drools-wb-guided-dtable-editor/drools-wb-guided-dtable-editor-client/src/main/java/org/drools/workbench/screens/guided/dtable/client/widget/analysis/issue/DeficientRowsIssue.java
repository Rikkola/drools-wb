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

import org.drools.workbench.screens.guided.dtable.analysis.checks.DetectDeficientRowsCheck;
import org.drools.workbench.screens.guided.dtable.client.widget.analysis.reporting.Issue;
import org.drools.workbench.screens.guided.dtable.client.widget.analysis.reporting.Severity;
import org.drools.workbench.screens.guided.dtable.client.resources.i18n.AnalysisConstants;

public class DeficientRowsIssue
        extends Issue {

    public DeficientRowsIssue( final DetectDeficientRowsCheck check ) {
        super( Severity.WARNING,
               AnalysisConstants.INSTANCE.DeficientRow(),
               check.getIndex() );

        getExplanation()
                .addParagraph( AnalysisConstants.INSTANCE.DeficientRowsP1() )
                .startNote()
                .addParagraph( AnalysisConstants.INSTANCE.DeficientRowsNoteP1() )
                .startExampleTable()
                .startHeader()
                .headerConditions( AnalysisConstants.INSTANCE.Salary(), AnalysisConstants.INSTANCE.Savings() )
                .headerActions( AnalysisConstants.INSTANCE.ApproveLoan() )
                .end()
                .startRow()
                .addConditions( "--", "100 000" ).addActions( "true" )
                .end()
                .startRow()
                .addConditions( "30 000", "--" ).addActions( "false" )
                .end()
                .end()
                .end()
                .addParagraph( AnalysisConstants.INSTANCE.DeficientRowsP2() );

    }
}
