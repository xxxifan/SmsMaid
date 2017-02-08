package com.xxxifan.smsmaid

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.xxxifan.smsmaid.base.Fragments

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Fragments.checkout(this, HomeFragment.newInstance())
                .into(R.id.activity_main)
    }
}
