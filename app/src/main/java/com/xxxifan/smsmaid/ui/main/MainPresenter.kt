package com.xxxifan.smsmaid.ui.main

import com.xxxifan.smsmaid.base.SmsHelper

/**
 * Created by BobPeng on 2017/2/13.
 */
class MainPresenter : MainContract.Presenter {
    var mView: MainContract.View? = null

    override fun showSmsList() {
       mView?.onShowSmsList(SmsHelper.getSmsNoContacts(mView?.getContext()))
//
//        Observable.from(providers)
//                .filter { provider -> sms.any { it.providerName?.equals(provider)!! } }
//                .map {
//                    {
//                        val providerName = SmsHelper.getSmsProviderName(it.body)
//                    }
//                }
    }

    override fun setView(view: MainContract.View?) {
        mView = view
    }
}