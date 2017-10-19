package com.hqj.universityfinance.utils;

import com.hqj.universityfinance.R;

/**
 * Created by wang on 17-9-13.
 */

public class ConfigUtils {

    public static final boolean DEBUG = true;

    public static final int DATABASE_VERSION = 6;
    public static final String DATABASE_NAME = "UniversityFinance.db";
    public static final String TABLE_STUDENT = "student_info";
    public static final String TABLE_PROJECT = "support_project";

    public static final String SUCCESSFUL = "{";
    public static final String ERROR = "error";
    public static final String SUCCESS = "success";

    public static final String SERVER_URL = "http://172.20.3.204:8080/WebConectTest/ServletTest";
    public static final String NEWS_JSON_URL =
            "http://172.20.3.204:8080/WebConectTest/json/banner_data.json";
    public static final String NOTICE_JSON_URL =
            "http://172.20.3.204:8080/WebConectTest/json/notice_data.json";
    public static final String NOTICE_LIST_JSON_URL =
            "http://172.20.3.204:8080/WebConectTest/json/notice_list_data.json";

    public final static int TYPE_NEWS = 1;
    public final static int TYPE_NOTICE = 2;
    public final static int TYPE_NOTICE_LIST = 3;

    public final static int TYPE_LOGIN_NET_ERROR = 3;
    public final static int TYPE_LOGIN_ACCOUNT_ERROR = 4;

    public final static String TYPE_POST_LOGIN = "L";
    public final static String TYPE_POST_GET_PROJECT = "P";

    public final static int ITEM_COUNT_X = 4;
    public final static int ITEM_COUNT_Y = 2;
    public final static int ITEMS_MAX_NUM = ITEM_COUNT_X * ITEM_COUNT_Y;

    public final static int CLEAR_BUTTON_RES = R.drawable.clear_all;
    public final static int VISIBLE_BUTTON_RES = R.drawable.visible3;
    public final static int INVISIBLE_BUTTON_RES = R.drawable.invisible3;

    public final static int[] iconIds = {
            R.drawable.icon_1,
            R.drawable.icon_2,
            R.drawable.icon_3,
            R.drawable.icon_4,
            R.drawable.icon_5,
            R.drawable.icon_6,
            R.drawable.icon_7,
            R.drawable.icon_8,
            R.drawable.icon_9,
            R.drawable.icon_10
    };

    public final static String[] projectIds = {
            "jxj01",
            "jxj1%",
            "jxj2%",
            "jxj3%",
            "jxj4%",
            "jxj51",
            "jxj6%",
            "jxj7%",
            "jxj81"
    };

}
