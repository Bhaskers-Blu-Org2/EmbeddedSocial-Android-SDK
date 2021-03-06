/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for license information.
 */

package com.microsoft.embeddedsocial.service.worker;

import com.microsoft.embeddedsocial.base.utils.debug.DebugLog;
import com.microsoft.embeddedsocial.data.storage.ActivityCache;
import com.microsoft.embeddedsocial.data.storage.PostStorage;
import com.microsoft.embeddedsocial.data.storage.UserActionCache;
import com.microsoft.embeddedsocial.data.storage.UserCache;
import com.microsoft.embeddedsocial.fcm.FcmTokenHolder;
import com.microsoft.embeddedsocial.server.sync.DataSynchronizer;

import android.content.Context;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

/**
 * Synchronizes local data with the server
 */
public class SynchronizationWorker extends Worker {
    public static final String PENDING_POST_SYNC_NAME = "posts";
    private final DataSynchronizer synchronizer = new DataSynchronizer();

    public SynchronizationWorker(Context context, WorkerParameters workerParameters) {
        super(context, workerParameters);
        UserActionCache userActionCache = new UserActionCache();
        PostStorage postStorage = new PostStorage(context);
        synchronizer.registerSyncProducer(postStorage::getPendingPosts, PENDING_POST_SYNC_NAME);
        synchronizer.registerSyncProducer(postStorage::getPendingDiscussionItems, "comments/replies");
        synchronizer.registerSyncProducer(postStorage::getPendingEditedTopics, "topic edits");
        synchronizer.registerSyncProducer(userActionCache::getPendingLikeActions, "likes");
        synchronizer.registerSyncProducer(userActionCache::getPendingPinActions, "pins");
        synchronizer.registerSyncProducer(userActionCache::getPendingHideTopicActions, "hidden topics");
        synchronizer.registerSyncProducer(userActionCache::getPendingReportContentActions,
                "reported content");
        synchronizer.registerSyncProducer(new ActivityCache(context)::getActivityHandleSyncActions,
                "notification updates");
        synchronizer.registerSyncProducer(new UserCache()::getPendingUserRelationOperations,
                "user relations");
        synchronizer.registerSyncProducer(userActionCache::getPendingContentRemovalActions,
                "removals");
        synchronizer.registerSyncProducer(FcmTokenHolder.create(context)::getTokenSyncOperations,
                "fcm");
    }

    @Override
    public Result doWork() {
        // currently this should be synchronized in app scope
        synchronized (SynchronizationWorker.class) {
            boolean synced = synchronizer.synchronize();
            DebugLog.i(synced ? "sync succeeded" : "sync failed");
        }

        return Result.success();
    }
}
