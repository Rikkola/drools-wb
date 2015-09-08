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

package org.drools.workbench.screens.guided.dtable.client.widget.analysis.panel;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.drools.workbench.models.guided.dtable.shared.model.ConditionCol52;
import org.drools.workbench.models.guided.dtable.shared.model.DTCellValue52;
import org.drools.workbench.models.guided.dtable.shared.model.GuidedDecisionTable52;
import org.drools.workbench.screens.guided.dtable.analysis.AnalysisContent;
import org.drools.workbench.screens.guided.dtable.analysis.AnalysisDecisionTableUtils;
import org.drools.workbench.screens.guided.dtable.analysis.ChecksGenerator;
import org.drools.workbench.screens.guided.dtable.analysis.RowInspector;
import org.drools.workbench.screens.guided.dtable.analysis.RowInspectorGenerator;
import org.drools.workbench.screens.guided.dtable.analysis.UpdateHandler;
import org.drools.workbench.screens.guided.dtable.analysis.cache.RowInspectorCache;
import org.drools.workbench.screens.guided.dtable.analysis.checks.base.Check;
import org.drools.workbench.screens.guided.dtable.analysis.checks.base.Checks;
import org.drools.workbench.screens.guided.dtable.client.utils.GuidedDecisionTableUtils;
import org.drools.workbench.screens.guided.dtable.service.AnalysisService;
import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.common.client.api.RemoteCallback;
import org.kie.workbench.common.widgets.client.datamodel.AsyncPackageDataModelOracle;

public class Analyser {

    private RowInspectorCache cache;
    private RowInspectorGenerator rowInspectorGenerator;
    private ChecksGenerator checksGenerator;
    private Checks checks;

    public Analyser( final Caller<AnalysisService> analysisService,
                     final AsyncPackageDataModelOracle oracle,
                     final GuidedDecisionTable52 model ) {

        analysisService.call( getRemoteCallback( oracle, model ) ).createCache( model );

    }

    private RemoteCallback<AnalysisContent> getRemoteCallback( final AsyncPackageDataModelOracle oracle, final GuidedDecisionTable52 model ) {
        return new RemoteCallback<AnalysisContent>() {
            @Override
            public void callback( final AnalysisContent content ) {
                Analyser.this.cache = content.getCache();
                Analyser.this.checks = content.getChecks();

                rowInspectorGenerator = new RowInspectorGenerator(
                        new AnalysisDecisionTableUtils() {
                            GuidedDecisionTableUtils utils = new GuidedDecisionTableUtils( model, oracle );

                            @Override public String getType( ConditionCol52 conditionColumn ) {
                                return utils.getType( conditionColumn );
                            }

                            @Override public String[] getValueList( ConditionCol52 conditionColumn ) {
                                return utils.getValueList( conditionColumn );
                            }

                            @Override public String format( Date dateValue ) {
                                return null;
                            }
                        },
                        model,
                        cache );
                checksGenerator = new ChecksGenerator( cache,
                                                       checks );

                rowInspectorGenerator.setUpdateHandler( new UpdateHandler() {
                    @Override
                    public void updateRow( final RowInspector oldRowInspector,
                                           final RowInspector newRowInspector ) {
                        checksGenerator.update( oldRowInspector,
                                                newRowInspector );
                    }
                } );

            }
        };
    }

    public void resetChecks( boolean resetCache ) {
        if ( resetCache ) {
            rowInspectorGenerator.reset();
        }
        checksGenerator.resetChecks();
    }

    public void run() {
        checksGenerator.getChecks().run();
    }

    public Collection<Check> getChecksWithIssues() {
        return checksGenerator.getChecksWithIssues();
    }

    public void removeRow( final int row ) {
        RowInspector removed = cache.removeRow( row );
        checksGenerator.getChecks().remove( removed );
    }

    public void addRow( final int index,
                        final List<DTCellValue52> dtCellValue52s ) {

        RowInspector rowInspector = rowInspectorGenerator.addRow( index,
                                                                  dtCellValue52s );
        checksGenerator.getChecks().add( rowInspector );
    }

    public boolean hasTheRowCountDecreased( int size ) {
        return cache.all().size() > size;
    }

    public boolean hasTheRowCountIncreased( int size ) {
        return cache.all().size() < size;
    }
}
