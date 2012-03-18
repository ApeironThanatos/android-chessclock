/*
 * LccFriendStatusListener.java
 */

package com.chess.lcc.android;

import com.chess.live.client.FriendStatusListener;
import com.chess.live.client.User;

public class LccFriendStatusListener implements FriendStatusListener {
	public LccFriendStatusListener(LccHolder lccHolder) {
		if (lccHolder == null) {
			throw new NullPointerException("LccHolder is null");
		}
		this.lccHolder = lccHolder;
	}

	public void onFriendStatusReceived(User friend) {
		LccHolder.LOG.info("FRIENDS STATUS LISTENER: onFriendStatusReceived " + friend);
		lccHolder.putFriend(friend);
	}

	public void onFriendRequested(User from, User to) {
		/*LccUser.LOG.info("FRIENDS STATUS LISTENER: onFriendRequested from " + from + " to " + to);

			if (user.getUser().getUsername().equals(from.getUsername()))
			{
			  return;
			}
			if(user.getConnection().isUserBlocked(from.getUsername()))
			{
			  LccUser.LOG.info("onFriendRequested: blocked user");
			  return;
			}
			I18n i18n = I18n.get(getClass());
			Object result = i18n.acceptOrDecline(OptionPanel.ACCEPT, "friendRequested", new Object[]{from.getUsername()});
			if (result == OptionPanel.ACCEPT)
			{
			  user.getClient().acceptFriendRequest(from, this);
			}
			else
			{
			  user.getClient().declineFriendRequest(from, this);
			}*/
	}

	public void onFriendDeleted(User from, User to) {
		LccHolder.LOG.info("FRIENDS STATUS LISTENER: onFriendDeleted from " + from + " to " + to);
		User deletedFriend = null;
		if (lccHolder.getUser().getUsername().equals(from.getUsername())) {
			deletedFriend = to;
		} else if (lccHolder.getUser().getUsername().equals(to.getUsername())) {
			deletedFriend = from;
		}
		lccHolder.removeFriend(deletedFriend);
	}

	public void onFriendRequestAccepted(User from, User to) {
		/*LccUser.LOG.info("FRIENDS STATUS LISTENER: onFriendRequestAccepted from " + from + " to " + to);
			if(user.getConnection().isUserBlocked(from.getUsername()))
			{
			  LccUser.LOG.info("onFriendRequestAccepted: blocked user");
			  return;
			}
			if (user.getUser().getUsername().equals(from.getUsername()))
			{
			  // if friend request by me ("from"), accepted by "to"
			  user.getConnection().fireFriendsEvent(to);
			}
			else if (user.getUser().getUsername().equals(to.getUsername()))
			{
			  // if friend request by "from", accepted by me "to"
			  user.getConnection().fireFriendsEvent(from);
			}*/
	}

	public void onFriendRequestDeclined(User from, User to) {
		/*LccUser.LOG.info("FRIENDS STATUS LISTENER: onFriendRequestDeclined from " + from + " to " + to);*/
	}

	private final LccHolder lccHolder;
}
