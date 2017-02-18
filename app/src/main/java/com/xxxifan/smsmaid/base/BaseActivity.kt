package com.xxxifan.smsmaid.base

import android.content.Context
import android.support.v7.app.AppCompatActivity

/**
 * Created by BobPeng on 2017/2/13.
 */
abstract class BaseActivity : AppCompatActivity() {
    abstract fun getSimpleName(): String
    fun getContext(): Context = this
}