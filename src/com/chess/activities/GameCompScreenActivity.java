package com.chess.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.*;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.*;
import android.widget.*;
import com.chess.R;
import com.chess.core.*;
import com.chess.engine.Board2;
import com.chess.engine.Move;
import com.chess.engine.MoveParser2;
import com.chess.lcc.android.GameEvent;
import com.chess.lcc.android.LccHolder;
import com.chess.model.GameListElement;
import com.chess.utilities.*;
import com.chess.views.BoardView2;
import com.mobclix.android.sdk.MobclixIABRectangleMAdView;

import java.util.ArrayList;
import java.util.Timer;

/**
 * GameTacticsScreenActivity class
 *
 * @author alien_roger
 * @created at: 08.02.12 7:17
 */
public class GameCompScreenActivity extends CoreActivityActionBar implements View.OnClickListener {

	private final static int MENU_COMPUTER_NEW_GAME = 0;
	private final static int MENU_COMPUTER_OPTIONS = 1;
	private final static int MENU_COMPUTER_RESIDE = 2;
	private final static int MENU_COMPUTER_HINT = 3;
	private final static int MENU_COMPUTER_PREVIOUS = 4;
	private final static int MENU_COMPUTER_NEXT = 5;
	private final static int MENU_COMPUTER_NEW_GAME_WHITE = 6;
	private final static int MENU_COMPUTER_NEW_GAME_BLACK = 7;
	private final static int MENU_COMPUTER_EMAIL_GAME = 8;
	private final static int MENU_COMPUTER_SETTINGS = 9;
//
//	private final static int MENU_LIVE_OPTIONS = 0;
//	private final static int MENU_LIVE_CHAT = 6;
//	private final static int MENU_LIVE_SETTINGS = 1;
//	private final static int MENU_LIVE_RESIDE = 2;
//	private final static int MENU_LIVE_DRAW_OFFER = 3;
//	private final static int MENU_LIVE_RESIGN_OR_ABORT = 4;
//	private final static int MENU_LIVE_MESSAGES = 5;
//
//	private final static int MENU_ECHESS_NEXT_GAME = 0;
//	private final static int MENU_ECHESS_ANALYSIS = 2;
//	private final static int MENU_ECHESS_CHAT = 3;
//	private final static int MENU_ECHESS_PREVIOUS = 4;
//	private final static int MENU_ECHESS_NEXT = 5;
//	private final static int MENU_ECHESS_SETTINGS = 6;
//	private final static int MENU_ECHESS_BACK_TO_GAME_LIST = 7;
//	private final static int MENU_ECHESS_MESSAGES = 8;
//	private final static int MENU_ECHESS_RESIDE = 9;
//	private final static int MENU_ECHESS_DRAW_OFFER = 10;
//	private final static int MENU_ECHESS_RESIGN_OR_ABORT = 11;
//
//	private final static int MENU_TACTICS_NEXT_GAME = 0;
//	private final static int MENU_TACTICS_RESIDE = 2;
//	private final static int MENU_TACTICS_ANALYSIS = 3;
//	private final static int MENU_TACTICS_PREVIOUS = 4;
//	private final static int MENU_TACTICS_NEXT = 5;
//	private final static int MENU_TACTICS_SKIP_PROBLEM = 6;
//	private final static int MENU_TACTICS_SHOW_ANSWER = 7;
//	private final static int MENU_TACTICS_SETTINGS = 8;

	private final static int DIALOG_TACTICS_LIMIT = 0;
	private final static int DIALOG_TACTICS_START_TACTICS = 1;
	private final static int DIALOG_TACTICS_HUNDRED = 2;
	private final static int DIALOG_TACTICS_OFFLINE_RATING = 3;
	private final static int DIALOG_DRAW_OFFER = 4;
	private final static int DIALOG_ABORT_OR_RESIGN = 5;

	private final static int CALLBACK_GAME_STARTED = 10;
	private final static int CALLBACK_GET_TACTICS = 7;
	private final static int CALLBACK_ECHESS_MOVE_WAS_SENT = 8;
	private final static int CALLBACK_REPAINT_UI = 0;
	private final static int CALLBACK_GAME_REFRESH = 9;
	private final static int CALLBACK_TACTICS_CORRECT = 6;
	private final static int CALLBACK_TACTICS_WRONG = 5;
	private final static int CALLBACK_SEND_MOVE = 1;
	private final static int CALLBACK_GET_ECHESS_GAME_AND_SEND_MOVE = 12;

	public BoardView2 boardView;
	private LinearLayout analysisLL;
	private LinearLayout analysisButtons;
	private RelativeLayout chatPanel;
	private TextView white, black, thinking, timer, movelist;
	private Timer onlineGameUpdate = null;
	private Timer tacticsTimer = null;
	private boolean msgShowed = false, isMoveNav = false, chat = false;
	private int resignOrAbort = R.string.resign;

	private com.chess.model.Game game;

	private TextView whiteClockView;
	private TextView blackClockView;

	protected AlertDialog adPopup;
	private TextView endOfGameMessage;
	private LinearLayout adViewWrapper;

//	private FirstTacticsDialogListener firstTackicsDialogListener;
//	private MaxTacticksDialogListener maxTackicksDialogListener;
//	private HundredTacticsDialogListener hundredTackicsDialogListener;
//	private OfflineModeDialogListener offlineModeDialogListener;
	private DrawOfferDialogListener drawOfferDialogListener;
	private AbortGameDialogListener abortGameDialogListener;

//	private WrongScoreDialogListener wrongScoreDialogListener;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (boardView.getBoard().analysis) {
				if (!MainApp.isTacticsGameMode(boardView)) {
					boardView.setBoard(new Board2(this));
					boardView.getBoard().init = true;
					boardView.getBoard().mode = extras.getInt(AppConstants.GAME_MODE);

					if (mainApp.getCurrentGame().values.get("game_type").equals("2"))
						boardView.getBoard().chess960 = true;

					if (!isUserColorWhite()) {
						boardView.getBoard().setReside(true);
					}
					String[] Moves = {};
					if (mainApp.getCurrentGame().values.get("move_list").contains("1.")) {
						Moves = mainApp.getCurrentGame().values.get("move_list")
								.replaceAll("[0-9]{1,4}[.]", "").replaceAll("  ", " ").substring(1).split(" ");
						boardView.getBoard().movesCount = Moves.length;
					}

					String FEN = mainApp.getCurrentGame().values.get("starting_fen_position");
					if (!FEN.equals("")) {
						boardView.getBoard().GenCastlePos(FEN);
						MoveParser2.FenParse(FEN, boardView.getBoard());
					}

					int i;
					for (i = 0; i < boardView.getBoard().movesCount; i++) {

						int[] moveFT = mainApp.isLiveChess() ? MoveParser2.parseCoordinate(boardView.getBoard(), Moves[i]) : MoveParser2.Parse(boardView.getBoard(), Moves[i]);
						if (moveFT.length == 4) {
							Move m;
							if (moveFT[3] == 2)
								m = new Move(moveFT[0], moveFT[1], 0, 2);
							else
								m = new Move(moveFT[0], moveFT[1], moveFT[2], moveFT[3]);
							boardView.getBoard().makeMove(m, false);
						} else {
							Move m = new Move(moveFT[0], moveFT[1], 0, 0);
							boardView.getBoard().makeMove(m, false);
						}
					}
					Update(CALLBACK_REPAINT_UI);
					boardView.getBoard().takeBack();
					boardView.invalidate();

					//last move anim
					new Thread(new Runnable() {
						@Override
						public void run() {
							try {
								Thread.sleep(1300);
								boardView.getBoard().takeNext();
								update.sendEmptyMessage(0);
							} catch (Exception e) {
							}
						}

						private Handler update = new Handler() {
							@Override
							public void dispatchMessage(Message msg) {
								super.dispatchMessage(msg);
								Update(CALLBACK_REPAINT_UI);
								boardView.invalidate();
							}
						};
					}).start();
				}/* else if (MainApp.isTacticsGameMode(boardView)) {
					if (mainApp.getTactic() != null && mainApp.getTactic().values.get(AppConstants.STOP).equals("1")) {
						openOptionsMenu();
						return true;
					}
					int sec = boardView.getBoard().sec;
					if (mainApp.guest || mainApp.noInternet) {
						boardView.setBoard(new Board2(this));
						boardView.getBoard().mode = AppConstants.GAME_MODE_TACTICS;

						String FEN = mainApp.getTacticsBatch().get(mainApp.currentTacticProblem).values.get(AppConstants.FEN);
						if (!FEN.equals("")) {
							boardView.getBoard().GenCastlePos(FEN);
							MoveParser2.FenParse(FEN, boardView.getBoard());
							String[] tmp = FEN.split(" ");
							if (tmp.length > 1) {
								if (tmp[1].trim().equals("w")) {
									boardView.getBoard().setReside(true);
								}
							}
						}
						if (mainApp.getTacticsBatch().get(mainApp.currentTacticProblem).values.get(AppConstants.MOVE_LIST).contains("1.")) {
							boardView.getBoard().setTacticMoves(mainApp.getTacticsBatch()
									.get(mainApp.currentTacticProblem).values.get(AppConstants.MOVE_LIST).replaceAll("[0-9]{1,4}[.]", "").replaceAll("[.]", "").replaceAll("  ", " ").substring(1).split(" "));
							boardView.getBoard().movesCount = 1;
						}
						boardView.getBoard().sec = sec;
						boardView.getBoard().left = Integer.parseInt(mainApp.getTacticsBatch()
								.get(mainApp.currentTacticProblem).values.get(AppConstants.AVG_SECONDS)) - sec;
						startTacticsTimer();
						int[] moveFT = MoveParser2.Parse(boardView.getBoard(), boardView.getBoard().getTacticMoves()[0]);
						if (moveFT.length == 4) {
							Move m;
							if (moveFT[3] == 2)
								m = new Move(moveFT[0], moveFT[1], 0, 2);
							else
								m = new Move(moveFT[0], moveFT[1], moveFT[2], moveFT[3]);
							boardView.getBoard().makeMove(m);
						} else {
							Move m = new Move(moveFT[0], moveFT[1], 0, 0);
							boardView.getBoard().makeMove(m);
						}
						Update(CALLBACK_REPAINT_UI);
						boardView.getBoard().takeBack();
						boardView.invalidate();

						//last move anim
						new Thread(new Runnable() {
							public void run() {
								try {
									Thread.sleep(1300);
									boardView.getBoard().takeNext();
									update.sendEmptyMessage(0);
								} catch (Exception e) {
								}
							}

							private Handler update = new Handler() {
								@Override
								public void dispatchMessage(Message msg) {
									super.dispatchMessage(msg);
									Update(CALLBACK_REPAINT_UI);
									boardView.invalidate();
								}
							};
						}).start();
					} else {
						if (mainApp.getTactic() != null && mainApp.getTactic().values.get(AppConstants.STOP).equals("1")) {
							openOptionsMenu();
							return true;
						}
						boardView.setBoard(new Board2(this));
						boardView.getBoard().mode = AppConstants.GAME_MODE_TACTICS;

						String FEN = mainApp.getTactic().values.get(AppConstants.FEN);
						if (!FEN.equals("")) {
							boardView.getBoard().GenCastlePos(FEN);
							MoveParser2.FenParse(FEN, boardView.getBoard());
							String[] tmp2 = FEN.split(" ");
							if (tmp2.length > 1) {
								if (tmp2[1].trim().equals("w")) {
									boardView.getBoard().setReside(true);
								}
							}
						}

						if (mainApp.getTactic().values.get(AppConstants.MOVE_LIST).contains("1.")) {
							boardView.getBoard().setTacticMoves(mainApp.getTactic().values.get(AppConstants.MOVE_LIST).replaceAll("[0-9]{1,4}[.]", "").replaceAll("[.]", "").replaceAll("  ", " ").substring(1).split(" "));
							boardView.getBoard().movesCount = 1;
						}
						boardView.getBoard().sec = sec;
						boardView.getBoard().left = Integer.parseInt(mainApp.getTactic().values.get(AppConstants.AVG_SECONDS)) - sec;
						int[] moveFT = MoveParser2.Parse(boardView.getBoard(), boardView.getBoard().getTacticMoves()[0]);
						if (moveFT.length == 4) {
							Move m;
							if (moveFT[3] == 2)
								m = new Move(moveFT[0], moveFT[1], 0, 2);
							else
								m = new Move(moveFT[0], moveFT[1], moveFT[2], moveFT[3]);
							boardView.getBoard().makeMove(m);
						} else {
							Move m = new Move(moveFT[0], moveFT[1], 0, 0);
							boardView.getBoard().makeMove(m);
						}
						Update(CALLBACK_REPAINT_UI);
						boardView.getBoard().takeBack();
						boardView.invalidate();

						//last move anim
						new Thread(new Runnable() {
							public void run() {
								try {
									Thread.sleep(1300);
									boardView.getBoard().takeNext();
									update.sendEmptyMessage(0);
								} catch (Exception e) {
								}
							}

							private Handler update = new Handler() {
								@Override
								public void dispatchMessage(Message msg) {
									super.dispatchMessage(msg);
									Update(CALLBACK_REPAINT_UI);
									boardView.invalidate();
								}
							};
						}).start();
					}
				}*/
			} else {
				LoadPrev(MainApp.loadPrev);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.chat) {
			chat = true;
			GetOnlineGame(mainApp.getGameId());
			chatPanel.setVisibility(View.GONE);
		} else if (view.getId() == R.id.prev) {
			boardView.finished = false;
			boardView.sel = false;
			boardView.getBoard().takeBack();
			boardView.invalidate();
			Update(CALLBACK_REPAINT_UI);
			isMoveNav = true;
		} else if (view.getId() == R.id.next) {
			boardView.getBoard().takeNext();
			boardView.invalidate();
			Update(CALLBACK_REPAINT_UI);
			isMoveNav = true;
		} else if (view.getId() == R.id.newGame) {
			startActivity(new Intent(this, OnlineNewGame.class));
		} else if (view.getId() == R.id.home) {
			startActivity(new Intent(this, Tabs.class));
		}
	}

//	private class FirstTacticsDialogListener implements DialogInterface.OnClickListener {
//		@Override
//		public void onClick(DialogInterface dialog, int whichButton) {
//			if (whichButton == DialogInterface.BUTTON_POSITIVE) {
//				InputStream f = getResources().openRawResource(R.raw.tactics100batch);
//				try {
//					ByteArrayBuffer baf = new ByteArrayBuffer(50);
//					int current = 0;
//					while ((current = f.read()) != -1) {
//						baf.append((byte) current);
//					}
//					String input = new String(baf.toByteArray());
//					String[] tmp = input.split("[|]");
//					int count = tmp.length - 1;
//					mainApp.setTacticsBatch(new ArrayList<Tactic>(count));
//					int i;
//					for (i = 1; i <= count; i++) {
//						mainApp.getTacticsBatch().add(new Tactic(tmp[i].split(":")));
//					}
//					f.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//
//				if (mainApp.guest)
//					GetGuestTacticsGame();
//				else
//					GetTacticsGame("");
//
//			} else if (whichButton == DialogInterface.BUTTON_NEGATIVE) {
//				//mainApp.getTabHost().setCurrentTab(0);
//				boardView.getBoard().setTacticCanceled(true);
//			}
//		}
//	}

//	private class MaxTacticksDialogListener implements DialogInterface.OnClickListener {
//		@Override
//		public void onClick(DialogInterface dialog, int whichButton) {
//			if (whichButton == DialogInterface.BUTTON_POSITIVE) {
//				FlurryAgent.onEvent("Upgrade From Tactics", null);
//				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www." + LccHolder.HOST + "/login.html?als=" + mainApp.getSharedData().getString(AppConstants.USER_TOKEN, "") + "&goto=http%3A%2F%2Fwww." + LccHolder.HOST + "%2Fmembership.html")));
//			} else if (whichButton == DialogInterface.BUTTON_NEGATIVE) {
//				//mainApp.getTabHost().setCurrentTab(0);
//				boardView.getBoard().setTacticCanceled(true);
//			}
//		}
//	}

//	private class HundredTacticsDialogListener implements DialogInterface.OnClickListener {
//		@Override
//		public void onClick(DialogInterface dialog, int whichButton) {
//			//mainApp.getTabHost().setCurrentTab(0);
//			mainApp.currentTacticProblem = 0;
//		}
//	}

//	private class OfflineModeDialogListener implements DialogInterface.OnClickListener {
//		@Override
//		public void onClick(DialogInterface dialog, int whichButton) {
//			if (whichButton == DialogInterface.BUTTON_POSITIVE) {
//				GetGuestTacticsGame();
//			} else if (whichButton == DialogInterface.BUTTON_NEGATIVE) {
//				//mainApp.getTabHost().setCurrentTab(0);
//				boardView.getBoard().setTacticCanceled(true);
//			}
//		}
//	}

	private class DrawOfferDialogListener implements DialogInterface.OnClickListener {
		@Override
		public void onClick(DialogInterface dialog, int whichButton) {
			if (whichButton == DialogInterface.BUTTON_POSITIVE) {
				/*if (mainApp.isLiveChess() && MainApp.isLiveOrEchessGameMode(boardView)) {
					final com.chess.live.client.Game game = lccHolder.getGame(mainApp.getGameId());
					LccHolder.LOG.info("Request draw: " + game);
					lccHolder.getAndroid().runMakeDrawTask(game);
				} else */
				{
					String Draw = "OFFERDRAW";
					if (mainApp.acceptdraw)
						Draw = "ACCEPTDRAW";
					String result = Web.Request("http://www." + LccHolder.HOST + "/api/submit_echess_action?id=" + mainApp.getSharedData().getString(AppConstants.USER_TOKEN, "") + "&chessid=" + mainApp.getCurrentGame().values.get(AppConstants.GAME_ID) + "&command=" + Draw + "&timestamp=" + mainApp.getCurrentGame().values.get(AppConstants.TIMESTAMP), "GET", null, null);
					if (result.contains("Success")) {
						mainApp.ShowDialog(coreContext, "", getString(R.string.drawoffered));
					} else if (result.contains("Error+")) {
						mainApp.ShowDialog(coreContext, "Error", result.split("[+]")[1]);
					} else {
						//mainApp.ShowDialog(Game.this, "Error", result);
					}
				}
			}
		}
	}

	private class AbortGameDialogListener implements DialogInterface.OnClickListener {

		@Override
		public void onClick(DialogInterface dialog, int whichButton) {
			if (whichButton == DialogInterface.BUTTON_POSITIVE) {
				/*if (mainApp.isLiveChess() && MainApp.isLiveOrEchessGameMode(boardView)) {
					final com.chess.live.client.Game game = lccHolder.getGame(mainApp.getGameId());

					if (lccHolder.isFairPlayRestriction(mainApp.getGameId())) {
						System.out.println("LCCLOG: resign game by fair play restriction: " + game);
						LccHolder.LOG.info("Resign game: " + game);
						lccHolder.getAndroid().runMakeResignTask(game);
					} else if (lccHolder.isAbortableBySeq(mainApp.getGameId())) {
						LccHolder.LOG.info("LCCLOG: abort game: " + game);
						lccHolder.getAndroid().runAbortGameTask(game);
					} else {
						LccHolder.LOG.info("LCCLOG: resign game: " + game);
						lccHolder.getAndroid().runMakeResignTask(game);
					}
					finish();
				} else*/
				{
					String result = Web.Request("http://www." + LccHolder.HOST
							+ "/api/submit_echess_action?id="
							+ mainApp.getSharedData().getString(AppConstants.USER_TOKEN, "")
							+ "&chessid=" + mainApp.getCurrentGame().values.get(AppConstants.GAME_ID)
							+ "&command=RESIGN&timestamp="
							+ mainApp.getCurrentGame().values.get(AppConstants.TIMESTAMP), "GET", null, null);
					if (result.contains("Success")) {
						if (MobclixHelper.isShowAds(mainApp)) {
							sendBroadcast(new Intent(IntentConstants.ACTION_SHOW_GAME_END_POPUP)
									.putExtra(AppConstants.MESSAGE, "GAME OVER")
									.putExtra(AppConstants.FINISHABLE, true));
						} else {
							finish();
						}
					} else if (result.contains("Error+")) {
						mainApp.ShowDialog(coreContext, "Error", result.split("[+]")[1]);
					} else {
						//mainApp.ShowDialog(Game.this, "Error", result);
					}
				}
			}
		}
	}



//	private class WrongScoreDialogListener implements DialogInterface.OnClickListener {
//		@Override
//		public void onClick(DialogInterface dialog, int which) {
//			if (which == 0) {
//				GetTacticsGame("");
//			} else if (which == 1) {
//				boardView.getBoard().retry = true;
//				GetTacticsGame(mainApp.getTactic().values.get(AppConstants.ID));
//			} else if (which == 2) {
//				boardView.finished = true;
//				mainApp.getTactic().values.put(AppConstants.STOP, "1");
//			}
//		}
//	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
//			case DIALOG_TACTICS_LIMIT:
//				FlurryAgent.onEvent("Tactics Daily Limit Exceded", null);
//				return new AlertDialog.Builder(this)
//						.setTitle(getString(R.string.daily_limit_exceeded))
//						.setMessage(getString(R.string.max_tackics_for_today_reached))
//						.setPositiveButton(getString(R.string.ok), maxTackicksDialogListener)
//						.setNegativeButton(R.string.cancel, maxTackicksDialogListener)
//						.create();
//			case DIALOG_TACTICS_START_TACTICS:
//				return new AlertDialog.Builder(this)
//						.setTitle(getString(R.string.ready_for_first_tackics_q))
//						.setPositiveButton(R.string.yes, firstTackicsDialogListener)
//						.setNegativeButton(R.string.no, firstTackicsDialogListener)
//						.create();
//			case DIALOG_TACTICS_HUNDRED:
//				return new AlertDialog.Builder(this)
//						.setTitle(R.string.hundred_tackics_completed)
//						.setNegativeButton(R.string.okay, hundredTackicsDialogListener)
//						.create();
//			case DIALOG_TACTICS_OFFLINE_RATING:
//				return new AlertDialog.Builder(this)
//						.setTitle(R.string.offline_mode)
//						.setMessage(getString(R.string.no_network_rating_not_changed))
//						.setPositiveButton(R.string.okay, offlineModeDialogListener)
//						.setNegativeButton(R.string.cancel, offlineModeDialogListener)
//						.create();
			case DIALOG_DRAW_OFFER:
				return new AlertDialog.Builder(this)
						.setTitle(R.string.drawoffer)
						.setMessage(getString(R.string.are_you_sure_q))
						.setPositiveButton(getString(R.string.ok), drawOfferDialogListener)
						.setNegativeButton(getString(R.string.cancel), drawOfferDialogListener)
						.create();
			case DIALOG_ABORT_OR_RESIGN:
				return new AlertDialog.Builder(this)
						.setTitle(R.string.abort_resign_game)
						.setMessage(getString(R.string.are_you_sure_q))
						.setPositiveButton(R.string.ok, abortGameDialogListener)
						.setNegativeButton(R.string.cancel, abortGameDialogListener)
						.create();

			default:
				break;
		}
		return super.onCreateDialog(id);
	}

	private void init() {
//		firstTackicsDialogListener = new FirstTacticsDialogListener();
//		maxTackicksDialogListener = new MaxTacticksDialogListener();
//		hundredTackicsDialogListener = new HundredTacticsDialogListener();
//		offlineModeDialogListener = new OfflineModeDialogListener();
		drawOfferDialogListener = new DrawOfferDialogListener();
		abortGameDialogListener = new AbortGameDialogListener();

//		wrongScoreDialogListener = new WrongScoreDialogListener();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/*if (mainApp.isLiveChess() && MainApp.isLiveOrEchessGameMode(extras.getInt(AppConstants.GAME_MODE))) {
			setContentView(R.layout.boardviewlive2);
//			lccHolder.getAndroid().setGameActivity(this);   //TODO
		} else*/
		{
			setContentView(R.layout.boardview2);
		}

		init();

		analysisLL = (LinearLayout) findViewById(R.id.analysis);
		analysisButtons = (LinearLayout) findViewById(R.id.analysisButtons);
//		if (mainApp.isLiveChess() && !MainApp.isTacticsGameMode(extras.getInt(AppConstants.GAME_MODE))) {
//		chatPanel = (RelativeLayout) findViewById(R.id.chatPanel);
//		ImageButton chatButton = (ImageButton) findViewById(R.id.chat);
//		chatButton.setOnClickListener(this);
//		}
//		if (!mainApp.isLiveChess()) {
		findViewById(R.id.prev).setOnClickListener(this);
		findViewById(R.id.next).setOnClickListener(this);
//		}

		white = (TextView) findViewById(R.id.white);
		black = (TextView) findViewById(R.id.black);
		thinking = (TextView) findViewById(R.id.thinking);
		timer = (TextView) findViewById(R.id.timer);
		movelist = (TextView) findViewById(R.id.movelist);

		whiteClockView = (TextView) findViewById(R.id.whiteClockView);
		blackClockView = (TextView) findViewById(R.id.blackClockView);
		/*if (mainApp.isLiveChess() && MainApp.isLiveOrEchessGameMode(extras.getInt(AppConstants.GAME_MODE))
				&& lccHolder.getWhiteClock() != null && lccHolder.getBlackClock() != null) {
			whiteClockView.setVisibility(View.VISIBLE);
			blackClockView.setVisibility(View.VISIBLE);
			lccHolder.getWhiteClock().paint();
			lccHolder.getBlackClock().paint();
			final com.chess.live.client.Game game = lccHolder.getGame(new Long(extras.getString(AppConstants.GAME_ID)));
			final User whiteUser = game.getWhitePlayer();
			final User blackUser = game.getBlackPlayer();
			final Boolean isWhite = (!game.isMoveOf(whiteUser) && !game.isMoveOf(blackUser)) ? null : game.isMoveOf(whiteUser);
			lccHolder.setClockDrawPointer(isWhite);
		}*/

		endOfGameMessage = (TextView) findViewById(R.id.endOfGameMessage);

		boardView = (BoardView2) findViewById(R.id.boardview);
		boardView.setFocusable(true);
		boardView.setBoard((Board2) getLastNonConfigurationInstance());

		lccHolder = mainApp.getLccHolder();

		if (boardView.getBoard() == null) {
			boardView.setBoard(new Board2(this));
			boardView.getBoard().init = true;
			boardView.getBoard().mode = extras.getInt(AppConstants.GAME_MODE);
			boardView.getBoard().GenCastlePos("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR");
			//boardView.getBoard().GenCastlePos("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");

			if (MainApp.isComputerGameMode(boardView)
					&& !mainApp.getSharedData().getString(AppConstants.SAVED_COMPUTER_GAME, "").equals("")) {
				int i;
				String[] moves = mainApp.getSharedData().getString(AppConstants.SAVED_COMPUTER_GAME, "").split("[|]");
				for (i = 1; i < moves.length; i++) {
					String[] move = moves[i].split(":");
					boardView.getBoard().makeMove(new Move(
							Integer.parseInt(move[0]),
							Integer.parseInt(move[1]),
							Integer.parseInt(move[2]),
							Integer.parseInt(move[3])), false);
				}
				if (MainApp.isComputerVsHumanBlackGameMode(boardView))
					boardView.getBoard().setReside(true);
			} else {
				if (MainApp.isComputerVsHumanBlackGameMode(boardView)) {
					boardView.getBoard().setReside(true);
					boardView.invalidate();
					boardView.ComputerMove(mainApp.strength[mainApp.getSharedData().getInt(mainApp.getSharedData().getString(AppConstants.USERNAME, "") + AppConstants.PREF_COMPUTER_STRENGTH, 0)]);
				}
				if (MainApp.isComputerVsComputerGameMode(boardView)) {
					boardView.ComputerMove(mainApp.strength[mainApp.getSharedData().getInt(mainApp.getSharedData().getString(AppConstants.USERNAME, "") + AppConstants.PREF_COMPUTER_STRENGTH, 0)]);
				}
				if (MainApp.isLiveOrEchessGameMode(boardView) || MainApp.isFinishedEchessGameMode(boardView))
					mainApp.setGameId(extras.getString(AppConstants.GAME_ID));
			}
//			if (MainApp.isTacticsGameMode(boardView)) {
//				showDialog(DIALOG_TACTICS_START_TACTICS);
//				return;
//			}
		}

		if (MobclixHelper.isShowAds(mainApp) /*&& getRectangleAdview() == null*/
				&& mainApp.getTabHost() != null && !mainApp.getTabHost().getCurrentTabTag().equals("tab4")) {
			setRectangleAdview(new MobclixIABRectangleMAdView(this));
			getRectangleAdview().setRefreshTime(-1);
			getRectangleAdview().addMobclixAdViewListener(new MobclixAdViewListenerImpl(true, mainApp));
			mainApp.setForceRectangleAd(false);
		}

		Update(CALLBACK_REPAINT_UI);
	}

	private void GetOnlineGame(final String game_id) {
		if (appService != null && appService.getRepeatableTimer() != null) {
			appService.getRepeatableTimer().cancel();
			appService.setRepeatableTimer(null);
		}
		mainApp.setGameId(game_id);

		/*if (mainApp.isLiveChess() && MainApp.isLiveOrEchessGameMode(boardView)) {
			Update(CALLBACK_GAME_STARTED);
		} else*/
		{
			if (appService != null) {
				appService.RunSingleTask(CALLBACK_GAME_STARTED,
						"http://www." + LccHolder.HOST + "/api/v3/get_game?id=" + mainApp.getSharedData().getString(AppConstants.USER_TOKEN, "") + "&gid=" + game_id,
						null/*progressDialog = MyProgressDialog.show(this, null, getString(R.string.loading), true)*/);
			}
		}
	}

//	private void GetTacticsGame(final String id) {
//		FlurryAgent.onEvent("Tactics Session Started For Registered", null);
//		if (!mainApp.noInternet) {
//			boardView.setBoard(new Board2(this));
//			boardView.getBoard().mode = AppConstants.GAME_MODE_TACTICS;
//
//			if (mainApp.getTactic() != null
//					&& id.equals(mainApp.getTactic().values.get(AppConstants.ID))) {
//				boardView.getBoard().retry = true;
//				String FEN = mainApp.getTactic().values.get(AppConstants.FEN);
//				if (!FEN.equals("")) {
//					boardView.getBoard().GenCastlePos(FEN);
//					MoveParser2.FenParse(FEN, boardView.getBoard());
//					String[] tmp2 = FEN.split(" ");
//					if (tmp2.length > 1) {
//						if (tmp2[1].trim().equals("w")) {
//							boardView.getBoard().setReside(true);
//						}
//					}
//				}
//
//				if (mainApp.getTactic().values.get(AppConstants.MOVE_LIST).contains("1.")) {
//					boardView.getBoard().setTacticMoves(mainApp.getTactic().values.get(AppConstants.MOVE_LIST).replaceAll("[0-9]{1,4}[.]", "").replaceAll("[.]", "").replaceAll("  ", " ").substring(1).split(" "));
//					boardView.getBoard().movesCount = 1;
//				}
//				boardView.getBoard().sec = 0;
//				boardView.getBoard().left = Integer.parseInt(mainApp.getTactic().values.get(AppConstants.AVG_SECONDS));
//				startTacticsTimer();
//				int[] moveFT = MoveParser2.Parse(boardView.getBoard(), boardView.getBoard().getTacticMoves()[0]);
//				if (moveFT.length == 4) {
//					Move m;
//					if (moveFT[3] == 2)
//						m = new Move(moveFT[0], moveFT[1], 0, 2);
//					else
//						m = new Move(moveFT[0], moveFT[1], moveFT[2], moveFT[3]);
//					boardView.getBoard().makeMove(m);
//				} else {
//					Move m = new Move(moveFT[0], moveFT[1], 0, 0);
//					boardView.getBoard().makeMove(m);
//				}
//				Update(CALLBACK_REPAINT_UI);
//				boardView.getBoard().takeBack();
//				boardView.invalidate();
//
//				//last move anim
//				new Thread(new Runnable() {
//					public void run() {
//						try {
//							Thread.sleep(1300);
//							boardView.getBoard().takeNext();
//							update.sendEmptyMessage(0);
//						} catch (Exception e) {
//						}
//					}
//
//					private Handler update = new Handler() {
//						@Override
//						public void dispatchMessage(Message msg) {
//							super.dispatchMessage(msg);
//							Update(CALLBACK_REPAINT_UI);
//							boardView.invalidate();
//						}
//					};
//				}).start();
//
//				return;
//			}
//		}
//		if (appService != null) {
//			appService.RunSingleTask(CALLBACK_GET_TACTICS,
//					"http://www." + LccHolder.HOST + "/api/tactics_trainer?id="
//							+ mainApp.getSharedData().getString(AppConstants.USER_TOKEN, "") + "&tactics_id=" + id,
//					progressDialog = new MyProgressDialog(ProgressDialog.show(this, null, getString(R.string.loading), false))
//			);
//		}
//	}

//	private void GetGuestTacticsGame() {
//		FlurryAgent.onEvent("Tactics Session Started For Guest", null);
//
//		if (mainApp.currentTacticProblem >= mainApp.getTacticsBatch().size()) {
//			showDialog(DIALOG_TACTICS_HUNDRED);
//			return;
//		}
//
//		boardView.setBoard(new Board2(this));
//		boardView.getBoard().mode = AppConstants.GAME_MODE_TACTICS;
//
//		String FEN = mainApp.getTacticsBatch().get(mainApp.currentTacticProblem).values.get(AppConstants.FEN);
//		if (!FEN.equals("")) {
//			boardView.getBoard().GenCastlePos(FEN);
//			MoveParser2.FenParse(FEN, boardView.getBoard());
//			String[] tmp = FEN.split(" ");
//			if (tmp.length > 1) {
//				if (tmp[1].trim().equals("w")) {
//					boardView.getBoard().setReside(true);
//				}
//			}
//		}
//		if (mainApp.getTacticsBatch().get(mainApp.currentTacticProblem).values.get(AppConstants.MOVE_LIST).contains("1.")) {
//			boardView.getBoard().setTacticMoves(mainApp.getTacticsBatch()
//					.get(mainApp.currentTacticProblem).values.get(AppConstants.MOVE_LIST)
//					.replaceAll("[0-9]{1,4}[.]", "").replaceAll("[.]", "")
//					.replaceAll("  ", " ").substring(1).split(" "));
//			boardView.getBoard().movesCount = 1;
//		}
//		boardView.getBoard().sec = 0;
//		boardView.getBoard().left = Integer.parseInt(mainApp.getTacticsBatch()
//				.get(mainApp.currentTacticProblem).values.get(AppConstants.AVG_SECONDS));
//		startTacticsTimer();
//		int[] moveFT = MoveParser2.Parse(boardView.getBoard(), boardView.getBoard().getTacticMoves()[0]);
//		if (moveFT.length == 4) {
//			Move m;
//			if (moveFT[3] == 2)
//				m = new Move(moveFT[0], moveFT[1], 0, 2);
//			else
//				m = new Move(moveFT[0], moveFT[1], moveFT[2], moveFT[3]);
//			boardView.getBoard().makeMove(m);
//		} else {
//			Move m = new Move(moveFT[0], moveFT[1], 0, 0);
//			boardView.getBoard().makeMove(m);
//		}
//		Update(CALLBACK_REPAINT_UI);
//		boardView.getBoard().takeBack();
//		boardView.invalidate();
//
//		//last move anim
//		new Thread(new Runnable() {
//			public void run() {
//				try {
//					Thread.sleep(1300);
//					boardView.getBoard().takeNext();
//					update.sendEmptyMessage(0);
//				} catch (Exception e) {
//				}
//			}
//
//			private Handler update = new Handler() {
//				@Override
//				public void dispatchMessage(Message msg) {
//					super.dispatchMessage(msg);
//					Update(CALLBACK_REPAINT_UI);
//					boardView.invalidate();
//				}
//			};
//		}).start();
//	}





	//	@Override
	public void LoadPrev(int code) {
		if (boardView.getBoard() != null && MainApp.isTacticsGameMode(boardView)) {
//			//mainApp.getTabHost().setCurrentTab(0);
			boardView.getBoard().setTacticCanceled(true);
			onBackPressed();
		} else {
			finish();
		}
	}

	@Override
	public void Update(int code) {
		int UPDATE_DELAY = 10000;
		int[] moveFT = new int[]{};
		switch (code) {
			case ERROR_SERVER_RESPONSE:
				if (!MainApp.isTacticsGameMode(boardView))
					finish();
				/*else if (MainApp.isTacticsGameMode(boardView)) {
					*//*//mainApp.getTabHost().setCurrentTab(0);
					boardView.getBoard().getTactic()Canceled = true;*//*
					if (mainApp.noInternet) {
						if (mainApp.offline) {
							GetGuestTacticsGame();
						} else {
							mainApp.offline = true;
							showDialog(DIALOG_TACTICS_OFFLINE_RATING);
						}
						return;
					}
				}*/
				//finish();
				break;
			case INIT_ACTIVITY:

				if (boardView.getBoard().init && MainApp.isLiveOrEchessGameMode(boardView) || MainApp.isFinishedEchessGameMode(boardView)) {
					//System.out.println("@@@@@@@@ POINT 1 mainApp.getGameId()=" + mainApp.getGameId());
					GetOnlineGame(mainApp.getGameId());
					boardView.getBoard().init = false;
				} else if (!boardView.getBoard().init) {
					/*if (MainApp.isLiveOrEchessGameMode(boardView) && appService != null
							&& appService.getRepeatableTimer() == null) {
						if (progressDialog != null) {
							progressDialog.dismiss();
							progressDialog = null;
						}
						if (!mainApp.isLiveChess()) {
							appService.RunRepeatbleTask(CALLBACK_GAME_REFRESH, UPDATE_DELAY, UPDATE_DELAY,
									"http://www." + LccHolder.HOST + "/api/v3/get_game?id=" + mainApp.getSharedData().getString(AppConstants.USER_TOKEN, "") + "&gid=" + mainApp.getGameId(),
									null*//*progressDialog*//*
							);
						}
					}*/
				}
				break;
			case CALLBACK_REPAINT_UI: {
				switch (boardView.getBoard().mode) {
					case AppConstants.GAME_MODE_COMPUTER_VS_HUMAN_WHITE: {	//w - human; b - comp
						white.setText(getString(R.string.Human));
						black.setText(getString(R.string.Computer));
						break;
					}
					case AppConstants.GAME_MODE_COMPUTER_VS_HUMAN_BLACK: {	//w - comp; b - human
						white.setText(getString(R.string.Computer));
						black.setText(getString(R.string.Human));
						break;
					}
					case AppConstants.GAME_MODE_HUMAN_VS_HUMAN: {	//w - human; b - human
						white.setText(getString(R.string.Human));
						black.setText(getString(R.string.Human));
						break;
					}
					case AppConstants.GAME_MODE_COMPUTER_VS_COMPUTER: {	//w - comp; b - comp
						white.setText(getString(R.string.Computer));
						black.setText(getString(R.string.Computer));
						break;
					}

					default:
						break;
				}

				if (MainApp.isComputerGameMode(boardView)) {
					hideAnalysisButtons();
				}

				/*if (MainApp.isLiveOrEchessGameMode(boardView) || MainApp.isFinishedEchessGameMode(boardView)) {
					if (mainApp.getCurrentGame() != null) {
						white.setText(mainApp.getCurrentGame().values.get(AppConstants.WHITE_USERNAME) + "\n(" + mainApp.getCurrentGame().values.get("white_rating") + ")");
						black.setText(mainApp.getCurrentGame().values.get(AppConstants.BLACK_USERNAME) + "\n(" + mainApp.getCurrentGame().values.get("black_rating") + ")");
					}
				}*/

				/*if (MainApp.isTacticsGameMode(boardView)) {
					if (boardView.getBoard().analysis) {
						timer.setVisibility(View.GONE);
						analysisLL.setVisibility(View.VISIBLE);
						if (!mainApp.isLiveChess() && analysisButtons != null) {
							showAnalysisButtons();
						}
					} else {
						white.setVisibility(View.GONE);
						black.setVisibility(View.GONE);
						timer.setVisibility(View.VISIBLE);
						analysisLL.setVisibility(View.GONE);
						if (!mainApp.isLiveChess() && analysisButtons != null) {
							hideAnalysisButtons();
						}
					}
				}*/
				movelist.setText(boardView.getBoard().MoveListSAN());
				/*if(mainApp.getCurrentGame() != null && mainApp.getCurrentGame().values.get("move_list") != null)
								{
								  movelist.setText(mainApp.getCurrentGame().values.get("move_list"));
								}
								else
								{
								  movelist.setText(boardView.getBoard().MoveListSAN());
								}*/
				boardView.invalidate();

				new Handler().post(new Runnable() {
					@Override
					public void run() {
						boardView.requestFocus();
					}
				});
				break;
			}
			case CALLBACK_SEND_MOVE: {
				findViewById(R.id.moveButtons).setVisibility(View.GONE);
				boardView.getBoard().submit = false;
				//String myMove = boardView.getBoard().MoveSubmit();
				/*if (mainApp.isLiveChess() && MainApp.isLiveOrEchessGameMode(boardView)) {
					final String move = boardView.getBoard().convertMoveLive();
					LccHolder.LOG.info("LCC make move: " + move);
					try {
						lccHolder.makeMove(mainApp.getCurrentGame().values.get(AppConstants.GAME_ID), move);
					} catch (IllegalArgumentException e) {
						LccHolder.LOG.info("LCC illegal move: " + move);
						e.printStackTrace();
					}
				} else */
				if (!mainApp.isLiveChess() && appService != null) {
					if (mainApp.getCurrentGame() == null) {
						if (appService.getRepeatableTimer() != null) {
							appService.getRepeatableTimer().cancel();
							appService.setRepeatableTimer(null);
						}
						appService.RunSingleTask(CALLBACK_GET_ECHESS_GAME_AND_SEND_MOVE,
								"http://www." + LccHolder.HOST + "/api/v3/get_game?id=" +
										mainApp.getSharedData().getString(AppConstants.USER_TOKEN, "") + "&gid=" + mainApp.getGameId(),
								null);
					} else {
						appService.RunSingleTask(CALLBACK_ECHESS_MOVE_WAS_SENT,
								"http://www." + LccHolder.HOST + "/api/submit_echess_action?id=" +
										mainApp.getSharedData().getString(AppConstants.USER_TOKEN, "") + "&chessid=" +
										mainApp.getCurrentGame().values.get(AppConstants.GAME_ID) + "&command=SUBMIT&newmove=" +
										boardView.getBoard().convertMoveEchess() + "&timestamp=" +
										mainApp.getCurrentGame().values.get(AppConstants.TIMESTAMP),
								progressDialog = new MyProgressDialog(
										ProgressDialog.show(this, null, getString(R.string.sendinggameinfo), true)));

						NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
						mNotificationManager.cancel(1);
						Notifications.resetCounter();
					}
				}
				break;
			}
			case CALLBACK_GET_ECHESS_GAME_AND_SEND_MOVE: {
				mainApp.setCurrentGame(ChessComApiParser.GetGameParseV3(response));
				if (!mainApp.isLiveChess() && appService != null) {
					appService.RunSingleTask(CALLBACK_ECHESS_MOVE_WAS_SENT,
							"http://www." + LccHolder.HOST + "/api/submit_echess_action?id=" +
									mainApp.getSharedData().getString(AppConstants.USER_TOKEN, "") + "&chessid=" +
									mainApp.getCurrentGame().values.get(AppConstants.GAME_ID) + "&command=SUBMIT&newmove=" +
									boardView.getBoard().convertMoveEchess() + "&timestamp=" +
									mainApp.getCurrentGame().values.get(AppConstants.TIMESTAMP),
							progressDialog = new MyProgressDialog(
									ProgressDialog.show(this, null, getString(R.string.sendinggameinfo), true)));
					NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
					mNotificationManager.cancel(1);
					Notifications.resetCounter();
				}
				break;
			}
			case 2: {
				white.setVisibility(View.GONE);
				black.setVisibility(View.GONE);
				thinking.setVisibility(View.VISIBLE);
				break;
			}
			case 3: {
				white.setVisibility(View.VISIBLE);
				black.setVisibility(View.VISIBLE);
				thinking.setVisibility(View.GONE);
				break;
			}
			case 4: {
//				CheckTacticMoves();
				break;
			}
			case CALLBACK_TACTICS_WRONG: {
				/*String[] tmp = response.split("[|]");
				if (tmp.length < 2 || tmp[1].trim().equals("")) {
					showDialog(DIALOG_TACTICS_LIMIT);
					return;
				}

				TacticResult result = new TacticResult(tmp[1].split(":"));

				new AlertDialog.Builder(this)
						.setTitle(getString(R.string.wrong_score,
								result.values.get(AppConstants.USER_RATING_CHANGE),
								result.values.get(AppConstants.USER_RATING)))
						.setItems(getResources().getTextArray(R.array.wrongtactic), wrongScoreDialogListener)
						.create().show();*/
				break;
			}
			case CALLBACK_TACTICS_CORRECT: {
			/*	String[] tmp = response.split("[|]");
				if (tmp.length < 2 || tmp[1].trim().equals("")) {
					showDialog(DIALOG_TACTICS_LIMIT);
					return;
				}

				TacticResult result = new TacticResult(tmp[1].split(":"));

				new AlertDialog.Builder(this)
						.setTitle(getString(R.string.correct_score,
								result.values.get(AppConstants.USER_RATING_CHANGE),
								result.values.get(AppConstants.USER_RATING)))
						.setItems(getResources().getTextArray(R.array.correcttactic), new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								if (which == 1) {
									GetTacticsGame("");
								}
							}
						})
						.create().show();*/
				break;
			}
			case CALLBACK_GET_TACTICS:
/*
				boardView.setBoard(new Board2(this));
				boardView.getBoard().mode = AppConstants.GAME_MODE_TACTICS;

				String[] tmp = response.trim().split("[|]");
				if (tmp.length < 3 || tmp[2].trim().equals("")) {
					showDialog(DIALOG_TACTICS_LIMIT);
					return;
				}

				mainApp.setTactic(new Tactic(tmp[2].split(":")));

				String FEN = mainApp.getTactic().values.get(AppConstants.FEN);
				if (!FEN.equals("")) {
					boardView.getBoard().GenCastlePos(FEN);
					MoveParser2.FenParse(FEN, boardView.getBoard());
					String[] tmp2 = FEN.split(" ");
					if (tmp2.length > 1) {
						if (tmp2[1].trim().equals("w")) {
							boardView.getBoard().setReside(true);
						}
					}
				}

				if (mainApp.getTactic().values.get(AppConstants.MOVE_LIST).contains("1.")) {
					boardView.getBoard().setTacticMoves(mainApp.getTactic().values.get(AppConstants.MOVE_LIST).replaceAll("[0-9]{1,4}[.]", "").replaceAll("[.]", "").replaceAll("  ", " ").substring(1).split(" "));
					boardView.getBoard().movesCount = 1;
				}
				boardView.getBoard().sec = 0;
				boardView.getBoard().left = Integer.parseInt(mainApp.getTactic().values.get(AppConstants.AVG_SECONDS));
				startTacticsTimer();
				c = MoveParser2.Parse(boardView.getBoard(), boardView.getBoard().getTacticMoves()[0]);
				if (moveFT.length == 4) {
					Move m;
					if (moveFT[3] == 2)
						m = new Move(moveFT[0], moveFT[1], 0, 2);
					else
						m = new Move(moveFT[0], moveFT[1], moveFT[2], moveFT[3]);
					boardView.getBoard().makeMove(m);
				} else {
					Move m = new Move(moveFT[0], moveFT[1], 0, 0);
					boardView.getBoard().makeMove(m);
				}
				Update(CALLBACK_REPAINT_UI);
				boardView.getBoard().takeBack();
				boardView.invalidate();

				//last move anim
				new Thread(new Runnable() {
					public void run() {
						try {
							Thread.sleep(1300);
							boardView.getBoard().takeNext();
							update.sendEmptyMessage(0);
						} catch (Exception e) {
						}
					}

					private Handler update = new Handler() {
						@Override
						public void dispatchMessage(Message msg) {
							super.dispatchMessage(msg);
							Update(CALLBACK_REPAINT_UI);
							boardView.invalidate();
						}
					};
				}).start();*/
				break;
			case CALLBACK_ECHESS_MOVE_WAS_SENT:
				// move was made
				if (mainApp.getSharedData().getInt(mainApp.getSharedData().getString(AppConstants.USERNAME, "")
						+ AppConstants.PREF_ACTION_AFTER_MY_MOVE, 0) == 2) {
					finish();
				} else if (mainApp.getSharedData().getInt(mainApp.getSharedData()
						.getString(AppConstants.USERNAME, "") + AppConstants.PREF_ACTION_AFTER_MY_MOVE, 0) == 0) {

					int i;
					ArrayList<GameListElement> currentGames = new ArrayList<GameListElement>();
					for (GameListElement gle : mainApp.getGameListItems()) {
						if (gle.type == 1 && gle.values.get("is_my_turn").equals("1")) {
							currentGames.add(gle);
						}
					}
					for (i = 0; i < currentGames.size(); i++) {
						if (currentGames.get(i).values.get(AppConstants.GAME_ID)
								.contains(mainApp.getCurrentGame().values.get(AppConstants.GAME_ID))) {
							if (i + 1 < currentGames.size()) {
								boardView.setBoard(new Board2(this));
								boardView.getBoard().analysis = false;
								boardView.getBoard().mode = AppConstants.GAME_MODE_LIVE_OR_ECHESS;

								if (progressDialog != null) {
									progressDialog.dismiss();
									progressDialog = null;
								}

								GetOnlineGame(currentGames.get(i + 1).values.get(AppConstants.GAME_ID));
								return;
							} else {
								finish();
								return;
							}
						}
					}
					finish();
					return;
				}
				break;
			case CALLBACK_GAME_REFRESH:
				if (boardView.getBoard().analysis)
					return;
				if (!mainApp.isLiveChess()) {
					game = ChessComApiParser.GetGameParseV3(responseRepeatable);
				}
				//System.out.println("!!!!!!!! mainApp.getCurrentGame() " + mainApp.getCurrentGame());
				//System.out.println("!!!!!!!! game " + game);

				if (mainApp.getCurrentGame() == null || game == null) {
					return;
				}

				if (!mainApp.getCurrentGame().equals(game)) {
					if (!mainApp.getCurrentGame().values.get("move_list").equals(game.values.get("move_list"))) {
						mainApp.setCurrentGame(game);
						String[] Moves = {};

						if (mainApp.getCurrentGame().values.get("move_list").contains("1.")
								|| ((mainApp.isLiveChess() && MainApp.isLiveOrEchessGameMode(boardView)))) {

							int beginIndex = (mainApp.isLiveChess() && MainApp.isLiveOrEchessGameMode(boardView)) ? 0 : 1;

							Moves = mainApp.getCurrentGame().values.get("move_list").replaceAll("[0-9]{1,4}[.]", "").replaceAll("  ", " ").substring(beginIndex).split(" ");

							if (Moves.length - boardView.getBoard().movesCount == 1) {
								if (mainApp.isLiveChess()) {
									moveFT = MoveParser2.parseCoordinate(boardView.getBoard(), Moves[Moves.length - 1]);
								} else {
									moveFT = MoveParser2.Parse(boardView.getBoard(), Moves[Moves.length - 1]);
								}
								boolean playSound = (mainApp.isLiveChess() && lccHolder.getGame(mainApp.getCurrentGame().values.get(AppConstants.GAME_ID)).getSeq() == Moves.length)
										|| !mainApp.isLiveChess();

								if (moveFT.length == 4) {
									Move m;
									if (moveFT[3] == 2)
										m = new Move(moveFT[0], moveFT[1], 0, 2);
									else
										m = new Move(moveFT[0], moveFT[1], moveFT[2], moveFT[3]);
									boardView.getBoard().makeMove(m, playSound);
								} else {
									Move m = new Move(moveFT[0], moveFT[1], 0, 0);
									boardView.getBoard().makeMove(m, playSound);
								}
								//mainApp.ShowMessage("Move list updated!");
								boardView.getBoard().movesCount = Moves.length;
								boardView.invalidate();
								Update(CALLBACK_REPAINT_UI);
							}
						}
						return;
					}
					if (game.values.get("has_new_message").equals("1")) {
						mainApp.setCurrentGame(game);
						if (!msgShowed) {
							msgShowed = true;
							new AlertDialog.Builder(coreContext)
									.setIcon(android.R.drawable.ic_dialog_alert)
									.setTitle(getString(R.string.you_got_new_msg))
									.setPositiveButton(R.string.browse, new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog, int whichButton) {
											chat = true;
											GetOnlineGame(mainApp.getGameId());
											msgShowed = false;
										}
									})
									.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog, int whichButton) {
										}
									}).create().show();
						}
						return;
					} else {
						msgShowed = false;
					}
				}
				break;

			case CALLBACK_GAME_STARTED:
				getSoundPlayer().playGameStart();

				if (mainApp.isLiveChess() && MainApp.isLiveOrEchessGameMode(boardView)) {
					mainApp.setCurrentGame(new com.chess.model.Game(lccHolder.getGameData(mainApp.getGameId(), -1), true));
					executePausedActivityGameEvents();
					//lccHolder.setActivityPausedMode(false);
					lccHolder.getWhiteClock().paint();
					lccHolder.getBlackClock().paint();
					/*int time = lccHolder.getGame(mainApp.getGameId()).getGameTimeConfig().getBaseTime() * 100;
							  lccHolder.setWhiteClock(new ChessClock(this, whiteClockView, time));
							  lccHolder.setBlackClock(new ChessClock(this, blackClockView, time));*/
				} else {
					mainApp.setCurrentGame(ChessComApiParser.GetGameParseV3(response));
				}

				if (chat) {
					if (!isUserColorWhite())
						mainApp.getSharedDataEditor().putString("opponent", mainApp.getCurrentGame()
								.values.get(AppConstants.WHITE_USERNAME));
					else
						mainApp.getSharedDataEditor().putString("opponent", mainApp.getCurrentGame()
								.values.get(AppConstants.BLACK_USERNAME));
					mainApp.getSharedDataEditor().commit();
					mainApp.getCurrentGame().values.put("has_new_message", "0");
					startActivity(new Intent(coreContext, mainApp.isLiveChess() ? ChatLive.class : Chat.class).
							putExtra(AppConstants.GAME_ID, mainApp.getCurrentGame().values.get(AppConstants.GAME_ID)).
							putExtra(AppConstants.TIMESTAMP, mainApp.getCurrentGame().values.get(AppConstants.TIMESTAMP)));
					chat = false;
					return;
				}

				if (mainApp.getCurrentGame().values.get("game_type").equals("2"))
					boardView.getBoard().chess960 = true;


				if (!isUserColorWhite()) {
					boardView.getBoard().setReside(true);
				}
				String[] Moves = {};


				if (mainApp.getCurrentGame().values.get("move_list").contains("1.")) {
					Moves = mainApp.getCurrentGame().values.get("move_list").replaceAll("[0-9]{1,4}[.]", "").replaceAll("  ", " ").substring(1).split(" ");
					boardView.getBoard().movesCount = Moves.length;
				} else if (!mainApp.isLiveChess()) {
					boardView.getBoard().movesCount = 0;
				}

				final com.chess.live.client.Game game = lccHolder.getGame(mainApp.getGameId());
				if (game != null && game.getSeq() > 0) {
					lccHolder.doReplayMoves(game);
				}

				String FEN = mainApp.getCurrentGame().values.get("starting_fen_position");
				if (!FEN.equals("")) {
					boardView.getBoard().GenCastlePos(FEN);
					MoveParser2.FenParse(FEN, boardView.getBoard());
				}

				int i;
				//System.out.println("@@@@@@@@ POINT 2 boardView.getBoard().movesCount=" + boardView.getBoard().movesCount);
				//System.out.println("@@@@@@@@ POINT 3 Moves=" + Moves);

				if (!mainApp.isLiveChess()) {
					for (i = 0; i < boardView.getBoard().movesCount; i++) {
						//System.out.println("@@@@@@@@ POINT 4 i=" + i);
						//System.out.println("================ POINT 5 Moves[i]=" + Moves[i]);
						moveFT = MoveParser2.Parse(boardView.getBoard(), Moves[i]);
						if (moveFT.length == 4) {
							Move m;
							if (moveFT[3] == 2) {
								m = new Move(moveFT[0], moveFT[1], 0, 2);
							} else {
								m = new Move(moveFT[0], moveFT[1], moveFT[2], moveFT[3]);
							}
							boardView.getBoard().makeMove(m, false);
						} else {
							Move m = new Move(moveFT[0], moveFT[1], 0, 0);
							boardView.getBoard().makeMove(m, false);
						}
					}
				}

				Update(CALLBACK_REPAINT_UI);
				boardView.getBoard().takeBack();
				boardView.invalidate();

				//last move anim
				new Thread(new Runnable() {
					public void run() {
						try {
							Thread.sleep(1300);
							boardView.getBoard().takeNext();
							update.sendEmptyMessage(0);
						} catch (Exception e) {
						}
					}

					private Handler update = new Handler() {
						@Override
						public void dispatchMessage(Message msg) {
							super.dispatchMessage(msg);
							Update(CALLBACK_REPAINT_UI);
							boardView.invalidate();
						}
					};
				}).start();

				if (MainApp.isLiveOrEchessGameMode(boardView) && appService != null && appService.getRepeatableTimer() == null) {
					if (progressDialog != null) {
						progressDialog.dismiss();
						progressDialog = null;
					}
					if (!mainApp.isLiveChess()) {
						appService.RunRepeatbleTask(CALLBACK_GAME_REFRESH, UPDATE_DELAY, UPDATE_DELAY,
								"http://www." + LccHolder.HOST + "/api/v3/get_game?id=" + mainApp.getSharedData().getString(AppConstants.USER_TOKEN, "") + "&gid=" + mainApp.getGameId(),
								null/*progressDialog*/
						);
					}
				}
				break;

			default:
				break;
		}
	}


	@Override
	public Object onRetainNonConfigurationInstance() {
		return boardView.getBoard();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		if (MainApp.isComputerGameMode(boardView)) {
			menuInflater.inflate(R.menu.game_comp, menu);
//			menu.add(0, MENU_COMPUTER_NEW_GAME, 0, getString(R.string.newgame)).setIcon(R.drawable.newgame);
////			SubMenu options = menu.addSubMenu(0, MENU_COMPUTER_OPTIONS, 0, getString(R.string.options))
////					.setIcon(R.drawable.options);
//			menu.add(0, MENU_COMPUTER_RESIDE, 0, getString(R.string.reside)).setIcon(R.drawable.reside);
//			menu.add(0, MENU_COMPUTER_HINT, 0, getString(R.string.hint)).setIcon(R.drawable.hint);
//			menu.add(0, MENU_COMPUTER_PREVIOUS, 0, getString(R.string.prev)).setIcon(R.drawable.prev);
//			menu.add(0, MENU_COMPUTER_NEXT, 0, getString(R.string.next)).setIcon(R.drawable.next);

//			options.add(0, MENU_COMPUTER_NEW_GAME_WHITE, 0, getString(R.string.ngwhite));
//			options.add(0, MENU_COMPUTER_NEW_GAME_BLACK, 0, getString(R.string.ngblack));
//			options.add(0, MENU_COMPUTER_EMAIL_GAME, 0, getString(R.string.emailgame));
//			options.add(0, MENU_COMPUTER_SETTINGS, 0, getString(R.string.settings));

		}

		return super.onCreateOptionsMenu(menu);
	}


	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
//		if (mainApp.getCurrentGame() != null && (MainApp.isLiveOrEchessGameMode(boardView)
//				|| MainApp.isFinishedEchessGameMode(boardView))) {
//			int itemPosition = mainApp.isLiveChess() ? 1 : 3;
//			if (mainApp.getCurrentGame().values.get("has_new_message").equals("1"))
//				menu.getItem(itemPosition).setIcon(R.drawable.chat_nm);
//			else
//				menu.getItem(itemPosition).setIcon(R.drawable.chat);
//		}
//

		return super.onPrepareOptionsMenu(menu);
	}


//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		switch (item.getItemId()) {
//			case android.R.id.home:
//				onBackPressed();
////			Toast.makeText(this, "Tapped home", Toast.LENGTH_SHORT).show();
//				break;
//
//			case R.id.menu_refresh:
//				break;
//
//			case R.id.menu_preferences:
//				// TODO show popup list
//				final CharSequence[] items = {"Red", "Green", "Blue"};
//
//				new AlertDialog.Builder(this)
//						.setTitle("Pick a color")
//						.setItems(items, new DialogInterface.OnClickListener() {
//							public void onClick(DialogInterface dialog, int item) {
//								Toast.makeText(getApplicationContext(), items[item], Toast.LENGTH_SHORT).show();
//							}
//						}).show();
////				AlertDialog alert = builder.create();
////				alert.show();
//				break;
//		}
//		return super.onOptionsItemSelected(item);
//	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (MainApp.isComputerGameMode(boardView)) {
			switch (item.getItemId()) {
				case MENU_COMPUTER_NEW_GAME:
					boardView.stopThinking = true;
					finish();
					return true;
				case MENU_COMPUTER_OPTIONS:
					boardView.stopThinking = true;
					return true;
				case MENU_COMPUTER_RESIDE:
					boardView.stopThinking = true;
					if (!boardView.compmoving) {
						boardView.getBoard().setReside(!boardView.getBoard().reside);
						if (MainApp.isComputerVsHumanGameMode(boardView)) {
							if (MainApp.isComputerVsHumanWhiteGameMode(boardView)) {
								boardView.getBoard().mode = AppConstants.GAME_MODE_COMPUTER_VS_HUMAN_BLACK;
							} else if (MainApp.isComputerVsHumanBlackGameMode(boardView)) {
								boardView.getBoard().mode = AppConstants.GAME_MODE_COMPUTER_VS_HUMAN_WHITE;
							}
							//boardView.getBoard().mode ^= 1;
							boardView.ComputerMove(mainApp.strength[mainApp.getSharedData()
									.getInt(mainApp.getSharedData().getString(AppConstants.USERNAME, "")
											+ AppConstants.PREF_COMPUTER_STRENGTH, 0)]);
						}
						boardView.invalidate();
						Update(CALLBACK_REPAINT_UI);
					}
					return true;
				case MENU_COMPUTER_HINT:
					boardView.stopThinking = true;
					if (!boardView.compmoving) {
						boardView.hint = true;
						boardView.ComputerMove(mainApp.strength[mainApp.getSharedData()
								.getInt(mainApp.getSharedData().getString(AppConstants.USERNAME, "")
										+ AppConstants.PREF_COMPUTER_STRENGTH, 0)]);
					}
					return true;
				case MENU_COMPUTER_PREVIOUS:
					boardView.stopThinking = true;
					if (!boardView.compmoving) {
						boardView.finished = false;
						boardView.sel = false;
						boardView.getBoard().takeBack();
						boardView.invalidate();
						Update(CALLBACK_REPAINT_UI);
						isMoveNav = true;
					}
					return true;
				case MENU_COMPUTER_NEXT:
					boardView.stopThinking = true;
					if (!boardView.compmoving) {
						boardView.sel = false;
						boardView.getBoard().takeNext();
						boardView.invalidate();
						Update(CALLBACK_REPAINT_UI);
						isMoveNav = true;
					}
					return true;
				case MENU_COMPUTER_NEW_GAME_WHITE: {
					boardView.setBoard(new Board2(this));
					boardView.getBoard().mode = AppConstants.GAME_MODE_COMPUTER_VS_HUMAN_WHITE;
					boardView.getBoard().GenCastlePos("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR");
					boardView.invalidate();
					Update(CALLBACK_REPAINT_UI);
					return true;
				}
				case MENU_COMPUTER_NEW_GAME_BLACK: {
					boardView.setBoard(new Board2(this));
					boardView.getBoard().mode = AppConstants.GAME_MODE_COMPUTER_VS_HUMAN_BLACK;
					boardView.getBoard().setReside(true);
					boardView.getBoard().GenCastlePos("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR");
					boardView.invalidate();
					Update(CALLBACK_REPAINT_UI);
					boardView.ComputerMove(mainApp.strength[mainApp.getSharedData().getInt(mainApp.getSharedData().getString(AppConstants.USERNAME, "") + AppConstants.PREF_COMPUTER_STRENGTH, 0)]);
					return true;
				}
				case MENU_COMPUTER_EMAIL_GAME: {
					String moves = movelist.getText().toString();
					Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
					emailIntent.setType("plain/text");
					emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Chess Game on Android - Chess.com");
					emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "[Site \"Chess.com Android\"]\n [White \"" + mainApp.getSharedData().getString(AppConstants.USERNAME, "") + "\"]\n [White \"" + mainApp.getSharedData().getString(AppConstants.USERNAME, "") + "\"]\n [Result \"X-X\"]\n \n \n " + moves + " \n \n Sent from my Android");
					startActivity(Intent.createChooser(emailIntent, "Send mail..."));
					return true;
				}
				case MENU_COMPUTER_SETTINGS: {
					startActivity(new Intent(coreContext, Preferences.class));
					return true;
				}
			}

		}
		return false;
	}

	@Override
	public void onOptionsMenuClosed(Menu menu) {
		if (isMoveNav) {
			new Handler().postDelayed(new Runnable() {
				public void run() {
					openOptionsMenu();
				}
			}, 10);
			isMoveNav = false;
		}
		super.onOptionsMenuClosed(menu);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		boardView.requestFocus();
		super.onWindowFocusChanged(hasFocus);
	}

	@Override
	protected void onResume() {
		if (MobclixHelper.isShowAds(mainApp) && mainApp.getTabHost() != null && !mainApp.getTabHost().getCurrentTabTag().equals("tab4") && adViewWrapper != null && getRectangleAdview() != null) {
			adViewWrapper.addView(getRectangleAdview());
			if (mainApp.isForceRectangleAd()) {
				getRectangleAdview().getAd();
			}
		}

		if (/*!mainApp.isNetworkChangedNotification() && */extras.containsKey(AppConstants.LIVE_CHESS)) {
			mainApp.setLiveChess(extras.getBoolean(AppConstants.LIVE_CHESS));
			if (!mainApp.isLiveChess()) {
				new AsyncTask<Void, Void, Void>() {
					@Override
					protected Void doInBackground(Void... voids) {
						mainApp.getLccHolder().logout();
						return null;
					}
				}.execute();
			}
		}

		super.onResume();

		registerReceiver(gameMoveReceiver, new IntentFilter(IntentConstants.ACTION_GAME_MOVE));
		registerReceiver(gameEndMessageReceiver, new IntentFilter(IntentConstants.ACTION_GAME_END));
		registerReceiver(gameInfoMessageReceived, new IntentFilter(IntentConstants.ACTION_GAME_INFO));
		registerReceiver(chatMessageReceiver, new IntentFilter(IntentConstants.ACTION_GAME_CHAT_MSG));
		registerReceiver(showGameEndPopupReceiver, new IntentFilter(IntentConstants.ACTION_SHOW_GAME_END_POPUP));

		/*if (MainApp.isTacticsGameMode(boardView)) {
			if (boardView.getBoard().isTacticCanceled()) {
				boardView.getBoard().setTacticCanceled(false);
				showDialog(DIALOG_TACTICS_START_TACTICS);
				startTacticsTimer();
			} else if (mainApp.getTactic() != null && mainApp.getTactic().values.get(AppConstants.STOP).equals("0")) {
				startTacticsTimer();
			}
		}*/
		/*if (mainApp.isLiveChess() && mainApp.getGameId() != null && mainApp.getGameId() != ""
				&& lccHolder.getGame(mainApp.getGameId()) != null) {
			game = new com.chess.model.Game(lccHolder.getGameData(mainApp.getGameId(),
					lccHolder.getGame(mainApp.getGameId()).getSeq() - 1), true);
//			lccHolder.getAndroid().setGameActivity(this); // TODO
			if (lccHolder.isActivityPausedMode()) {
				executePausedActivityGameEvents();
				lccHolder.setActivityPausedMode(false);
			}
			//lccHolder.updateClockTime(lccHolder.getGame(mainApp.getGameId()));
		}*/

		/*MobclixAdView bannerAdview = mainApp.getBannerAdview();
	 LinearLayout bannerAdviewWrapper = mainApp.getBannerAdviewWrapper();
	 if (bannerAdviewWrapper != null)
	 {
		 bannerAdviewWrapper.removeView(bannerAdview);
	 }*/
		MobclixHelper.pauseAdview(mainApp.getBannerAdview(), mainApp);
		/*mainApp.setBannerAdview(null);
	 mainApp.setBannerAdviewWrapper(null);*/
		//mainApp.setForceBannerAdOnFailedLoad(true);

		disableScreenLock();
	}

	@Override
	protected void onPause() {
		System.out.println("LCCLOG2: GAME ONPAUSE");
		unregisterReceiver(gameMoveReceiver);
		unregisterReceiver(gameEndMessageReceiver);
		unregisterReceiver(gameInfoMessageReceived);
		unregisterReceiver(chatMessageReceiver);
		unregisterReceiver(showGameEndPopupReceiver);

		super.onPause();
		System.out.println("LCCLOG2: GAME ONPAUSE adViewWrapper="
				+ adViewWrapper + ", getRectangleAdview() " + getRectangleAdview());
		if (adViewWrapper != null && getRectangleAdview() != null) {
			System.out.println("LCCLOG2: GAME ONPAUSE 1");
			getRectangleAdview().cancelAd();
			System.out.println("LCCLOG2: GAME ONPAUSE 2");
			adViewWrapper.removeView(getRectangleAdview());
			System.out.println("LCCLOG2: GAME ONPAUSE 3");
		}
		lccHolder.setActivityPausedMode(true);
		lccHolder.getPausedActivityGameEvents().clear();

		boardView.stopThinking = true;

//		stopTacticsTimer();
		if (onlineGameUpdate != null)
			onlineGameUpdate.cancel();

		/*if (MobclixHelper.isShowAds(mainApp))
		{
			MobclixHelper.pauseAdview(getRectangleAdview(), mainApp);
		}*/

		enableScreenLock();
	}

//	public void stopTacticsTimer() {
//		if (tacticsTimer != null) {
//			tacticsTimer.cancel();
//			tacticsTimer = null;
//		}
//	}

//	public void startTacticsTimer() {
//		stopTacticsTimer();
//		boardView.finished = false;
//		if (mainApp.getTactic() != null) {
//			mainApp.getTactic().values.put(AppConstants.STOP, "0");
//		}
//		tacticsTimer = new Timer();
//		tacticsTimer.scheduleAtFixedRate(new TimerTask() {
//			@Override
//			public void run() {
//				if (boardView.getBoard().analysis)
//					return;
//				boardView.getBoard().sec++;
//				if (boardView.getBoard().left > 0)
//					boardView.getBoard().left--;
//				update.sendEmptyMessage(0);
//			}
//
//			private Handler update = new Handler() {
//				@Override
//				public void dispatchMessage(Message msg) {
//					super.dispatchMessage(msg);
//					timer.setText(getString(R.string.bonus_time_left, boardView.getBoard().left
//							, boardView.getBoard().sec));
//				}
//			};
//		}, 0, 1000);
//	}

	private BroadcastReceiver gameMoveReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			LccHolder.LOG.info("LCCLOG ANDROID: receive broadcast intent, action=" + intent.getAction());
			game = (com.chess.model.Game) intent.getSerializableExtra(AppConstants.OBJECT);
			Update(CALLBACK_GAME_REFRESH);
		}
	};

	private BroadcastReceiver gameEndMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, final Intent intent) {
			LccHolder.LOG.info("LCCLOG ANDROID: receive broadcast intent, action=" + intent.getAction());

			final com.chess.live.client.Game game = lccHolder.getGame(mainApp.getGameId());
			Integer newWhiteRating = null;
			Integer newBlackRating = null;
			switch (game.getGameTimeConfig().getGameTimeClass()) {
				case BLITZ: {
					newWhiteRating = game.getWhitePlayer().getBlitzRating();
					newBlackRating = game.getBlackPlayer().getBlitzRating();
					break;
				}
				case LIGHTNING: {
					newWhiteRating = game.getWhitePlayer().getQuickRating();
					newBlackRating = game.getBlackPlayer().getQuickRating();
					break;
				}
				case STANDARD: {
					newWhiteRating = game.getWhitePlayer().getStandardRating();
					newBlackRating = game.getBlackPlayer().getStandardRating();
					break;
				}
			}
			/*final String whiteRating =
					(newWhiteRating != null && newWhiteRating != 0) ?
					newWhiteRating.toString() : mainApp.getCurrentGame().values.get("white_rating");
				  final String blackRating =
					(newBlackRating != null && newBlackRating != 0) ?
					newBlackRating.toString() : mainApp.getCurrentGame().values.get("black_rating");*/
			white.setText(game.getWhitePlayer().getUsername() + "(" + newWhiteRating + ")");
			black.setText(game.getBlackPlayer().getUsername() + "(" + newBlackRating + ")");
			boardView.finished = true;

			if (MobclixHelper.isShowAds(mainApp)) {
				final LayoutInflater inflater = (LayoutInflater) coreContext.getSystemService(LAYOUT_INFLATER_SERVICE);
				final View layout = inflater.inflate(R.layout.ad_popup,
						(ViewGroup) findViewById(R.id.layout_root));
				showGameEndPopup(layout, intent.getExtras().getString(AppConstants.TITLE) + ": " + intent.getExtras().getString(AppConstants.MESSAGE));

				final View newGame = layout.findViewById(R.id.newGame);
				newGame.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (adPopup != null) {
							try {
								adPopup.dismiss();
							} catch (Exception e) {
							}
							adPopup = null;
						}
						startActivity(new Intent(coreContext, OnlineNewGame.class));
					}
				});
				newGame.setVisibility(View.VISIBLE);

				final View home = layout.findViewById(R.id.home);
				home.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (adPopup != null) {
							try {
								adPopup.dismiss();
							} catch (Exception e) {
							}
							adPopup = null;
						}
						startActivity(new Intent(coreContext, Tabs.class));
					}
				});
				home.setVisibility(View.VISIBLE);
			}

			endOfGameMessage.setText(/*intent.getExtras().getString(AppConstants.TITLE) + ": " +*/ intent.getExtras().getString(AppConstants.MESSAGE));
			//mainApp.ShowDialog(Game.this, intent.getExtras().getString(AppConstants.TITLE), intent.getExtras().getString(AppConstants.MESSAGE));
			findViewById(R.id.moveButtons).setVisibility(View.GONE);
			findViewById(R.id.endOfGameButtons).setVisibility(View.VISIBLE);
			chatPanel.setVisibility(View.GONE);
			findViewById(R.id.newGame).setOnClickListener(GameCompScreenActivity.this);
			findViewById(R.id.home).setOnClickListener(GameCompScreenActivity.this);
			getSoundPlayer().playGameEnd();
		}
	};

	private BroadcastReceiver gameInfoMessageReceived = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			LccHolder.LOG.info("LCCLOG ANDROID: receive broadcast intent, action=" + intent.getAction());
			mainApp.ShowDialog(coreContext, intent.getExtras()
					.getString(AppConstants.TITLE), intent.getExtras().getString(AppConstants.MESSAGE));
		}
	};

	public TextView getWhiteClockView() {
		return whiteClockView;
	}

	public TextView getBlackClockView() {
		return blackClockView;
	}

	private void executePausedActivityGameEvents() {
		if (/*lccHolder.isActivityPausedMode() && */lccHolder.getPausedActivityGameEvents().size() > 0) {
			//boolean fullGameProcessed = false;
			GameEvent gameEvent = lccHolder.getPausedActivityGameEvents().get(GameEvent.Event.Move);
			if (gameEvent != null &&
					(lccHolder.getCurrentGameId() == null
							|| lccHolder.getCurrentGameId().equals(gameEvent.getGameId()))) {
				//lccHolder.processFullGame(lccHolder.getGame(gameEvent.getGameId().toString()));
				//fullGameProcessed = true;
				lccHolder.getPausedActivityGameEvents().remove(gameEvent);
				//lccHolder.getAndroid().processMove(gameEvent.getGameId(), gameEvent.moveIndex);
				game = new com.chess.model.Game(lccHolder.getGameData(
						gameEvent.getGameId().toString(), gameEvent.getMoveIndex()), true);
				Update(CALLBACK_GAME_REFRESH);
			}

			gameEvent = lccHolder.getPausedActivityGameEvents().get(GameEvent.Event.DrawOffer);
			if (gameEvent != null &&
					(lccHolder.getCurrentGameId() == null
							|| lccHolder.getCurrentGameId().equals(gameEvent.getGameId()))) {
				/*if (!fullGameProcessed)
						{
						  lccHolder.processFullGame(lccHolder.getGame(gameEvent.getGameId().toString()));
						  fullGameProcessed = true;
						}*/
				lccHolder.getPausedActivityGameEvents().remove(gameEvent);
				lccHolder.getAndroid().processDrawOffered(gameEvent.getDrawOffererUsername());
			}

			gameEvent = lccHolder.getPausedActivityGameEvents().get(GameEvent.Event.EndOfGame);
			if (gameEvent != null &&
					(lccHolder.getCurrentGameId() == null || lccHolder.getCurrentGameId().equals(gameEvent.getGameId()))) {
				/*if (!fullGameProcessed)
						{
						  lccHolder.processFullGame(lccHolder.getGame(gameEvent.getGameId().toString()));
						  fullGameProcessed = true;
						}*/
				lccHolder.getPausedActivityGameEvents().remove(gameEvent);
				lccHolder.getAndroid().processGameEnd(gameEvent.getGameEndedMessage());
			}
		}
	}

	/*public void onStop()
	  {
		mainApp.getCurrentGame() = null;
		boardView.board = null;
		super.onStop();
	  }*/

	private void showAnalysisButtons() {
		analysisButtons.setVisibility(View.VISIBLE);
		findViewById(R.id.moveButtons).setVisibility(View.GONE);
		/*boardView.getBoard().takeBack();
			boardView.getBoard().movesCount--;
			boardView.invalidate();
			boardView.getBoard().submit = false;*/
	}

	private void hideAnalysisButtons() {
		analysisButtons.setVisibility(View.GONE);
	}

	private BroadcastReceiver chatMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			//LccHolder.LOG.info("ANDROID: receive broadcast intent, action=" + intent.getAction());
			chatPanel.setVisibility(View.VISIBLE);
		}
	};

	private BroadcastReceiver showGameEndPopupReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, final Intent intent) {
			if (!MobclixHelper.isShowAds(mainApp)) {
				return;
			}

			final LayoutInflater inflater = (LayoutInflater) coreContext.getSystemService(LAYOUT_INFLATER_SERVICE);
			final View layout = inflater.inflate(R.layout.ad_popup, (ViewGroup) findViewById(R.id.layout_root));
			showGameEndPopup(layout, intent.getExtras().getString(AppConstants.MESSAGE));

			final Button ok = (Button) layout.findViewById(R.id.home);
			ok.setText(getString(R.string.okay));
			ok.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (adPopup != null) {
						try {
							adPopup.dismiss();
						} catch (Exception e) {
						}
						adPopup = null;
					}
					if (intent.getBooleanExtra(AppConstants.FINISHABLE, false)) {
						finish();
					}
				}
			});
			ok.setVisibility(View.VISIBLE);
		}
	};

	public void showGameEndPopup(final View layout, final String message) {
		if (!MobclixHelper.isShowAds(mainApp)) {
			return;
		}

		if (adPopup != null) {
			try {
				adPopup.dismiss();
			} catch (Exception e) {
				System.out.println("MOBCLIX: EXCEPTION IN showGameEndPopup");
				e.printStackTrace();
			}
			adPopup = null;
		}

		try {
			if (adViewWrapper != null && getRectangleAdview() != null) {
				adViewWrapper.removeView(getRectangleAdview());
			}
			adViewWrapper = (LinearLayout) layout.findViewById(R.id.adview_wrapper);
			System.out.println("MOBCLIX: GET WRAPPER " + adViewWrapper);
			adViewWrapper.addView(getRectangleAdview());

			adViewWrapper.setVisibility(View.VISIBLE);
			//showGameEndAds(adViewWrapper);

			TextView endOfGameMessagePopup = (TextView) layout.findViewById(R.id.endOfGameMessage);
			endOfGameMessagePopup.setText(message);

			adPopup.setOnCancelListener(new DialogInterface.OnCancelListener() {
				public void onCancel(DialogInterface dialogInterface) {
					if (adViewWrapper != null && getRectangleAdview() != null) {
						adViewWrapper.removeView(getRectangleAdview());
					}
				}
			});
			adPopup.setOnDismissListener(new DialogInterface.OnDismissListener() {
				public void onDismiss(DialogInterface dialogInterface) {
					if (adViewWrapper != null && getRectangleAdview() != null) {
						adViewWrapper.removeView(getRectangleAdview());
					}
				}
			});
		} catch (Exception e) {
			System.out.println("MOBCLIX: EXCEPTION IN showGameEndPopup");
			e.printStackTrace();
		}

		new Handler().postDelayed(new Runnable() {
			public void run() {
				AlertDialog.Builder builder;
				//Context mContext = getApplicationContext();
				builder = new AlertDialog.Builder(coreContext);
				builder.setView(layout);
				adPopup = builder.create();
				adPopup.setCancelable(true);
				adPopup.setCanceledOnTouchOutside(true);
				try {
					adPopup.show();
				} catch (Exception e) {
					return;
				}
			}
		}, 1500);
	}
}