package com.chess.db.tasks;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import com.chess.backend.RestHelper;
import com.chess.backend.entity.LoadItem;
import com.chess.backend.entity.new_api.FriendsItem;
import com.chess.backend.entity.new_api.VideoItem;
import com.chess.backend.interfaces.TaskUpdateInterface;
import com.chess.backend.statics.AppData;
import com.chess.backend.statics.StaticData;
import com.chess.backend.tasks.AbstractUpdateTask;
import com.chess.db.DBConstants;
import com.chess.db.DBDataManager;
import com.google.gson.Gson;

import java.util.List;


public class SaveVideosListTask extends AbstractUpdateTask<VideoItem.VideoDataItem, Long> {
	private static final String TAG = "SaveFriendsListTask";

	private ContentResolver contentResolver;
	protected static String[] arguments = new String[1];

	public SaveVideosListTask(TaskUpdateInterface<VideoItem.VideoDataItem> taskFace, List<VideoItem.VideoDataItem> currentItems,
							  ContentResolver resolver) {
        super(taskFace);
		this.itemList = currentItems;
		this.contentResolver = resolver;
	}

	@Override
    protected Integer doTheTask(Long... ids) {

		for (VideoItem.VideoDataItem currentItem : itemList) {
			final String[] arguments2 = arguments;
			arguments2[0] = String.valueOf(currentItem.getName());

			// TODO implement beginTransaction logic for performance increase
			Uri uri = DBConstants.VIDEOS_CONTENT_URI;

			Cursor cursor = contentResolver.query(uri, DBDataManager.PROJECTION_NAME,
					DBDataManager.SELECTION_NAME, arguments2, null);
			if (cursor.moveToFirst()) {
				contentResolver.update(Uri.parse(uri.toString() + DBDataManager.SLASH_ + DBDataManager.getId(cursor)),
						DBDataManager.putVideoItemToValues(currentItem), null, null);
			} else {
				contentResolver.insert(uri, DBDataManager.putVideoItemToValues(currentItem));
			}

			cursor.close();
		}

        result = StaticData.RESULT_OK;

        return result;
    }

}