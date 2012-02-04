package jp.group.android.atec.allowlog;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;

/**
 * {@link RegisterActivity}のテスト<br/>
 * {@link MainActivity}にて100円と入力した状態で起動される<br/>
 * 下記の挙動をテストする。<br/>
 *
 * <ul style="list-style-type : numeric;">
 *     <li>登録ボタンを押した時 -&gt; 使用金額100円のレコードが登録される</li>
 *     <li>キャンセルボタンを押した時 -&gt; レコードが登録されない</li>
 * </ul>
 * @author mike_neck
 */
public class RegisterActivityTest extends ActivityInstrumentationTestCase2<RegisterActivity> {

    private static final Class<RegisterActivity> CLASS = RegisterActivity.class;

    private Activity activity;

    private Instrumentation instrumentation;

    private static final String TABLE = "ALLOWANCE_LOG";

    private static final String AMOUNT = "AMOUNT";

    private static final String SORT_ORDER = "LOG_DATE DESC";

    public RegisterActivityTest() {
        super(CLASS);
    }

    /**
     * 使用金額100円を入力した後に起動された{@link RegisterActivity}をテストする.
     *
     * @throws Exception
     */
    @Override
    public void setUp() throws Exception {
        super.setUp();

        Intent intent = new Intent();
        intent.putExtra("ALLOWANCE_PAYMENT", "100");
        setActivityIntent(intent);

        activity = getActivity();
        instrumentation = getInstrumentation();
        setActivityInitialTouchMode(false);

        SQLiteOpenHelper sqliteOpenHelper = getAllowanceDatabase();
        SQLiteDatabase database = sqliteOpenHelper.getWritableDatabase();

        database.beginTransaction();
        database.delete(TABLE, null, null);
        database.setTransactionSuccessful();
        database.endTransaction();
        database.close();
    }

    /**
     * 登録ボタンをクリックした時の挙動に関するテスト.<br/>
     *
     * 登録されるレコードの件数 : 1件 <br/>
     * 登録されたレコードのamount : 100
     */
    public void test使用金額１００円を登録する () {
        SQLiteOpenHelper sqliteOpenHelper = getAllowanceDatabase();
        SQLiteDatabase database = sqliteOpenHelper.getReadableDatabase();

        Cursor cursor = countAllowanceLogs(database);
        assertEquals(0, cursor.getInt(0));

        cursor.close();
        database.close();

        int registerButtonId = RegisterActivity.Controller.REGISTER.getHoldingId();
        final Button registerButton = (Button) activity.findViewById(registerButtonId);
        assertEquals("登録", registerButton.getText());

        activity.runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        registerButton.performClick();
                    }
                }
        );

        sqliteOpenHelper = getAllowanceDatabase();
        database = sqliteOpenHelper.getReadableDatabase();

        cursor = countAllowanceLogs(database);
        assertEquals(1, cursor.getInt(0));
        cursor.close();

        cursor = selectAllFromAllowanceLog(database);

        assertTrue(cursor.getCount() > 0);

        int index = cursor.getColumnIndex(AMOUNT);
        long amountValue = cursor.getLong(index);
        assertEquals(100L, amountValue);

        cursor.close();
        database.close();
    }

    /**
     * キャンセルボタンを押した時の挙動に関するテスト.
     */
    public void testキャンセルボタンを押す () {
        SQLiteOpenHelper sqliteOpenHelper = getAllowanceDatabase();
        SQLiteDatabase database = sqliteOpenHelper.getReadableDatabase();

        Cursor cursor = countAllowanceLogs(database);
        assertEquals(0, cursor.getInt(0));

        cursor.close();
        database.close();

        int cancelButtonId = RegisterActivity.Controller.CANCEL.getHoldingId();
        final Button cancelButton = (Button) activity.findViewById(cancelButtonId);
        assertEquals("キャンセル", cancelButton.getText());

        activity.runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        cancelButton.performClick();
                    }
                }
        );

        sqliteOpenHelper = getAllowanceDatabase();
        database = sqliteOpenHelper.getReadableDatabase();

        cursor = countAllowanceLogs(database);
        assertEquals(0, cursor.getInt(0));

        cursor.close();
        database.close();
    }

    @Override
    public void tearDown () throws Exception {
        super.tearDown();
    }
    /**
     * データを全件を取得する.
     *
     * @param database
     * @return
     */
    private Cursor selectAllFromAllowanceLog(SQLiteDatabase database) {
        Cursor cursor = database.rawQuery("SELECT AMOUNT FROM ALLOWANCE_LOG;", null);
        manageCursor(cursor);
        return cursor;
    }

    /**
     * データ件数を取得する.
     *
     * @param database
     * @return
     */
    private Cursor countAllowanceLogs(SQLiteDatabase database) {
        Cursor cursor = database.rawQuery("SELECT COUNT(*) AS CNT FROM ALLOWANCE_LOG;", null);
        manageCursor(cursor);
        return cursor;
    }

    /**
     * カーソル管理.
     *
     * @param cursor
     */
    private void manageCursor(Cursor cursor) {
        activity.startManagingCursor(cursor);
        cursor.moveToFirst();
    }

    /**
     * activityから{@link jp.group.android.atec.allowlog.AllowanceDatabase}を取得する.
     *
     * @return
     */
    private SQLiteOpenHelper getAllowanceDatabase() {
        return new AllowanceDatabase(activity);
    }
}
