/*
 * Copyright 2011 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.drools.workbench.screens.guided.dtable.client.widget.analysis.panel;

import java.util.HashSet;
import java.util.Set;

import com.google.gwt.event.shared.EventBus;
import org.drools.workbench.models.guided.dtable.shared.model.GuidedDecisionTable52;
import org.drools.workbench.screens.guided.dtable.analysis.checks.base.Check;
import org.drools.workbench.screens.guided.dtable.client.widget.analysis.issue.IssueProvider;
import org.drools.workbench.screens.guided.dtable.service.AnalysisService;
import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.ioc.client.container.IOC;
import org.kie.workbench.common.widgets.client.datamodel.AsyncPackageDataModelOracle;
import org.kie.workbench.common.widgets.decoratedgrid.client.widget.data.Coordinate;
import org.kie.workbench.common.widgets.decoratedgrid.client.widget.events.AfterColumnDeleted;
import org.kie.workbench.common.widgets.decoratedgrid.client.widget.events.AfterColumnInserted;
import org.kie.workbench.common.widgets.decoratedgrid.client.widget.events.AppendRowEvent;
import org.kie.workbench.common.widgets.decoratedgrid.client.widget.events.DeleteRowEvent;
import org.kie.workbench.common.widgets.decoratedgrid.client.widget.events.InsertRowEvent;
import org.kie.workbench.common.widgets.decoratedgrid.client.widget.events.UpdateColumnDataEvent;
import org.uberfire.mvp.PlaceRequest;

public class DecisionTableAnalyzer
        implements ValidateEvent.Handler,
                   DeleteRowEvent.Handler,
                   AfterColumnDeleted.Handler,
                   UpdateColumnDataEvent.Handler,
                   AppendRowEvent.Handler,
                   InsertRowEvent.Handler,
                   AfterColumnInserted.Handler {

    private PlaceRequest place;
    private final GuidedDecisionTable52 model;
    private final IssueProvider issueProvider = new IssueProvider();
    private final EventManager eventManager = new EventManager();

    private final Analyser analyser;

    public DecisionTableAnalyzer( final PlaceRequest place,
                                  final AsyncPackageDataModelOracle oracle,
                                  final GuidedDecisionTable52 model,
                                  final Caller<AnalysisService> analysisService,
                                  final EventBus eventBus ) {
        this.place = place;
        this.model = model;

        analyser = new Analyser( analysisService,
                                 oracle,
                                 model );


        eventBus.addHandler( ValidateEvent.TYPE,
                             this );
        eventBus.addHandler( DeleteRowEvent.TYPE,
                             this );
        eventBus.addHandler( AfterColumnDeleted.TYPE,
                             this );
        eventBus.addHandler( UpdateColumnDataEvent.TYPE,
                             this );
        eventBus.addHandler( AppendRowEvent.TYPE,
                             this );
        eventBus.addHandler( InsertRowEvent.TYPE,
                             this );
        eventBus.addHandler( AfterColumnInserted.TYPE,
                             this );
    }

    private AnalysisReport makeAnalysisReport() {
        final AnalysisReport report = new AnalysisReport( place );

        for (Check check : analyser.getChecksWithIssues()) {
            report.addIssue( issueProvider.getIssue( check ) );
        }

        return report;
    }

    private void sendReport() {
        sendReport( makeAnalysisReport() );
    }

    protected void sendReport( final AnalysisReport report ) {
        IOC.getBeanManager().lookupBean( AnalysisReportScreen.class ).getInstance().showReport( report );
    }

    @Override
    public void onValidate( final ValidateEvent event ) {

        if ( event.getUpdates().isEmpty() ) {

            // TODO: This should be server side
            analyser.resetChecks( false );
        } else {

            // TODO: This should be server side ( depends how big the change is )
//            cache.updateRowInspectors( transform( event.getUpdates().keySet() ),
//                                       model.getData() );
        }

        analyser.run();
        sendReport();
    }

    private Set<org.drools.workbench.screens.guided.dtable.analysis.cache.Coordinate> transform( Set<Coordinate> coordinates ) {
        Set<org.drools.workbench.screens.guided.dtable.analysis.cache.Coordinate> result = new HashSet<org.drools.workbench.screens.guided.dtable.analysis.cache.Coordinate>();

        for (Coordinate coordinate : coordinates) {
            result.add( new org.drools.workbench.screens.guided.dtable.analysis.cache.Coordinate( coordinate.getCol(),
                                                                                                  coordinate.getRow() ) );
        }

        return result;
    }

    @Override
    public void onAfterDeletedColumn( final AfterColumnDeleted event ) {

        // TODO: This should be server side
        analyser.resetChecks( true );

        analyser.run();
        sendReport();
    }

    @Override
    public void onAfterColumnInserted( final AfterColumnInserted event ) {

        // TODO: This should be server side
        analyser.resetChecks( true );

        analyser.run();
        sendReport();
    }

    @Override
    public void onUpdateColumnData( final UpdateColumnDataEvent event ) {

        if ( analyser.hasTheRowCountIncreased( event.getColumnData().size() ) ) {

            analyser.addRow( eventManager.getNewIndex(),
                             model.getData().get( eventManager.getNewIndex() ) );
            analyser.run();
            sendReport();

        } else if ( analyser.hasTheRowCountDecreased( event.getColumnData().size() ) ) {

            analyser.removeRow( eventManager.rowDeleted );

            analyser.run();
            sendReport();
        }

        eventManager.clear();
    }

    @Override
    public void onDeleteRow( final DeleteRowEvent event ) {
        eventManager.rowDeleted = event.getIndex();
    }

    @Override
    public void onAppendRow( final AppendRowEvent event ) {
        eventManager.rowAppended = true;
    }

    @Override
    public void onInsertRow( final InsertRowEvent event ) {
        eventManager.rowInserted = event.getIndex();
    }

    public void onFocus() {
        sendReport( makeAnalysisReport() );
    }

    class EventManager {

        boolean rowAppended = false;
        Integer rowInserted = null;
        Integer rowDeleted = null;

        public void clear() {

            rowAppended = false;
            rowInserted = null;
            rowDeleted = null;
        }

        int getNewIndex() {
            if ( eventManager.rowAppended ) {
                return model.getData().size() - 1;
            } else if ( eventManager.rowInserted != null ) {
                return eventManager.rowInserted;
            }

            throw new IllegalStateException( "There are no active updates" );
        }
    }
}
