package com.xxxifan.smsmaid.db

import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.structure.BaseModel
import com.xxxifan.smsmaid.AppDatabase
import com.xxxifan.smsmaid.base.Strings.Companion.EMPTY

/**
 * Created by BobPeng on 2017/2/10.
 */
@Table(database = AppDatabase::class)
class SmsTable() : BaseModel() {
    constructor(id: Int, threadId: Int, address: String, person: Int, date: Long, body: String) : this() {
        this.id = id
        this.threadId = threadId
        this.address = address
        this.person = person
        this.date = date
        this.body = body
    }

    @PrimaryKey(quickCheckAutoIncrement = true) var id: Int = 0
    @Column var threadId: Int = 0
    @Column var address: String = EMPTY
    @Column var person: Int = 0
    @Column var date: Long = 0L
    @Column var body: String = EMPTY
    @Column var providerName: String? = ""
}