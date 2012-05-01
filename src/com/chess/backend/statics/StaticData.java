package com.chess.backend.statics;

/**
 * StaticData class
 *
 * @author alien_roger
 * @created at: 20.03.12 5:36
 */
public class StaticData {
	/*Result constatnts*/
	public static final int UNKNOWN_ERROR = -1;
	public static final int RESULT_OK = 0;
	public static final int EMPTY_DATA = 1;
	public static final int DATA_EXIST = 2;

	public static final String CLEAR_CHAT_NOTIFICATION = "clear_chat_notification";

	public static final String REQUEST_CODE = "pending_intent_request_code";
	public static final String NAVIGATION_CMD = "navigation_command";

	public static final int NAV_FINISH_2_LOGIN = 55;
	public static final long WAKE_SCREEN_TIMEOUT = 3*60*1000;
//	public static final long WAKE_SCREEN_TIMEOUT = 20*1000;
	
	/* Notification requests codes */
	public static final int MOVE_REQUEST_CODE = 22;
//	public static final String SHP_USER_LAST_MOVE_UPDATE_TIME = "user_last_saw_your_move_time";
	public static final String SHARED_DATA_NAME = "sharedData";

    /* After move actions */
    public static final int AFTER_MOVE_GO_TO_NEXT_GAME = 0;
    public static final int AFTER_MOVE_STAY_ON_SAME_GAME = 1;
    public static final int AFTER_MOVE_RETURN_TO_GAME_LIST = 2;
}
