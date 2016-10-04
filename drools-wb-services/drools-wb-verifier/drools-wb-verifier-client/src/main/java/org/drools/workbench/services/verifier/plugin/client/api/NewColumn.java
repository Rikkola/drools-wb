/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.drools.workbench.services.verifier.plugin.client.api;

import org.drools.workbench.models.guided.dtable.shared.model.GuidedDecisionTable52;
import org.jboss.errai.common.client.api.annotations.MapsTo;
import org.jboss.errai.common.client.api.annotations.Portable;

@Portable
public class NewColumn {

    private final GuidedDecisionTable52 model;
    private final HeaderMetaData headerMetaData;
    private final FactTypes factTypes;
    private final int columnIndex;

    public NewColumn( @MapsTo("model") final GuidedDecisionTable52 model,
                      @MapsTo( "headerMetaData" ) HeaderMetaData headerMetaData,
                      @MapsTo("factTypes") final FactTypes factTypes,
                      @MapsTo("columnIndex") final int columnIndex ) {
        this.model = model;
        this.headerMetaData = headerMetaData;
        this.factTypes = factTypes;
        this.columnIndex = columnIndex;
    }

    public FactTypes getFactTypes() {
        return factTypes;
    }

    public GuidedDecisionTable52 getModel() {
        return model;
    }

    public HeaderMetaData getHeaderMetaData() {
        return headerMetaData;
    }

    public int getColumnIndex() {
        return columnIndex;
    }
}
