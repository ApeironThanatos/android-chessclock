package com.chess.db;

import android.net.Uri;

/**
 * @author alien_roger
 * @created 27.10.12
 * @modified 27.10.12
 */
public class DBConstants {

    static final int DATABASE_VERSION = 36;  // change version on every DB scheme changes


	public static final String PROVIDER_NAME = "com.chess.db_provider";

	public static final String CONTENT_PATH = "content://";
	public static final String SLASH = "/";

	/*
	 * DB table names
	 */
    static final String DATABASE_NAME  = "Chess DB";

	// TODO replace with enum!
	public static final String[] tablesArray = new String[]{
			"tactics_batch",
			"tactics_results",
			"daily_finished_games",
			"daily_current_games",
			"friends",

			"articles",
			"article_categories",
			"videos",
			"video_categories",

			"user_stats_live_standard",
			"user_stats_live_blitz",
			"user_stats_live_lightning",
			"user_stats_daily_chess",
			"user_stats_daily_chess960",
			"user_stats_tactics",
			"user_stats_lessons",

			"game_stats_live_standard",
			"game_stats_live_blitz",
			"game_stats_live_lightning",
			"game_stats_daily_chess",
			"game_stats_daily_chess960",

			"video_viewed",

			"forum_topics",
			"forum_categories",
			"forum_comments"
	};

	// Content URI
	public static final Uri[] uriArray = new Uri[tablesArray.length];

	static {
		for (int i = 0; i < tablesArray.length; i++) {
			String table = tablesArray[i];
			uriArray[i] = Uri.parse(CONTENT_PATH + PROVIDER_NAME + SLASH + table);
		}
	}

    // uri paths      // TODO replace with enum!
    public static final int TACTICS_BATCH = 0;
    public static final int TACTICS_RESULTS = 1;
    public static final int DAILY_FINISHED_GAMES = 2;
    public static final int DAILY_CURRENT_GAMES = 3;
    public static final int FRIENDS = 4;

    public static final int ARTICLES = 5;
    public static final int ARTICLE_CATEGORIES = 6;
    public static final int VIDEOS = 7;
    public static final int VIDEO_CATEGORIES = 8;

    public static final int USER_STATS_LIVE_STANDARD = 9;
    public static final int USER_STATS_LIVE_BLITZ = 10;
    public static final int USER_STATS_LIVE_LIGHTNING = 11;
    public static final int USER_STATS_DAILY_CHESS = 12;
    public static final int USER_STATS_DAILY_CHESS960 = 13;
    public static final int USER_STATS_TACTICS = 14;
    public static final int USER_STATS_LESSONS = 15;

    public static final int GAME_STATS_LIVE_STANDARD = 16;
    public static final int GAME_STATS_LIVE_BLITZ = 17;
    public static final int GAME_STATS_LIVE_LIGHTNING = 18;
    public static final int GAME_STATS_DAILY_CHESS = 19;
    public static final int GAME_STATS_DAILY_CHESS960 = 20;

    public static final int VIDEO_VIEWED = 21;
    public static final int FORUM_TOPICS = 22;
    public static final int FORUM_CATEGORIES = 23;
    public static final int FORUM_POSTS = 24;


    // general fields
    public static final String _ID = "_id";
    public static final String _COUNT = "_count";

    /* TacticsItem Fields */

    public static final String V_USER     		= "user";
    public static final String V_FEN      		= "fen";
    public static final String V_MOVE_LIST      = "move_list";
    public static final String V_ATTEMPT_CNT    = "attempt_cnt";
    public static final String V_PASSED_CNT     = "passed_cnt";
    public static final String V_RATING       	= "rating";
    public static final String V_AVG_SECONDS 	= "avg_seconds";
    public static final String V_SECONDS_SPENT 	= "seconds_spent";
    public static final String V_STOP 	        = "stop";
    public static final String V_WAS_SHOWED     = "was_showed";
    public static final String V_IS_RETRY       = "is_retry";

    public static final String V_SCORE                  = "score";
    public static final String V_USER_RATING_CHANGE     = "user_rating_change";
    public static final String V_USER_RATING            = "user_rating";
    public static final String V_PROBLEM_RATING_CHANGE  = "problem_rating_change";
    public static final String V_PROBLEM_RATING         = "problem_rating";


	/* ECHESS_GAMES */
	public static final String V_FINISHED 				= "is_finished";
	public static final String V_I_PLAY_AS 				= "i_play_as";
	public static final String V_GAME_TYPE 				= "game_type";
	public static final String V_GAME_NAME 				= "game_name";
	public static final String V_WHITE_USERNAME 		= "white_username";
	public static final String V_BLACK_USERNAME 		= "black_username";
	public static final String V_FEN_START_POSITION 	= "fen_start_position";
	public static final String V_WHITE_RATING 			= "white_rating";
	public static final String V_BLACK_RATING 			= "black_rating";
	public static final String V_WHITE_AVATAR 			= "white_avatar";
	public static final String V_BLACK_AVATAR 			= "black_avatar";
	public static final String V_WHITE_PREMIUM_STATUS	= "white_premium_status";
	public static final String V_BLACK_PREMIUM_STATUS	= "black_premium_status";
	public static final String V_HAS_NEW_MESSAGE 		= "has_new_message";
	public static final String V_IS_OPPONENT_ONLINE		= "is_opponent_online";
	public static final String V_RATED 					= "rated";
	public static final String V_DAYS_PER_MOVE 			= "days_per_move";
	public static final String V_OPPONENT_OFFERED_DRAW 	= "opponent_offered_draw";

	public static final String V_TIME_REMAINING 		= "time_remaining";
	public static final String V_TIMESTAMP 				= "timestamp";
	public static final String V_LAST_MOVE_FROM_SQUARE 	= "last_move_from_square";
	public static final String V_LAST_MOVE_TO_SQUARE 	= "last_move_to_square";
	public static final String V_GAME_SCORE 			= "game_score";
	public static final String V_RESULT_MESSAGE 		= "result_message";
	public static final String V_IS_MY_TURN 			= "is_my_turn";

	/*Friends*/
	public static final String V_USERNAME 				= "username";
	public static final String V_USER_ID 				= "user_id";
	public static final String V_LOCATION 				= "location";
	public static final String V_COUNTRY_ID 			= "country_id";
	public static final String V_PHOTO_URL 				= "photo_url";
	public static final String V_PREMIUM_STATUS			= "premium_status";
	public static final String V_LAST_LOGIN_DATE		= "last_login_date";

	/*Articles*/
	public static final String V_TITLE 					= "title";
	public static final String V_CREATE_DATE 			= "create_date";
	public static final String V_CATEGORY 				= "category";
	public static final String V_CATEGORY_ID 			= "category_id";
	public static final String V_CHESS_TITLE 			= "chess_title";
	public static final String V_FIRST_NAME 			= "first_name";
	public static final String V_LAST_NAME 				= "last_name";
	public static final String V_THUMB_CONTENT 			= "thumb_in_content";

	/*Articles Categories*/
	public static final String V_ID 				= "id";
	public static final String V_NAME 				= "name";
	public static final String V_DISPLAY_ORDER 		= "display_order";

	/*Videos*/
	public static final String V_DESCRIPTION 		= "description";
	public static final String V_SKILL_LEVEL 		= "skill_level";
	public static final String V_MINUTES 			= "minutes";
	public static final String V_URL 				= "url";
	public static final String V_KEY_FEN 			= "key_fen";
	public static final String V_USER_AVATAR 		= "user_avatar";
	public static final String V_VIEW_COUNT 		= "view_count";
	public static final String V_COMMENT_COUNT 		= "comment_count";
	public static final String V_VIDEO_VIEWED 		= "video_viewed";

	/* Forums */
	public static final String V_LAST_POST_USERNAME = "last_post_username";
	public static final String V_LAST_POST_DATE = "last_post_date";
	public static final String V_POST_COUNT 		= "post_count";
	public static final String V_TOPIC_COUNT 		= "topic_count";
	public static final String V_MIN_MEMBERSHIP 	= "min_membership_lvl";

	/* common commands */
    private static final String CREATE_TABLE_IF_NOT_EXISTS = "create table if not exists ";
    private static final String _INT_NOT_NULL 		= " INT not null";
    private static final String _LONG_NOT_NULL 		= " LONG not null";
    private static final String _TEXT_NOT_NULL 		= " TEXT not null";
    private static final String _COMMA 				= ",";
    private static final String _CLOSE 				= ");";
    private static final String ID_INTEGER_PRIMARY_KEY_AUTOINCREMENT = " (_id integer primary key autoincrement, ";


    static final String TACTICS_BATCH_TABLE_CREATE =
            CREATE_TABLE_IF_NOT_EXISTS + tablesArray[TACTICS_BATCH] + ID_INTEGER_PRIMARY_KEY_AUTOINCREMENT
			+ V_USER 		    + _TEXT_NOT_NULL + _COMMA
			+ V_ID       		+ _LONG_NOT_NULL + _COMMA
			+ V_FEN 		    + _TEXT_NOT_NULL + _COMMA
			+ V_MOVE_LIST 	    + _TEXT_NOT_NULL + _COMMA
			+ V_ATTEMPT_CNT     + _INT_NOT_NULL + _COMMA
			+ V_PASSED_CNT 	    + _INT_NOT_NULL + _COMMA
			+ V_RATING          + _INT_NOT_NULL + _COMMA
			+ V_STOP 		    + _INT_NOT_NULL + _COMMA
			+ V_WAS_SHOWED	    + _INT_NOT_NULL + _COMMA
			+ V_IS_RETRY	    + _INT_NOT_NULL + _COMMA
			+ V_SECONDS_SPENT	+ _LONG_NOT_NULL + _COMMA
			+ V_AVG_SECONDS     + _TEXT_NOT_NULL + _CLOSE;

    static final String TACTICS_RESULTS_TABLE_CREATE =
            CREATE_TABLE_IF_NOT_EXISTS + tablesArray[TACTICS_RESULTS] + ID_INTEGER_PRIMARY_KEY_AUTOINCREMENT
            + V_USER 		        	+ _TEXT_NOT_NULL + _COMMA
            + V_ID           			+ _LONG_NOT_NULL + _COMMA
            + V_SCORE               	+ _TEXT_NOT_NULL + _COMMA
            + V_USER_RATING_CHANGE		+ _INT_NOT_NULL + _COMMA
            + V_USER_RATING         	+ _INT_NOT_NULL + _COMMA
            + V_PROBLEM_RATING_CHANGE 	+ _INT_NOT_NULL + _COMMA
            + V_PROBLEM_RATING      	+ _INT_NOT_NULL + _CLOSE;

	static final String DAILY_CURRENT_GAMES_CREATE =
			CREATE_TABLE_IF_NOT_EXISTS + tablesArray[DAILY_CURRENT_GAMES] + ID_INTEGER_PRIMARY_KEY_AUTOINCREMENT
			+ V_USER 				    	+ _TEXT_NOT_NULL + _COMMA
			+ V_ID 				    		+ _LONG_NOT_NULL + _COMMA
			+ V_FINISHED 					+ _INT_NOT_NULL + _COMMA
			+ V_RATED 						+ _INT_NOT_NULL + _COMMA
			+ V_I_PLAY_AS				    + _INT_NOT_NULL + _COMMA
			+ V_GAME_TYPE 				    + _INT_NOT_NULL + _COMMA
			+ V_DAYS_PER_MOVE 			    + _INT_NOT_NULL + _COMMA
			+ V_FEN 	    				+ _TEXT_NOT_NULL + _COMMA
			+ V_TIMESTAMP 				    + _LONG_NOT_NULL + _COMMA
			+ V_GAME_NAME 	    			+ _TEXT_NOT_NULL + _COMMA
			+ V_LAST_MOVE_FROM_SQUARE 	    + _TEXT_NOT_NULL + _COMMA
			+ V_LAST_MOVE_TO_SQUARE 	    + _TEXT_NOT_NULL + _COMMA
			+ V_IS_OPPONENT_ONLINE 			+ _INT_NOT_NULL + _COMMA
			+ V_HAS_NEW_MESSAGE 			+ _INT_NOT_NULL + _COMMA
			+ V_WHITE_USERNAME 				+ _TEXT_NOT_NULL + _COMMA
			+ V_BLACK_USERNAME 				+ _TEXT_NOT_NULL + _COMMA
			+ V_WHITE_RATING 		    	+ _INT_NOT_NULL + _COMMA
			+ V_BLACK_RATING 				+ _INT_NOT_NULL + _COMMA
			+ V_WHITE_PREMIUM_STATUS		+ _INT_NOT_NULL + _COMMA
			+ V_BLACK_PREMIUM_STATUS		+ _INT_NOT_NULL + _COMMA
			+ V_WHITE_AVATAR 				+ _TEXT_NOT_NULL + _COMMA
			+ V_BLACK_AVATAR 				+ _TEXT_NOT_NULL + _COMMA
			+ V_TIME_REMAINING 				+ _INT_NOT_NULL + _COMMA
			+ V_FEN_START_POSITION 	    	+ _TEXT_NOT_NULL + _COMMA
			+ V_MOVE_LIST		 	    	+ _TEXT_NOT_NULL + _COMMA
			+ V_OPPONENT_OFFERED_DRAW       + _INT_NOT_NULL + _COMMA
			+ V_IS_MY_TURN 	        		+ _INT_NOT_NULL + _CLOSE;

    static final String DAILY_FINISHED_GAMES_CREATE =
			CREATE_TABLE_IF_NOT_EXISTS + tablesArray[DAILY_FINISHED_GAMES] + ID_INTEGER_PRIMARY_KEY_AUTOINCREMENT
			+ V_USER 				    	+ _TEXT_NOT_NULL + _COMMA
			+ V_ID 				    		+ _LONG_NOT_NULL + _COMMA
			+ V_FINISHED 					+ _INT_NOT_NULL + _COMMA
			+ V_RATED 						+ _INT_NOT_NULL + _COMMA
			+ V_I_PLAY_AS				    + _INT_NOT_NULL + _COMMA
			+ V_GAME_TYPE 				    + _INT_NOT_NULL + _COMMA
			+ V_DAYS_PER_MOVE 			    + _INT_NOT_NULL + _COMMA
			+ V_FEN 	    				+ _TEXT_NOT_NULL + _COMMA
			+ V_TIMESTAMP 				    + _LONG_NOT_NULL + _COMMA
			+ V_GAME_NAME 	    			+ _TEXT_NOT_NULL + _COMMA
			+ V_LAST_MOVE_FROM_SQUARE 	    + _TEXT_NOT_NULL + _COMMA
			+ V_LAST_MOVE_TO_SQUARE 	    + _TEXT_NOT_NULL + _COMMA
			+ V_IS_OPPONENT_ONLINE 			+ _INT_NOT_NULL + _COMMA
			+ V_HAS_NEW_MESSAGE 			+ _INT_NOT_NULL + _COMMA
			+ V_WHITE_USERNAME 				+ _TEXT_NOT_NULL + _COMMA
			+ V_BLACK_USERNAME 				+ _TEXT_NOT_NULL + _COMMA
			+ V_WHITE_AVATAR 				+ _TEXT_NOT_NULL + _COMMA
			+ V_BLACK_AVATAR 				+ _TEXT_NOT_NULL + _COMMA
			+ V_WHITE_RATING 		    	+ _INT_NOT_NULL + _COMMA
			+ V_BLACK_RATING 				+ _INT_NOT_NULL + _COMMA
			+ V_WHITE_PREMIUM_STATUS		+ _INT_NOT_NULL + _COMMA
			+ V_BLACK_PREMIUM_STATUS		+ _INT_NOT_NULL + _COMMA
			+ V_TIME_REMAINING 				+ _INT_NOT_NULL + _COMMA
			+ V_FEN_START_POSITION 	    	+ _TEXT_NOT_NULL + _COMMA
			+ V_MOVE_LIST		 	    	+ _TEXT_NOT_NULL + _COMMA
			+ V_RESULT_MESSAGE 				+ _TEXT_NOT_NULL + _COMMA
			+ V_GAME_SCORE 					+ _INT_NOT_NULL + _CLOSE;

	static final String FRIENDS_CREATE =
			CREATE_TABLE_IF_NOT_EXISTS + tablesArray[FRIENDS] + ID_INTEGER_PRIMARY_KEY_AUTOINCREMENT
			+ V_USER 					+ _TEXT_NOT_NULL + _COMMA
			+ V_USERNAME 				+ _TEXT_NOT_NULL + _COMMA
			+ V_LOCATION 	    		+ _TEXT_NOT_NULL + _COMMA
			+ V_COUNTRY_ID 	    		+ _INT_NOT_NULL + _COMMA
			+ V_PREMIUM_STATUS    		+ _INT_NOT_NULL + _COMMA
			+ V_IS_OPPONENT_ONLINE 		+ _INT_NOT_NULL + _COMMA
			+ V_LAST_LOGIN_DATE 		+ _LONG_NOT_NULL + _COMMA
			+ V_USER_ID 	    		+ _INT_NOT_NULL + _COMMA
			+ V_PHOTO_URL 	    		+ _TEXT_NOT_NULL + _CLOSE;


	static final String ARTICLES_CREATE =
			CREATE_TABLE_IF_NOT_EXISTS + tablesArray[ARTICLES] + ID_INTEGER_PRIMARY_KEY_AUTOINCREMENT
			+ V_ID 						+ _LONG_NOT_NULL + _COMMA
			+ V_TITLE 					+ _TEXT_NOT_NULL + _COMMA
			+ V_CREATE_DATE 			+ _LONG_NOT_NULL + _COMMA
			+ V_CATEGORY 				+ _TEXT_NOT_NULL + _COMMA
			+ V_CATEGORY_ID 			+ _LONG_NOT_NULL + _COMMA
			+ V_USER_ID 				+ _LONG_NOT_NULL + _COMMA
			+ V_USERNAME 	    		+ _TEXT_NOT_NULL + _COMMA
			+ V_COUNTRY_ID 	    		+ _INT_NOT_NULL + " DEFAULT 0"+ _COMMA
			+ V_FIRST_NAME 	    		+ _TEXT_NOT_NULL + " DEFAULT TestFirstName"+ _COMMA
			+ V_LAST_NAME 	    		+ _TEXT_NOT_NULL + " DEFAULT TestLastName"+ _COMMA
			+ V_USER_AVATAR 	    	+ _TEXT_NOT_NULL + _COMMA
			+ V_PHOTO_URL 	    		+ _TEXT_NOT_NULL + _COMMA
			+ V_THUMB_CONTENT 	    	+ _TEXT_NOT_NULL + _COMMA
			+ V_CHESS_TITLE 	    	+ _TEXT_NOT_NULL + " DEFAULT TestIM"+ _CLOSE;

	static final String ARTICLE_CATEGORIES_CREATE =
			CREATE_TABLE_IF_NOT_EXISTS + tablesArray[ARTICLE_CATEGORIES] + ID_INTEGER_PRIMARY_KEY_AUTOINCREMENT
			+ V_NAME 					+ _TEXT_NOT_NULL + _COMMA
			+ V_CATEGORY_ID 			+ _INT_NOT_NULL + _COMMA
			+ V_DISPLAY_ORDER 	    	+ _INT_NOT_NULL + _CLOSE;

	static final String VIDEOS_CREATE =
			CREATE_TABLE_IF_NOT_EXISTS + tablesArray[VIDEOS] + ID_INTEGER_PRIMARY_KEY_AUTOINCREMENT
			+ V_TITLE 					+ _TEXT_NOT_NULL + _COMMA
			+ V_DESCRIPTION 			+ _TEXT_NOT_NULL + _COMMA
			+ V_CATEGORY 				+ _TEXT_NOT_NULL + _COMMA
			+ V_CATEGORY_ID 			+ _INT_NOT_NULL + _COMMA
			+ V_ID 						+ _INT_NOT_NULL + _COMMA
			+ V_SKILL_LEVEL 			+ _TEXT_NOT_NULL + _COMMA
			+ V_USERNAME				+ _TEXT_NOT_NULL + _COMMA
			+ V_USER_AVATAR				+ _TEXT_NOT_NULL + _COMMA
			+ V_MINUTES 				+ _INT_NOT_NULL + _COMMA
			+ V_VIEW_COUNT 				+ _INT_NOT_NULL + _COMMA
			+ V_COUNTRY_ID 				+ _INT_NOT_NULL + _COMMA
			+ V_COMMENT_COUNT 			+ _INT_NOT_NULL + _COMMA
			+ V_CREATE_DATE 	    	+ _LONG_NOT_NULL + _COMMA
			+ V_URL 					+ _TEXT_NOT_NULL + _COMMA
			+ V_KEY_FEN 	    		+ _TEXT_NOT_NULL + _COMMA
			+ V_FIRST_NAME 	    		+ _TEXT_NOT_NULL + _COMMA
			+ V_LAST_NAME 	    		+ _TEXT_NOT_NULL + _COMMA
			+ V_CHESS_TITLE 	    	+ _TEXT_NOT_NULL + _CLOSE;

	static final String VIDEO_VIEWED_CREATE =
			CREATE_TABLE_IF_NOT_EXISTS + tablesArray[VIDEO_VIEWED] + ID_INTEGER_PRIMARY_KEY_AUTOINCREMENT
			+ V_USER 					+ _TEXT_NOT_NULL + _COMMA
			+ V_ID 						+ _INT_NOT_NULL + _COMMA
			+ V_VIDEO_VIEWED 	    	+ _INT_NOT_NULL + _CLOSE;

	static final String VIDEO_CATEGORIES_CREATE =
			CREATE_TABLE_IF_NOT_EXISTS + tablesArray[VIDEO_CATEGORIES] + ID_INTEGER_PRIMARY_KEY_AUTOINCREMENT
			+ V_NAME 					+ _TEXT_NOT_NULL + _COMMA
			+ V_CATEGORY_ID 			+ _INT_NOT_NULL + _COMMA
			+ V_DISPLAY_ORDER 	    	+ _INT_NOT_NULL + _CLOSE;

	static final String FORUM_TOPIC_CREATE =
			CREATE_TABLE_IF_NOT_EXISTS + tablesArray[FORUM_TOPICS] + ID_INTEGER_PRIMARY_KEY_AUTOINCREMENT
			+ V_TITLE 					+ _TEXT_NOT_NULL + _COMMA
			+ V_ID 						+ _INT_NOT_NULL + _COMMA
			+ V_CATEGORY_ID 			+ _INT_NOT_NULL + _COMMA
			+ V_CATEGORY 				+ _TEXT_NOT_NULL + _COMMA
			+ V_URL 					+ _TEXT_NOT_NULL + _COMMA
			+ V_USERNAME 				+ _TEXT_NOT_NULL + _COMMA
			+ V_LAST_POST_USERNAME 		+ _TEXT_NOT_NULL + _COMMA
			+ V_POST_COUNT 				+ _INT_NOT_NULL + _COMMA
			+ V_LAST_POST_DATE 	    	+ _LONG_NOT_NULL + _CLOSE;

	static final String FORUM_CATEGORIES_CREATE =
			CREATE_TABLE_IF_NOT_EXISTS + tablesArray[FORUM_CATEGORIES] + ID_INTEGER_PRIMARY_KEY_AUTOINCREMENT
			+ V_ID 						+ _INT_NOT_NULL + _COMMA
			+ V_NAME 					+ _TEXT_NOT_NULL + _COMMA
			+ V_CREATE_DATE				+ _LONG_NOT_NULL + _COMMA
			+ V_LAST_POST_DATE			+ _LONG_NOT_NULL + _COMMA
			+ V_DISPLAY_ORDER			+ _INT_NOT_NULL + _COMMA
			+ V_DESCRIPTION				+ _TEXT_NOT_NULL + _COMMA
			+ V_TOPIC_COUNT				+ _INT_NOT_NULL + _COMMA
			+ V_POST_COUNT				+ _INT_NOT_NULL + _COMMA
			+ V_MIN_MEMBERSHIP			+ _INT_NOT_NULL + _CLOSE;

	static final String FORUM_POSTS_CREATE =
			CREATE_TABLE_IF_NOT_EXISTS + tablesArray[FORUM_POSTS] + ID_INTEGER_PRIMARY_KEY_AUTOINCREMENT
			+ V_ID 						+ _INT_NOT_NULL + _COMMA    // Topic id!
			+ V_USERNAME				+ _TEXT_NOT_NULL + _COMMA
			+ V_CREATE_DATE				+ _LONG_NOT_NULL + _COMMA
			+ V_DESCRIPTION				+ _LONG_NOT_NULL + _CLOSE;

	/* ==================== User Stats ============================== */
	 /*Rating*/
	public static final String V_CURRENT 				= "current";
	public static final String V_HIGHEST_RATING 		= "highest_rating";
	public static final String V_HIGHEST_TIMESTAMP 		= "highest_timestamp";
	public static final String V_BEST_WIN_RATING 		= "best_win_rating";
	public static final String V_BEST_WIN_GAME_ID 		= "best_win_game_id";
	public static final String V_BEST_WIN_USERNAME 		= "best_win_username";
	public static final String V_AVERAGE_OPPONENT 		= "average_opponent";
	/*Games*/
	public static final String V_GAMES_TOTAL 			= "games_total";
	public static final String V_GAMES_WINS 			= "games_wins";
	public static final String V_GAMES_LOSSES 			= "games_losses";
	public static final String V_GAMES_DRAWS 			= "games_draws";
	/* Daily */
	public static final String V_RANK 					= "rank";
	public static final String V_TOTAL_PLAYER_COUNT 	= "total_player_count";
	public static final String V_TIMEOUTS 				= "timeouts";
	public static final String V_TIME_PER_MOVE 			= "time_per_move";

	/* Tactics */
	public static final String V_LOWEST_RATING 			= "lowest_rating";
	public static final String V_LOWEST_TIMESTAMP 		= "lowest_timestamp";
	public static final String V_ATTEMPT_COUNT 			= "attempt_count";
	public static final String V_PASSED_COUNT 			= "passed_count";
	public static final String V_FAILED_COUNT 			= "failed_count";
	public static final String V_TOTAL_SECONDS 			= "total_seconds";
	public static final String V_TODAYS_ATTEMPTS 		= "todays_attempts";
	public static final String V_TODAYS_AVG_SCORE		= "todays_average_score";

	/* Lessons*/
	public static final String V_LESSONS_TRIED 				= "lessons_tried";
	public static final String V_TOTAL_LESSON_COUNT 		= "total_lesson_count";
	public static final String V_LESSON_COMPLETE_PERCENTAGE = "lesson_complete_percentage";
	public static final String V_TOTAL_TRAINING_SECONDS 	= "total_training_seconds";


	static final String USER_STATS_LIVE_STANDARD_CREATE =
			CREATE_TABLE_IF_NOT_EXISTS + tablesArray[USER_STATS_LIVE_STANDARD] + ID_INTEGER_PRIMARY_KEY_AUTOINCREMENT
			+ V_USER 					+ _TEXT_NOT_NULL + _COMMA
			+ V_CURRENT 				+ _INT_NOT_NULL + _COMMA
			+ V_HIGHEST_RATING 	    	+ _INT_NOT_NULL + _COMMA
			+ V_HIGHEST_TIMESTAMP 	    + _LONG_NOT_NULL + _COMMA
			+ V_BEST_WIN_RATING 	    + _INT_NOT_NULL + _COMMA
			+ V_BEST_WIN_USERNAME 	    + _TEXT_NOT_NULL + _COMMA
			+ V_AVERAGE_OPPONENT 	    + _INT_NOT_NULL + _COMMA
			+ V_GAMES_TOTAL  	    	+ _INT_NOT_NULL + _COMMA
			+ V_GAMES_WINS   	    	+ _INT_NOT_NULL + _COMMA
			+ V_GAMES_LOSSES 	    	+ _INT_NOT_NULL + _COMMA
			+ V_GAMES_DRAWS  	    	+ _INT_NOT_NULL + _CLOSE;

	static final String USER_STATS_LIVE_LIGHTNING_CREATE =
			CREATE_TABLE_IF_NOT_EXISTS + tablesArray[USER_STATS_LIVE_LIGHTNING] + ID_INTEGER_PRIMARY_KEY_AUTOINCREMENT
			+ V_USER 					+ _TEXT_NOT_NULL + _COMMA
			+ V_CURRENT 				+ _INT_NOT_NULL + _COMMA
			+ V_HIGHEST_RATING 	    	+ _INT_NOT_NULL + _COMMA
			+ V_HIGHEST_TIMESTAMP 	    + _LONG_NOT_NULL + _COMMA
			+ V_BEST_WIN_RATING 	    + _INT_NOT_NULL + _COMMA
			+ V_BEST_WIN_USERNAME 	    + _TEXT_NOT_NULL + _COMMA
			+ V_AVERAGE_OPPONENT 	    + _INT_NOT_NULL + _COMMA
			+ V_GAMES_TOTAL  	    	+ _INT_NOT_NULL + _COMMA
			+ V_GAMES_WINS   	    	+ _INT_NOT_NULL + _COMMA
			+ V_GAMES_LOSSES 	    	+ _INT_NOT_NULL + _COMMA
			+ V_GAMES_DRAWS  	    	+ _INT_NOT_NULL + _CLOSE;

	static final String USER_STATS_LIVE_BLITZ_CREATE =
			CREATE_TABLE_IF_NOT_EXISTS + tablesArray[USER_STATS_LIVE_BLITZ] + ID_INTEGER_PRIMARY_KEY_AUTOINCREMENT
			+ V_USER 					+ _TEXT_NOT_NULL + _COMMA
			+ V_CURRENT 				+ _INT_NOT_NULL + _COMMA
			+ V_HIGHEST_RATING 	    	+ _INT_NOT_NULL + _COMMA
			+ V_HIGHEST_TIMESTAMP 	    + _LONG_NOT_NULL + _COMMA
			+ V_BEST_WIN_RATING 	    + _INT_NOT_NULL + _COMMA
			+ V_BEST_WIN_USERNAME 	    + _TEXT_NOT_NULL + _COMMA
			+ V_AVERAGE_OPPONENT 	    + _INT_NOT_NULL + _COMMA
			+ V_GAMES_TOTAL  	    	+ _INT_NOT_NULL + _COMMA
			+ V_GAMES_WINS   	    	+ _INT_NOT_NULL + _COMMA
			+ V_GAMES_LOSSES 	    	+ _INT_NOT_NULL + _COMMA
			+ V_GAMES_DRAWS  	    	+ _INT_NOT_NULL + _CLOSE;

	static final String USER_STATS_DAILY_CHESS_CREATE =
			CREATE_TABLE_IF_NOT_EXISTS + tablesArray[USER_STATS_DAILY_CHESS] + ID_INTEGER_PRIMARY_KEY_AUTOINCREMENT
			+ V_USER 					+ _TEXT_NOT_NULL + _COMMA
			+ V_CURRENT 				+ _INT_NOT_NULL + _COMMA
			+ V_HIGHEST_RATING 	    	+ _INT_NOT_NULL + _COMMA
			+ V_HIGHEST_TIMESTAMP 	    + _LONG_NOT_NULL + _COMMA
			+ V_BEST_WIN_RATING 	    + _INT_NOT_NULL + _COMMA
			+ V_BEST_WIN_GAME_ID 	    + _LONG_NOT_NULL + _COMMA
			+ V_BEST_WIN_USERNAME 	    + _TEXT_NOT_NULL + _COMMA
			+ V_AVERAGE_OPPONENT 	    + _INT_NOT_NULL + _COMMA
			+ V_RANK 	    			+ _TEXT_NOT_NULL + _COMMA
			+ V_TOTAL_PLAYER_COUNT 	    + _INT_NOT_NULL + _COMMA
			+ V_TIMEOUTS 	    		+ _TEXT_NOT_NULL + _COMMA
			+ V_TIME_PER_MOVE 	    	+ _LONG_NOT_NULL + _COMMA
			+ V_GAMES_TOTAL  	    	+ _INT_NOT_NULL + _COMMA
			+ V_GAMES_WINS   	    	+ _INT_NOT_NULL + _COMMA
			+ V_GAMES_LOSSES 	    	+ _INT_NOT_NULL + _COMMA
			+ V_GAMES_DRAWS  	    	+ _INT_NOT_NULL + _CLOSE;

	static final String USER_STATS_DAILY_CHESS960_CREATE =
			CREATE_TABLE_IF_NOT_EXISTS + tablesArray[USER_STATS_DAILY_CHESS960] + ID_INTEGER_PRIMARY_KEY_AUTOINCREMENT
			+ V_USER 					+ _TEXT_NOT_NULL + _COMMA
			+ V_CURRENT 				+ _INT_NOT_NULL + _COMMA
			+ V_HIGHEST_RATING 	    	+ _INT_NOT_NULL + _COMMA
			+ V_HIGHEST_TIMESTAMP 	    + _LONG_NOT_NULL + _COMMA
			+ V_BEST_WIN_RATING 	    + _INT_NOT_NULL + _COMMA
			+ V_BEST_WIN_GAME_ID 	    + _LONG_NOT_NULL + _COMMA
			+ V_BEST_WIN_USERNAME 	    + _TEXT_NOT_NULL + _COMMA
			+ V_AVERAGE_OPPONENT 	    + _INT_NOT_NULL + _COMMA
			+ V_RANK 	    			+ _TEXT_NOT_NULL + _COMMA
			+ V_TOTAL_PLAYER_COUNT 	    + _INT_NOT_NULL + _COMMA
			+ V_TIMEOUTS 	    		+ _TEXT_NOT_NULL + _COMMA
			+ V_TIME_PER_MOVE 	    	+ _LONG_NOT_NULL + _COMMA
			+ V_GAMES_TOTAL  	    	+ _INT_NOT_NULL + _COMMA
			+ V_GAMES_WINS   	    	+ _INT_NOT_NULL + _COMMA
			+ V_GAMES_LOSSES 	    	+ _INT_NOT_NULL + _COMMA
			+ V_GAMES_DRAWS  	    	+ _INT_NOT_NULL + _CLOSE;


	static final String USER_STATS_TACTICS_CREATE =
			CREATE_TABLE_IF_NOT_EXISTS + tablesArray[USER_STATS_TACTICS] + ID_INTEGER_PRIMARY_KEY_AUTOINCREMENT
			+ V_USER 					+ _TEXT_NOT_NULL + _COMMA
			+ V_CURRENT 				+ _INT_NOT_NULL + _COMMA
			+ V_HIGHEST_RATING 	    	+ _INT_NOT_NULL + _COMMA
			+ V_HIGHEST_TIMESTAMP 	    + _LONG_NOT_NULL + _COMMA
			+ V_LOWEST_RATING 	    	+ _INT_NOT_NULL + _COMMA
			+ V_LOWEST_TIMESTAMP 	    + _LONG_NOT_NULL + _COMMA
			+ V_ATTEMPT_COUNT  	    	+ _INT_NOT_NULL + _COMMA
			+ V_PASSED_COUNT   	    	+ _INT_NOT_NULL + _COMMA
			+ V_FAILED_COUNT 	    	+ _INT_NOT_NULL + _COMMA
			+ V_TODAYS_ATTEMPTS 		+ _INT_NOT_NULL + _COMMA
			+ V_TODAYS_AVG_SCORE	    + _INT_NOT_NULL + _COMMA
			+ V_TOTAL_SECONDS  	    	+ _INT_NOT_NULL + _CLOSE;

	static final String USER_STATS_CHESS_MENTOR_CREATE =
			CREATE_TABLE_IF_NOT_EXISTS + tablesArray[USER_STATS_LESSONS] + ID_INTEGER_PRIMARY_KEY_AUTOINCREMENT
			+ V_USER 					+ _TEXT_NOT_NULL + _COMMA
			+ V_CURRENT 				+ _INT_NOT_NULL + _COMMA
			+ V_HIGHEST_RATING 	    	+ _INT_NOT_NULL + _COMMA
			+ V_HIGHEST_TIMESTAMP 	    + _LONG_NOT_NULL + _COMMA
			+ V_LOWEST_RATING 	    	+ _INT_NOT_NULL + _COMMA
			+ V_LOWEST_TIMESTAMP 	    + _LONG_NOT_NULL + _COMMA
			+ V_LESSONS_TRIED  	    	+ _INT_NOT_NULL + _COMMA
			+ V_TOTAL_LESSON_COUNT   	    + _INT_NOT_NULL + _COMMA
			+ V_LESSON_COMPLETE_PERCENTAGE 	+ _TEXT_NOT_NULL + _COMMA
			+ V_TOTAL_TRAINING_SECONDS  	+ _LONG_NOT_NULL + _CLOSE;


	/* ==================== Game Stats ============================== */

	public static final String V_GAMES_WHITE 			= "games_white";
	public static final String V_GAMES_BLACK 			= "games_black";
	public static final String V_PERCENTILE 			= "percentile";
	public static final String V_GLICKO_RD 				= "glicko_rd";
	public static final String V_AVG_OPPONENT_RATING_WIN = "average_opponent_rating_when_i_win";
	public static final String V_AVG_OPPONENT_RATING_LOSE = "average_opponent_rating_when_i_lose";
	public static final String V_AVG_OPPONENT_RATING_DRAW = "average_opponent_rating_when_i_draw";
	public static final String V_UNRATED 				= "unrated";
	public static final String V_IN_PROGRESS 			= "in_progress";

	public static final String V_WINS_TOTAL 			= "wins_total";
	public static final String V_WINS_WHITE 			= "wins_white";
	public static final String V_WINS_BLACK 			= "wins_black";
	public static final String V_LOSSES_TOTAL 			= "losses_total";
	public static final String V_LOSSES_WHITE 			= "losses_white";
	public static final String V_LOSSES_BLACK 			= "losses_black";
	public static final String V_DRAWS_TOTAL 			= "draws_total";
	public static final String V_DRAWS_WHITE 			= "draws_white";
	public static final String V_DRAWS_BLACK 			= "draws_black";
	public static final String V_WINNING_STREAK 		= "winning_streak";
	public static final String V_LOSING_STREAK 			= "losing_streak";
	public static final String V_FREQUENT_OPPONENT_NAME 			= "frequent_opponent_name";
	public static final String V_FREQUENT_OPPONENT_GAMES_PLAYED 	= "frequent_opponent_games_played";

	public static final String V_TOURNAMENTS_LEADERBOARD_POINTS 	= "tournaments_leaderboard_points";
	public static final String V_TOURNAMENTS_EVENTS_ENTERED 		= "tournaments_events_entered";
	public static final String V_TOURNAMENTS_FIRST_PLACE_FINISHES 	= "tournaments_first_place_finishes";
	public static final String V_TOURNAMENTS_SECOND_PLACE_FINISHES 	= "tournaments_second_place_finishes";
	public static final String V_TOURNAMENTS_THIRD_PLACE_FINISHES 	= "tournaments_third_place_finishes";
	public static final String V_TOURNAMENTS_WITHDRAWALS 			= "tournaments_withdrawals";
	public static final String V_TOURNAMENTS_HOSTED 				= "tournaments_hosted";
	public static final String V_TOTAL_COUNT_PLAYERS_HOSTED 		= "total_count_players_hosted";

	public static final String V_TOURNAMENTS_GAMES_TOTAL 			= "tournaments_games_total";
	public static final String V_TOURNAMENTS_GAMES_WON 				= "tournaments_games_won";
	public static final String V_TOURNAMENTS_GAMES_LOST 			= "tournaments_games_lost";
	public static final String V_TOURNAMENTS_GAMES_DRAWN 			= "tournaments_games_drawn";
	public static final String V_TOURNAMENTS_GAMES_IN_PROGRESS 		= "tournaments_games_in_progress";

	static final String GAME_STATS_LIVE_STANDARD_CREATE =
			CREATE_TABLE_IF_NOT_EXISTS + tablesArray[GAME_STATS_LIVE_STANDARD] + ID_INTEGER_PRIMARY_KEY_AUTOINCREMENT
			+ V_USER 					+ _TEXT_NOT_NULL + _COMMA
			+ V_CURRENT 				+ _INT_NOT_NULL + _COMMA
			+ V_RANK 	    			+ _TEXT_NOT_NULL + _COMMA
			+ V_TOTAL_PLAYER_COUNT 	    + _INT_NOT_NULL + _COMMA
			+ V_PERCENTILE 	    		+ _TEXT_NOT_NULL + _COMMA
			+ V_GLICKO_RD 	    		+ _INT_NOT_NULL + _COMMA
			+ V_HIGHEST_RATING 	    	+ _INT_NOT_NULL + _COMMA
			+ V_HIGHEST_TIMESTAMP 	    + _LONG_NOT_NULL + _COMMA
			+ V_LOWEST_RATING 	    	+ _INT_NOT_NULL + _COMMA
			+ V_LOWEST_TIMESTAMP 	    + _LONG_NOT_NULL + _COMMA
			+ V_AVERAGE_OPPONENT 	    + _INT_NOT_NULL + _COMMA
			+ V_BEST_WIN_RATING 	    + _INT_NOT_NULL + _COMMA
			+ V_BEST_WIN_GAME_ID 	    + _LONG_NOT_NULL + _COMMA
			+ V_BEST_WIN_USERNAME 	    + _TEXT_NOT_NULL + _COMMA
			+ V_AVG_OPPONENT_RATING_WIN + _INT_NOT_NULL + _COMMA
			+ V_AVG_OPPONENT_RATING_LOSE + _INT_NOT_NULL + _COMMA
			+ V_AVG_OPPONENT_RATING_DRAW + _INT_NOT_NULL + _COMMA
			+ V_UNRATED  	    		+ _INT_NOT_NULL + _COMMA
			+ V_IN_PROGRESS  	    	+ _INT_NOT_NULL + _COMMA
			+ V_TIMEOUTS  		+ _TEXT_NOT_NULL + _COMMA
					/* Games */
			+ V_GAMES_TOTAL  	    	+ _INT_NOT_NULL + _COMMA
			+ V_GAMES_WHITE  	    	+ _INT_NOT_NULL + _COMMA
			+ V_GAMES_BLACK  	    	+ _INT_NOT_NULL + _COMMA
			+ V_WINS_TOTAL  	    	+ _INT_NOT_NULL + _COMMA
			+ V_WINS_WHITE  	    	+ _INT_NOT_NULL + _COMMA
			+ V_WINS_BLACK  	    	+ _INT_NOT_NULL + _COMMA
			+ V_LOSSES_TOTAL  	    	+ _INT_NOT_NULL + _COMMA
			+ V_LOSSES_WHITE  	    	+ _INT_NOT_NULL + _COMMA
			+ V_LOSSES_BLACK  	    	+ _INT_NOT_NULL + _COMMA
			+ V_DRAWS_TOTAL  	    	+ _INT_NOT_NULL + _COMMA
			+ V_DRAWS_WHITE  	    	+ _INT_NOT_NULL + _COMMA
			+ V_DRAWS_BLACK  	    	+ _INT_NOT_NULL + _COMMA
			+ V_WINNING_STREAK  	    + _INT_NOT_NULL + _COMMA
			+ V_LOSING_STREAK	  	    + _INT_NOT_NULL + _COMMA
			+ V_FREQUENT_OPPONENT_NAME  	    + _TEXT_NOT_NULL + _COMMA
			+ V_FREQUENT_OPPONENT_GAMES_PLAYED	+ _INT_NOT_NULL + _CLOSE;


	static final String GAME_STATS_LIVE_LIGHTNING_CREATE =
			CREATE_TABLE_IF_NOT_EXISTS + tablesArray[GAME_STATS_LIVE_LIGHTNING] + ID_INTEGER_PRIMARY_KEY_AUTOINCREMENT
			+ V_USER 					+ _TEXT_NOT_NULL + _COMMA
			+ V_CURRENT 				+ _INT_NOT_NULL + _COMMA
			+ V_RANK 	    			+ _TEXT_NOT_NULL + _COMMA
			+ V_TOTAL_PLAYER_COUNT 	    + _INT_NOT_NULL + _COMMA
			+ V_PERCENTILE 	    		+ _TEXT_NOT_NULL + _COMMA
			+ V_GLICKO_RD 	    		+ _INT_NOT_NULL + _COMMA
			+ V_HIGHEST_RATING 	    	+ _INT_NOT_NULL + _COMMA
			+ V_HIGHEST_TIMESTAMP 	    + _LONG_NOT_NULL + _COMMA
			+ V_LOWEST_RATING 	    	+ _INT_NOT_NULL + _COMMA
			+ V_LOWEST_TIMESTAMP 	    + _LONG_NOT_NULL + _COMMA
			+ V_AVERAGE_OPPONENT 	    + _INT_NOT_NULL + _COMMA
			+ V_BEST_WIN_RATING 	    + _INT_NOT_NULL + _COMMA
			+ V_BEST_WIN_GAME_ID 	    + _LONG_NOT_NULL + _COMMA
			+ V_BEST_WIN_USERNAME 	    + _TEXT_NOT_NULL + _COMMA
			+ V_AVG_OPPONENT_RATING_WIN + _INT_NOT_NULL + _COMMA
			+ V_AVG_OPPONENT_RATING_LOSE + _INT_NOT_NULL + _COMMA
			+ V_AVG_OPPONENT_RATING_DRAW + _INT_NOT_NULL + _COMMA
			+ V_UNRATED  	    		+ _INT_NOT_NULL + _COMMA
			+ V_IN_PROGRESS  	    	+ _INT_NOT_NULL + _COMMA
			+ V_TIMEOUTS  		+ _TEXT_NOT_NULL + _COMMA
					/* Games */
			+ V_GAMES_TOTAL  	    	+ _INT_NOT_NULL + _COMMA
			+ V_GAMES_WHITE  	    	+ _INT_NOT_NULL + _COMMA
			+ V_GAMES_BLACK  	    	+ _INT_NOT_NULL + _COMMA
			+ V_WINS_TOTAL  	    	+ _INT_NOT_NULL + _COMMA
			+ V_WINS_WHITE  	    	+ _INT_NOT_NULL + _COMMA
			+ V_WINS_BLACK  	    	+ _INT_NOT_NULL + _COMMA
			+ V_LOSSES_TOTAL  	    	+ _INT_NOT_NULL + _COMMA
			+ V_LOSSES_WHITE  	    	+ _INT_NOT_NULL + _COMMA
			+ V_LOSSES_BLACK  	    	+ _INT_NOT_NULL + _COMMA
			+ V_DRAWS_TOTAL  	    	+ _INT_NOT_NULL + _COMMA
			+ V_DRAWS_WHITE  	    	+ _INT_NOT_NULL + _COMMA
			+ V_DRAWS_BLACK  	    	+ _INT_NOT_NULL + _COMMA
			+ V_WINNING_STREAK  	    + _INT_NOT_NULL + _COMMA
			+ V_LOSING_STREAK  	    	+ _INT_NOT_NULL + _COMMA
			+ V_FREQUENT_OPPONENT_NAME  	    + _TEXT_NOT_NULL + _COMMA
			+ V_FREQUENT_OPPONENT_GAMES_PLAYED	+ _INT_NOT_NULL + _CLOSE;

	static final String GAME_STATS_LIVE_BLITZ_CREATE =
			CREATE_TABLE_IF_NOT_EXISTS + tablesArray[GAME_STATS_LIVE_BLITZ] + ID_INTEGER_PRIMARY_KEY_AUTOINCREMENT
			+ V_USER 					+ _TEXT_NOT_NULL + _COMMA
			+ V_CURRENT 				+ _INT_NOT_NULL + _COMMA
			+ V_RANK 	    			+ _TEXT_NOT_NULL + _COMMA
			+ V_TOTAL_PLAYER_COUNT 	    + _INT_NOT_NULL + _COMMA
			+ V_PERCENTILE 	    		+ _TEXT_NOT_NULL + _COMMA
			+ V_GLICKO_RD 	    		+ _INT_NOT_NULL + _COMMA
			+ V_HIGHEST_RATING 	    	+ _INT_NOT_NULL + _COMMA
			+ V_HIGHEST_TIMESTAMP 	    + _LONG_NOT_NULL + _COMMA
			+ V_LOWEST_RATING 	    	+ _INT_NOT_NULL + _COMMA
			+ V_LOWEST_TIMESTAMP 	    + _LONG_NOT_NULL + _COMMA
			+ V_AVERAGE_OPPONENT 	    + _INT_NOT_NULL + _COMMA
			+ V_BEST_WIN_RATING 	    + _INT_NOT_NULL + _COMMA
			+ V_BEST_WIN_GAME_ID 	    + _LONG_NOT_NULL + _COMMA
			+ V_BEST_WIN_USERNAME 	    + _TEXT_NOT_NULL + _COMMA
			+ V_AVG_OPPONENT_RATING_WIN + _INT_NOT_NULL + _COMMA
			+ V_AVG_OPPONENT_RATING_LOSE + _INT_NOT_NULL + _COMMA
			+ V_AVG_OPPONENT_RATING_DRAW + _INT_NOT_NULL + _COMMA
			+ V_UNRATED  	    		+ _INT_NOT_NULL + _COMMA
			+ V_IN_PROGRESS  	    	+ _INT_NOT_NULL + _COMMA
			+ V_TIMEOUTS  		+ _TEXT_NOT_NULL + _COMMA
					/* Games */
			+ V_GAMES_TOTAL  	    	+ _INT_NOT_NULL + _COMMA
			+ V_GAMES_WHITE  	    	+ _INT_NOT_NULL + _COMMA
			+ V_GAMES_BLACK  	    	+ _INT_NOT_NULL + _COMMA
			+ V_WINS_TOTAL  	    	+ _INT_NOT_NULL + _COMMA
			+ V_WINS_WHITE  	    	+ _INT_NOT_NULL + _COMMA
			+ V_WINS_BLACK  	    	+ _INT_NOT_NULL + _COMMA
			+ V_LOSSES_TOTAL  	    	+ _INT_NOT_NULL + _COMMA
			+ V_LOSSES_WHITE  	    	+ _INT_NOT_NULL + _COMMA
			+ V_LOSSES_BLACK  	    	+ _INT_NOT_NULL + _COMMA
			+ V_DRAWS_TOTAL  	    	+ _INT_NOT_NULL + _COMMA
			+ V_DRAWS_WHITE  	    	+ _INT_NOT_NULL + _COMMA
			+ V_DRAWS_BLACK  	    	+ _INT_NOT_NULL + _COMMA
			+ V_WINNING_STREAK  	    + _INT_NOT_NULL + _COMMA
			+ V_LOSING_STREAK  	    	+ _INT_NOT_NULL + _COMMA
			+ V_FREQUENT_OPPONENT_NAME  	    + _TEXT_NOT_NULL + _COMMA
			+ V_FREQUENT_OPPONENT_GAMES_PLAYED	+ _INT_NOT_NULL + _CLOSE;

	static final String GAME_STATS_DAILY_CHESS_CREATE =
			CREATE_TABLE_IF_NOT_EXISTS + tablesArray[GAME_STATS_DAILY_CHESS] + ID_INTEGER_PRIMARY_KEY_AUTOINCREMENT
			+ V_USER 					+ _TEXT_NOT_NULL + _COMMA
			+ V_CURRENT 				+ _INT_NOT_NULL + _COMMA
			+ V_RANK 	    			+ _TEXT_NOT_NULL + _COMMA
			+ V_TOTAL_PLAYER_COUNT 	    + _INT_NOT_NULL + _COMMA
			+ V_PERCENTILE 	    		+ _TEXT_NOT_NULL + _COMMA
			+ V_GLICKO_RD 	    		+ _INT_NOT_NULL + _COMMA
			+ V_HIGHEST_RATING 	    	+ _INT_NOT_NULL + _COMMA
			+ V_HIGHEST_TIMESTAMP 	    + _LONG_NOT_NULL + _COMMA
			+ V_LOWEST_RATING 	    	+ _INT_NOT_NULL + _COMMA
			+ V_LOWEST_TIMESTAMP 	    + _LONG_NOT_NULL + _COMMA
			+ V_AVERAGE_OPPONENT 	    + _INT_NOT_NULL + _COMMA
			+ V_BEST_WIN_RATING 	    + _INT_NOT_NULL + _COMMA
			+ V_BEST_WIN_GAME_ID 	    + _LONG_NOT_NULL + _COMMA
			+ V_BEST_WIN_USERNAME 	    + _TEXT_NOT_NULL + _COMMA
			+ V_AVG_OPPONENT_RATING_WIN + _INT_NOT_NULL + _COMMA
			+ V_AVG_OPPONENT_RATING_LOSE + _INT_NOT_NULL + _COMMA
			+ V_AVG_OPPONENT_RATING_DRAW + _INT_NOT_NULL + _COMMA
			+ V_UNRATED  	    		+ _INT_NOT_NULL + _COMMA
			+ V_IN_PROGRESS  	    	+ _INT_NOT_NULL + _COMMA
			+ V_TIMEOUTS  		+ _TEXT_NOT_NULL + _COMMA
			/* Games */
			+ V_GAMES_TOTAL  	    	+ _INT_NOT_NULL + _COMMA
			+ V_GAMES_WHITE  	    	+ _INT_NOT_NULL + _COMMA
			+ V_GAMES_BLACK  	    	+ _INT_NOT_NULL + _COMMA
			+ V_WINS_TOTAL  	    	+ _INT_NOT_NULL + _COMMA
			+ V_WINS_WHITE  	    	+ _INT_NOT_NULL + _COMMA
			+ V_WINS_BLACK  	    	+ _INT_NOT_NULL + _COMMA
			+ V_LOSSES_TOTAL  	    	+ _INT_NOT_NULL + _COMMA
			+ V_LOSSES_WHITE  	    	+ _INT_NOT_NULL + _COMMA
			+ V_LOSSES_BLACK  	    	+ _INT_NOT_NULL + _COMMA
			+ V_DRAWS_TOTAL  	    	+ _INT_NOT_NULL + _COMMA
			+ V_DRAWS_WHITE  	    	+ _INT_NOT_NULL + _COMMA
			+ V_DRAWS_BLACK  	    	+ _INT_NOT_NULL + _COMMA
			+ V_WINNING_STREAK  	    + _INT_NOT_NULL + _COMMA
			+ V_LOSING_STREAK  	    	+ _INT_NOT_NULL + _COMMA
			+ V_FREQUENT_OPPONENT_NAME  	    + _TEXT_NOT_NULL + _COMMA
			+ V_FREQUENT_OPPONENT_GAMES_PLAYED	+ _INT_NOT_NULL + _COMMA
			/* Tournaments */
			+ V_TOURNAMENTS_LEADERBOARD_POINTS  	    + _INT_NOT_NULL + _COMMA
			+ V_TOURNAMENTS_EVENTS_ENTERED  	    	+ _INT_NOT_NULL + _COMMA
			+ V_TOURNAMENTS_FIRST_PLACE_FINISHES  	    + _INT_NOT_NULL + _COMMA
			+ V_TOURNAMENTS_SECOND_PLACE_FINISHES  	    + _INT_NOT_NULL + _COMMA
			+ V_TOURNAMENTS_THIRD_PLACE_FINISHES  	    + _INT_NOT_NULL + _COMMA
			+ V_TOURNAMENTS_WITHDRAWALS  	    		+ _INT_NOT_NULL + _COMMA
			+ V_TOURNAMENTS_HOSTED  	    			+ _INT_NOT_NULL + _COMMA
			+ V_TOTAL_COUNT_PLAYERS_HOSTED  	    	+ _INT_NOT_NULL + _COMMA
			+ V_TOURNAMENTS_GAMES_TOTAL  	   			+ _INT_NOT_NULL + _COMMA
			+ V_TOURNAMENTS_GAMES_WON  	    			+ _INT_NOT_NULL + _COMMA
			+ V_TOURNAMENTS_GAMES_LOST  	    		+ _INT_NOT_NULL + _COMMA
			+ V_TOURNAMENTS_GAMES_DRAWN  	    		+ _INT_NOT_NULL + _COMMA
			+ V_TOURNAMENTS_GAMES_IN_PROGRESS  	    	+ _INT_NOT_NULL + _CLOSE;

	static final String GAME_STATS_DAILY_CHESS960_CREATE =
			CREATE_TABLE_IF_NOT_EXISTS + tablesArray[GAME_STATS_DAILY_CHESS960] + ID_INTEGER_PRIMARY_KEY_AUTOINCREMENT
			+ V_USER 					+ _TEXT_NOT_NULL + _COMMA
			+ V_CURRENT 				+ _INT_NOT_NULL + _COMMA
			+ V_RANK 	    			+ _TEXT_NOT_NULL + _COMMA
			+ V_TOTAL_PLAYER_COUNT 	    + _INT_NOT_NULL + _COMMA
			+ V_PERCENTILE 	    		+ _TEXT_NOT_NULL + _COMMA
			+ V_GLICKO_RD 	    		+ _INT_NOT_NULL + _COMMA
			+ V_HIGHEST_RATING 	    	+ _INT_NOT_NULL + _COMMA
			+ V_HIGHEST_TIMESTAMP 	    + _LONG_NOT_NULL + _COMMA
			+ V_LOWEST_RATING 	    	+ _INT_NOT_NULL + _COMMA
			+ V_LOWEST_TIMESTAMP 	    + _LONG_NOT_NULL + _COMMA
			+ V_AVERAGE_OPPONENT 	    + _INT_NOT_NULL + _COMMA
			+ V_BEST_WIN_RATING 	    + _INT_NOT_NULL + _COMMA
			+ V_BEST_WIN_GAME_ID 	    + _LONG_NOT_NULL + _COMMA
			+ V_BEST_WIN_USERNAME 	    + _TEXT_NOT_NULL + _COMMA
			+ V_AVG_OPPONENT_RATING_WIN + _INT_NOT_NULL + _COMMA
			+ V_AVG_OPPONENT_RATING_LOSE + _INT_NOT_NULL + _COMMA
			+ V_AVG_OPPONENT_RATING_DRAW + _INT_NOT_NULL + _COMMA
			+ V_UNRATED  	    		+ _INT_NOT_NULL + _COMMA
			+ V_IN_PROGRESS  	    	+ _INT_NOT_NULL + _COMMA
			+ V_TIMEOUTS  				+ _TEXT_NOT_NULL + _COMMA
			/* Games */
			+ V_GAMES_TOTAL  	    	+ _INT_NOT_NULL + _COMMA
			+ V_GAMES_WHITE  	    	+ _INT_NOT_NULL + _COMMA
			+ V_GAMES_BLACK  	    	+ _INT_NOT_NULL + _COMMA
			+ V_WINS_TOTAL  	    	+ _INT_NOT_NULL + _COMMA
			+ V_WINS_WHITE  	    	+ _INT_NOT_NULL + _COMMA
			+ V_WINS_BLACK  	    	+ _INT_NOT_NULL + _COMMA
			+ V_LOSSES_TOTAL  	    	+ _INT_NOT_NULL + _COMMA
			+ V_LOSSES_WHITE  	    	+ _INT_NOT_NULL + _COMMA
			+ V_LOSSES_BLACK  	    	+ _INT_NOT_NULL + _COMMA
			+ V_DRAWS_TOTAL  	    	+ _INT_NOT_NULL + _COMMA
			+ V_DRAWS_WHITE  	    	+ _INT_NOT_NULL + _COMMA
			+ V_DRAWS_BLACK  	    	+ _INT_NOT_NULL + _COMMA
			+ V_WINNING_STREAK  	    + _INT_NOT_NULL + _COMMA
			+ V_LOSING_STREAK  	    	+ _INT_NOT_NULL + _COMMA
			+ V_FREQUENT_OPPONENT_NAME  	    + _TEXT_NOT_NULL + _COMMA
			+ V_FREQUENT_OPPONENT_GAMES_PLAYED	+ _INT_NOT_NULL + _COMMA
			/* Tournaments */
			+ V_TOURNAMENTS_LEADERBOARD_POINTS  	    + _INT_NOT_NULL + _COMMA
			+ V_TOURNAMENTS_EVENTS_ENTERED  	    	+ _INT_NOT_NULL + _COMMA
			+ V_TOURNAMENTS_FIRST_PLACE_FINISHES  	    + _INT_NOT_NULL + _COMMA
			+ V_TOURNAMENTS_SECOND_PLACE_FINISHES  	    + _INT_NOT_NULL + _COMMA
			+ V_TOURNAMENTS_THIRD_PLACE_FINISHES  	    + _INT_NOT_NULL + _COMMA
			+ V_TOURNAMENTS_WITHDRAWALS  	    		+ _INT_NOT_NULL + _COMMA
			+ V_TOURNAMENTS_HOSTED  	    			+ _INT_NOT_NULL + _COMMA
			+ V_TOTAL_COUNT_PLAYERS_HOSTED  	    	+ _INT_NOT_NULL + _COMMA
			+ V_TOURNAMENTS_GAMES_TOTAL  	   			+ _INT_NOT_NULL + _COMMA
			+ V_TOURNAMENTS_GAMES_WON  	    			+ _INT_NOT_NULL + _COMMA
			+ V_TOURNAMENTS_GAMES_LOST  	    		+ _INT_NOT_NULL + _COMMA
			+ V_TOURNAMENTS_GAMES_DRAWN  	    		+ _INT_NOT_NULL + _COMMA
			+ V_TOURNAMENTS_GAMES_IN_PROGRESS  	    	+ _INT_NOT_NULL + _CLOSE;
}
