package com.xxxifan.smsmaid;

import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.sqlcipher.SQLCipherOpenHelper;
import com.raizlabs.android.dbflow.structure.database.DatabaseHelperListener;

/**
 * Created by BobPeng on 2017/2/18.
 */

public class SQLCipher extends SQLCipherOpenHelper {
    public SQLCipher(DatabaseDefinition databaseDefinition, DatabaseHelperListener listener) {
        super(databaseDefinition, listener);
    }

    @Override
    protected String getCipherSecret() {
        return "xxxifan";
    }
}
