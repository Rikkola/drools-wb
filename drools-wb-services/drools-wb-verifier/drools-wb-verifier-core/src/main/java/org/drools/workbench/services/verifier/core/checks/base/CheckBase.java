/*
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
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

package org.drools.workbench.services.verifier.core.checks.base;

import java.util.Optional;

import org.drools.workbench.services.verifier.api.client.configuration.AnalyzerConfiguration;
import org.drools.workbench.services.verifier.api.client.reporting.CheckType;
import org.drools.workbench.services.verifier.api.client.reporting.Issue;
import org.drools.workbench.services.verifier.api.client.reporting.Severity;
import org.uberfire.commons.validation.PortablePreconditions;

abstract class CheckBase
        implements Check {

    protected final AnalyzerConfiguration configuration;

    protected boolean hasIssues = false;

    public CheckBase( final AnalyzerConfiguration configuration ) {
        this.configuration = PortablePreconditions.checkNotNull( "configuration",
                                                                 configuration );
    }

    @Override
    public boolean hasIssues() {
        return hasIssues;
    }

    @Override
    public final Issue getIssue() {
        return makeIssue( resolveSeverity(),
                          getCheckType() );
    }

    protected abstract Issue makeIssue( final Severity severity,
                                        final CheckType checkType );

    protected abstract CheckType getCheckType();

    protected abstract Severity getDefaultSeverity();

    protected Severity resolveSeverity() {
        final Optional<Severity> severityOverwrite = configuration.getCheckWhiteList()
                .getSeverityOverwrite( getCheckType() );

        if ( severityOverwrite.isPresent() ) {
            return severityOverwrite.get();
        } else {
            return getDefaultSeverity();
        }
    }
}
