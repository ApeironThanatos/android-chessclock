package com.chess.ui.fragments.settings;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.chess.R;
import com.chess.ui.adapters.ItemsAdapter;
import com.chess.ui.fragments.CommonLogicFragment;
import com.chess.ui.fragments.home.HomeTabsFragment;
import com.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: roger sent2roger@gmail.com
 * Date: 15.04.13
 * Time: 20:43
 */
public class NewSettingsGridFragment extends CommonLogicFragment implements AdapterView.OnItemClickListener {

	private GridView gridView;
	private List<SettingsMenuItem> menuItems;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		menuItems = new ArrayList<SettingsMenuItem>();
		menuItems.add(new SettingsMenuItem(R.string.profile, R.string.glyph_profile));
		menuItems.add(new SettingsMenuItem(R.string.board_and_pieces, R.string.glyph_board));
		menuItems.add(new SettingsMenuItem(R.string.daily_chess, R.string.glyph_daily_game));
		menuItems.add(new SettingsMenuItem(R.string.live_chess, R.string.glyph_live_standard));
		menuItems.add(new SettingsMenuItem(R.string.tactics, R.string.glyph_tactics_game));
		menuItems.add(new SettingsMenuItem(R.string.lessons, R.string.glyph_lessons));
		menuItems.add(new SettingsMenuItem(R.string.theme, R.string.glyph_theme));
		menuItems.add(new SettingsMenuItem(R.string.profile, R.string.glyph_info));
		menuItems.add(new SettingsMenuItem(R.string.privacy, R.string.glyph_settings));
		menuItems.add(new SettingsMenuItem(R.string.blocking, R.string.glyph_blocking));
		menuItems.add(new SettingsMenuItem(R.string.tracking, R.string.glyph_tracking));
		menuItems.add(new SettingsMenuItem(R.string.sharing, R.string.glyph_share));
		menuItems.add(new SettingsMenuItem(R.string.alerts_and_emails, R.string.glyph_email_dark));
		menuItems.add(new SettingsMenuItem(R.string.password, R.string.glyph_password));
		menuItems.add(new SettingsMenuItem(R.string.account_history, R.string.glyph_history));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.new_settings_grid_frame, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		updateTitle(R.string.settings);

		gridView = (GridView) view.findViewById(R.id.gridView);
		gridView.setOnItemClickListener(this);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		NewNavigationMenuAdapter adapter = new NewNavigationMenuAdapter(getActivity(), menuItems);

		gridView.setAdapter(adapter);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		for (SettingsMenuItem menuItem : menuItems) {
			menuItem.selected = false;
		}

		menuItems.get(position).selected = true;
		SettingsMenuItem menuItem = (SettingsMenuItem) gridView.getItemAtPosition(position);
		menuItem.selected = true;
		((BaseAdapter)parent.getAdapter()).notifyDataSetChanged();

		// TODO adjust switch/closeBoard when the same fragment opened
		switch (menuItem.iconRes) {
			case R.drawable.ic_nav_home:
				getActivityFace().switchFragment(new HomeTabsFragment()); // TODO clear stack
				getActivityFace().toggleMenu(SlidingMenu.LEFT);
				break;
			default: break;

		}
	}

	private class SettingsMenuItem {
		public int nameId;
		public int iconRes;
		public boolean selected;

		public SettingsMenuItem(int nameId, int iconRes) {
			this.nameId = nameId;
			this.iconRes = iconRes;
		}
	}

	private class NewNavigationMenuAdapter extends ItemsAdapter<SettingsMenuItem> {

		public NewNavigationMenuAdapter(Context context, List<SettingsMenuItem> menuItems) {
			super(context, menuItems);
		}

		@Override
		protected View createView(ViewGroup parent) {
			return inflater.inflate(R.layout.new_settings_menu_grid_item, parent, false);
		}

		@Override
		protected void bindView(SettingsMenuItem item, int pos, View convertView) {
			TextView icon = (TextView) convertView.findViewById(R.id.iconTxt);
			icon.setText(item.iconRes);

			TextView title = (TextView) convertView.findViewById(R.id.rowTitleTxt);
			title.setText(item.nameId);
		}

		public Context getContext() {
			return context;
		}
	}
}