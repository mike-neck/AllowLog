
package jp.group.android.atec.allowlog.model;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import jp.group.android.atec.allowlog.exception.AppException;
import jp.group.android.atec.allowlog.util.AppDateUtil;

public class AllowanceLogData {

    public static void createRecord(SQLiteDatabase database, String amountString) throws AppException {
        long amount = Long.parseLong(amountString);

        if ( amount <= 0 ) {
            throw new AppException();
        }

        ContentValues contents = new ContentValues();
        contents.put("LOG_DATE", AppDateUtil.now());
        contents.put("AMOUNT", amount);

        database.insert("ALLOWANCE_LOG", null, contents);
    }

}
