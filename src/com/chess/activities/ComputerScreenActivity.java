package com.chess.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RadioButton;
import android.widget.Spinner;
import com.chess.R;
import com.chess.core.AppConstants;
import com.chess.core.CoreActivityActionBar;
import com.chess.views.BackgroundChessDrawable;
import com.flurry.android.FlurryAgent;

/**
 * ComputerScreenActivity class
 *
 * @author alien_roger
 * @created at: 08.02.12 7:21
 */
public class ComputerScreenActivity extends CoreActivityActionBar implements View.OnClickListener {

	private Spinner strength;
	private LogoutTask logoutTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.computer);
		findViewById(R.id.mainView).setBackgroundDrawable(new BackgroundChessDrawable(this));

		logoutTask = new LogoutTask();

		strength = (Spinner) findViewById(R.id.PrefStrength);
		strength.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> a, View v, int pos, long id) {
				try {
					if (mainApp.getSharedDataEditor() != null && mainApp.getSharedData() != null && pos >= 0) {
						mainApp.getSharedDataEditor().putInt(mainApp.getSharedData().getString(AppConstants.USERNAME, "") + AppConstants.PREF_COMPUTER_STRENGTH, pos);
						mainApp.getSharedDataEditor().commit();
					}
				} catch (Exception e) {
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> a) {
			}
		});

		findViewById(R.id.start).setOnClickListener(this);
	}

	@Override
	public void onClick(View view) { // make code more clear
		if(view.getId() == R.id.load){
			FlurryAgent.onEvent("New Game VS Computer", null);
			startActivity(new Intent(coreContext, Game.class).putExtra(AppConstants.GAME_MODE, Integer.parseInt(mainApp.getSharedData().getString(AppConstants.SAVED_COMPUTER_GAME, "").substring(0, 1))));
		}else if(view.getId() == R.id.start){
			RadioButton wh, bh;
			wh = (RadioButton) findViewById(R.id.wHuman);
			bh = (RadioButton) findViewById(R.id.bHuman);

			int mode = 0;
			if (!wh.isChecked() && bh.isChecked())
				mode = 1;
			else if (wh.isChecked() && bh.isChecked())
				mode = 2;
			else if (!wh.isChecked() && !bh.isChecked())
				mode = 3;

			mainApp.getSharedDataEditor().putString(AppConstants.SAVED_COMPUTER_GAME, "");
			mainApp.getSharedDataEditor().commit();

			FlurryAgent.onEvent("New Game VS Computer", null);
			startActivity(new Intent(this, Game.class).putExtra(AppConstants.GAME_MODE, mode));
		}
	}

	private class LogoutTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... voids) {
			mainApp.getLccHolder().logout();
			return null;
		}
	}

	@Override
	protected void onResume() {
		if (mainApp.isLiveChess()) {
			mainApp.setLiveChess(false);
			logoutTask.execute();  // do not create new instance every time
		}
		super.onResume();

		if (strength != null && mainApp != null && mainApp.getSharedData() != null) {
			strength.post(new Runnable() {
				public void run() {
					strength.setSelection(mainApp.getSharedData().getInt(mainApp.getSharedData().getString(AppConstants.USERNAME, "") + AppConstants.PREF_COMPUTER_STRENGTH, 0));
				}
			});
			if (!mainApp.getSharedData().getString(AppConstants.SAVED_COMPUTER_GAME, "").equals("")) {
				findViewById(R.id.load).setVisibility(View.VISIBLE);
				findViewById(R.id.load).setOnClickListener(this);
			} else {
				findViewById(R.id.load).setVisibility(View.GONE);
			}
		}
	}



//	@Override
//	public void LoadPrev(int code) {
//		//finish();
//		mainApp.getTabHost().setCurrentTab(0);
//	}

	@Override
	public void Update(int code) {
	}
}