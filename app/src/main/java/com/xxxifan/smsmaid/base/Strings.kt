/*
 * Copyright(c) 2016 xxxifan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xxxifan.smsmaid.base

import android.util.Base64
import com.xxxifan.smsmaid.App
import com.xxxifan.smsmaid.R
import java.nio.charset.Charset
import java.security.MessageDigest
import java.util.regex.Pattern

/**
 * Created by xifan on 6/8/16.
 */
class Strings private constructor() {
    companion object {
        val EMPTY = ""

        private val PHONE_PATTERN = Pattern.compile("^(1(3|4|5|7|8))\\d{9}$")
        private val TIME_VALUE = intArrayOf(1, //s
                60, //m
                60 * 60, //h
                24 * 60 * 60)//d
        private var sTimeUnits: Array<String>? = null

        fun isEmpty(str: CharSequence?): Boolean {
            return str == null || str.length == 0 || str.toString().trim { it <= ' ' }.isEmpty()
        }

        /**
         * @return true if every item is empty
         */
        fun isEmpty(vararg str: CharSequence?): Boolean {
            var i = 0
            val s = str.size
            while (i < s) {
                if (!isEmpty(str[i])) {
                    return false
                }
                i++
            }
            return true
        }

        /**
         * Parse timestamp to a down count timer like '34 hours ago'

         * @param timestamp specified time
         * *
         * @param unitLimit displayed unit limit, normally it will return full units of time like
         * *                  3 days 2 hours 34 minutes 23 seconds, this will limit to largest unit,
         * *                  if you passing 2, then it will return only 3 days 2 hours, negative number for no limits.
         * *
         * @return parsed time format like '3 days 2 hours 34 minutes 23 seconds'
         */
        fun downTimer(timestamp: Long, unitLimit: Int): String {
            if (sTimeUnits == null) {
                sTimeUnits = App.get().resources.getStringArray(R.array.time_unit)
            }
            var value = EMPTY
            var interval = (System.currentTimeMillis() - timestamp) / 1000 // start with seconds
            if (interval < 0) {
                interval = -interval
            }

            var count = 0
            for (i in sTimeUnits!!.indices.reversed()) {
                if (count == unitLimit) { // limit reached
                    break
                }

                val result = interval / TIME_VALUE[i]
                if (result > 0) {
                    value += result.toString() + sTimeUnits!![i]
                    interval %= TIME_VALUE[i].toLong()
                    count++
                }
            }
            return value
        }

        fun escapeExprSpecialWord(keyword: String): String {
            var newKeyword = keyword
            val fbsArr = arrayOf("\\", "$", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}", "|")
            fbsArr.filter { newKeyword.contains(it) }
                    .forEach { newKeyword = newKeyword.replace(it, "\\" + it) }
            return newKeyword
        }

        /**
         * @return true if every item is NOT empty
         */
        fun hasEmpty(vararg str: CharSequence): Boolean {
            var i = 0
            val s = str.size
            while (i < s) {
                if (isEmpty(str[i])) {
                    return true
                }
                i++
            }
            return false
        }

        fun contains(source: String, key: String): Boolean {
            return !isEmpty(source) && source.contains(key)
        }

        fun equals(source: String, key: String?): Boolean {
            return !isEmpty(source) && source == key
        }

        /**
         * Check is phone number
         */
        fun isPhoneNum(phone: String): Boolean {
            if (isEmpty(phone)) {
                return false
            }
            val m = PHONE_PATTERN.matcher(phone)
            return m.matches()
        }

        fun encodeMD5(string: String): String {
            try {
                val hash = MessageDigest.getInstance("MD5").digest(string.toByteArray(charset("UTF-8")))

                val hex = StringBuilder(hash.size * 2)
                for (b in hash) {
                    if (b.toInt() and 0xFF < 0x10) hex.append("0")
                    hex.append(Integer.toHexString(b.toInt() and 0xFF))
                }
                return hex.toString()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return EMPTY
        }

        fun encodeBase64(encodeStr: String): String {
            return encodeBase64(encodeStr.toByteArray())
        }

        fun encodeBase64(data: ByteArray): String {
            return Base64.encodeToString(data, Base64.NO_WRAP)
        }

        fun decodeBase64(decodeStr: String): ByteArray {
            return Base64.decode(decodeStr, Base64.NO_WRAP)
        }

        fun decodeBase64ToString(decodeStr: String): String {
            return String(decodeBase64(decodeStr), Charset.forName("utf-8"))
        }


        fun encodeSHA1ToString(string: String): String {
            try {
                val digest = MessageDigest.getInstance("SHA-1")
                val hash = digest.digest(string.toByteArray(charset("UTF-8")))
                val hex = StringBuilder(hash.size * 2)
                for (b in hash) {
                    if (b.toInt() and 0xFF < 0x10) hex.append("0")
                    hex.append(Integer.toHexString(b.toInt() and 0xFF))
                }
                return hex.toString()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return EMPTY
        }

        fun encodeSHA1(string: String): ByteArray {
            try {
                val digest = MessageDigest.getInstance("SHA-1")
                return digest.digest(string.toByteArray(charset("UTF-8")))
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return ByteArray(0)
        }
    }
}
