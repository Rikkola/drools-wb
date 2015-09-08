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

package org.drools.workbench.screens.guided.dtable.analysis.cache;

import java.util.ArrayList;
import java.util.Collection;

import org.drools.workbench.screens.guided.dtable.analysis.RowInspector;
import org.jboss.errai.common.client.api.annotations.Portable;

@Portable
public class RowInspectorCache {

    private final RowInspectorList rowInspectorList = new RowInspectorList();
    private final Conditions conditions = new Conditions();

    public Collection<RowInspector> all() {
        return rowInspectorList.getSortedList();
    }

    public Collection<RowInspector> all( final Filter filter ) {
        ArrayList<RowInspector> result = new ArrayList<RowInspector>();
        for (RowInspector rowInspector : all()) {
            if ( filter.accept( rowInspector ) ) {
                result.add( rowInspector );
            }
        }
        return result;
    }

    public Conditions getConditions() {
        return conditions;
    }



    public RowInspector removeRow( final int rowNumber ) {
        RowInspector removed = rowInspectorList.removeRowInspector( rowNumber );
        rowInspectorList.decreaseRowNumbers( rowNumber );
        return removed;
    }

    public RowInspectorList getRowInspectors() {
        return rowInspectorList;
    }
}