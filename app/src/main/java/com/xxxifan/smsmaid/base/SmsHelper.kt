package com.xxxifan.smsmaid.base

import android.content.Context
import android.net.Uri

/**
 * Created by BobPeng on 2017/2/9.
 */
class SmsHelper {
    companion object {
        const val INBOX = "content://sms/inbox"
        const val ADDRESS = "address"
        const val PERSON = "person"
        const val BODY = "body"
        const val DATE = "date"
        const val PROTOCOL = "protocol"
        const val PROVIDER_START = "【"
        const val PROVIDER_END = "】"
        fun getSmsNoContacts(context: Context) {
            val uri = Uri.parse(INBOX)
            val projection = arrayOf(ADDRESS, PERSON, BODY, DATE, PROTOCOL)
            val cur = context.contentResolver.query(uri, projection, "protocol=0", null, "date desc")
            while (cur.moveToNext()) {
                val addressIndex = cur.getColumnIndex(ADDRESS)
                val personIndex = cur.getColumnIndex(PERSON)
                val bodyIndex = cur.getColumnIndex(BODY)

                val address = cur.getString(addressIndex)
                val person = cur.getInt(personIndex)
                val body = cur.getString(bodyIndex)
            }
            cur.close()
        }

        fun getSmsProviderNameFromBody(body: String):String? {
            val start = body.indexOf(PROVIDER_START)
            val end = body.indexOf(PROVIDER_END)
            if (start > 0 && end < body.length - 1) {
                return null
            }
            return body.substring(start + 1, end)
        }
    }
}