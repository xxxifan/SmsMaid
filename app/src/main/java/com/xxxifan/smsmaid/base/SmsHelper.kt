package com.xxxifan.smsmaid.base

import android.content.Context
import android.net.Uri
import android.telephony.SmsManager
import com.raizlabs.android.dbflow.sql.language.SQLite
import com.xxxifan.smsmaid.db.ProviderTable
import com.xxxifan.smsmaid.db.SmsTable
import java.util.*


/**
 * Created by BobPeng on 2017/2/9.
 */
class SmsHelper {
    companion object {
        private val INBOX = "content://sms/inbox"

        private val PROVIDER_START = "【"
        private val PROVIDER_END = "】"
        private val KEY_LAST_CHECK = "last_check"

        private val ID = "_id"
        private val THREAD_ID = "thread_id"
        private val ADDRESS = "address"
        private val PERSON = "person"
        private val BODY = "body"
        private val DATE = "date"
        private val PROTOCOL = "protocol"

        private val ID_INDEX = 0
        private val THREADID_INDEX = 1
        private val ADDRESS_INDEX = 2
        private val PERSON_INDEX = 3
        private val DATE_INDEX = 4
        private val BODY_INDEX = 12

        fun getSmsNoContacts(context: Context?): ArrayList<SmsTable> {
            if (context == null) {
                return ArrayList()
            }

            val lastCheck = AppPref.getLong(KEY_LAST_CHECK, 0)
            if (System.currentTimeMillis() - lastCheck > 24 * 60 * 60 * 1000) {
                val smsArray = ArrayList<SmsTable>()
                val projection = arrayOf(ID, THREAD_ID, ADDRESS, PERSON, DATE, PROTOCOL, BODY)

                context.contentResolver
                        .query(Uri.parse(INBOX), projection, "protocol=0 and person is NULL", null, "date desc")
                        .use { cur ->
                            while (cur.moveToNext()) {
                                val id = cur.getInt(ID_INDEX)
                                val tid = cur.getInt(THREADID_INDEX)
                                val address = cur.getString(cur.getColumnIndex(ADDRESS))
                                val person = cur.getInt(PERSON_INDEX)
                                val date = cur.getLong(DATE_INDEX)
                                val body = cur.getString(cur.getColumnIndex(BODY))

                                if (person <= 0) { // no contacts info
                                    // TODO: 2017/2/11 this is not the best way to determine a number is a contact
                                    val sms = SmsTable(id, tid, address, person, date, body)
                                    guessProvider(sms)
                                    smsArray.add(sms)
                                }
                            }
                        }
                AppPref.putLong(KEY_LAST_CHECK, System.currentTimeMillis())
                return smsArray
            } else {
                return getSmsList()
            }
        }

        private fun guessProvider(sms: SmsTable) {
            val body = sms.body
            val start = body.indexOf(PROVIDER_START)
            val end = body.indexOf(PROVIDER_END)
            if (start < 0 || start > 0 && end < body.length - 1) {
                return
            }

            body.substring(start + 1, end)
        }

        fun getSmsList(): ArrayList<SmsTable> {
            return SQLite.select()
                    .from(SmsTable::class.java)
                    .queryList() as ArrayList<SmsTable>
        }

        fun getProviderList(): ArrayList<ProviderTable> {
            return SQLite.select()
                    .from(ProviderTable::class.java)
                    .queryList() as ArrayList<ProviderTable>
        }

        fun getSmsProviderName(body: String): String? {
            val start = body.indexOf(PROVIDER_START)
            val end = body.indexOf(PROVIDER_END)
            if (start < 0 || start > 0 && end < body.length - 1) {
                return null
            }

            return body.substring(start + 1, end)
        }

        fun sendSMS(phoneNumber: String, message: String) {
            val smsManager = SmsManager.getDefault()
            val divideContents = smsManager.divideMessage(message)
            for (text in divideContents) {
                smsManager.sendTextMessage(phoneNumber, null, text, null, null)
            }

//            val SENT_SMS_ACTION = "SENT_SMS_ACTION"
//            val sentIntent = Intent(SENT_SMS_ACTION)
//            val sentPI = PendingIntent.getBroadcast(context, 0, sentIntent,
//                    0)
//            context.registerReceiver(object : BroadcastReceiver() {
//                override fun onReceive(_context: Context, _intent: Intent) {
//                    when (resultCode) {
//                        Activity.RESULT_OK -> Toast.makeText(context,
//                                "短信发送成功", Toast.LENGTH_SHORT)
//                                .show()
//                        SmsManager.RESULT_ERROR_GENERIC_FAILURE -> {
//                        }
//                        SmsManager.RESULT_ERROR_RADIO_OFF -> {
//                        }
//                        SmsManager.RESULT_ERROR_NULL_PDU -> {
//                        }
//                    }
//                }
//            }, IntentFilter(SENT_SMS_ACTION))
        }
    }
}