/*
 * Copyright 2020 Red Hat, Inc. and/or its affiliates.
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
package org.drools.workbench.screens.guided.dtable.backend.server.conversion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.drools.workbench.models.datamodel.rule.ActionInsertFact;
import org.drools.workbench.models.guided.dtable.backend.GuidedDTDRLPersistence;
import org.drools.workbench.models.guided.dtable.shared.model.BRLActionColumn;
import org.drools.workbench.models.guided.dtable.shared.model.BRLConditionColumn;
import org.drools.workbench.models.guided.dtable.shared.model.BRLVariableColumn;
import org.drools.workbench.models.guided.dtable.shared.model.DTCellValue52;
import org.drools.workbench.models.guided.dtable.shared.model.GuidedDecisionTable52;

public class BRLColumnSubHeaderBuilder {

    private int BRLActionColumnCount;
    private final GuidedDecisionTable52 dtable;
    final Map<String, String> varListInOrder = new HashMap<>();// original, new one

    public BRLColumnSubHeaderBuilder(final GuidedDecisionTable52 dtable,
                                     final int BRLActionColumnCount) {

        this.dtable = dtable;
        this.BRLActionColumnCount = BRLActionColumnCount;
    }

    public String brlActions(final BRLActionColumn brlColumn) {

        final GuidedDecisionTable52 dt = new GuidedDecisionTable52();
        dt.getConditions().addAll(dtable.getConditions());
        final ArrayList<DTCellValue52> list = new ArrayList<>();

        for (Object o : brlColumn.getDefinition()) {
            if (o instanceof ActionInsertFact) {
                ActionInsertFact insertFact = (ActionInsertFact) o;
                if (insertFact.getBoundName() == null) {
                    insertFact.setBoundName("brlColumnFact" + BRLActionColumnCount++);
                }
            }
        }

        for (int i = 0; i < dt.getExpandedColumns().size(); i++) {
            list.add(dtable.getData().get(0).get(i));
        }

        list.addAll(setUpVarNamesWithTemps(brlColumn.getChildColumns()));
        dt.getData().add(list);

        dt.getActionCols().add(brlColumn);

        return replaceTempVars(brlColumn.getChildColumns(),
                               subString(GuidedDTDRLPersistence.getInstance().marshal(dt), "then", "end"));
    }

    public String brlConditions(final BRLConditionColumn brlColumn) {

        final GuidedDecisionTable52 dt = new GuidedDecisionTable52();
        dt.getConditions().add(brlColumn);
        final ArrayList<DTCellValue52> list = new ArrayList<>();
        list.add(new DTCellValue52(1));
        list.add(new DTCellValue52(""));

        list.addAll(setUpVarNamesWithTemps(brlColumn.getChildColumns()));
        dt.getData().add(list);

        return replaceTempVars(brlColumn.getChildColumns(),
                               subString(GuidedDTDRLPersistence.getInstance().marshal(dt), "when", "then"));
    }

    private List<DTCellValue52> setUpVarNamesWithTemps(final List<? extends BRLVariableColumn> childColumns) {
        final List<DTCellValue52> result = new ArrayList<>();

        for (int i = 0; i < childColumns.size(); i++) {
            final BRLVariableColumn brlConditionVariableColumn = childColumns.get(i);
            if (brlConditionVariableColumn.getVarName().isEmpty()) {
                result.add(new DTCellValue52(true));
            } else {
                String varName = brlConditionVariableColumn.getVarName();
                String key = "";
                if (!varName.startsWith("$")) {
                    key = varName;
                    varName = "@{" + varName + "}";
                } else {
                    key = Integer.toString(i);
                    varName = "@{" + i + "}";
                }
                varListInOrder.put(brlConditionVariableColumn.getVarName(), key);
                result.add(new DTCellValue52(varName));
            }
        }

        return result;
    }

    private String replaceTempVars(final List<? extends BRLVariableColumn> childColumns,
                                   final String marshal) {
        int varIndex = 1;
        String result = marshal;
        for (BRLVariableColumn childColumn : childColumns) {

            final String var = varListInOrder.get(childColumn.getVarName());

            final Pattern pattern = Pattern.compile("@\\{(" + var + ")}");
            final Matcher matcher = pattern.matcher(result);
            result = matcher.replaceAll("\\$" + varIndex++);
        }
        return result;
    }

    private String subString(final String marshal,
                             final String from,
                             final String to) {

        final StringBuilder builder = new StringBuilder();

        boolean found = false;
        for (String row : marshal.split("\\r?\\n")) {
            if (found && Objects.equals(row.trim(), to)) {
                break;
            }
            if (found) {
                final String trim = row.trim();
                if (!trim.isEmpty()) {
                    builder.append(trim);
                    builder.append("\n");
                }
            }
            if (!found && Objects.equals(row.trim(), from)) {
                found = true;
            }
        }

        return builder.toString();
    }

    public int getBRLActionColumnCount() {
        return BRLActionColumnCount;
    }
}
