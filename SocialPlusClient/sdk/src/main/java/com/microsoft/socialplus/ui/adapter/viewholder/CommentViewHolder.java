/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for license information.
 *
 */

package com.microsoft.socialplus.ui.adapter.viewholder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.microsoft.socialplus.base.utils.ViewUtils;
import com.microsoft.socialplus.sdk.R;
import com.microsoft.socialplus.server.model.view.CommentView;
import com.microsoft.socialplus.ui.adapter.QuantityStringUtils;
import com.microsoft.socialplus.ui.theme.ThemeAttributes;
import com.microsoft.socialplus.ui.util.ButtonStyleHelper;

/**
 * Init comment view layout.
 */
public class CommentViewHolder extends UserHeaderViewHolder {
	private final CommentButtonListener commentButtonListener;

	private View commentRootView;
	private TextView commentText;

	private TextView commentLikesCountButton;
	private TextView commentRepliesCountButton;
	private ImageView commentButton;
	private ImageView likeButton;
	private ViewGroup contentButton;
	private ButtonStyleHelper buttonStyleHelper;

	public static CommentViewHolder create(
		CommentButtonListener commentButtonListener,
		ViewGroup parent,
		HolderType holderType) {
		View view;
		if (holderType == HolderType.CONTENT) {
			view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sp_layout_comment, parent, false);
		} else {
			view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sp_layout_feed_comment, parent, false);
		}
		return new CommentViewHolder(commentButtonListener, view);
	}

	public CommentViewHolder(CommentButtonListener commentButtonListener, View view) {
		super(view);
		this.commentRootView = view.findViewById(R.id.sp_comment_root);
		this.commentButtonListener = commentButtonListener;
		this.buttonStyleHelper = new ButtonStyleHelper(view.getContext());
		initViews(view);
	}

	public void renderItem(int position, CommentView comment) {
		if (comment == null) {
			return;
		}
		renderUserHeader(comment.getUser(), comment.getHandle(), comment.getElapsedSeconds());

		commentText.setText(comment.getCommentText());

		long totalLikes = comment.getTotalLikes();
		commentLikesCountButton.setText(
			commentLikesCountButton.getResources().getQuantityString(R.plurals.sp_topic_likes_pattern,
					QuantityStringUtils.convertLongToInt(totalLikes),
					totalLikes));

		long totalReplies = comment.getTotalReplies();
		commentRepliesCountButton.setText(
			commentLikesCountButton.getResources().getQuantityString(R.plurals.sp_topic_replies_pattern,
					QuantityStringUtils.convertLongToInt(totalReplies),
					totalReplies));

		likeButton.setTag(R.id.sp_keyHandle, comment.getHandle());
		likeButton.setTag(R.id.sp_keyIsAdd, !comment.isLikeStatus());
		buttonStyleHelper.applyAccentColor(likeButton, comment.isLikeStatus());

		ViewUtils.setVisible(commentButton, !comment.isLocal());
		ViewUtils.setVisible(likeButton, !comment.isLocal());
		int actionViewsVisibility = comment.isLocal() ? View.INVISIBLE : View.VISIBLE;
		commentLikesCountButton.setVisibility(actionViewsVisibility);
		commentRepliesCountButton.setVisibility(actionViewsVisibility);
		contentButton.setVisibility((comment.isLocal() || position == 0) ? View.GONE : View.VISIBLE);
		contentButton.setOnClickListener(comment.isLocal() ? null : commentButtonListener::onClickContent);

		commentLikesCountButton.setTag(R.id.sp_keyHandle, comment.getHandle());
		commentRepliesCountButton.setTag(R.id.sp_keyHandle, comment.getHandle());
		commentRepliesCountButton.setTag(R.id.sp_keyPosition, position);
		commentRepliesCountButton.setTag(R.id.sp_keyComment, comment);
		commentButton.setTag(R.id.sp_keyHandle, comment.getHandle());
		commentButton.setTag(R.id.sp_keyComment, comment);
		contentButton.setTag(R.id.sp_keyComment, comment);

		contextMenuButton.setTag(R.id.sp_keyComment, comment);

		setCommentBackgroundColor(comment);
	}

	private void setCommentBackgroundColor(CommentView comment) {
		int commentBackground = comment.isLocal()
			? ThemeAttributes.getColor(getContext(), R.styleable.sp_AppTheme_sp_uploadingItemColor)
			: getResources().getColor(R.color.sp_comment_background);
		commentRootView.setBackgroundColor(commentBackground);
	}

	public void renderSingleItem(CommentView comment) {
		if (comment == null) {
			return;
		}
		renderUserHeader(comment.getUser(), comment.getHandle(), comment.getElapsedSeconds());
		contextMenuButton.setTag(R.id.sp_keyComment, comment);
		commentText.setText(comment.getCommentText());

		long totalLikes = comment.getTotalLikes();
		commentLikesCountButton.setText(
				commentLikesCountButton.getResources().getQuantityString(R.plurals.sp_topic_likes_pattern,
						QuantityStringUtils.convertLongToInt(totalLikes),
						totalLikes));
		commentLikesCountButton.setTag(R.id.sp_keyHandle, comment.getHandle());

		commentRepliesCountButton.setVisibility(View.GONE);
		likeButton.setVisibility(View.GONE);
		commentButton.setVisibility(View.GONE);
	}

	private void initViews(View view) {
		setContextMenuClickListener(commentButtonListener::onClickContextMenu);
		setHeaderClickable(true);

		contentButton = (ViewGroup) view.findViewById(R.id.sp_contentButton);

		commentText = (TextView) view.findViewById(R.id.sp_commentText);
		commentLikesCountButton = (TextView) view.findViewById(R.id.sp_commentLikesCountButton);
		buttonStyleHelper.applyAccentColor(commentLikesCountButton);
		commentRepliesCountButton = (TextView) view.findViewById(R.id.sp_commentRepliesCountButton);
		buttonStyleHelper.applyAccentColor(commentRepliesCountButton);
		commentButton = (ImageView) view.findViewById(R.id.sp_commentButton);
		likeButton = (ImageView) view.findViewById(R.id.sp_likeButton);

		commentLikesCountButton.setOnClickListener(commentButtonListener::onClickLikesCount);
		commentRepliesCountButton.setOnClickListener(commentButtonListener::onClickRepliesCount);
		commentButton.setOnClickListener(commentButtonListener::onClickComment);
		likeButton.setOnClickListener(commentButtonListener::onClickLike);
	}
}
