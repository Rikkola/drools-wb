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

package org.drools.workbench.screens.guided.dtable.analysis;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.drools.workbench.models.guided.dtable.shared.model.ActionCol52;
import org.drools.workbench.models.guided.dtable.shared.model.ActionInsertFactCol52;
import org.drools.workbench.models.guided.dtable.shared.model.ActionSetFieldCol52;
import org.drools.workbench.models.guided.dtable.shared.model.BRLActionColumn;
import org.drools.workbench.models.guided.dtable.shared.model.ConditionCol52;
import org.drools.workbench.models.guided.dtable.shared.model.DTCellValue52;
import org.drools.workbench.models.guided.dtable.shared.model.DTColumnConfig52;
import org.drools.workbench.models.guided.dtable.shared.model.GuidedDecisionTable52;
import org.drools.workbench.models.guided.dtable.shared.model.LimitedEntryCol;
import org.drools.workbench.models.guided.dtable.shared.model.Pattern52;
import org.drools.workbench.screens.guided.dtable.analysis.action.ActionInspector;
import org.drools.workbench.screens.guided.dtable.analysis.action.ActionInspectorKey;
import org.drools.workbench.screens.guided.dtable.analysis.action.FactFieldColumnActionInspectorKey;
import org.drools.workbench.screens.guided.dtable.analysis.action.UnrecognizedActionInspectorKey;
import org.drools.workbench.screens.guided.dtable.analysis.cache.Coordinate;
import org.drools.workbench.screens.guided.dtable.analysis.cache.RowInspectorCache;
import org.drools.workbench.screens.guided.dtable.analysis.condition.ConditionInspector;
import org.drools.workbench.screens.guided.dtable.analysis.condition.ConditionInspectorBuilder;

public class RowInspectorGenerator {

    private static final int DESCRIPTION_COLUMN = 1;

    private RowInspectorCache cache;
    private AnalysisDecisionTableUtils utils;
    private GuidedDecisionTable52 model;
    private RowInspector rowInspector;
    private List<DTCellValue52> row;

    private UpdateHandler updateHandler;

    public RowInspectorGenerator() {
    }

    public RowInspectorGenerator( AnalysisDecisionTableUtils utils,
                                  GuidedDecisionTable52 model,
                                  RowInspectorCache cache ) {
        this.cache = cache;
        this.utils = utils;
        this.model = model;

    }

    public void reset() {
        cache.getRowInspectors().clear();
        cache.getConditions().clear();
        for (RowInspector rowInspector : generate()) {
            add( rowInspector );
        }
    }

    private boolean add( final RowInspector rowInspector ) {
        boolean add = cache.getRowInspectors().add( rowInspector );
        cache.getConditions().addAll( rowInspector.getConditions().allValues() );
        return add;
    }
    public List<RowInspector> generate() {
        ArrayList<RowInspector> rowInspectors = new ArrayList<RowInspector>();

        int index = 0;
        for ( List<DTCellValue52> row : model.getData() ) {
            rowInspectors.add( generate( index,
                                         row ) );
            index++;
        }

        return rowInspectors;
    }

    public RowInspector generate( final int index,
                                  final List<DTCellValue52> row ) {
        this.row = row;

        rowInspector = new RowInspector( index,
                                         model.getTableFormat(),
                                         cache );

        addConditionInspectors();
        addActionInspector();

        return rowInspector;
    }

    public void updateRowInspectors( final Set<Coordinate> coordinates,
                                     final List<List<DTCellValue52>> data ) {
        for (Coordinate coordinate : coordinates) {
            if ( coordinate.getCol() != DESCRIPTION_COLUMN ) {
                RowInspector oldRow = cache.getRowInspectors().getRowInspector( coordinate.getRow() );
                List<DTCellValue52> row = data.get( coordinate.getRow() );
                RowInspector rowInspector = generate( coordinate.getRow(), row );

                updateHandler.updateRow( oldRow, rowInspector );
                cache.getRowInspectors().set( coordinate.getRow(), rowInspector );
            }
        }

    }
    private void addActionInspector() {
        for ( ActionCol52 actionCol : model.getActionCols() ) {

            //BRLActionColumns cannot be analysed
            if ( actionCol instanceof BRLActionColumn ) {
                continue;
            }

            int columnIndex = model.getExpandedColumns().indexOf( actionCol );
            if ( rowHasIndex( columnIndex ) ) {
                addActionInspector( actionCol,
                                    row.get( columnIndex ) );
            }

        }
    }

    private void addConditionInspectors() {
        for ( Pattern52 pattern : model.getPatterns() ) {
            for ( ConditionCol52 conditionCol : pattern.getChildColumns() ) {
                int columnIndex = model.getExpandedColumns().indexOf( conditionCol );

                if ( rowHasIndex( columnIndex ) ) {
                    addConditionInspector( pattern, conditionCol, row.get( columnIndex ) );
                }
            }
        }
    }

    public void setUpdateHandler( final UpdateHandler updateHandler ) {
        this.updateHandler = updateHandler;
    }

    public RowInspector addRow( final int index,
                                final List<DTCellValue52> row ) {
        RowInspector rowInspector = generate( index,
                                              row );
        cache.getRowInspectors().increaseRowNumbers( index );
        add( rowInspector );
        return rowInspector;
    }

    private boolean rowHasIndex( final int columnIndex ) {
        return columnIndex > 0 && columnIndex < row.size();
    }

    private void addActionInspector( final ActionCol52 actionCol,
                                     final DTCellValue52 visibleCellValue ) {
        // Blank cells are ignored
        if ( isCellNotBlank( actionCol,
                             visibleCellValue ) ) {
            rowInspector.addActionInspector( buildActionInspector( actionCol,
                                                                   visibleCellValue ) );
        }
    }

    private void addConditionInspector( final Pattern52 pattern,
                                        final ConditionCol52 conditionColumn,
                                        final DTCellValue52 visibleCellValue ) {
        // Blank cells are ignored
        if ( isCellNotBlank( conditionColumn,
                             visibleCellValue ) ) {
            rowInspector.addConditionInspector( buildConditionInspector( pattern,
                                                                         conditionColumn,
                                                                         visibleCellValue ) );
        }
    }

    private ConditionInspector buildConditionInspector( final Pattern52 pattern,
                                                        final ConditionCol52 conditionColumn,
                                                        final DTCellValue52 visibleCellValue ) {

        return new ConditionInspectorBuilder(
                utils,
                pattern,
                conditionColumn,
                getRealCellValue( conditionColumn,
                                  visibleCellValue )
        ).buildConditionInspector();

    }

    private ActionInspector buildActionInspector( final ActionCol52 actionCol,
                                                  final DTCellValue52 visibleCellValue ) {
        return new ActionInspector( utils,
                                    getKey( actionCol ),
                                    getRealCellValue( actionCol,
                                                      visibleCellValue ) );
    }

    private ActionInspectorKey getKey( final ActionCol52 actionCol ) {
        if ( actionCol instanceof ActionSetFieldCol52 ) {
            return new FactFieldColumnActionInspectorKey( (ActionSetFieldCol52) actionCol );
        } else if ( actionCol instanceof ActionInsertFactCol52 ) {
            return new FactFieldColumnActionInspectorKey( (ActionInsertFactCol52) actionCol );
        } else {
            return new UnrecognizedActionInspectorKey( actionCol );
        }
    }

    private DTCellValue52 getRealCellValue( final DTColumnConfig52 config52,
                                            final DTCellValue52 visibleCellValue ) {
        if ( config52 instanceof LimitedEntryCol ) {
            return ( (LimitedEntryCol) config52 ).getValue();
        } else {
            return visibleCellValue;
        }
    }

    private boolean isCellNotBlank( final DTColumnConfig52 config52,
                                    final DTCellValue52 visibleCellValue ) {
        if ( config52 instanceof LimitedEntryCol ) {
            return visibleCellValue.getBooleanValue();
        } else {
            return visibleCellValue.hasValue();
        }
    }
}
