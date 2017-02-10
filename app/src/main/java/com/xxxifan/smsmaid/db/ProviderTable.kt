package com.xxxifan.smsmaid.db

import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.annotation.Unique
import com.raizlabs.android.dbflow.structure.BaseModel
import com.xxxifan.smsmaid.AppDatabase
import com.xxxifan.smsmaid.base.Strings.Companion.EMPTY

/**
 * Created by BobPeng on 2017/2/10.
 */
@Table(database = AppDatabase::class)
class ProviderTable : BaseModel() {
    @PrimaryKey(autoincrement = true) var id: Int = 0
    @Column var providerName: String? = EMPTY
    @Column @Unique var address: String? = EMPTY

}