/*
 * Copyright 2014 JBoss Inc
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
package org.drools.workbench.screens.globals.backend.server.util.indexing;

import javax.enterprise.context.ApplicationScoped;

import org.drools.workbench.screens.globals.backend.server.indexing.GlobalsFileIndexer;
import org.drools.workbench.screens.globals.type.GlobalResourceTypeDefinition;
import org.kie.workbench.common.services.refactoring.backend.server.TestIndexer;
import org.uberfire.io.IOService;

/**
 * Test indexer
 */
@ApplicationScoped
public class TestGlobalsFileIndexer extends GlobalsFileIndexer implements TestIndexer<GlobalResourceTypeDefinition> {

    @Override
    public void setIOService( final IOService ioService ) {
        this.ioService = ioService;
    }

    @Override
    public void setResourceTypeDefinition( final GlobalResourceTypeDefinition type ) {
        this.type = type;
    }

}
