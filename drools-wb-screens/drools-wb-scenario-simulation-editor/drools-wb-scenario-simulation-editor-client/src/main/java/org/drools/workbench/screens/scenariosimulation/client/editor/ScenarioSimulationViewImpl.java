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

package org.drools.workbench.screens.scenariosimulation.client.editor;

import java.util.Map;

import javax.inject.Inject;

import com.google.gwt.user.client.Command;
import org.drools.workbench.screens.scenariosimulation.client.factories.ScenarioSimulationViewProvider;
import org.drools.workbench.screens.scenariosimulation.client.resources.i18n.ScenarioSimulationEditorConstants;
import org.drools.workbench.screens.scenariosimulation.client.widgets.ScenarioGridPanel;
import org.kie.workbench.common.widgets.metadata.client.KieEditorViewImpl;
import org.uberfire.workbench.model.menu.MenuItem;

/**
 * Implementation of the main view for the ScenarioSimulation editor.
 * <p>
 * This view contains a <code>ScenarioGridPanel</code>.
 */
public class ScenarioSimulationViewImpl
        extends KieEditorViewImpl
        implements ScenarioSimulationView {

    private ScenarioGridPanel scenarioGridPanel;
    private ScenarioSimulationEditorPresenter presenter;

    @Inject
    public ScenarioSimulationViewImpl() {
        this.scenarioGridPanel = ScenarioSimulationViewProvider.newScenarioGridPanel();
        initWidget(scenarioGridPanel);
    }

    @Override
    public void init(ScenarioSimulationEditorPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setContent(Map<Integer, String> headersMap, Map<Integer, Map<Integer, String>> rowsMap) {
        scenarioGridPanel.getScenarioGrid().setContent(headersMap, rowsMap);
    }

    @Override
    public ScenarioGridPanel getScenarioGridPanel() {
        return scenarioGridPanel;
    }

    @Override
    public MenuItem getRunScenarioMenuItem() {
        return new RunScenarioMenuItem(ScenarioSimulationEditorConstants.INSTANCE.RunScenario(),
                                       new Command() {
                                           @Override
                                           public void execute() {
                                               presenter.onRunScenario();
                                           }
                                       });
    }

    @Override
    public void setContent(Map<Integer, String> headersMap, Map<Integer, Map<Integer, String>> rowsMap) {
        scenarioGridPanel.getScenarioGrid().setContent(headersMap, rowsMap);
    }

}