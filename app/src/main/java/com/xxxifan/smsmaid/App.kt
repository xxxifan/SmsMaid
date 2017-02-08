package com.xxxifan.smsmaid

import android.app.Application

/**
 * Created by xifan on 2017/1/2.
 */
class App :Application() {
    companion object {
        private var app: App? = null

        fun get():App {
            return app!!
        }
    }

    override fun onCreate() {
        super.onCreate()
        App.app = this
    }
}