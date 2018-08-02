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

import org.jboss.errai.ui.shared.api.annotations.Templated;
import org.kie.workbench.common.screens.library.client.screens.importrepository.ImportPresenter;
import org.kie.workbench.common.screens.library.client.screens.importrepository.ImportProjectsView;
import org.kie.workbench.common.screens.library.client.widgets.example.ExampleProjectWidget;
import org.uberfire.client.callbacks.Callback;

@Alternative
@Templated("/org/kie/workbench/common/screens/library/client/screens/importrepository/ImportProjectsView.html")
public class ImportProjectsViewAlternative
        extends ImportProjectsView {

    private ImportPresenter presenter;
    private String title;
    private Callback<ExampleProjectWidget> waitForProjectCallback;

    @Override
    public void addProject(ExampleProjectWidget projectWidget) {

        if (waitForProjectCallback != null) {
            waitForProjectCallback.callback(projectWidget);
        }

        super.addProject(projectWidget);
    }

    @Override
    public void setTitle(final String title) {
        this.title = title;
        super.setTitle(title);
    }

    @Override
    public void init(final ImportPresenter presenter) {
        this.presenter = presenter;
        super.init(presenter);
    }

    public ImportPresenter getPresenter() {
        return presenter;
    }

    public String getTitle() {
        return title;
    }

    public void whenProjectIsAdded(Callback<ExampleProjectWidget> waitForProjectCallback) {
        this.waitForProjectCallback = waitForProjectCallback;
    }
}
