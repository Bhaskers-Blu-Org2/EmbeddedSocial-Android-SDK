/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for license information.
 */

package com.microsoft.embeddedsocial.server.model.notification;

import com.microsoft.embeddedsocial.autorest.models.FeedResponseActivityView;
import com.microsoft.embeddedsocial.server.model.ListResponse;
import com.microsoft.embeddedsocial.server.model.view.ActivityView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GetNotificationFeedResponse extends ListResponse<ActivityView> {

    private List<ActivityView> activities;
    // Setting this handle is required to properly update the "last-read" notification
    private String deliveredActivityHandle;

    public GetNotificationFeedResponse(@NotNull List<ActivityView> activities) {
        this.activities = activities;
        if (activities == null) {
            this.deliveredActivityHandle = "";
            return;
        }
        this.deliveredActivityHandle = (activities.isEmpty() ? "" : activities.get(0).getHandle());
    }

    public GetNotificationFeedResponse(@NotNull FeedResponseActivityView response) {
        activities = new ArrayList<>();
        if (response == null) {
            this.deliveredActivityHandle = "";
            return;
        }
        for (com.microsoft.embeddedsocial.autorest.models.ActivityView view : response.getData()) {
            activities.add(new ActivityView(view));
        }
        // Update the deliveredActivityHandle to be the most recent activity handle
        this.deliveredActivityHandle = (activities.isEmpty() ? "" : activities.get(0).getHandle());
        setContinuationKey(response.getCursor());
    }

    @Override
    public List<ActivityView> getData() {
        return activities != null ? activities : Collections.emptyList();
    }

    public String getDeliveredActivityHandle() {
        return deliveredActivityHandle;
    }
}
