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
import java.util.Optional;

import javax.enterprise.inject.Alternative;

import com.google.gwt.core.client.GWT;
import org.kie.workbench.common.screens.home.client.widgets.home.HomePresenter;
import org.kie.workbench.common.screens.library.client.screens.HasView;
import org.uberfire.client.callbacks.Callback;
import org.uberfire.client.mvp.AbstractWorkbenchScreenActivity;
import org.uberfire.client.mvp.Activity;
import org.uberfire.client.mvp.ActivityManagerImpl;

@Alternative
public class ActivityManagerAlternative
        extends ActivityManagerImpl {

    private final Map<String, Callback<Activity>> map = new HashMap<>();

    public Optional<Activity> getActivityIfOpen( final String identifier) {
        for (Activity activity : startedActivities.keySet()) {

            GWT.log("ActivityManagerAlternative looking for : " +identifier + " found: " + activity.getName() + " " + activity.getPlace());

            if (activity.getPlace().getIdentifier().equals(identifier)) {
                return Optional.of(activity);
            }
        }

        return Optional.empty();
    }

    public <P, V> void doOnceOpen(final String placeId,
                                  final ScreenOpenCallback<P, V> activityCallback) {

        final Optional<Activity> activityOptional = getActivityIfOpen(placeId);

        if (activityOptional.isPresent()) {
            final Activity activity = activityOptional.get();
            final V view = (V) ((HomePresenter) ((AbstractWorkbenchScreenActivity) activity).getRealPresenter()).getView();
            final P presenter = (P) ((HasView<V>) view).getView();

            activityCallback.callback(presenter, view);
        } else {
            map.put(placeId,
                    result -> {
                        final Activity activity = activityOptional.get();
                        final V view = (V) ((HomePresenter) ((AbstractWorkbenchScreenActivity) activity).getRealPresenter()).getView();
                        final P presenter = (P) ((HasView<V>) view).getView();
                        activityCallback.callback(presenter, view);
                    });
        }
    }

    public interface ScreenOpenCallback<P, V> {

        void callback(P presenter,
                      V view);
    }
}
