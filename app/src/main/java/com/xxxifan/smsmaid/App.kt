package com.xxxifan.smsmaid

import android.app.Application
import com.raizlabs.android.dbflow.config.DatabaseConfig
import com.raizlabs.android.dbflow.config.FlowConfig
import com.raizlabs.android.dbflow.config.FlowManager


/**
 * Created by xifan on 2017/1/2.
 */
class App : Application() {
    companion object {
        private var app: App? = null

        fun get(): App {
            return app!!
        }
    }

    override fun onCreate() {
        super.onCreate()
        App.app = this

        initDB()
    }

    private fun initDB() {
        val dbConfig = DatabaseConfig.Builder(AppDatabase::class.java)
                .openHelper(::SQLCipher)
                .build()
        FlowManager.init(FlowConfig.Builder(this)
                .addDatabaseConfig(dbConfig)
                .build())
    }
}