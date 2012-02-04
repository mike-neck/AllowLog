
package jp.group.android.atec.allowlog;

import android.app.Activity;
import android.app.Instrumentation;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 初回起動時のViewに関するテスト<br/>
 *
 * <ul>
 *     <li>使用金額が０円</li>
 *     <li>今月使用された金額が０円</li>
 * </ul>
 * @author mike_neck
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private static final Class<MainActivity> CLASS = MainActivity.class;

    private Activity activity;

    private Instrumentation instrumentation;

    private static final String TABLE = "ALLOWANCE_LOG";

    public MainActivityTest() {
        super(CLASS);
    }

    @Override
    public void setUp () {
        activity = getActivity();
        instrumentation = getInstrumentation();
        setActivityInitialTouchMode(false);

        SQLiteOpenHelper sqLiteOpenHelper = getAllowanceDatabase();
        SQLiteDatabase database = sqLiteOpenHelper.getWritableDatabase();

        database.beginTransaction();
        database.delete(TABLE, null, null);
        database.setTransactionSuccessful();
        database.endTransaction();
        database.close();
    }

    public void test最初に表示される金額は０円 () {
        instrumentation.waitForIdleSync();
        EditText editText = (EditText) activity.findViewById(R.id.payment);
        String actual = editText.getText().toString();
        assertEquals("", actual);
    }

    public void test今月はまだ一度も使っていないので０円 () {
        instrumentation.waitForIdleSync();
        TextView textView = (TextView) activity.findViewById(R.id.total);
        assertEquals("0", textView.getText());
    }

    private SQLiteOpenHelper getAllowanceDatabase() {
        return new AllowanceDatabase(activity);
    }

}
