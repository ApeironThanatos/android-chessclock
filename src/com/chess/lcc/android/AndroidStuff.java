/*
 * AndroidStuff.java
 */

package com.chess.lcc.android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import com.chess.backend.statics.AppConstants;
import com.chess.backend.statics.StaticData;
import com.chess.live.client.Challenge;
import com.chess.live.client.Game;
import com.chess.model.GameItem;
import com.chess.ui.activities.GameBaseActivity;
import com.chess.ui.activities.OnlineScreenActivity;
import com.chess.ui.core.IntentConstants;
import com.chess.ui.core.MainApp;
import com.chess.utilities.MyProgressDialog;

import java.io.Serializable;

public class AndroidStuff {
	private MainApp mainApp;
	private SharedPreferences sharedData;
	private SharedPreferences.Editor sharedDataEditor;
	private MyProgressDialog currentProgressDialog;
	private LccHolder lccHolder;
	private GameBaseActivity gameActivity;
	private Handler clockHandler = new Handler();
	private MyProgressDialog connectingIndicator;
	private MyProgressDialog reconnectingIndicator;

	public AndroidStuff(LccHolder lccHolder) {
		this.lccHolder = lccHolder;
	}

	public MainApp getMainApp() {
		return mainApp;
	}

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}

	public Context getContext(){
		return mainApp.getApplicationContext();
	}
	
	public void setCurrentProgressDialog(MyProgressDialog currentProgressDialog) {
		this.currentProgressDialog = currentProgressDialog;
	}

	public SharedPreferences getSharedData() {
		if (sharedData == null) {
			sharedData = mainApp.getSharedPreferences(StaticData.SHARED_DATA_NAME, 0);
		}
		return sharedData;
	}

	public SharedPreferences.Editor getSharedDataEditor() {
		if (sharedDataEditor == null) {
			sharedDataEditor = getSharedData().edit();
		}
		return sharedDataEditor;
	}

	public GameBaseActivity getGameActivity() {
		return gameActivity;
	}

	public void setGameActivity(GameBaseActivity gameActivity) {
		this.gameActivity = gameActivity;
	}

	public Handler getClockHandler() {
		return clockHandler;
	}

	/*public Handler getUpdateBoardHandler() {
		return updateBoardHandler;
	}*/

	/*public void sendConnectionBroadcastIntent(boolean result, int code, String... errorMessage) {
		lccHolder.getAndroid().getContext().sendBroadcast(new Intent(WebService.BROADCAST_ACTION)
				.putExtra(AppConstants.REPEATABLE_TASK, false)
				.putExtra(AppConstants.CALLBACK_CODE, code)
				.putExtra(AppConstants.REQUEST_RESULT,
						result ? RestHelper.R_SUCCESS : RestHelper.R_ERROR + errorMessage[0])
		);
	}*/

	public void sendBroadcastObjectIntent(int code, String broadcastAction, Serializable object) {
		LccHolder.LOG.info(AppConstants.LCCLOG_ANDROID_SEND_BROADCAST_OBJECT_INTENT_ACTION + broadcastAction);
		lccHolder.getAndroid().getMainApp().sendBroadcast(
				new Intent(broadcastAction)
						.putExtra(AppConstants.CALLBACK_CODE, code)
						.putExtra(AppConstants.OBJECT, object)
		);
		if (currentProgressDialog != null) {
			currentProgressDialog.dismiss();
		}
	}

	public void sendBroadcastMessageIntent(int code, String broadcastAction, String title, String message) {
		LccHolder.LOG.info(AppConstants.LCCLOG_ANDROID_SEND_BROADCAST_OBJECT_INTENT_ACTION + broadcastAction);
		lccHolder.getAndroid().getMainApp().sendBroadcast(
				new Intent(broadcastAction)
						.putExtra(AppConstants.CALLBACK_CODE, code)
						.putExtra(AppConstants.TITLE, title)
						.putExtra(AppConstants.MESSAGE, message)
		);
		if (currentProgressDialog != null) {
			currentProgressDialog.dismiss();
		}
	}

	public void sendBroadcastIntent(int code, String broadcastAction) {
		LccHolder.LOG.info(AppConstants.LCCLOG_ANDROID_SEND_BROADCAST_OBJECT_INTENT_ACTION + broadcastAction);
		lccHolder.getAndroid().getMainApp().sendBroadcast(
				new Intent(broadcastAction)
						.putExtra(AppConstants.CALLBACK_CODE, code)
		);
		if (currentProgressDialog != null) {
			currentProgressDialog.dismiss();
		}
	}

	public void updateChallengesList() {
		sendBroadcastIntent(OnlineScreenActivity.ONLINE_CALLBACK_CODE, IntentConstants.CHALLENGES_LIST_UPDATE);
	}

	public void processMove(long gameId, int moveIndex) {
//		final GameItem gameData = new GameItem(lccHolder.getGameData(gameId.toString(), moveIndex), true);
		final GameItem gameData = new GameItem(lccHolder.getGameData(gameId, moveIndex), true);
		lccHolder.getAndroid().sendBroadcastObjectIntent(9, IntentConstants.ACTION_GAME_MOVE, gameData);
	}

	public void processDrawOffered(String offererUsername) {
		lccHolder.getAndroid().sendBroadcastMessageIntent(0, IntentConstants.FILTER_DRAW_OFFERED, "DRAW OFFER",
				offererUsername + " has offered a draw");
	}

	public void processGameEnd(String message) {
		lccHolder.getAndroid().sendBroadcastMessageIntent(0, IntentConstants.ACTION_GAME_END, "GAME OVER", message);
	}

	public void setConnectingIndicator(MyProgressDialog connectingIndicator) {
		this.connectingIndicator = connectingIndicator;
	}

	public MyProgressDialog getConnectingIndicator() {
		return connectingIndicator;
	}

	public void setReconnectingIndicator(MyProgressDialog reconnectingIndicator) {
		this.reconnectingIndicator = reconnectingIndicator;
	}

	public MyProgressDialog getReconnectingIndicator() {
		return reconnectingIndicator;
	}

	public void manageProgressDialog(String broadcastAction, boolean enable, String message) {
		LccHolder.LOG.info(AppConstants.LCCLOG_ANDROID_SEND_BROADCAST_OBJECT_INTENT_ACTION + broadcastAction);
		lccHolder.getAndroid().getMainApp().sendBroadcast(
				new Intent(broadcastAction)
						.putExtra(AppConstants.ENABLE_LIVE_CONNECTING_INDICATOR, enable)
						.putExtra(AppConstants.MESSAGE, message)
		);
	}

	/*public void showLoggingInIndicator()
	  {
		manageProgressDialog("com.chess.lcc.android-logging-in-info", true, "Loading Live Chess");
	  }*/

	public void closeLoggingInIndicator() {
		manageProgressDialog(IntentConstants.FILTER_LOGINING_INFO, false, StaticData.SYMBOL_EMPTY);
	}

	public void showReconnectingIndicator() {
		manageProgressDialog(IntentConstants.FILTER_RECONNECT_INFO, true, "Reconnecting...");
	}

	public void closeReconnectingIndicator() {
		manageProgressDialog(IntentConstants.FILTER_RECONNECT_INFO, false, StaticData.SYMBOL_EMPTY);
	}

	public void informAndExit(String title, String message) {
		informAndExit(IntentConstants.FILTER_EXIT_INFO, title, message);
	}

	public void processOtherClientEntered() {
		informAndExit(IntentConstants.FILTER_EXIT_INFO, StaticData.SYMBOL_EMPTY, "Another login has been detected.");
	}

	public void informAndExit(String broadcastAction, String title, String message) {
		LccHolder.LOG.info(AppConstants.LCCLOG_ANDROID_SEND_BROADCAST_OBJECT_INTENT_ACTION + broadcastAction);
		lccHolder.getAndroid().getMainApp().sendBroadcast(
				new Intent(broadcastAction)
						.putExtra(AppConstants.TITLE, title)
						.putExtra(AppConstants.MESSAGE, message)
		);
	}

	public void processObsoleteProtocolVersion() {
		lccHolder.getAndroid().getMainApp().sendBroadcast(new Intent(IntentConstants.FILTER_PROTOCOL_VERSION));
	}

	/*public void startSigninActivity()
	  {
		getContext().getSharedDataEditor().putString("password", StaticData.SYMBOL_EMPTY);
		getContext().getSharedDataEditor().putString(AppConstants.USER_TOKEN, StaticData.SYMBOL_EMPTY);
		getContext().getSharedDataEditor().commit();
		final Intent intent = new Intent(mainApp, Singin.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		getContext().startActivity(intent);
	  }*/

	public void runSendChallengeTask(MyProgressDialog PD, Challenge challenge) {
		//this.CODE = CODE;
		//this.progressDialog = progressDialog;
		lccHolder.getAndroid().setCurrentProgressDialog(PD);
		new LiveSendChallengeTask().execute(challenge);
	}

	private class LiveSendChallengeTask extends AsyncTask<Challenge, Void, Void> {
		@Override
		protected Void doInBackground(Challenge... challenge) {
			lccHolder.getClient().sendChallenge(challenge[0], lccHolder.getChallengeListener());
			//stopSelf();
			return null;
		}
	}

	public void runCancelChallengeTask(Challenge challenge) {
		new LiveCancelChallengeTask().execute(challenge);
	}

	private class LiveCancelChallengeTask extends AsyncTask<Challenge, Void, Void> {
		@Override
		protected Void doInBackground(Challenge... challenge) {
			lccHolder.getClient().cancelChallenge(challenge[0]);
			//stopSelf();
			return null;
		}
	}

	public void runDisconnectTask() {
		new LiveDisconnectTask().execute();
	}

	private class LiveDisconnectTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... voids) {
			lccHolder.getClient().disconnect();
			return null;
		}
	}

	public void runMakeDrawTask(com.chess.live.client.Game game) {
		new LiveMakeDrawTask().execute(game);
	}

	private class LiveMakeDrawTask extends AsyncTask<com.chess.live.client.Game, Void, Void> {
		@Override
		protected Void doInBackground(com.chess.live.client.Game... game) {
			lccHolder.getClient().makeDraw(game[0], StaticData.SYMBOL_EMPTY);
			//stopSelf();
			return null;
		}
	}

	public void runMakeResignTask(com.chess.live.client.Game game) {
		new LiveMakeResignTask().execute(game);
	}

	private class LiveMakeResignTask extends AsyncTask<com.chess.live.client.Game, Void, Void> {
		@Override
		protected Void doInBackground(com.chess.live.client.Game... game) {
			lccHolder.getClient().makeResign(game[0], StaticData.SYMBOL_EMPTY);
			//stopSelf();
			return null;
		}
	}

	public void runAbortGameTask(com.chess.live.client.Game game) {
		new LiveAbortGameTask().execute(game);
	}

	private class LiveAbortGameTask extends AsyncTask<com.chess.live.client.Game, Void, Void> {
		@Override
		protected Void doInBackground(com.chess.live.client.Game... game) {
			lccHolder.getClient().abortGame(game[0], StaticData.SYMBOL_EMPTY);
			//stopSelf();
			return null;
		}
	}

	public void runAcceptChallengeTask(Challenge challenge) {
		new LiveAcceptChallengeTask().execute(challenge);
	}

	private class LiveAcceptChallengeTask extends AsyncTask<Challenge, Void, Void> {
		@Override
		protected Void doInBackground(Challenge... challenge) {
			lccHolder.getClient().acceptChallenge(challenge[0], lccHolder.getChallengeListener());
			//stopSelf();
			return null;
		}
	}

	public void runRejectChallengeTask(Challenge challenge) {
		new LiveRejectChallengeTask().execute(challenge);
	}

	private class LiveRejectChallengeTask extends AsyncTask<Challenge, Void, Void> {
		@Override
		protected Void doInBackground(Challenge... challenge) {
			lccHolder.getClient().rejectChallenge(challenge[0], lccHolder.getChallengeListener());
			//stopSelf();
			return null;
		}
	}

	public void runRejectBatchChallengeTask(Challenge[] challenge) {
		new LiveRejectBatchChallengeTask().execute(challenge);
	}
	
	private class LiveRejectBatchChallengeTask extends AsyncTask<Challenge, Void, Void> {
		@Override
		protected Void doInBackground(Challenge... challenges) {
			for (Challenge challenge : challenges) {
				lccHolder.getClient().rejectChallenge(challenge, lccHolder.getChallengeListener());
			}
			return null;
		}
	}

	
	
	public void runRejectDrawTask(Game game) {
		new LiveRejectDrawTask().execute(game);
	}

	private class LiveRejectDrawTask extends AsyncTask<com.chess.live.client.Game, Void, Void> {
		@Override
		protected Void doInBackground(com.chess.live.client.Game... game) {
			lccHolder.getClient().rejectDraw(game[0], StaticData.SYMBOL_EMPTY);
			//stopSelf();
			return null;
		}
	}

}

