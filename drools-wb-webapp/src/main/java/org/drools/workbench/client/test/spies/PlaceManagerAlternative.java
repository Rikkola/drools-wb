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

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.inject.Alternative;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HasWidgets;
import org.jboss.errai.common.client.dom.HTMLElement;
import org.uberfire.backend.vfs.Path;
import org.uberfire.client.mvp.Activity;
import org.uberfire.client.mvp.PlaceManagerImpl;
import org.uberfire.mvp.PlaceRequest;
import org.uberfire.workbench.model.PanelDefinition;
import org.uberfire.workbench.model.PartDefinition;

@Alternative
public class PlaceManagerAlternative
        extends PlaceManagerImpl {

    private Map<String, Object> presenters = new HashMap<>();

    public Map<PlaceRequest, Activity> getExistingWorkbenchActivities() {
        return existingWorkbenchActivities;
    }

    public Map<PlaceRequest, PartDefinition> getVisibleWorkbenchParts() {
        return visibleWorkbenchParts;
    }

    @Override
    public void goTo(String identifier, PanelDefinition panel) {
        GWT.log("GO TO 1 " + identifier);
        super.goTo(identifier, panel);
    }

    @Override
    public void goTo(String identifier) {
        GWT.log("GO TO 2 " + identifier);
        super.goTo(identifier);
    }

    @Override
    public void goTo(PlaceRequest place) {
        GWT.log("GO TO 3 " + place.getIdentifier());
        super.goTo(place);
    }

    @Override
    public void goTo(Path path, PanelDefinition panel) {
        super.goTo(path, panel);
    }

    @Override
    public void goTo(Path path) {
        super.goTo(path);
    }

    @Override
    public void goTo(Path path, PlaceRequest placeRequest, PanelDefinition panel) {
        GWT.log("GO TO 4 " + placeRequest.getIdentifier());
        super.goTo(path, placeRequest, panel);
    }

    @Override
    public void goTo(Path path, PlaceRequest placeRequest) {
        GWT.log("GO TO 5 " + placeRequest.getIdentifier());
        super.goTo(path, placeRequest);
    }

    @Override
    public void goTo(PlaceRequest place, PanelDefinition panel) {
        GWT.log("GO TO 6 " + place.getIdentifier());
        super.goTo(place, panel);
    }

    @Override
    public void goTo(PlaceRequest place, HasWidgets addTo) {
        GWT.log("GO TO 7 " + place.getIdentifier());
        super.goTo(place, addTo);
    }

    @Override
    public void goTo(PlaceRequest place, HTMLElement addTo) {
        GWT.log("GO TO 8 " + place.getIdentifier());
        super.goTo(place, addTo);
    }

    @Override
    public void goTo(PartDefinition part, PanelDefinition panel) {
        GWT.log("GO TO 9 " + part.getPlace().getIdentifier());

        super.goTo(part, panel);
    }
}
