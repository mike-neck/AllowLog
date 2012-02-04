
package jp.group.android.atec.allowlog.model;

import jp.group.android.atec.allowlog.AllowanceDatabase;
import jp.group.android.atec.allowlog.exception.AppException;
import jp.group.android.atec.allowlog.util.AppDateUtil;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AllowanceLogData {

    public static void createRecord(Context context, String amountString) throws AppException {
        long amount = Long.parseLong(amountString);

        if ( amount <= 0 ) {
            throw new AppException();

        } else {
            SQLiteOpenHelper sqliteOpenHelper = new AllowanceDatabase(context);
            SQLiteDatabase database = sqliteOpenHelper.getWritableDatabase();

            try {
                database.beginTransaction();
                ContentValues contents = new ContentValues();
                contents.put("LOG_DATE", AppDateUtil.now());
                contents.put("AMOUNT", amount);

                database.insert("ALLOWANCE_LOG", null, contents);
                database.setTransactionSuccessful();

            } finally {
                database.endTransaction();
                database.close();
            }
        }
    }

}
