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

package org.drools.workbench.screens.guided.dtable.client.widget.analysis;

import java.util.Date;

import org.drools.workbench.models.guided.dtable.shared.model.ConditionCol52;
import org.drools.workbench.models.guided.dtable.shared.model.GuidedDecisionTable52;
import org.drools.workbench.screens.guided.dtable.analysis.AnalysisContent;
import org.drools.workbench.screens.guided.dtable.analysis.AnalysisDecisionTableUtils;
import org.drools.workbench.screens.guided.dtable.analysis.ChecksGenerator;
import org.drools.workbench.screens.guided.dtable.analysis.RowInspectorGenerator;
import org.drools.workbench.screens.guided.dtable.analysis.cache.RowInspectorCache;
import org.drools.workbench.screens.guided.dtable.analysis.checks.base.Checks;
import org.drools.workbench.screens.guided.dtable.service.AnalysisService;
import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.common.client.api.ErrorCallback;
import org.jboss.errai.common.client.api.RemoteCallback;

public class AnalysisServiceCallerMock implements Caller<AnalysisService> {

    private RowInspectorCache cache;
    private RowInspectorGenerator rowInspectorGenerator;
    private RemoteCallback remoteCallback;
    private ChecksGenerator checksGenerator;

    public void setModel( GuidedDecisionTable52 model ) {

        cache = new RowInspectorCache();
        rowInspectorGenerator = new RowInspectorGenerator(
                new AnalysisDecisionTableUtils() {

                    @Override
                    public String getType( ConditionCol52 conditionColumn ) {
                        return null;
                    }

                    @Override
                    public String[] getValueList( ConditionCol52 conditionColumn ) {
                        return new String[0];
                    }

                    @Override
                    public String format( Date dateValue ) {
                        return null;
                    }
                },
                model,
                cache );

        checksGenerator = new ChecksGenerator( cache, new Checks() );
    }

    @Override public AnalysisService call() {
        return null;
    }

    @Override public AnalysisService call( RemoteCallback<?> remoteCallback ) {
        return call( remoteCallback, null );
    }

    @Override public AnalysisService call( RemoteCallback<?> remoteCallback,
                                           ErrorCallback<?> errorCallback ) {
        this.remoteCallback = remoteCallback;
        return new AnalysisServiceMock();
    }

    class AnalysisServiceMock
            implements AnalysisService {

        @Override
        public AnalysisContent createCache( GuidedDecisionTable52 model ) {
            AnalysisContent content = new AnalysisContent();
            rowInspectorGenerator.reset();
            checksGenerator.resetChecks();
            content.setCache( cache );
            content.setChecks( checksGenerator.getChecks() );
            remoteCallback.callback( content );
            return null;
        }
    }
}
