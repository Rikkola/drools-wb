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

import org.drools.workbench.screens.guided.dtable.analysis.checks.DetectRedundantRowsCheck;
import org.drools.workbench.screens.guided.dtable.client.widget.analysis.reporting.Issue;
import org.drools.workbench.screens.guided.dtable.client.widget.analysis.reporting.Severity;
import org.drools.workbench.screens.guided.dtable.client.resources.i18n.AnalysisConstants;

public class RedundantRowsIssue extends Issue {

    public RedundantRowsIssue( DetectRedundantRowsCheck check ) {
        super( Severity.WARNING,
               getMessage( check ),
               check.getIndex(),
               check.getOtherIndex() );

        setExplanation( check );
    }

    private void setExplanation( final DetectRedundantRowsCheck check ) {

        if ( check.isRedundant() ) {
            getExplanation()
                    .addParagraph( AnalysisConstants.INSTANCE.RedundantRowsP1() )
                    .addParagraph( AnalysisConstants.INSTANCE.RedundantRowsP2() )
                    .addParagraph( AnalysisConstants.INSTANCE.RedundantRowsP3() );

        } else if ( check.subsumes() ) {
            getExplanation()
                    .addParagraph( AnalysisConstants.INSTANCE.SubsumptantRowsP1() )
                    .addParagraph( AnalysisConstants.INSTANCE.SubsumptantRowsP2() );
        }
    }

    private static String getMessage( final DetectRedundantRowsCheck check ) {
        if ( check.isRedundant() ) {
            return AnalysisConstants.INSTANCE.RedundantRows();
        } else if ( check.subsumes() ) {
            return AnalysisConstants.INSTANCE.SubsumptantRows();
        } else {
            return "";
        }
    }

}
