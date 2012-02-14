package jp.group.android.atec.allowlog.util;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import jp.group.android.atec.allowlog.AllowanceDatabase;
import jp.group.android.atec.allowlog.model.AllowanceLogData;
import jp.group.android.atec.allowlog.model.entity.AllowanceLog;

import java.util.Date;
import java.util.List;

/**
 * @author: mike
 * @since: 12/02/12
 */
public class DatabaseUtil {

    /**
     * テーブル名.
     */
    private static final String TABLE = "ALLOWANCE_LOG";

    /**
     * 日付カラム.
     */
    private static final String LOG_DATE = "LOG_DATE";

    /**
     * 金額カラム.
    */
    private static final String AMOUNT = "AMOUNT";

    /**
     * 書き込み可能のデータベースを{@code android.content.Context}から取得します.
     * @param context - 起動中のアプリケーション{@code android.content.Context}
     * @return - 書き込み可能{@code android.database.sqlite.SQLiteDatabase}
     */
    public static SQLiteDatabase getWritableDatabase(Context context) {
        SQLiteOpenHelper openHelper = new AllowanceDatabase(context);
        return openHelper.getWritableDatabase();
    }

    /**
     * 読み込み可能データベースを{@code android.content.Context}から取得します.
     * @param context - 起動中のアプリケーション{@code android.content.Context}
     * @return - 書き込み可能{@code android.database.sqlite.SQLiteDatabase}
     */
    public static SQLiteDatabase getReadableDatabase(Context context) {
        SQLiteOpenHelper openHelper = new AllowanceDatabase(context);
        return openHelper.getReadableDatabase();
    }

    /**
     * データベースのレコードを全件削除します.
     * @param database - 書き込み可能{@code android.database.sqlite.SQLiteDatabase}
     */
    public static void cleanUpDatabase(SQLiteDatabase database) {
        database.beginTransaction();
        database.delete(TABLE, null, null);
        database.setTransactionSuccessful();
        database.endTransaction();

        database.close();
    }

    /**
     * テスト用にデータベースへレコードを追加します.
     * @param context - 起動中のアプリケーション{@code android.content.Context}
     * @param records - 登録する小遣い金額 {@code java.util.List&lt;java.lang.Long&gt;}
     */
    static public void createAllowanceDataFromLong(Context context, List<Long> records) {
        SQLiteDatabase database = DatabaseUtil.getWritableDatabase(context);
        try {
            database.beginTransaction();
            for (long item : records) {
                ContentValues values = AllowanceLogData.convertContentValues(item);
                database.insert(TABLE, null, values);
            }
            database.setTransactionSuccessful();
            database.endTransaction();
        } finally {
            database.close();
        }
    }

    /**
     * テスト用にデータベースへレコードを追加します.
     * @param context - 起動中のアプリケーション{@code androi.content.Context}
     * @param logs - 登録する小遣い金額 {@code java.util.List&lt;AllowanceLog&gt;}
     */
    static public void createAllowanceDataFromEntity(Context context, List<AllowanceLog> logs) {
        SQLiteDatabase database = DatabaseUtil.getWritableDatabase(context);
        try {
            database.beginTransaction();
            for (AllowanceLog item : logs) {
                ContentValues values = AllowanceLogData.convertContentValue(item);
                database.insert(TABLE, null, values);
            }
            database.setTransactionSuccessful();
            database.endTransaction();
        } finally {
            database.close();
        }
    }
}
