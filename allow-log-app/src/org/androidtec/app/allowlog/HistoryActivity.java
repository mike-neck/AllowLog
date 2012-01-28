/**
 * 
 */

package org.androidtec.app.allowlog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

/**
 * @author mike
 * 
 */
public class HistoryActivity extends Activity {

    private final int MAX_PAGE_SIZE = 20;

    private int position;

    private long lastDate = 0L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);
        this.position = 0;

        SQLiteOpenHelper helper = new AllowanceDatabase(this);
        SQLiteDatabase database = helper.getReadableDatabase();

        Cursor cursor = searchHistory(database);

        int records = 0;
        List<Map<String, String>> payments = new ArrayList<Map<String, String>>();
        while (cursor.moveToNext()) {
            Map<String, String> map = new HashMap<String, String>();

            int columnIndex = cursor.getColumnIndex("LOG_DATE");
            lastDate = cursor.getLong(columnIndex);
            String date = formatLogDate(lastDate);
            map.put("LOG_DATE", date);

            columnIndex = cursor.getColumnIndex("AMOUNT");
            int amount = cursor.getInt(columnIndex);
            map.put("AMOUNT", Integer.toString(amount));

            payments.add(map);
            records += 1;
        }

        String[] keys = {
                "LOG_DATE", "AMOUNT"
        };
        int[] ids = {
                R.id.date, R.id.amount
        };

        ListAdapter adapter = new SimpleAdapter(this, payments, R.layout.payment, keys, ids);
        ListView listView = (ListView) findViewById(R.id.history);
        listView.setAdapter(adapter);

        position += records;
        cursor.close();
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

    Cursor searchHistory(SQLiteDatabase database) {
        String[] columns = {
                "LOG_DATE", "AMOUNT"
        };
        String condition = "LOG_DATE <= ?";
        String[] conditionArg = getNowInMillis(lastDate);

        Cursor cursor = database.query("ALLOWANCE_LOG", columns, condition, conditionArg, null, null, "LOG_DATE DESC",
                Integer.toString(MAX_PAGE_SIZE));
        startManagingCursor(cursor);
        return cursor;
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
