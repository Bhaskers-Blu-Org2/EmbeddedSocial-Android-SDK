/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for license information.
 *
 */

package com.microsoft.socialplus.server;

import com.microsoft.socialplus.server.exception.NetworkRequestException;
import com.microsoft.socialplus.server.model.blob.AddBlobRequest;

public interface IBlobService {

    String addBlob(AddBlobRequest request) throws NetworkRequestException;
}
