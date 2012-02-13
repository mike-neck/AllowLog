package jp.group.android.atec.allowlog;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.ListView;
import jp.group.android.atec.allowlog.util.DatabaseUtil;

import java.util.*;

/**
 * @author: mike
 * @since: 12/02/11
 */
public class HistoryActivityTest extends ActivityInstrumentationTestCase2<HistoryActivity> {

    private static final Class<HistoryActivity> CLASS = HistoryActivity.class;

    private Activity activity;

    private Instrumentation instrumentation;

    public HistoryActivityTest() {
        super(CLASS);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        setActivityInitialTouchMode(false);
        instrumentation = getInstrumentation();
        Context context = instrumentation.getTargetContext();

        SQLiteDatabase database = DatabaseUtil.getWritableDatabase(context);
        DatabaseUtil.cleanUpDatabase(database);

        activity = getActivity();
        final Bundle bundle = new Bundle();
        callActivityOnCreate(bundle);
    }

    public void testデータが登録されていなければ０件しか表示されない() {
        ListView listView = (ListView) activity.findViewById(R.id.history);
        assertEquals(0, listView.getCount());

        final Bundle bundle = new Bundle();
        callActivityOnCreate(bundle);

        listView = (ListView) activity.findViewById(R.id.history);
        assertEquals(0, listView.getChildCount());
    }

    public void testデータが１件の場合はリストに１件表示される() {
        ListView listView = (ListView) activity.findViewById(R.id.history);
        assertEquals(0, listView.getCount());

        List<Long> records = Arrays.asList(100L);
        Context context = instrumentation.getTargetContext();
        DatabaseUtil.createAllowanceData(context, records);

        final Bundle bundle = new Bundle();
        callActivityOnCreate(bundle);

        listView = (ListView) activity.findViewById(R.id.history);
        assertEquals(1, listView.getCount());
    }

    public void testデータは２０件までしか表示されない() {
        ListView listView = (ListView) activity.findViewById(R.id.history);
        assertEquals(0, listView.getCount());

        List<Long> records = new ArrayList<Long>(21);
        for (long num = 100L; num <= 2100L; num += 100L) {
            records.add(num);
        }
        Context context = instrumentation.getTargetContext();
        DatabaseUtil.createAllowanceData(context, records);

        final Bundle bundle = new Bundle();
        callActivityOnCreate(bundle);

        listView = (ListView) activity.findViewById(R.id.history);
        assertEquals(20, listView.getCount());
    }

    private void callActivityOnCreate(final Bundle bundle) {
        activity.runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        instrumentation.callActivityOnCreate(activity, bundle);
                    }
                }
        );
        instrumentation.waitForIdleSync();
    }

}
