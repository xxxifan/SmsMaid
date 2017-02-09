package com.xxxifan.smsmaid

import android.Manifest
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.tbruyelle.rxpermissions.RxPermissions
import com.xxxifan.smsmaid.base.Fragments
import com.xxxifan.smsmaid.base.SmsHelper
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class MainActivity : AppCompatActivity() {
    val PERMISSIONS = arrayOf(
            Manifest.permission.READ_SMS,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_CONTACTS
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestPermission()
        Fragments.checkout(this, HomeFragment.newInstance())
                .into(R.id.activity_main)

        Observable.just(null)
                .map { SmsHelper.getSmsNoContacts(this) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }

    private fun requestPermission() {
        RxPermissions(this).request(*PERMISSIONS)
                .subscribe({
                    if (!it)
                        MaterialDialog.Builder(this)
                                .content("只有获取联系人/短信相关权限本应用才能正确执行，是否继续？")
                                .positiveText(android.R.string.ok)
                                .negativeText(android.R.string.cancel)
                                .onPositive { materialDialog, dialogAction -> requestPermission() }
                                .onNegative { materialDialog, dialogAction -> this@MainActivity.finish() }
                                .build()
                                .show()
                })
    }
}
