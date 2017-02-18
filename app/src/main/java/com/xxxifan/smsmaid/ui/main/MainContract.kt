package com.xxxifan.smsmaid.ui.main

import com.xxxifan.smsmaid.base.BasePresenter
import com.xxxifan.smsmaid.base.BaseView
import com.xxxifan.smsmaid.db.SmsTable

/**
 * Created by BobPeng on 2017/2/13.
 */
interface MainContract {
    interface Presenter : BasePresenter<View> {
        fun showSmsList()
    }

    interface View : BaseView<Presenter> {
        fun onShowSmsList(smsList: List<SmsTable>)
    }

}