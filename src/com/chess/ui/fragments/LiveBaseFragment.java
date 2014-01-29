package com.chess.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import com.chess.R;
import com.chess.backend.LiveChessService;
import com.chess.backend.interfaces.ActionBarUpdateListener;
import com.chess.lcc.android.DataNotValidException;
import com.chess.lcc.android.LccHelper;
import com.chess.lcc.android.interfaces.LccEventListener;
import com.chess.live.client.Game;
import com.chess.model.GameLiveItem;
import com.chess.ui.activities.LiveBaseActivity;
import com.chess.ui.fragments.live.GameLiveFragment;
import com.chess.ui.fragments.live.GameLiveFragmentTablet;
import com.chess.utilities.LogMe;

/**
 * Created with IntelliJ IDEA.
 * User: roger sent2roger@gmail.com
 * Date: 11.04.13
 * Time: 13:06
 */
public abstract class LiveBaseFragment extends CommonLogicFragment implements LccEventListener {

	private static final String TAG = "LccLog-LiveBaseFragment";

	protected LiveBaseActivity liveBaseActivity;
	private LiveChessService liveService;
	protected boolean isLCSBound;
	protected GameTaskListener gameTaskListener;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		liveBaseActivity = (LiveBaseActivity) activity;
		if (getAppData().isLiveChess()) {
			setLCSBound(liveBaseActivity.isLCSBound());
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		gameTaskListener = new GameTaskListener();
	}

	@Override
	public void onResume() {
		super.onResume();

		if (getAppData().isLiveChess()) {
			setLCSBound(liveBaseActivity.isLCSBound());
		}
		// update state of inherited fragments re-registering services
		if (isLCSBound) {
			LiveChessService liveChessService;
			try {
				liveChessService = getLiveService();
				liveChessService.setLccEventListener(this);
				liveChessService.setGameTaskListener(gameTaskListener);
			} catch (DataNotValidException e) {
				e.printStackTrace();
			}
		}
	}

	protected LiveChessService getLiveService() throws DataNotValidException {
		LiveChessService service = liveBaseActivity.getLiveService();
		if (service == null || !service.isUserConnected()) {
			if (service == null) {
				throw new DataNotValidException(DataNotValidException.SERVICE_NULL);
			} else if (service.getLccHelper() == null) {
				throw new DataNotValidException(DataNotValidException.LCC_HELPER_NULL);
			} else if (service.getUser() == null) {
				throw new DataNotValidException(DataNotValidException.USER_NULL);
			} else /*if (!service.isConnected())*/ {
				throw new DataNotValidException(DataNotValidException.NOT_CONNECTED);
			}
		} else {
			return service;
		}
	}

	public void onLiveServiceConnected() {
	}

	public void onLiveServiceDisconnected() {
	}

	@Override
	public void setWhitePlayerTimer(String timer) {
	}

	@Override
	public void setBlackPlayerTimer(String timer) {
	}

	@Override
	public void onGameRefresh(GameLiveItem gameItem) {
	}

	@Override
	public void onDrawOffered(String drawOfferUsername) {
	}

	@Override
	public void onGameEnd(Game game, String gameEndMessage) {
	}

	@Override
	public void onInform(String title, String message) {
	}

	@Override
	public void startGameFromService() {
	}

	@Override
	public void createSeek() {
	}

	@Override
	public void onClockFinishing() {
	}

	@Override
	public void onFriendsStatusChanged() {
	}

	@Override
	public void onChallengeRejected(final String by) {
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				showToast(getString(R.string.challengeRejected, by)); // todo: show dialog popup with OK button if necessary
			}
		});
	}

	@Override
	public void expireGame() {
	}

	@Override
	public void updateOpponentOnlineStatus(boolean online) {
	}

	public void setLCSBound(boolean LCSBound) {
		isLCSBound = LCSBound;
		if (isLCSBound) {
			liveService = liveBaseActivity.getLiveService();
		} else {
			onLiveServiceDisconnected();
		}
	}

	protected void logoutFromLive() {
		if (isLCSBound) {
			liveService.logout();
			liveBaseActivity.unBindAndStopLiveService();
		}
	}

	protected class GameTaskListener extends ActionBarUpdateListener<Game> {
		public GameTaskListener() {
			super(getInstance());
		}
	}

	public void onLiveClientConnected() {

		// onAttach
		liveBaseActivity = (LiveBaseActivity) getActivity();
		if (getAppData().isLiveChess()) {
			setLCSBound(liveBaseActivity.isLCSBound());
		}//

		LiveChessService liveService;
		try {
			liveService = getLiveService();
		} catch (DataNotValidException e) {
			LogMe.dl(TAG, e.getMessage());
			return;
		}
		liveService.setLccEventListener(this);
		liveService.setGameTaskListener(gameTaskListener);

		if (liveService.isActiveGamePresent() && !liveService.isCurrentGameObserved()) {
			synchronized(LccHelper.GAME_SYNC_LOCK) {
				Long gameId = liveService.getCurrentGameId();

				GameLiveFragment liveFragment = liveBaseActivity.getGameLiveFragment();
				if (liveFragment == null) {
					if (!isTablet) {
						liveFragment = GameLiveFragment.createInstance(gameId);
					} else {
						liveFragment = GameLiveFragmentTablet.createInstance(gameId);
					}
				} else {
					liveFragment.invalidateGameScreen();
				}
//				getActivityFace().openFragment(liveFragment, true);
				getActivityFace().openFragment(liveFragment);
			}
		} else {
			createSeek();
		}
	}
}
