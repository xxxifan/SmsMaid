package com.xxxifan.smsmaid;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by BobPeng on 2017/2/13.
 */
@Database(name = AppDatabase.NAME, version = AppDatabase.VERSION)
public class AppDatabase {
    public static final String NAME = "AppDatabase";
    public static final int VERSION = 1;
}
