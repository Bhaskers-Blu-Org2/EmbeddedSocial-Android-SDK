package com.microsoft.socialplus.server.model.account;

import com.microsoft.autorest.models.LinkedAccountView;
import com.microsoft.autorest.models.UserProfileView;
import com.microsoft.rest.ServiceException;
import com.microsoft.rest.ServiceResponse;
import com.microsoft.socialplus.server.model.UserRequest;
import com.microsoft.socialplus.server.model.view.UserAccountView;

import java.io.IOException;
import java.util.List;

public class GetUserAccountRequest extends UserRequest {

    @Override
    public GetUserAccountResponse send() throws ServiceException, IOException {
        ServiceResponse<UserProfileView> myProfileResponse =
                USERS.getMyProfile(bearerToken);
        ServiceResponse<List<LinkedAccountView>> myLinkedAccountsResponse =
                LINKED_ACCOUNTS.getLinkedAccounts(bearerToken);
        return new GetUserAccountResponse(
                new UserAccountView(myProfileResponse.getBody(), myLinkedAccountsResponse.getBody()));
    }
}
