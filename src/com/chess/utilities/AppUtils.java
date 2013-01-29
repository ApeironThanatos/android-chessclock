package com.chess.utilities;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.text.SpannableStringBuilder;
import android.text.style.CharacterStyle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import com.chess.R;
import com.chess.backend.statics.AppConstants;
import com.chess.backend.statics.AppData;
import com.chess.backend.statics.StaticData;
import com.chess.lcc.android.LccHolder;
import com.chess.live.client.User;
import com.chess.model.BaseGameItem;
import com.chess.model.GameListCurrentItem;
import com.chess.ui.activities.GameOnlineScreenActivity;
import com.chess.ui.views.drawables.BackgroundChessDrawable;
import com.google.gson.Gson;
import org.apache.http.HttpEntity;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * AppUtils class
 *
 * @author alien_roger
 * @created at: 01.02.12 7:50
 */
public class AppUtils {

	private static final int MDPI_DENSITY = 1;
	private static boolean ENABLE_LOG = true;
	private static final String DAYS = "d";
	private static final String H = "h";
	private static final String M = "m";

	public static final boolean HONEYCOMB_PLUS_API = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;

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
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
			mainView.setBackground(new BackgroundChessDrawable(context));
		} else {
			mainView.setBackgroundDrawable(new BackgroundChessDrawable(context));
		}

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
		Configuration config = context.getResources().getConfiguration();
		return (displayMetrics.density < MDPI_DENSITY || displayMetrics.densityDpi == DisplayMetrics.DENSITY_LOW)
				&& config.orientation == Configuration.ORIENTATION_PORTRAIT;
	}

	/**
	 * For mdpi normal screens we don't need a action bar only
	 * @param context
	 * @return
	 */
	public static boolean noNeedTitleBar(Context context) {
		DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
		Configuration config = context.getResources().getConfiguration();
		return (displayMetrics.density == MDPI_DENSITY || displayMetrics.densityDpi == DisplayMetrics.DENSITY_MEDIUM)
				&& (displayMetrics.heightPixels <= 480 && config.orientation == Configuration.ORIENTATION_PORTRAIT
				|| displayMetrics.heightPixels <= 300 && config.orientation == Configuration.ORIENTATION_LANDSCAPE);
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

		Intent intent = new Intent(context, clazz);
//		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

		PendingIntent contentIntent = PendingIntent.getActivity(context, id, intent, PendingIntent.FLAG_ONE_SHOT);

		notification.setLatestEventInfo(context, title, body, contentIntent);
		notifyManager.cancelAll();
		notifyManager.notify(R.id.notification_message, notification);

		SharedPreferences preferences = AppData.getPreferences(context);
		boolean playSounds = preferences.getBoolean(AppData.getUserName(context) + AppConstants.PREF_SOUNDS, false);
		if(playSounds){
			final MediaPlayer player = MediaPlayer.create(context, R.raw.move_opponent);

			if(player == null) // someone hasn't player?
				return;

			player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer mediaPlayer) {
					player.stop();
					player.release();
				}
			});
			player.start();
		}
	}


	public static void showNewMoveStatusNotification(Context context, String title,  String body, int id,
													 GameListCurrentItem currentGameItem) {
		NotificationManager notifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		notifyManager.cancelAll(); // clear all previous notifications

		Notification notification = new Notification(R.drawable.ic_stat_chess, title, System.currentTimeMillis());
		notification.flags |= Notification.FLAG_AUTO_CANCEL;

		Intent intent = new Intent(context, GameOnlineScreenActivity.class);
		intent.putExtra(BaseGameItem.GAME_ID, currentGameItem.getGameId());
		intent.putExtra(AppConstants.NOTIFICATION, true);
//		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);  // as we are using singleTask mode for GameOnlineActivity we call enter there via onNewIntent callback

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

	public static void cancelNotifications(Context context) {
		NotificationManager notifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		notifyManager.cancelAll();
	}



	public static void cancelNotification(Context context, int id){
		NotificationManager notifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		notifyManager.cancel(id);
	}

	public static boolean isNeedToUpgrade(Context context){
		boolean liveMembershipLevel = false;
		User user = LccHolder.getInstance(context).getUser();
		if (user != null) {
			liveMembershipLevel = AppData.isLiveChess(context)
					&& (user.getMembershipLevel() < StaticData.GOLD_LEVEL);
		}
		return AppData.isGuest(context)
				|| liveMembershipLevel
				|| (!AppData.isLiveChess(context) && AppData.getUserPremiumStatus(context) < StaticData.GOLD_USER)
				&& AppData.getUserPremiumStatus(context) != StaticData.NOT_INITIALIZED_USER;
	}

	public static boolean isNeedToUpgradePremium(Context context){
		boolean liveMembershipLevel = false;
		User user = LccHolder.getInstance(context).getUser();
		if (user != null) {
			liveMembershipLevel = AppData.isLiveChess(context)
					&& (user.getMembershipLevel() < StaticData.DIAMOND_LEVEL);
		}
		return liveMembershipLevel
				|| (!AppData.isLiveChess(context) && AppData.getUserPremiumStatus(context) < StaticData.DIAMOND_USER)
				&& AppData.getUserPremiumStatus(context) != StaticData.NOT_INITIALIZED_USER;
	}

	public static String getTimeLeftFromSeconds(long duration, Context context) {
		long minutes = duration /60%60;
		long hours = duration /3600%24;
		long days = duration /86400;
		StringBuilder sb = new StringBuilder();

		if (days > 0) {
			sb.append(days).append(StaticData.SYMBOL_SPACE).append(context.getString(R.string.days)).append(StaticData.SYMBOL_SPACE);
		} else if (hours > 0) {
			if (!sb.toString().trim().equals(StaticData.SYMBOL_EMPTY))
				sb.append(StaticData.SYMBOL_SPACE);
			sb.append(hours).append(StaticData.SYMBOL_SPACE).append(context.getString(R.string.hours)).append(StaticData.SYMBOL_SPACE);
		} else if (minutes > 0) {
			if (!sb.toString().trim().equals(StaticData.SYMBOL_EMPTY))
				sb.append(StaticData.SYMBOL_SPACE);
			sb.append(context.getString(R.string.min_arg, minutes));
		}

		return sb.toString();
	}

	public static String getSecondsTimeFromSecondsStr(long duration) {
		long seconds = duration %60;
		long minutes = duration /60%60;
		long hours = duration /3600%24;
		long days = duration /86400;
		StringBuilder sb = new StringBuilder();

		if (days > 0) {
            sb.append(days).append(StaticData.SYMBOL_COLON);
        }

		if (hours > 0) {
			sb.append(hours).append(StaticData.SYMBOL_COLON);
		}

        if (minutes < 10) {
            sb.append(0);
        }
        sb.append(minutes).append(StaticData.SYMBOL_COLON);

        if (seconds < 10) {
            sb.append(0);
        }
        sb.append(seconds);

		return sb.toString();
	}

	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager cm
				= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		return networkInfo != null && networkInfo.isConnected();
	}

	public static String getI18nString(Context context, String codeMessage, String... parameters) {
		int stringIdentifier = context.getResources().getIdentifier(codeMessage, "string", context.getPackageName());
		return stringIdentifier != 0 ? context.getString(stringIdentifier, parameters) : null;
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

	public static class DeviceInfo {
		public String MODEL;
		public int SDK_API;
		public String APP_VERSION_NAME = StaticData.SYMBOL_EMPTY;
		public int APP_VERSION_CODE = 0;
		public String android_id;
		/*
		 * Get information about device model, App version and API version
		 */
		public DeviceInfo getDeviceInfo(Context context) {
			DeviceInfo deviceInfo = new DeviceInfo();

			deviceInfo.android_id =  Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);


			deviceInfo.MODEL = Build.MODEL;
			Log.i("requested MODEL = ", deviceInfo.MODEL);

			deviceInfo.SDK_API = Build.VERSION.SDK_INT;
			Log.i("requested SDK_INT = ", deviceInfo.SDK_API + StaticData.SYMBOL_EMPTY);
			// get version number and name
			try {
				PackageManager manager = context.getPackageManager();
				PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
				deviceInfo.APP_VERSION_CODE = info.versionCode;
				Log.i("requested versionCode = ", deviceInfo.APP_VERSION_CODE + StaticData.SYMBOL_EMPTY);

				deviceInfo.APP_VERSION_NAME = info.versionName;
				Log.i("requested versionName = ", deviceInfo.APP_VERSION_NAME);

			} catch (PackageManager.NameNotFoundException nnf) {
				nnf.printStackTrace();
			}
			return deviceInfo;
		}
	}

	public static String httpEntityToString(HttpEntity entity) throws IOException {

		InputStream inputStream = entity.getContent();
		int numberBytesRead = 0;
		StringBuilder out = new StringBuilder();
		byte[] bytes = new byte[4096];

		while (numberBytesRead != -1) {
			out.append(new String(bytes, 0, numberBytesRead));
			numberBytesRead = inputStream.read(bytes);
		}

		inputStream.close();

		return out.toString();
	}

	public static <ItemType> String parseJsonToString(ItemType jRequest) {
		Gson gson = new Gson();
		return gson.toJson(jRequest);
	}

	public static String convertStreamToString(java.io.InputStream is) {
		Scanner scanner = new java.util.Scanner(is).useDelimiter("\\A");
		return scanner.hasNext() ? scanner.next() : "";
	}

	public static void showKeyBoard(Context context, EditText view){
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(view, 0 );
	}

//	public static void showKeyBoard(Context context, EditText editText){
//		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
//		imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
//	}
//
	public static void hideKeyBoard(Context context, View editText){
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
	}

	public static List<String> convertArrayToList(String[] array){
		List<String> items = new ArrayList<String>();
		items.addAll(Arrays.asList(array));
		return items;
	}

	/**
	 * Given either a Spannable String or a regular String and a token, apply
	 * the given CharacterStyle to the span between the tokens, and also
	 * remove tokens.
	 * <p>
	 * For example, {@code setSpanBetweenTokens("Hello ##world##!", "##",
	 * new ForegroundColorSpan(0xFFFF0000));} will return a CharSequence
	 * {@code "Hello world!"} with {@code world} in red.
	 *
	 * @param text The text, with the tokens, to adjust.
	 * @param token The token string; there should be at least two instances
	 *             of token in text.
	 * @param cs The style to apply to the CharSequence. WARNING: You cannot
	 *            send the same two instances of this parameter, otherwise
	 *            the second call will remove the original span.
	 * @return A Spannable CharSequence with the new style applied.
	 *
	 * @see {@link <a>http://developer.android.com/reference/android/text/style/CharacterStyle.html</a> }
	 */
	public static CharSequence setSpanBetweenTokens(CharSequence text,
													String token, CharacterStyle... cs)
	{
		// Start and end refer to the points where the span will apply
		int tokenLen = token.length();
		int start = text.toString().indexOf(token) + tokenLen;
		int end = text.toString().indexOf(token, start);

		if (start > -1 && end > -1)
		{
			// Copy the spannable string to a mutable spannable string
			SpannableStringBuilder ssb = new SpannableStringBuilder(text);
			for (CharacterStyle c : cs)
				ssb.setSpan(c, start, end, 0);

			// Delete the tokens before and after the span
			ssb.delete(end, end + tokenLen);
			ssb.delete(start - tokenLen, start);

			text = ssb;
		}

		return text;
	}
}
