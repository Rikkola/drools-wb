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
package org.drools.workbench.client.test;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.drools.workbench.client.test.spies.ActivityManagerAlternative;
import org.drools.workbench.client.test.spies.HomeViewAlternative;
import org.drools.workbench.client.test.spies.ImportProjectsViewAlternative;
import org.drools.workbench.client.test.spies.LibraryViewAlternative;
import org.kie.workbench.common.screens.home.client.widgets.home.HomePresenter;
import org.kie.workbench.common.screens.library.client.screens.EmptyLibraryScreen;
import org.kie.workbench.common.screens.library.client.screens.importrepository.ImportPresenter;

@Dependent
public class OpenAnAssetTest
        implements Test {

    @Inject
    ActivityManagerAlternative activityManager;

    @Override
    public void run() {

        activityManager.doOnceOpen("org.kie.workbench.common.screens.home.client.HomePresenter",
                                   (ActivityManagerAlternative.ScreenOpenCallback<HomePresenter, HomeViewAlternative>) (presenter, view)
                                           -> view.getShortcuts().get(0).getLinks().get(0).onGoToPerspective());

        activityManager.doOnceOpen("LibraryScreen",
                                   (ActivityManagerAlternative.ScreenOpenCallback<EmptyLibraryScreen, LibraryViewAlternative>) (presenter, view)
                                           -> presenter.trySamples());

        activityManager.doOnceOpen("TrySamplesScreen",
                                   (ActivityManagerAlternative.ScreenOpenCallback<ImportPresenter, ImportProjectsViewAlternative>) (presenter, view)
                                           -> view.whenProjectIsAdded(
                                           exampleProjectWidget -> {
                                               if (exampleProjectWidget.getName().equals("Mortgages")) {

                                                   exampleProjectWidget.select();

                                                   view.getPresenter().ok();
                                               }
                                           }));
    }
}
