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
package org.drools.workbench.client;

import java.util.Date;
import java.util.Stack;

import javax.inject.Inject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.user.client.ui.RootPanel;
import org.drools.workbench.client.test.Test;
import org.jboss.errai.ioc.client.api.AfterInitialization;
import org.jboss.errai.ioc.client.api.EntryPoint;

@EntryPoint
public class TestRunner {

    private Stack<KeyPressed> code;

    private long lastPress = new Date().getTime();

    @Inject
    Test test;

    @Inject
    public TestRunner() {
        fillStack();
    }

    protected void fillStack() {
        code = new Stack<>();
        code.add(new KeyPressed(KeyCodes.KEY_D));
        code.add(new KeyPressed(KeyCodes.KEY_Q));
        code.add(new KeyPressed(KeyCodes.KEY_D));
        code.add(new KeyPressed(KeyCodes.KEY_D));
        code.add(new KeyPressed(KeyCodes.KEY_I));
    }

    @AfterInitialization
    public void initTestRunner() {

        GWT.log("TEST Runner starting");

        RootPanel.get().addDomHandler(
                keyDownEvent -> {
                    if (lastPress + 2000 < new Date().getTime()) {
                        fillStack();
                    }

                    lastPress = new Date().getTime();

                    if (code.isEmpty()) {
                        return;
                    } else if (code.peek() != null && code.peek().matches(keyDownEvent.getNativeKeyCode())) {
                        code.pop();
                    }
                    if (code.isEmpty()) {
                        GWT.log("Start test ");
                        test.run();
            }
        }, KeyDownEvent.getType());
    }

    class KeyPressed {

        private int keyCode;

        public KeyPressed(int keyCode) {
            this.keyCode = keyCode;
        }

        public boolean matches(int keyCode) {
            return this.keyCode == keyCode;
        }
    }
}
