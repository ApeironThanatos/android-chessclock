package com.chess.utilities;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import com.chess.R;
import com.chess.backend.AlarmReceiver;
import com.chess.backend.entity.DataHolder;
import com.chess.backend.statics.AppData;
import com.chess.backend.statics.StaticData;
import com.chess.lcc.android.LccHolder;
import com.chess.live.client.User;
import com.chess.model.BaseGameItem;
import com.chess.model.GameListCurrentItem;
import com.chess.ui.activities.GameOnlineScreenActivity;
import com.chess.ui.views.BackgroundChessDrawable;

import java.util.Arrays;

/**
 * AppUtils class
 *
 * @author alien_roger
 * @created at: 01.02.12 7:50
 */
public class AppUtils {

	private static final int MDPI_DENSITY = 1;
	private static boolean ENABLE_LOG = true;

	public static class ListSelector implements Runnable{
		private int pos;
		private ListView listView;

		public ListSelector(int pos, ListView listView){
			this.pos = pos;
			this.listView = listView;
		}
		@Override
		public void run() {
			listView.setSelection(pos);
		}
	}

	public static void setBackground(View mainView, Context context) {
		mainView.setBackgroundDrawable(new BackgroundChessDrawable(context));

		int paddingTop = (int) context.getResources().getDimension(R.dimen.dashboard_padding_top);
		int paddingLeft = (int) context.getResources().getDimension(R.dimen.dashboard_padding_side);
		int paddingRight = (int) context.getResources().getDimension(R.dimen.dashboard_padding_side);
		int paddingBot = (int) context.getResources().getDimension(R.dimen.dashboard_padding_bot);
		mainView.setPadding(paddingLeft, paddingTop, paddingRight, paddingBot);
	}

	/**
	 * For QVGA screens we don't need a title bar and Action bar
	 * @param context
	 * @return
	 */
	public static boolean needFullScreen(Context context) {
		DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
		return displayMetrics.density < MDPI_DENSITY
				|| displayMetrics.densityDpi == DisplayMetrics.DENSITY_LOW;
	}

	/**
	 * For mdpi normal screens we don't need a action bar only
	 * @param context
	 * @return
	 */
	public static boolean noNeedTitleBar(Context context) {
		DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
		return (displayMetrics.density == MDPI_DENSITY
				|| displayMetrics.densityDpi == DisplayMetrics.DENSITY_MEDIUM)
				&& displayMetrics.heightPixels <= 480;
	}

	/**
	 * Fire notification with defined arguments
	 *
	 * @param context - Application Context for resources
	 * @param title - title that will be visible at status bar
	 * @param id - request code id
	 * @param sound - sound to play
	 * @param body - short description for notification message content
	 * @param clazz - which class to open when User press notification
	 */
	public static void showNotification(Context context, String title, long id,
										String sound,String body,Class<?> clazz) { // TODO unify
		NotificationManager notifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

		Notification notification = new Notification(R.drawable.ic_stat_chess, context.getString(R.string.you_got_new_msg), System.currentTimeMillis());
//		notification.sound = Uri.parse(sound); // SettingsActivity.getAlarmRingtone(context);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;

		Intent openList = new Intent(context, clazz);
		openList.putExtra(StaticData.CLEAR_CHAT_NOTIFICATION, true);
		openList.putExtra(BaseGameItem.GAME_ID, id);
		openList.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
				|Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, openList, PendingIntent.FLAG_ONE_SHOT);

		notification.setLatestEventInfo(context, context.getText(R.string.you_got_new_msg), context.getText(R.string.open_app_t_see_msg), contentIntent);

		notifyManager.notify(R.string.you_got_new_msg, notification);
	}

	/**
	 * Fire simplified notification with defined arguments
	 *
	 * @param context - Application Context for resources
	 * @param title - title that will be visible at status bar
	 * @param id - request code id
	 * @param body - short description for notification message content
	 * @param clazz - which class to open when User press notification
	 */
	public static void showMoveStatusNotification(Context context, String title,  String body, int id, Class<?> clazz) {
		NotificationManager notifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

		Notification notification = new Notification(R.drawable.ic_stat_chess, title, System.currentTimeMillis());
		notification.flags |= Notification.FLAG_AUTO_CANCEL;

		final MediaPlayer player = MediaPlayer.create(context, R.raw.move_opponent);


		Intent intent = new Intent(context, clazz);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

		PendingIntent contentIntent = PendingIntent.getActivity(context, id, intent, PendingIntent.FLAG_ONE_SHOT);

		notification.setLatestEventInfo(context, title, body, contentIntent);
		notifyManager.notify(R.id.notification_message, notification);
		player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mediaPlayer) {
				player.stop();
				player.release();
			}
		});
		player.start();
	}


	public static void showNewMoveStatusNotification(Context context, String title,  String body, int id,
													 GameListCurrentItem currentGameItem) {
		NotificationManager notifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

		Notification notification = new Notification(R.drawable.ic_stat_chess, title, System.currentTimeMillis());
		notification.flags |= Notification.FLAG_AUTO_CANCEL;

		Intent intent = new Intent(context, GameOnlineScreenActivity.class);
		intent.putExtra(BaseGameItem.GAME_INFO_ITEM, currentGameItem);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

		PendingIntent contentIntent = PendingIntent.getActivity(context, id, intent, PendingIntent.FLAG_ONE_SHOT);

		notification.setLatestEventInfo(context, title, body, contentIntent);

		notifyManager.notify((int) currentGameItem.getGameId(), notification);
	}

	/**
	 * Use default android.util.Log with Flag trigger
	 * Use this method to track changes, but avoid to use in uncertain cases,
	 * where release version can tell where some bugs were born
	 * @param tag
	 * @param message
	 */
	public static void logD(String tag, String message){
		if(ENABLE_LOG) // can be set false for release version.
			Log.d(tag, message);
	}

	public static void startNotificationsUpdate(Context context){
		Intent statusUpdate = new Intent(context, AlarmReceiver.class);

		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, StaticData.YOUR_MOVE_UPDATE_ID,
				statusUpdate, PendingIntent.FLAG_UPDATE_CURRENT);

		// schedule the service for updating
		AlarmManager alarms = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		alarms.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis(), StaticData.REMIND_ALARM_INTERVAL, pendingIntent);
	}

	public static void stopNotificationsUpdate(Context context){
		Intent statusUpdate = new Intent(context, AlarmReceiver.class);

		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, StaticData.YOUR_MOVE_UPDATE_ID, statusUpdate,
				PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager alarms = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		alarms.cancel(pendingIntent);

		NotificationManager notifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

		notifyManager.cancel(R.id.notification_message);
	}

	public static boolean isNeedToUpgrade(Context context){
		boolean liveMembershipLevel = false;
		User user = LccHolder.getInstance(context).getUser();
		if (user != null) {
			liveMembershipLevel = DataHolder.getInstance().isLiveChess()
					&& (user.getMembershipLevel() < StaticData.GOLD_LEVEL);
		}
		return liveMembershipLevel
				|| (!DataHolder.getInstance().isLiveChess() && AppData.getUserPremiumStatus(context) < StaticData.GOLD_USER);
	}

	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager cm
				= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		return networkInfo != null && networkInfo.isConnected();
	}

	public static String getI18nString(Context context, String codeMessage, String... parameters) {
		return context.getString(context.getResources().getIdentifier(codeMessage, "string", context.getPackageName()), parameters);
	}

	public static String getI18nStringForAPIError(Context context, String message) {
		final Resources resources = context.getResources();

		if (isLocaleEn(resources)) {
			return message;
		}

		final int positionOfMessage = Arrays.asList(resources.getStringArray(R.array.site_api_error_messages)).indexOf(message);
		if (positionOfMessage != -1) {
			final String messageKey = resources.getStringArray(R.array.site_api_error_keys)[positionOfMessage];
			return context.getString(resources.getIdentifier(messageKey, "string", context.getPackageName()));
		}

		return message;
	}

	private static boolean isLocaleEn(Resources resources) {
		return resources.getConfiguration().locale.getLanguage().equals(StaticData.LOCALE_EN);
	}
}
