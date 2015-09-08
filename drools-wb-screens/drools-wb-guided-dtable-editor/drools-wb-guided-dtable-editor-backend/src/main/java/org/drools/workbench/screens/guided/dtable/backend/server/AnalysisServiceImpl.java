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

package org.drools.workbench.screens.guided.dtable.backend.server;

import javax.enterprise.context.ApplicationScoped;

import org.drools.workbench.models.guided.dtable.shared.model.GuidedDecisionTable52;
import org.drools.workbench.screens.guided.dtable.analysis.AnalysisContent;
import org.drools.workbench.screens.guided.dtable.analysis.AnalysisDecisionTableUtilsServerSide;
import org.drools.workbench.screens.guided.dtable.analysis.RowInspectorGenerator;
import org.drools.workbench.screens.guided.dtable.analysis.cache.RowInspectorCache;
import org.drools.workbench.screens.guided.dtable.service.AnalysisService;
import org.jboss.errai.bus.server.annotations.Service;

@Service
@ApplicationScoped
public class AnalysisServiceImpl
        implements AnalysisService {

    @Override
    public AnalysisContent createCache( GuidedDecisionTable52 model ) {
        RowInspectorCache cache = new RowInspectorCache();

        new RowInspectorGenerator( new AnalysisDecisionTableUtilsServerSide(),
                                   model,
                                   cache );

        return new AnalysisContent();
    }

}
