package com.chess.ui.fragments.upgrade;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.chess.R;
import com.chess.backend.image_load.EnhancedImageDownloader;
import com.chess.backend.image_load.ProgressImageView;
import com.chess.ui.fragments.CommonLogicFragment;
import com.chess.utilities.FontsHelper;
import com.chess.widgets.RoboTextView;

/**
 * Created with IntelliJ IDEA.
 * User: roger sent2roger@gmail.com
 * Date: 26.11.13
 * Time: 13:28
 */
public class UpgradeFragmentTablet extends CommonLogicFragment {

	private static final String IMG_URL_1 = "http://images.chesscomfiles.com/images/icons/reviews/membership/peter.png";
	private static final String IMG_URL_2 = "http://images.chesscomfiles.com/images/icons/reviews/membership/lou.png";
	private EnhancedImageDownloader imageDownloader;
	private int imageSize;
	private ProgressImageView quoteImg;
	private ProgressImageView quoteImg2;
	private LinearLayout.LayoutParams optionsParams;
	private LinearLayout.LayoutParams valuesParams;
	private int optionsTextSize;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
		transaction.replace(R.id.diamondFragmentContainer, UpgradeDetailsFragmentTablet.createInstance(UpgradeDetailsFragment.DIAMOND)
				, "Diamond");
		transaction.replace(R.id.platinumFragmentContainer, UpgradeDetailsFragmentTablet.createInstance(UpgradeDetailsFragment.PLATINUM)
				, "Platinum");
		transaction.replace(R.id.goldFragmentContainer, UpgradeDetailsFragmentTablet.createInstance(UpgradeDetailsFragment.GOLD)
				, "Gold");
		transaction.commitAllowingStateLoss();


		imageDownloader = new EnhancedImageDownloader(getActivity());
		imageSize = (int) (80 * density);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.upgrade_frame, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		setTitle(R.string.upgrade);

		widgetsInit(view);
	}

	@Override
	public void onResume() {
		super.onResume();

		imageDownloader.download(IMG_URL_1, quoteImg, imageSize);
		imageDownloader.download(IMG_URL_2, quoteImg2, imageSize);
	}

	private void widgetsInit(View view) {
		quoteImg = (ProgressImageView) view.findViewById(R.id.quoteImg);
		quoteImg2 = (ProgressImageView) view.findViewById(R.id.quoteImg2);

		Resources resources = getResources();

		{// fill features comparison list
			LinearLayout diamondOptionsLay = (LinearLayout) view.findViewById(R.id.diamondOptionsLay);
			LinearLayout platinumOptionsLay = (LinearLayout) view.findViewById(R.id.platinumOptionsLay);
			LinearLayout goldOptionsLay = (LinearLayout) view.findViewById(R.id.goldOptionsLay);

			optionsParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
			optionsParams.weight = 1;

			valuesParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);

			optionsTextSize = (int) (resources.getDimensionPixelSize(R.dimen.upgrade_options_text_size) / density);

			String[] diamondValues = resources.getStringArray(R.array.upgrade_diamond_features);
			String[] platinumValues = resources.getStringArray(R.array.upgrade_platinum_features);
			String[] goldValues = resources.getStringArray(R.array.upgrade_gold_features);

			int diamondTextColor = resources.getColor(R.color.upgrade_diamond_sub_title);
			int platinumTextColor = resources.getColor(R.color.upgrade_platinum_sub_title);
			int goldTextColor = resources.getColor(R.color.upgrade_gold_sub_title);

			int diamondBackResId = R.drawable.button_upgrade_diamond_flat;
			int platinumBackResId = R.drawable.button_upgrade_platinum_flat;
			int goldBackResId = R.drawable.button_upgrade_gold_flat;

			setOptionsValues(diamondOptionsLay, diamondValues, diamondBackResId, diamondTextColor);
			setOptionsValues(platinumOptionsLay, platinumValues, platinumBackResId, platinumTextColor);
			setOptionsValues(goldOptionsLay, goldValues, goldBackResId, goldTextColor);
		}
	}

	private void setOptionsValues(LinearLayout optionsLay, String[] values, int backResId, int color) {
		for (String option : values) {
			int padding = (int) (18 * density);
			LinearLayout linearLayout = new LinearLayout(getActivity());
			linearLayout.setOrientation(LinearLayout.HORIZONTAL);
			linearLayout.setMinimumHeight((int) (51 * density));
			linearLayout.setBackgroundResource(backResId);
			linearLayout.setPadding(padding, padding, padding, padding);
			linearLayout.setGravity(Gravity.CENTER_VERTICAL);

			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
			int marginBetween = (int) (2 * density);
			layoutParams.setMargins(0, 0, 0, marginBetween);
			linearLayout.setLayoutParams(layoutParams);

			{// add option
				RoboTextView optionTxt = new RoboTextView(getActivity());
				optionTxt.setText(option);
				optionTxt.setTextColor(color);
				optionTxt.setFont(FontsHelper.BOLD_FONT);
				optionTxt.setTextSize(optionsTextSize);

				linearLayout.addView(optionTxt, optionsParams);
			}
//			{// add value
//				RoboTextView valueTxt = new RoboTextView(getActivity());
//				valueTxt.setText(option);
//				valueTxt.setTextColor(color);
//				valueTxt.setFont(FontsHelper.BOLD_FONT);
//				valueTxt.setTextSize(optionsTextSize);
//
//				linearLayout.addView(valueTxt, valuesParams);
//
//			}

			optionsLay.addView(linearLayout);
		}
	}
}
