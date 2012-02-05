
package jp.group.android.atec.allowlog.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import jp.group.android.atec.allowlog.exception.AppException;
import jp.group.android.atec.allowlog.util.AppDateUtil;

public class AllowanceLogData {

    public static long getTotalAmount(SQLiteDatabase database, long from, long to) {
        Cursor cursor = database.rawQuery("SELECT SUM(AMOUNT) FROM ALLOWANCE_LOG WHERE LOG_DATE BETWEEN ? AND ?",
                new String[] {
                        String.valueOf(from), String.valueOf(to)
                });
        if ( cursor.moveToFirst() ) {
            int count = cursor.getCount();
            if ( count > 0 ) {
                long totalValue = cursor.getLong(0);
                return totalValue;
            }
        }

        return 0;
    }

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
