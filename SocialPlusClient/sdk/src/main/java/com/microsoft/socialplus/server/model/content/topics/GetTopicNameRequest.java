/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for license information.
 *
 */

package com.microsoft.socialplus.server.model.content.topics;

import com.microsoft.socialplus.autorest.models.GetTopicNameResponse;
import com.microsoft.socialplus.autorest.models.PublisherType;
import com.microsoft.rest.ServiceException;
import com.microsoft.rest.ServiceResponse;
import com.microsoft.socialplus.server.exception.NetworkRequestException;
import com.microsoft.socialplus.server.model.UserRequest;

import java.io.IOException;

public class GetTopicNameRequest extends UserRequest {

    private String topicName;
    private PublisherType publisherType;

    public GetTopicNameRequest(String topicName, PublisherType publisherType) {
        this.topicName = topicName;
        this.publisherType = publisherType;
    }

    @Override
    public String send() throws NetworkRequestException {
        ServiceResponse<GetTopicNameResponse> serviceResponse;
        try {
            serviceResponse = TOPICS.getTopicName(topicName, publisherType, authorization);
        } catch (ServiceException|IOException e) {
            throw new NetworkRequestException(e.getMessage());
        }
        checkResponseCode(serviceResponse);

        return serviceResponse.getBody().getTopicHandle();
    }
}
