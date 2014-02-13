package com.chess.ui.fragments.stats;

import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.chess.R;
import com.chess.backend.LoadItem;
import com.chess.backend.RestHelper;
import com.chess.backend.entity.api.stats.GameStatsItem;
import com.chess.backend.tasks.RequestJsonTask;
import com.chess.db.DbDataManager;
import com.chess.db.DbHelper;
import com.chess.db.DbScheme;
import com.chess.db.QueryParams;
import com.chess.db.tasks.LoadDataFromDbTask;
import com.chess.db.tasks.SaveGameStatsTask;
import com.chess.statics.FlurryData;
import com.chess.statics.StaticData;
import com.chess.statics.Symbol;
import com.chess.ui.fragments.CommonLogicFragment;
import com.chess.ui.fragments.upgrade.UpgradeFragment;
import com.chess.ui.fragments.upgrade.UpgradeFragmentTablet;
import com.chess.ui.views.PieChartView;
import com.chess.ui.views.RatingGraphView;
import com.chess.utilities.AppUtils;
import com.flurry.android.FlurryAgent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.chess.ui.fragments.stats.StatsGameFragment.*;

/**
 * Created with IntelliJ IDEA.
 * User: roger sent2roger@gmail.com
 * Date: 23.01.13
 * Time: 10:37
 */
public class StatsGameDetailsFragment extends CommonLogicFragment implements RadioGroup.OnCheckedChangeListener, ViewTreeObserver.OnGlobalLayoutListener {

	public static final int FIRST = 0;
	public static final int LAST = 1;

	public static final String GREY_COLOR_DIVIDER = "##";

	public static final int HIGHEST_ID = 0x00002000;
	public static final int LOWEST_ID = 0x00002100;
	public static final int AVERAGE_ID = 0x00002200;
	public static final int BEST_WIN_ID = 0x00002300;

	public static final int RATING_SUBTITLE_ID = 0x00000001;
	public static final int RATING_VALUE_ID = 0x00000002;
	// 05/27/08
	private final static SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yy");

	private final static String GAME_TYPE = "gameType";
	private static final String USERNAME = "username";

	private TextView winCntValueTxt;
	private TextView loseCntValueTxt;
	private TextView drawCntValueTxt;
	private TextView winningStreakValueTxt;
	private TextView losingStreakValueTxt;
	private TextView currentRatingTxt;
	private TextView absoluteRankTxt;
	private TextView totalRankedTxt;
	private TextView percentileValueTxt;
	private TextView totalGamesValueTxt;
	private PieChartView pieChartView;
	private TextView timeoutsValueTxt;
	private TextView glickoValueTxt;
	private TextView mostFrequentOpponentTxt;
	private TextView mostFrequentOpponentGamesTxt;
	private TextView timeoutsLabelTxt;
	private ForegroundColorSpan foregroundSpan;
	private int gameType;
	private String username;
	private RatingGraphView ratingGraphView;
	private boolean showTitle;
	private long lastTimestamp;
	private int previousCheckedId;
	private SaveStatsUpdateListener saveStatsUpdateListener;
	private CursorUpdateListener gameStatsCursorUpdateListener;
	private String gameTypeStr;
	private StatsItemUpdateListener statsItemUpdateListener;

	public StatsGameDetailsFragment() {
		Bundle bundle = new Bundle();
		bundle.putInt(GAME_TYPE, 0);

		setArguments(bundle);
	}

	public static StatsGameDetailsFragment createInstance(int code, boolean showTitle, String username) {
		StatsGameDetailsFragment fragment = createInstance(code, username);
		fragment.showTitle = showTitle;
		return fragment;
	}

	public static StatsGameDetailsFragment createInstance(int code, String username) {
		StatsGameDetailsFragment fragment = new StatsGameDetailsFragment();
		Bundle bundle = new Bundle();
		bundle.putInt(GAME_TYPE, code);
		bundle.putString(USERNAME, username);

		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments() != null) {
			username = getArguments().getString(USERNAME);
			gameType = getArguments().getInt(GAME_TYPE);
		} else {
			username = savedInstanceState.getString(USERNAME);
			gameType = savedInstanceState.getInt(GAME_TYPE);
		}

		if (TextUtils.isEmpty(username)) {
			username = getUsername();
		}

		init();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.stats_game_details_frame, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		if (showTitle) {
			if (!username.equals(getUsername())) {
				setTitle(username + Symbol.SPACE + getString(R.string.stats));
			} else {
				setTitle(R.string.stats);
			}
		}

		currentRatingTxt = (TextView) view.findViewById(R.id.currentRatingTxt);

		absoluteRankTxt = (TextView) view.findViewById(R.id.absoluteRankTxt);
		totalRankedTxt = (TextView) view.findViewById(R.id.totalRankedTxt);

		percentileValueTxt = (TextView) view.findViewById(R.id.percentileValueTxt);

		totalGamesValueTxt = (TextView) view.findViewById(R.id.totalGamesValueTxt);


		pieChartView = (PieChartView) view.findViewById(R.id.pieChartView);
		ratingGraphView = (RatingGraphView) view.findViewById(R.id.ratingGraphView);
		ratingGraphView.setOnCheckChangeListener(this);

		LinearLayout ratingsLinearView = (LinearLayout) view.findViewById(R.id.ratingsLinearView);
		addRatingsViews(ratingsLinearView);

		winCntValueTxt = (TextView) view.findViewById(R.id.winCntValueTxt);
		loseCntValueTxt = (TextView) view.findViewById(R.id.loseCntValueTxt);
		drawCntValueTxt = (TextView) view.findViewById(R.id.drawCntValueTxt);

		winningStreakValueTxt = (TextView) view.findViewById(R.id.winningStreakValueTxt);
		losingStreakValueTxt = (TextView) view.findViewById(R.id.losingStreakValueTxt);
		timeoutsLabelTxt = (TextView) view.findViewById(R.id.timeoutsLabelTxt);
		timeoutsValueTxt = (TextView) view.findViewById(R.id.timeoutsValueTxt);
		glickoValueTxt = (TextView) view.findViewById(R.id.glickoValueTxt);

		mostFrequentOpponentTxt = (TextView) view.findViewById(R.id.mostFrequentOpponentTxt);
		mostFrequentOpponentGamesTxt = (TextView) view.findViewById(R.id.mostFrequentOpponentGamesTxt);

		if (isNeedToUpgrade()) {
			view.findViewById(R.id.upgradeBtn).setOnClickListener(this);
			((TextView) view.findViewById(R.id.lessonsUpgradeMessageTxt)).setText(R.string.get_detailed_stats);
			view.findViewById(R.id.demoOverlayView).setVisibility(View.VISIBLE);
		}

		view.getViewTreeObserver().addOnGlobalLayoutListener(this);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putInt(GAME_TYPE, gameType);
		outState.putString(USERNAME, username);
	}

	private void init() {

		switch (gameType) {
			case LIVE_STANDARD:
				gameTypeStr = SaveGameStatsTask.STANDARD;
				break;
			case LIVE_LIGHTNING:
				gameTypeStr = SaveGameStatsTask.LIGHTNING;
				break;
			case LIVE_BLITZ:
				gameTypeStr = SaveGameStatsTask.BLITZ;
				break;
			case DAILY_CHESS:
				gameTypeStr = SaveGameStatsTask.CHESS;
				break;
			case DAILY_CHESS960:
				gameTypeStr = SaveGameStatsTask.CHESS960;
				break;
		}

		saveStatsUpdateListener = new SaveStatsUpdateListener();
		statsItemUpdateListener = new StatsItemUpdateListener();

		gameStatsCursorUpdateListener = new CursorUpdateListener();

		int lightGrey = getResources().getColor(R.color.stats_label_light_grey);
		foregroundSpan = new ForegroundColorSpan(lightGrey);
	}

	private void loadFromDb() {
		DbScheme.Tables table = null;
		switch (gameType) {
			case LIVE_STANDARD:
				table = DbScheme.Tables.GAME_STATS_LIVE_STANDARD;
				break;
			case LIVE_LIGHTNING:
				table = DbScheme.Tables.GAME_STATS_LIVE_LIGHTNING;
				break;
			case LIVE_BLITZ:
				table = DbScheme.Tables.GAME_STATS_LIVE_BLITZ;
				break;
			case DAILY_CHESS:
				table = DbScheme.Tables.GAME_STATS_DAILY_CHESS;
				break;
			case DAILY_CHESS960:
				table = DbScheme.Tables.GAME_STATS_DAILY_CHESS960;
				break;
		}
		new LoadDataFromDbTask(gameStatsCursorUpdateListener, DbHelper.getTableForUser(username,
				table), getContentResolver()).executeTask();
	}

	@Override
	public void onClick(View view) {
		super.onClick(view);

		if (view.getId() == R.id.upgradeBtn) {

			FlurryAgent.logEvent(FlurryData.UPGRADE_FROM_STATS);
			if (!isTablet) {
				getActivityFace().openFragment(new UpgradeFragment());
			} else {
				getActivityFace().openFragment(new UpgradeFragmentTablet());
			}
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		if (previousCheckedId == checkedId || getActivity() == null) {
			return;
		}

		previousCheckedId = checkedId;
		lastTimestamp = System.currentTimeMillis();
		switch (checkedId) {
			case R.id.thirtyDaysBtn:
				lastTimestamp = AppUtils.getLast30DaysTimeStamp();
				break;
			case R.id.ninetyDaysBtn:
				lastTimestamp = AppUtils.getLast90DaysTimeStamp();
				break;
			case R.id.oneYearBtn:
				lastTimestamp = AppUtils.getLastYearTimeStamp();
				break;
			case R.id.allTimeBtn:
				lastTimestamp = getAppData().getUserCreateDate();
				break;
		}
		updateGraphAfter(lastTimestamp);
	}

	private void updateGraphAfter(long lastTimestamp) {
		this.lastTimestamp = lastTimestamp;

		if (isNeedToUpgrade()) { // show stub stats
			List<long[]> series = new ArrayList<long[]>();
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.MONTH, -3);

			int pointsCnt = 100;
			long startPoint = calendar.getTimeInMillis();
			long endPoint = System.currentTimeMillis();
			long step = (endPoint - startPoint) / pointsCnt;

			for (int i = 0; i < pointsCnt; i++) {
				startPoint += step;
				int rating = (int) (1200 + Math.random() * 50);
				long[] point = new long[]{startPoint, rating};
				series.add(point);
			}
			ratingGraphView.setGraphData(series, getView().getWidth());

			return;
		}

		long[] edgeTimestamps = DbDataManager.getEdgeTimestampForGamesGraph(getContentResolver(), username, gameTypeStr);

		long today = System.currentTimeMillis() / 1000;
		long oneDay = AppUtils.SECONDS_IN_DAY;
		// if we have saved data from last timestamp(30 days ago) until today, we don't load it from server
		if ((edgeTimestamps[LAST] >= today - oneDay) && edgeTimestamps[FIRST] <= lastTimestamp - oneDay) {
			loadFromDb();
		} else { // else we only load difference since last saved point
			getFullStats();
		}
	}

	private void getFullStats() {
		LoadItem loadItem = new LoadItem();
		loadItem.setLoadPath(RestHelper.getInstance().CMD_GAME_STATS);
		loadItem.addRequestParams(RestHelper.P_LOGIN_TOKEN, getUserToken());
		loadItem.addRequestParams(RestHelper.P_GAME_TYPE, gameTypeStr);
		if (getAppData().getUserPremiumStatus() != StaticData.GOLD_USER || !username.equals(getUsername())) {
			loadItem.addRequestParams(RestHelper.P_VIEW_USERNAME, username);
		}
		loadItem.addRequestParams(RestHelper.P_LAST_GRAPH_TIMESTAMP, lastTimestamp);

		new RequestJsonTask<GameStatsItem>(statsItemUpdateListener).executeTask(loadItem);
	}

	@Override
	public void onGlobalLayout() {
		if (need2update) {
			ratingGraphView.setChecked(R.id.thirtyDaysBtn);
		}
	}

	private class CursorUpdateListener extends ChessUpdateListener<Cursor> {

		public CursorUpdateListener() {
			super();
		}

		@Override
		public void updateData(Cursor returnedObj) {
			super.updateData(returnedObj);

			{ // top info view
				int current = DbDataManager.getInt(returnedObj, DbScheme.V_CURRENT);
				currentRatingTxt.setText(String.valueOf(current));

				int rank = DbDataManager.getInt(returnedObj, DbScheme.V_RANK);
				if (rank == 0) {
					absoluteRankTxt.setText(R.string.not_available);

				} else {
					absoluteRankTxt.setText(String.valueOf(rank));
					int totalPlayers = DbDataManager.getInt(returnedObj, DbScheme.V_TOTAL_PLAYER_COUNT);
					totalRankedTxt.setText(getString(R.string.of_arg, totalPlayers));
				}

				String percentile = DbDataManager.getString(returnedObj, DbScheme.V_PERCENTILE);
				if (percentile.equals(String.valueOf(0.f))) {
					percentileValueTxt.setText(R.string.not_available);
				} else {
					percentileValueTxt.setText(percentile + Symbol.PERCENT);
				}
			}

			int totalGamesPlayed = DbDataManager.getInt(returnedObj, DbScheme.V_GAMES_TOTAL);
			totalGamesValueTxt.setText(String.valueOf(totalGamesPlayed));

			fillRatings(returnedObj);

			{// avg opponent rating when i
				int winCnt = DbDataManager.getInt(returnedObj, DbScheme.V_AVG_OPPONENT_RATING_WIN);
				winCntValueTxt.setText(String.valueOf(winCnt));

				int loseCnt = DbDataManager.getInt(returnedObj, DbScheme.V_AVG_OPPONENT_RATING_LOSE);
				loseCntValueTxt.setText(String.valueOf(loseCnt));

				int drawCnt = DbDataManager.getInt(returnedObj, DbScheme.V_AVG_OPPONENT_RATING_DRAW);
				drawCntValueTxt.setText(String.valueOf(drawCnt));
			}

			{// Streaks
				int winCnt = DbDataManager.getInt(returnedObj, DbScheme.V_WINNING_STREAK);
				winningStreakValueTxt.setText(String.valueOf(winCnt));

				int loseCnt = DbDataManager.getInt(returnedObj, DbScheme.V_LOSING_STREAK);
				losingStreakValueTxt.setText(String.valueOf(loseCnt));
			}

			{ // Graph Rating Data
				QueryParams params = DbHelper.getGraphItemForUser(username, gameTypeStr, lastTimestamp);
				Cursor cursor = DbDataManager.query(getContentResolver(), params);

				if (cursor != null && cursor.moveToFirst()) {
					final List<long[]> series = new ArrayList<long[]>();
					do {
						long timestamp = DbDataManager.getLong(cursor, DbScheme.V_TIMESTAMP) * 1000;
						int rating = DbDataManager.getInt(cursor, DbScheme.V_RATING);
						long[] point = new long[]{timestamp, rating};

						series.add(point);
					} while (cursor.moveToNext());

					if (getView().getWidth() == 0) {
						handler.postDelayed(new Runnable() {
							@Override
							public void run() {
								ratingGraphView.setGraphData(series, getView().getWidth());
							}
						}, VIEW_UPDATE_DELAY);
					} else {
						ratingGraphView.setGraphData(series, getView().getWidth());
					}
				}
			}

			// donut/pie chart
			pieChartView.setGames(DbDataManager.getGameStatsGamesByResultFromCursor(returnedObj));

			{// timeouts
				String timeoutsStr = getString(R.string.timeouts_last_90_days);
				timeoutsStr = timeoutsStr.replace(Symbol.LEFT_PAR, GREY_COLOR_DIVIDER + Symbol.LEFT_PAR);
				timeoutsStr = timeoutsStr.replace(Symbol.RIGHT_PAR, Symbol.RIGHT_PAR + GREY_COLOR_DIVIDER);
				CharSequence timeoutChr = timeoutsStr;
				timeoutChr = AppUtils.setSpanBetweenTokens(timeoutChr, GREY_COLOR_DIVIDER, foregroundSpan);
				timeoutsLabelTxt.setText(timeoutChr);

				int timeouts = DbDataManager.getInt(returnedObj, DbScheme.V_TIMEOUTS);
				if (timeouts == 0) {
					timeoutsValueTxt.setText(R.string.not_available);
				} else {
					timeoutsValueTxt.setText(String.valueOf(timeouts));
				}
			}

			int glickoRd = DbDataManager.getInt(returnedObj, DbScheme.V_GLICKO_RD);
			glickoValueTxt.setText(String.valueOf(glickoRd));

			String mostFrequentOpponentName = DbDataManager.getString(returnedObj, DbScheme.V_FREQUENT_OPPONENT_NAME);
			int mostFrequentOpponentGamesPlayed = DbDataManager.getInt(returnedObj, DbScheme.V_FREQUENT_OPPONENT_GAMES_PLAYED);
			if (mostFrequentOpponentGamesPlayed == 0) {
				mostFrequentOpponentGamesTxt.setText(R.string.not_available);
			} else {
				mostFrequentOpponentTxt.setText(mostFrequentOpponentName);
				mostFrequentOpponentGamesTxt.setText(getString(R.string.games_arg, mostFrequentOpponentGamesPlayed));
			}

			need2update = false;
		}

		@Override
		public void errorHandle(Integer resultCode) {
			super.errorHandle(resultCode);

			if (resultCode == StaticData.EMPTY_DATA) {
				getFullStats();
			}
		}
	}

	private class StatsItemUpdateListener extends ChessLoadUpdateListener<GameStatsItem> {

		public StatsItemUpdateListener() {
			super(GameStatsItem.class);
		}

		@Override
		public void showProgress(boolean show) {
			if (!isTablet) {
				super.showProgress(show);
			}
		}

		@Override
		public void updateData(GameStatsItem returnedObj) {
			super.updateData(returnedObj);

			new SaveGameStatsTask(saveStatsUpdateListener, returnedObj.getData(), getContentResolver(),
					gameTypeStr, username).executeTask();
		}
	}

	private class SaveStatsUpdateListener extends ChessLoadUpdateListener<GameStatsItem.Data> {

		@Override
		public void updateData(GameStatsItem.Data returnedObj) {
			super.updateData(returnedObj);
			loadFromDb();
		}
	}

	private void fillRatings(Cursor cursor) {
		{ // highest
			int rating = DbDataManager.getInt(cursor, DbScheme.V_HIGHEST_RATING);
			long ratingTime = DbDataManager.getLong(cursor, DbScheme.V_HIGHEST_TIMESTAMP) * 1000L;

			setTextById((HIGHEST_ID + RATING_VALUE_ID), String.valueOf(rating));
			setTextById((HIGHEST_ID + RATING_SUBTITLE_ID), dateFormatter.format(new Date(ratingTime)));
		}

		{ // lowest
			int rating = DbDataManager.getInt(cursor, DbScheme.V_LOWEST_RATING);
			long ratingTime = DbDataManager.getLong(cursor, DbScheme.V_LOWEST_TIMESTAMP) * 1000L;

			setTextById((LOWEST_ID + RATING_VALUE_ID), String.valueOf(rating));
			setTextById((LOWEST_ID + RATING_SUBTITLE_ID), dateFormatter.format(new Date(ratingTime)));
		}

		{ // average opponent
			String rating = DbDataManager.getString(cursor, DbScheme.V_AVERAGE_OPPONENT_RATING);
			int ratingInt = Integer.parseInt(rating); // TODO should be fixed in later releases
			setTextById((AVERAGE_ID + RATING_VALUE_ID), String.valueOf(ratingInt));
		}

		{ // best win on
			int rating = DbDataManager.getInt(cursor, DbScheme.V_BEST_WIN_RATING);
			if (rating == 0) {
				setTextById((BEST_WIN_ID + RATING_VALUE_ID), R.string.not_available);
			} else {
				String username = DbDataManager.getString(cursor, DbScheme.V_BEST_WIN_USERNAME);

				setTextById((BEST_WIN_ID + RATING_VALUE_ID), String.valueOf(rating));
				setTextById((BEST_WIN_ID + RATING_SUBTITLE_ID), username);
			}
		}
	}

	private void addRatingsViews(LinearLayout ratingsLinearView) {
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		// set id's to view for further set data to them
		{// Highest Rating
			RelativeLayout highestRatingView = (RelativeLayout) inflater.inflate(R.layout.stats_rating_item_view, null, false);
			((TextView) highestRatingView.findViewById(R.id.ratingLabelTxt)).setText(R.string.highest_rating);
			highestRatingView.findViewById(R.id.subtitleTxt).setId(HIGHEST_ID + RATING_SUBTITLE_ID);
			highestRatingView.findViewById(R.id.ratingValueTxt).setId(HIGHEST_ID + RATING_VALUE_ID);

			ratingsLinearView.addView(highestRatingView);
		}

		{// Lowest Rating
			RelativeLayout highestRatingView = (RelativeLayout) inflater.inflate(R.layout.stats_rating_item_view, null, false);
			((TextView) highestRatingView.findViewById(R.id.ratingLabelTxt)).setText(R.string.lowest_rating);
			highestRatingView.findViewById(R.id.subtitleTxt).setId(LOWEST_ID + RATING_SUBTITLE_ID);
			highestRatingView.findViewById(R.id.ratingValueTxt).setId(LOWEST_ID + RATING_VALUE_ID);

			ratingsLinearView.addView(highestRatingView);
		}

		{// Average Opponent Rating
			RelativeLayout highestRatingView = (RelativeLayout) inflater.inflate(R.layout.stats_rating_item_view, null, false);
			((TextView) highestRatingView.findViewById(R.id.ratingLabelTxt)).setText(R.string.avg_opponent_rating);
			highestRatingView.findViewById(R.id.ratingValueTxt).setId(AVERAGE_ID + RATING_VALUE_ID);
			((TextView) highestRatingView.findViewById(R.id.subtitleTxt)).setText(R.string.three_months);

			ratingsLinearView.addView(highestRatingView);
		}

		{// Best Win Rating
			RelativeLayout highestRatingView = (RelativeLayout) inflater.inflate(R.layout.stats_rating_item_view, null, false);
			((TextView) highestRatingView.findViewById(R.id.ratingLabelTxt)).setText(R.string.best_win_rating);
			highestRatingView.findViewById(R.id.subtitleTxt).setId(BEST_WIN_ID + RATING_SUBTITLE_ID);
			highestRatingView.findViewById(R.id.ratingValueTxt).setId(BEST_WIN_ID + RATING_VALUE_ID);
			((TextView) highestRatingView.findViewById(BEST_WIN_ID + RATING_SUBTITLE_ID))
					.setTextColor(getResources().getColor(R.color.new_text_blue));

			ratingsLinearView.addView(highestRatingView);
		}
	}

	private void setTextById(int id, String text) {
		((TextView) getView().findViewById(id)).setText(text);
	}

	private void setTextById(int id, int textId) {
		((TextView) getView().findViewById(id)).setText(textId);
	}
}
