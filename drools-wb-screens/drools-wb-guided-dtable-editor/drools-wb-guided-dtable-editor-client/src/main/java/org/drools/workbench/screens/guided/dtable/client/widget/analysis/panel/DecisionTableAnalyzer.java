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

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.i18n.client.DateTimeFormat;
import org.drools.workbench.models.guided.dtable.shared.model.ConditionCol52;
import org.drools.workbench.models.guided.dtable.shared.model.GuidedDecisionTable52;
import org.drools.workbench.screens.guided.dtable.analysis.AnalysisDecisionTableUtils;
import org.drools.workbench.screens.guided.dtable.analysis.RowInspector;
import org.drools.workbench.screens.guided.dtable.analysis.UpdateHandler;
import org.drools.workbench.screens.guided.dtable.analysis.cache.RowInspectorCache;
import org.drools.workbench.screens.guided.dtable.analysis.checks.base.Check;
import org.drools.workbench.screens.guided.dtable.analysis.checks.base.Checks;
import org.drools.workbench.screens.guided.dtable.client.utils.GuidedDecisionTableUtils;
import org.drools.workbench.screens.guided.dtable.client.widget.analysis.issue.IssueProvider;
import org.jboss.errai.ioc.client.container.IOC;
import org.kie.workbench.common.services.shared.preferences.ApplicationPreferences;
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

    private final RowInspectorCache cache;
    private PlaceRequest place;
    private final GuidedDecisionTable52 model;
    private final Checks checks = new Checks();
    private final IssueProvider issueProvider = new IssueProvider();
    private final EventManager eventManager = new EventManager();

    public DecisionTableAnalyzer( final PlaceRequest place,
                                  final AsyncPackageDataModelOracle oracle,
                                  final GuidedDecisionTable52 model,
                                  final EventBus eventBus ) {
        this.place = place;
        this.model = model;

        cache = new RowInspectorCache(
                new AnalysisDecisionTableUtils() {

                    private final String DATE_FORMAT = ApplicationPreferences.getDroolsDateFormat();
                    private final DateTimeFormat DATE_FORMATTER = DateTimeFormat.getFormat( DATE_FORMAT );

                    GuidedDecisionTableUtils utils = new GuidedDecisionTableUtils( model,
                                                                                   oracle );

                    @Override
                    public String getType( ConditionCol52 conditionColumn ) {
                        return utils.getType( conditionColumn );
                    }

                    @Override
                    public String[] getValueList( ConditionCol52 conditionColumn ) {
                        return utils.getValueList( conditionColumn );
                    }

                    @Override
                    public String format( Date dateValue ) {
                        return DATE_FORMATTER.format( dateValue );
                    }
                },
                model,
                new UpdateHandler() {
                    @Override
                    public void updateRow( final RowInspector oldRowInspector,
                                           final RowInspector newRowInspector ) {
                        checks.update( oldRowInspector,
                                       newRowInspector );
                    }
                } );

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

    private void resetChecks() {
        for ( RowInspector rowInspector : cache.all() ) {
            checks.add( rowInspector );
        }
    }

    private void analyze() {

        this.checks.run();

        sendReport( makeAnalysisReport() );
    }

    private AnalysisReport makeAnalysisReport() {
        final AnalysisReport report = new AnalysisReport( place );
        for (RowInspector rowInspector : cache.all()) {
            for (Check check : checks.get( rowInspector )) {
                if ( check.hasIssues() ) {
                    report.addIssue( issueProvider.getIssue( check ) );
                }
            }
        }
        return report;
    }

    protected void sendReport( final AnalysisReport report ) {
        IOC.getBeanManager().lookupBean( AnalysisReportScreen.class ).getInstance().showReport( report );
    }

    @Override
    public void onValidate( final ValidateEvent event ) {

        if ( event.getUpdates().isEmpty() || checks.isEmpty() ) {
            resetChecks();
        } else {
            cache.updateRowInspectors( transform( event.getUpdates().keySet() ),
                                       model.getData() );
        }

        analyze();
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

        cache.reset();

        resetChecks();

        analyze();
    }

    @Override
    public void onAfterColumnInserted( final AfterColumnInserted event ) {

        cache.reset();

        resetChecks();

        analyze();
    }

    @Override
    public void onUpdateColumnData( final UpdateColumnDataEvent event ) {

        if ( hasTheRowCountIncreased( event ) ) {

            addRow( eventManager.getNewIndex() );
            analyze();

        } else if ( hasTheRowCountDecreased( event ) ) {

            RowInspector removed = cache.removeRow( eventManager.rowDeleted );
            checks.remove( removed );

            analyze();
        }

        eventManager.clear();
    }

    private boolean hasTheRowCountDecreased( final UpdateColumnDataEvent event ) {
        return cache.all().size() > event.getColumnData().size();
    }

    private boolean hasTheRowCountIncreased( final UpdateColumnDataEvent event ) {
        return cache.all().size() < event.getColumnData().size();
    }

    private void addRow( final int index ) {
        RowInspector rowInspector = cache.addRow( index,
                                                  model.getData().get( index ) );
        checks.add(rowInspector);
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
        if ( checks.isEmpty() ) {
            resetChecks();
            analyze();
        } else {
            sendReport( makeAnalysisReport() );
        }
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
