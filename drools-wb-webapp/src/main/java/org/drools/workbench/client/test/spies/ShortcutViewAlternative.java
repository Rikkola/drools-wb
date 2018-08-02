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

import com.google.gwt.core.client.GWT;
import org.jboss.errai.ui.shared.api.annotations.Templated;
import org.kie.workbench.common.screens.home.client.widgets.shortcut.ShortcutView;
import org.kie.workbench.common.screens.home.client.widgets.shortcut.subheading.ShortcutSubHeadingLinkPresenter;

@Alternative
@Templated("/org/kie/workbench/common/screens/home/client/widgets/shortcut/ShortcutView.html")
public class ShortcutViewAlternative
        extends ShortcutView {

    private List<ShortcutSubHeadingLinkPresenter> links = new ArrayList<>();

    @Override
    public void addSubHeadingChild(ShortcutSubHeadingLinkPresenter link) {

        GWT.log("ShortcutViewAlternative adding link " + link.toString());

        this.links.add(link);

        super.addSubHeadingChild(link);
    }

    public List<ShortcutSubHeadingLinkPresenter> getLinks() {
        return links;
    }
}
