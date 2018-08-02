/*
 * Copyright 2018 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.drools.workbench.client.test.spies;

import javax.enterprise.inject.Alternative;
import javax.inject.Inject;

import org.drools.workbench.client.test.ListOfSpies;
import org.jboss.errai.ioc.client.api.AfterInitialization;
import org.jboss.errai.ui.shared.api.annotations.Templated;
import org.kie.workbench.common.screens.library.client.screens.HasView;
import org.kie.workbench.common.screens.library.client.screens.LibraryView;

@Alternative
@Templated("/org/kie/workbench/common/screens/library/client/screens/LibraryView.html")
public class LibraryViewAlternative
        extends LibraryView {

    private HasView hasView;

    @Inject
    ListOfSpies listOfSpies;

    @AfterInitialization
    public void hello() {

        listOfSpies.add(this);
//        Window.alert("HELLO");
    }

    @Override
    public void updateContent(HasView hasView) {
        this.hasView = hasView;

        super.updateContent(hasView);
    }

    public HasView getHasView() {
        return hasView;
    }
}
