package com.xxxifan.smsmaid.base

import android.content.Context
import android.widget.Toast

/**
 * Created by BobPeng on 2017/2/13.
 */
interface BaseView<in T> {
    fun setPresenter(presenter: T)

    fun getContext(): Context

    fun showMessage(msg: String) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show()
    }
}
