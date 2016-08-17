/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for license information.
 *
 */

package com.microsoft.socialplus.server.model.relationship;

import com.microsoft.socialplus.autorest.UserFollowingOperations;
import com.microsoft.socialplus.autorest.UserFollowingOperationsImpl;
import com.microsoft.socialplus.autorest.models.FeedResponseUserCompactView;
import com.microsoft.rest.ServiceException;
import com.microsoft.rest.ServiceResponse;
import com.microsoft.socialplus.server.exception.NetworkRequestException;
import com.microsoft.socialplus.server.model.UsersListResponse;

import java.io.IOException;

public final class GetFollowingFeedRequest extends GetFollowFeedRequest {
    private static final UserFollowingOperations USER_FOLLOWING =
            new UserFollowingOperationsImpl(RETROFIT, CLIENT);

    public GetFollowingFeedRequest(String queryUserHandle) {
        super(queryUserHandle);
    }

    @Override
    public UsersListResponse send() throws NetworkRequestException {
        ServiceResponse<FeedResponseUserCompactView> serviceResponse;
        try {
            serviceResponse = USER_FOLLOWING.getFollowing(getQueryUserHandle(), authorization,
                    getCursor(), getBatchSize());
        } catch (ServiceException|IOException e) {
            throw new NetworkRequestException(e.getMessage());
        }
        checkResponseCode(serviceResponse);

        return new UsersListResponse(serviceResponse.getBody());
    }
}
