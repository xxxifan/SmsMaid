package com.xxxifan.smsmaid.db;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.xxxifan.smsmaid.AppDatabase;

/**
 * Created by BobPeng on 2017/2/13.
 */
@Table(database = AppDatabase.class)
public class ProviderTable extends BaseModel {
    @PrimaryKey(autoincrement = true, quickCheckAutoIncrement = true)
    public int id = 0;
    @Column
    public String providerName;
    @Column
    public long lastMsgTime;
}
