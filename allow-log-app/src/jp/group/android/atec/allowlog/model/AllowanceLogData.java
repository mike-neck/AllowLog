
package jp.group.android.atec.allowlog.model;

import java.util.ArrayList;
import java.util.List;

import jp.group.android.atec.allowlog.AllowanceDatabase;
import jp.group.android.atec.allowlog.HistoryActivity;
import jp.group.android.atec.allowlog.exception.AppException;
import jp.group.android.atec.allowlog.model.entity.AllowanceLog;
import jp.group.android.atec.allowlog.util.AppDateUtil;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AllowanceLogData {

    public static SQLiteDatabase writableDatabase(Context context) {
        SQLiteOpenHelper helper = getHelper(context);
        return helper.getWritableDatabase();
    }

    public static SQLiteDatabase readableDatabase(Context context) {
        SQLiteOpenHelper helper = getHelper(context);
        return helper.getReadableDatabase();
    }

    private static SQLiteOpenHelper getHelper(Context context) {
        return new AllowanceDatabase(context);
    }

    /**
     * 期間中の小遣い額の合計を取得する.
     * 
     * @param database
     *            - 読み込み可能Database.
     * @param from
     *            - 集計開始日時.
     * @param to
     *            - 集計終了日時.
     * @return - 小遣いの合計金額.
     */
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

    /**
     * 小遣い使用レコードを登録する.
     * 
     * @param database
     *            - 書き込み可能データベース.
     * @param amountString
     *            - 使用した小遣い金額{@code String}
     * @throws AppException
     *             - 小遣い金額が数値形式として不適切な場合.
     */
    public static void createRecord(SQLiteDatabase database, String amountString) throws AppException {
        long amount = Long.parseLong(amountString);

        if ( amount <= 0 ) {
            throw new AppException();
        }

        ContentValues values = convertContentValues(amount);

        database.insert("ALLOWANCE_LOG", null, values);
    }

    /**
     * 指定期間中の使用履歴を取得する.
     * 
     * @param database
     *            - 読み込み可能データベース.
     * @param to
     *            - 取得終了期間.
     * @return - {@code Cursor} - データベースカーソル.
     */
    public static List<AllowanceLog> searchHistory(SQLiteDatabase database, long to) {
        String[] columns = {
                AllowanceLog.LOG_DATE, AllowanceLog.AMOUNT
        };
        String condition = new StringBuilder().append(AllowanceLog.LOG_DATE).append("<= ?").toString();
        String[] condArgs = {
            Long.toString(to)
        };
        String order = new StringBuilder().append(AllowanceLog.LOG_DATE).append(" DESC").toString();
        Cursor cursor = database.query(AllowanceLog.TABLE, columns, condition, condArgs, null, null, order,
                Integer.toString(HistoryActivity.MAX_PAGE_SIZE));

        List<AllowanceLog> logs = new ArrayList<AllowanceLog>();
        while (cursor.moveToNext()) {
            AllowanceLog allowanceLog = new AllowanceLog();
            int index = cursor.getColumnIndex(AllowanceLog.LOG_DATE);
            long logDate = cursor.getLong(index);
            allowanceLog.setLogDateInLong(logDate);

            index = cursor.getColumnIndex(AllowanceLog.AMOUNT);
            long amount = cursor.getLong(index);
            allowanceLog.setAmount(amount);

            logs.add(allowanceLog);
        }

        return logs;
    }

    /**
     * データ登録用の{@code ContentValues}をPOJO({@code AllowanceLog})から生成する.
     * 
     * @param allowanceLog
     *            - データ {@link AllowanceLog} 形式
     * @return - {@code ContentValues}形式のデータ
     */
    public static ContentValues convertContentValue(AllowanceLog allowanceLog) {
        ContentValues values = new ContentValues();
        values.put(AllowanceLog.LOG_DATE, allowanceLog.getLogDateAsLong());
        values.put(AllowanceLog.AMOUNT, allowanceLog.getAmount());
        return values;
    }

    public static ContentValues convertContentValues(long amount) {
        ContentValues values = new ContentValues();
        values.put(AllowanceLog.LOG_DATE, AppDateUtil.now());
        values.put(AllowanceLog.AMOUNT, amount);
        return values;
    }
}
