package com.xxxifan.smsmaid.ui.main

import android.Manifest.permission
import android.os.Bundle
import com.afollestad.materialdialogs.MaterialDialog
import com.tbruyelle.rxpermissions2.RxPermissions
import com.xxxifan.smsmaid.HomeFragment
import com.xxxifan.smsmaid.R
import com.xxxifan.smsmaid.base.BaseActivity
import com.xxxifan.smsmaid.base.Fragments
import com.xxxifan.smsmaid.db.SmsTable

class MainActivity : BaseActivity(), MainContract.View {
    override fun getSimpleName(): String = "MainActivity"

    val PERMISSIONS = arrayOf(
            permission.READ_SMS,
            permission.RECEIVE_SMS,
            permission.READ_CONTACTS,
            permission.SEND_SMS
    )

    lateinit var mPresenter: MainContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setPresenter(MainPresenter())
        mPresenter.setView(this)

        requestPermission()
        Fragments.checkout(this, HomeFragment.newInstance())
                .into(R.id.activity_main)
    }

    private fun requestPermission() {
        RxPermissions(this).request(*PERMISSIONS)
                .subscribe({ success ->
                    when {
                        success -> mPresenter.showSmsList()
                        else -> alertPermission()
                    }
                }, {e -> e.printStackTrace()})
    }

    private fun alertPermission() {
        MaterialDialog.Builder(getContext())
                .content("只有获取联系人/短信相关权限本应用才能正确执行，是否继续？")
                .positiveText(android.R.string.ok)
                .negativeText(android.R.string.cancel)
                .onPositive { _, _ -> requestPermission() }
                .onNegative { _, _ -> this@MainActivity.finish() }
                .build()
                .show()
    }

    override fun onShowSmsList(smsList: List<SmsTable>) {
        smsList.forEach { println(it.address) }
    }

    override fun setPresenter(presenter: MainContract.Presenter) {
        mPresenter = presenter
    }

}
