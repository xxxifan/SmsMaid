package com.xxxifan.smsmaid

import com.raizlabs.android.dbflow.annotation.Database

/**
 * Created by BobPeng on 2017/2/10.
 */
@Database(name = AppDatabase.NAME, version = AppDatabase.VERSION)
class AppDatabase {
    companion object {
        const val NAME = "AppDatabase"
        const val VERSION = 1
    }
}
