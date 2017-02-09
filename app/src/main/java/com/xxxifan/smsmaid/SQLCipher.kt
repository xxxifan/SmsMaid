package com.xxxifan.smsmaid

import com.raizlabs.android.dbflow.config.DatabaseDefinition
import com.raizlabs.android.dbflow.structure.database.DatabaseHelperListener
import com.raizlabs.dbflow.android.sqlcipher.SQLCipherOpenHelper

/**
 * Created by BobPeng on 2017/2/10.
 */
class SQLCipher(database: DatabaseDefinition, listener: DatabaseHelperListener) : SQLCipherOpenHelper(database, listener) {

    override fun getCipherSecret(): String = "xxxifan"

}