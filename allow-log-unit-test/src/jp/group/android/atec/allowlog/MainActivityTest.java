
package jp.group.android.atec.allowlog;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import jp.group.android.atec.allowlog.model.entity.AllowanceLog;
import jp.group.android.atec.allowlog.util.AppDateUtil;
import jp.group.android.atec.allowlog.util.DatabaseUtil;

import java.util.*;

/**
 * 初回起動時のViewに関するテスト<br/>
 *
 * <ul style="list-style-type : numeric;">
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
        setActivityInitialTouchMode(false);
        instrumentation = getInstrumentation();

        Context context = instrumentation.getTargetContext();
        SQLiteDatabase database = DatabaseUtil.getWritableDatabase(context);
        DatabaseUtil.cleanUpDatabase(database);

        activity = getActivity();
    }

    public void test最初に表示される金額は０円 () {
        instrumentation.waitForIdleSync();
        EditText editText = (EditText) activity.findViewById(R.id.payment);
        String actual = editText.getText().toString();
        assertEquals("", actual);
    }

    public void test今月はまだ一度も使っていないので０円 () {
        activity.runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        instrumentation.callActivityOnResume(activity);
                    }
                }
        );
        instrumentation.waitForIdleSync();
        TextView textView = (TextView) activity.findViewById(R.id.total);
        assertEquals("0", textView.getText());
    }

    public void test先月分は集計対象外() {
        final TimeZone tokyo = TimeZone.getTimeZone("Asia/Tokyo");
        List<AllowanceLog> allowanceLogs = new ArrayList<AllowanceLog>(4);

        Calendar lastMonth = Calendar.getInstance(tokyo);
        lastMonth.set(Calendar.DATE, 20);
        lastMonth.add(Calendar.MONTH, -1);
        AllowanceLog lastMonthAllowance = new AllowanceLog();
        lastMonthAllowance.setAmount(100000);
        lastMonthAllowance.setLogDate(lastMonth.getTime());
        allowanceLogs.add(lastMonthAllowance);

        Calendar lastMonthLastDay = Calendar.getInstance(tokyo);
        lastMonthLastDay.set(Calendar.DATE, 1);
        lastMonthLastDay.set(Calendar.HOUR_OF_DAY, 0);
        lastMonthLastDay.set(Calendar.MINUTE, 0);
        lastMonthLastDay.set(Calendar.SECOND, 0);
        lastMonthLastDay.set(Calendar.MILLISECOND, 0);
        lastMonthLastDay.add(Calendar.MILLISECOND, -1);
        AllowanceLog lastMonthFinal = new AllowanceLog();
        lastMonthFinal.setAmount(200000);
        lastMonthFinal.setLogDate(lastMonthLastDay.getTime());
        allowanceLogs.add(lastMonthFinal);

        Calendar firstDate = Calendar.getInstance(tokyo);
        firstDate.set(Calendar.DATE, 1);
        firstDate.set(Calendar.HOUR_OF_DAY, 0);
        firstDate.set(Calendar.MINUTE, 0);
        firstDate.set(Calendar.SECOND, 0);
        firstDate.set(Calendar.MILLISECOND, 0);
        AllowanceLog thisMonth = new AllowanceLog();
        thisMonth.setAmount(100);
        thisMonth.setLogDate(firstDate.getTime());
        allowanceLogs.add(thisMonth);

        AllowanceLog now = new AllowanceLog();
        now.setAmount(200);
        now.setLogDate(Calendar.getInstance(tokyo).getTime());
        allowanceLogs.add(now);

        Context context = instrumentation.getTargetContext();
        DatabaseUtil.createAllowanceDataFromEntity(context, allowanceLogs);

        activity.runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        instrumentation.callActivityOnResume(activity);
                    }
                }
        );
        instrumentation.waitForIdleSync();
        TextView textView = (TextView) activity.findViewById(R.id.total);
        assertEquals("300", textView.getText());
    }

}
