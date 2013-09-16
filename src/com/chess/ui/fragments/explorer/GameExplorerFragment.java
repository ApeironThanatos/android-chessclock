package com.chess.ui.fragments.explorer;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.chess.R;
import com.chess.backend.LoadHelper;
import com.chess.backend.LoadItem;
import com.chess.backend.entity.api.ExplorerMovesItem;
import com.chess.backend.tasks.RequestJsonTask;
import com.chess.db.DbDataManager;
import com.chess.db.DbHelper;
import com.chess.db.DbScheme;
import com.chess.db.tasks.LoadDataFromDbTask;
import com.chess.db.tasks.SaveExplorerMovesListTask;
import com.chess.ui.adapters.ExplorerMovesCursorAdapter;
import com.chess.ui.engine.ChessBoardExplorer;
import com.chess.ui.engine.Move;
import com.chess.ui.fragments.game.GameBaseFragment;
import com.chess.ui.interfaces.ItemClickListenerFace;
import com.chess.ui.interfaces.boards.BoardFace;
import com.chess.ui.interfaces.game_ui.GameFace;
import com.chess.ui.views.chess_boards.ChessBoardExplorerView;
import com.chess.utilities.AppUtils;

/**
 * Created with IntelliJ IDEA.
 * User: roger sent2roger@gmail.com
 * Date: 03.09.13
 * Time: 6:42
 */
public class GameExplorerFragment extends GameBaseFragment implements GameFace, ItemClickListenerFace, AdapterView.OnItemClickListener {

	private ExplorerMovesUpdateListener explorerMovesUpdateListener;
	private SaveExplorerMovesUpdateListener saveExplorerMovesUpdateListener;
	private ExplorerMovesCursorUpdateListener explorerMovesCursorUpdateListener;
	private ExplorerMovesCursorAdapter explorerMovesCursorAdapter;

	private ChessBoardExplorerView boardView;
	private String fen;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		init();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.new_game_explorer_frame, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		setTitle(R.string.game_explorer);

		widgetsInit(view);
	}

	@Override
	public void onResume() {
		super.onResume();

		if (need2update) {
			adjustBoardForGame();

			fen = getBoardFace().generateFullFen();
			boolean haveSavedData = DbDataManager.haveSavedExplorerMoves(getActivity(), fen);

			if (haveSavedData) {
				loadFromDb();
			} else if (AppUtils.isNetworkAvailable(getActivity())) {
				updateData(fen);
			} /*else { // TODO check logic if we need this case
				emptyView.setText(R.string.no_network);
				showEmptyView(true);
			}*/
		}
	}

	private void adjustBoardForGame() {
		ChessBoardExplorer.resetInstance();
		ChessBoardExplorer.getInstance(this);
	}

	private void updateData(String fen) {
		LoadItem loadItem = LoadHelper.getExplorerMoves(getUserToken(), fen);
		new RequestJsonTask<ExplorerMovesItem>(explorerMovesUpdateListener).executeTask(loadItem);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
		Cursor cursor = (Cursor) parent.getItemAtPosition(position);
		String moveStr = DbDataManager.getString(cursor, DbScheme.V_MOVE);

		final BoardFace boardFace = getBoardFace();

		{
			// get next valid move
			final Move move = boardFace.convertMoveAlgebraic(moveStr);
			logTest(" new moveStr = " + moveStr + " new move = " + move.toString());
			boardFace.setMovesCount(boardFace.getMovesCount());

			// play move animation
			boardView.setMoveAnimator(move, true);
			boardView.resetValidMoves();
			// make actual move
			boardFace.makeMove(move, true);
			invalidateGameScreen();
		}

		// restore move back
//		handler.postDelayed(new Runnable() {
//			@Override
//			public void run() {
//				boardView.setMoveAnimator(getBoardFace().getLastMove(), false);
//				boardView.resetValidMoves();
//				getBoardFace().takeBack();
//				invalidateGameScreen();
//			}
//		}, 1000);

		// update FEN and get next moves
//		fen = getBoardFace().generateFullFen();
		fen = getBoardFace().generateBaseFen();
		updateData(fen);
	}

	private class ExplorerMovesUpdateListener extends ChessLoadUpdateListener<ExplorerMovesItem> {

		public ExplorerMovesUpdateListener() {
			super(ExplorerMovesItem.class);
		}

		@Override
		public void updateData(ExplorerMovesItem returnedObj) {
			super.updateData(returnedObj);

			new SaveExplorerMovesListTask(saveExplorerMovesUpdateListener, returnedObj.getData().getMoves(),
					getContentResolver(), fen).executeTask();
		}
	}

	private class SaveExplorerMovesUpdateListener extends ChessUpdateListener<ExplorerMovesItem.Move> {

		@Override
		public void updateData(ExplorerMovesItem.Move returnedObj) {
			super.updateData(returnedObj);

			loadFromDb();
		}
	}

	private void loadFromDb() {
		new LoadDataFromDbTask(explorerMovesCursorUpdateListener,
				DbHelper.getExplorerMovesForFen(fen),
				getContentResolver()).executeTask();
	}

	private class ExplorerMovesCursorUpdateListener extends ChessUpdateListener<Cursor> {

		@Override
		public void updateData(Cursor returnedObj) {
			super.updateData(returnedObj);

			explorerMovesCursorAdapter.changeCursor(returnedObj);
		}
	}

	@Override
	public Boolean isUserColorWhite() {
		return true;
	}

	@Override
	public Long getGameId() {
		return 0L;
	}

	@Override
	public void showOptions(View view) {

	}

	@Override
	public void newGame() {

	}

	@Override
	public void switch2Analysis() {

	}

	@Override
	public void updateAfterMove() {

	}

	@Override
	public void invalidateGameScreen() {
		boardView.invalidate();
	}

	@Override
	public String getWhitePlayerName() {
		return getUsername();
	}

	@Override
	public String getBlackPlayerName() {
		return "Comp";
	}

	@Override
	public boolean currentGameExist() {
		return true;
	}

	@Override
	public ChessBoardExplorer getBoardFace() {
		return ChessBoardExplorer.getInstance(this);
	}

	@Override
	public void toggleSides() {

	}

	@Override
	protected void restoreGame() {

	}

	private void init() {
		explorerMovesUpdateListener = new ExplorerMovesUpdateListener();
		saveExplorerMovesUpdateListener = new SaveExplorerMovesUpdateListener();
		explorerMovesCursorUpdateListener = new ExplorerMovesCursorUpdateListener();
		explorerMovesCursorAdapter = new ExplorerMovesCursorAdapter(getActivity(), null);
	}

	private void widgetsInit(View view) {
		if (AppUtils.isNexus4Kind(getActivity())) {
			view.findViewById(R.id.moveVariationTxt).setVisibility(View.GONE);
		}

		ListView listView = (ListView) view.findViewById(R.id.listView);
		listView.setAdapter(explorerMovesCursorAdapter);
		listView.setOnItemClickListener(this);

		boardView = (ChessBoardExplorerView) view.findViewById(R.id.boardview);
		boardView.setFocusable(true);
		boardView.setGameUiFace(this);

		setBoardView(boardView);
	}
}