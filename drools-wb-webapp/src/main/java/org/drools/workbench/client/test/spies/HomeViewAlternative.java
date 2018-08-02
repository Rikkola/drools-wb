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

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.inject.Alternative;
import javax.inject.Inject;

import com.google.gwt.core.client.GWT;
import org.drools.workbench.client.test.ListOfSpies;
import org.jboss.errai.ioc.client.api.AfterInitialization;
import org.jboss.errai.ui.shared.api.annotations.Templated;
import org.kie.workbench.common.screens.home.client.widgets.home.HomeView;
import org.kie.workbench.common.screens.home.client.widgets.shortcut.ShortcutPresenter;

@Alternative
@Templated("/org/kie/workbench/common/screens/home/client/widgets/home/HomeView.html")
public class HomeViewAlternative extends HomeView {

    private List<ShortcutViewAlternative> shortcuts = new ArrayList<>();

    @Inject
    ListOfSpies listOfSpies;

    @AfterInitialization
    public void hello() {
        listOfSpies.add(this);
    }

    @Override
    public void addShortcut(final ShortcutPresenter shortcutPresenter) {
        GWT.log("adding a shortcut");

        shortcuts.add((ShortcutViewAlternative) shortcutPresenter.getView());

        super.addShortcut(shortcutPresenter);
    }

    public List<ShortcutViewAlternative> getShortcuts() {
        return shortcuts;
    }

    @Override
    public String toString() {
        return "HomeViewAlternative{" +
                "shortcuts=" + shortcuts +
                '}';
    }
}
