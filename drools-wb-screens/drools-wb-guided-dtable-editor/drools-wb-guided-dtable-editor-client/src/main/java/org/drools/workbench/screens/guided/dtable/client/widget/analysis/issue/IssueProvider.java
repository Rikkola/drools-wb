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

import org.drools.workbench.screens.guided.dtable.analysis.checks.DetectConflictingRowsCheck;
import org.drools.workbench.screens.guided.dtable.analysis.checks.DetectDeficientRowsCheck;
import org.drools.workbench.screens.guided.dtable.analysis.checks.DetectImpossibleMatchCheck;
import org.drools.workbench.screens.guided.dtable.analysis.checks.DetectMissingActionCheck;
import org.drools.workbench.screens.guided.dtable.analysis.checks.DetectMissingConditionCheck;
import org.drools.workbench.screens.guided.dtable.analysis.checks.DetectMultipleValuesForOneActionCheck;
import org.drools.workbench.screens.guided.dtable.analysis.checks.DetectRedundantActionCheck;
import org.drools.workbench.screens.guided.dtable.analysis.checks.DetectRedundantConditionsCheck;
import org.drools.workbench.screens.guided.dtable.analysis.checks.DetectRedundantRowsCheck;
import org.drools.workbench.screens.guided.dtable.analysis.checks.base.Check;
import org.drools.workbench.screens.guided.dtable.client.widget.analysis.reporting.Issue;

public class IssueProvider {

    public Issue getIssue( Check check ) {
        if ( check instanceof DetectConflictingRowsCheck ) {
            return new ConflictingRowsIssue( (DetectConflictingRowsCheck) check );
        } else if ( check instanceof DetectDeficientRowsCheck ) {
            return new DeficientRowsIssue( (DetectDeficientRowsCheck) check );
        } else if ( check instanceof DetectImpossibleMatchCheck ) {
            return new ImpossibleMatchIssue( (DetectImpossibleMatchCheck) check );
        } else if ( check instanceof DetectMissingActionCheck ) {
            return new MissingActionIssue( (DetectMissingActionCheck) check );
        } else if ( check instanceof DetectMissingConditionCheck ) {
            return new MissingConditionIssue( (DetectMissingConditionCheck) check );
        } else if ( check instanceof DetectMultipleValuesForOneActionCheck ) {
            return new MultipleValuesForOneActionIssue( (DetectMultipleValuesForOneActionCheck) check );
        } else if ( check instanceof DetectRedundantActionCheck ) {
            return new RedundantActionIssue( (DetectRedundantActionCheck) check );
        } else if ( check instanceof DetectRedundantConditionsCheck ) {
            return new RedundantConditionsIssue( (DetectRedundantConditionsCheck) check );
        } else if ( check instanceof DetectRedundantRowsCheck ) {
            return new RedundantRowsIssue( (DetectRedundantRowsCheck) check );
        }

        return null;
    }
}
