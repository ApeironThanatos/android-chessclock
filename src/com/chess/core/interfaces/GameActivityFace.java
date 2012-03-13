package com.chess.core.interfaces;

import android.content.Context;
import com.chess.core.MainApp;

/**
 * GameActivityFace class
 *
 * @author alien_roger
 * @created at: 13.03.12 7:08
 */
public interface GameActivityFace {
	void update(int code);
	void showOptions();
	Context getMeContext();

	MainApp getMainApp();

	void showSubmitButtonsLay(boolean show);

	void showChoosePieceDialog(final int col,final int row);

	void switch2Chat();

	void newGame();
}
