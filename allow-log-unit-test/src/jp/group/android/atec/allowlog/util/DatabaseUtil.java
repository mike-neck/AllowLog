package jp.group.android.atec.allowlog.util;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import jp.group.android.atec.allowlog.AllowanceDatabase;

/**
 * @author: mike
 * @since: 12/02/12
 */
public class DatabaseUtil {

    private static final String TABLE = "ALLOWANCE_LOG";

    public static SQLiteDatabase getWritableDatabase(Context context) {
        SQLiteOpenHelper openHelper = new AllowanceDatabase(context);
        return openHelper.getWritableDatabase();
    }

    public static SQLiteDatabase getReadableDatabase(Context context) {
        SQLiteOpenHelper openHelper = new AllowanceDatabase(context);
        return openHelper.getReadableDatabase();
    }

    public static void cleanUpDatabase(SQLiteDatabase database) {
        database.beginTransaction();
        database.delete(TABLE, null, null);
        database.setTransactionSuccessful();
        database.endTransaction();

        database.close();
    }
}
