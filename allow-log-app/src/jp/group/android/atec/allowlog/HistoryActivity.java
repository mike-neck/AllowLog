/**
 * 
 */

package jp.group.android.atec.allowlog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import jp.group.android.atec.allowlog.model.AllowanceLogData;
import jp.group.android.atec.allowlog.model.entity.AllowanceLog;
import jp.group.android.atec.allowlog.util.AppDateUtil;
import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

/**
 * @author mike
 * 
 */
public class HistoryActivity extends Activity {

    public static final int MAX_PAGE_SIZE = 20;

    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);
        this.position = 0;

        SQLiteDatabase database = AllowanceLogData.readableDatabase(this);
        List<AllowanceLog> logs = AllowanceLogData.searchHistory(database, new Date().getTime());

        int records = 0;
        List<Map<String, String>> payments = new ArrayList<Map<String, String>>();
        for ( AllowanceLog value : logs ) {
            Map<String, String> map = new HashMap<String, String>();

            Date logDate = value.getLogDate();
            String date = AppDateUtil.formatDate(logDate);
            map.put(AllowanceLog.LOG_DATE, date);
            map.put(AllowanceLog.AMOUNT, Long.toString(value.getAmount()));

            payments.add(map);
            records += 1;
        }

        String[] keys = {
                AllowanceLog.LOG_DATE, AllowanceLog.AMOUNT
        };
        int[] ids = {
                R.id.date, R.id.amount
        };

        ListAdapter adapter = new SimpleAdapter(this, payments, R.layout.payment, keys, ids);
        ListView listView = (ListView) findViewById(R.id.history);
        listView.setAdapter(adapter);

        position += records;
        database.close();
    }

    String formatLogDate(long date) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tokyo"));
        calendar.setTimeInMillis(date);
        StringBuilder builder = new StringBuilder();
        return builder.append(calendar.get(Calendar.YEAR)).append("/").append(calendar.get(Calendar.MONTH) + 1)
                .append("/").append(calendar.get(Calendar.DATE)).append("\n")
                .append(calendar.get(Calendar.HOUR_OF_DAY)).append(":").append(calendar.get(Calendar.MINUTE))
                .toString();
    }

    String[] getNowInMillis(long date) {
        long now;
        if ( date == 0 ) {
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tokyo"));
            now = cal.getTimeInMillis();
        } else {
            now = date;
        }
        String[] conditionArg = {
            Long.toString(now)
        };
        return conditionArg;
    }

}
