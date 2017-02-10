package com.xxxifan.smsmaid.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsMessage

/**
 * Created by BobPeng on 2017/2/10.
 */
class SmsReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val pdus = intent?.extras?.get("pdus") as Array<*>
        for (p in pdus) {
            val sms = p as ByteArray
            val message = SmsMessage.createFromPdu(sms)

            val content = message.messageBody
            val number = message.originatingAddress
        }
    }
}
