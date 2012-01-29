
package jp.group.android.atec.allowlog;

import java.util.Calendar;
import java.util.TimeZone;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {

    /**
     * @param savedInstanceState
     *            - the status before this method was called in this
     *            application.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        View historyButton = findViewById(R.id.to_history);
        historyButton.setOnClickListener(this);

        View registerButton = findViewById(R.id.allowance_registration);
        registerButton.setOnClickListener(this);

        SQLiteDatabase sqLiteDatabase = prepareSql();
        String sql = countSql();
        long totalValue = getTotalAmount(sqLiteDatabase, sql);
        sqLiteDatabase.close();

        TextView total = (TextView) findViewById(R.id.total);
        total.setText(Long.toString(totalValue));
    }

    long getTotalAmount(SQLiteDatabase sqLiteDatabase, String sql) {
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        cursor.moveToFirst();
        int count = cursor.getCount();
        if ( count > 0 ) {
            long totalValue = cursor.getLong(0);
            return totalValue;
        } else {
            return 0L;
        }
    }

    private SQLiteDatabase prepareSql() {
        SQLiteOpenHelper database = new AllowanceDatabase(this);
        return database.getReadableDatabase();
    }

    private String countSql() {
        StringBuilder builder = new StringBuilder();
        long from = getFromDate();
        long to = getToDate();
        return builder.append("SELECT ").append("SUM(AMOUNT) ").append("FROM ").append("ALLOWANCE_LOG ")
                .append("WHERE LOG_DATE BETWEEN ").append(from).append(" AND ").append(to).append(";").toString();
    }

    private long getToDate() {
        Calendar toCalendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tokyo"));
        toCalendar.add(Calendar.MONTH, 1);
        toCalendar.set(Calendar.DATE, 1);
        toCalendar.set(Calendar.HOUR, 0);
        toCalendar.set(Calendar.MINUTE, 0);
        toCalendar.set(Calendar.SECOND, 0);
        toCalendar.set(Calendar.MILLISECOND, 0);
        toCalendar.add(Calendar.MILLISECOND, -1);
        return toCalendar.getTimeInMillis();
    }

    private long getFromDate() {
        Calendar fromCalendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tokyo"));
        fromCalendar.set(Calendar.DATE, 1);
        fromCalendar.set(Calendar.HOUR, 0);
        fromCalendar.set(Calendar.MINUTE, 0);
        fromCalendar.set(Calendar.SECOND, 0);
        fromCalendar.set(Calendar.MILLISECOND, 0);
        return fromCalendar.getTimeInMillis();
    }

    /**
     * @param view
     *            - the View user clicked.
     */
    @Override
    public void onClick(View view) {
        int id = view.getId();
        Intent intent;
        switch ( id ) {
        case R.id.to_history:
            intent = new Intent(this, HistoryActivity.class);
            startActivity(intent);
            break;
        case R.id.allowance_registration:
            intent = new Intent(this, RegisterActivity.class);
            EditText edit = (EditText) findViewById(R.id.payment);
            CharSequence amount = edit.getText();
            intent.putExtra("ALLOWANCE_PAYMENT", amount);
            startActivity(intent);
            break;
        default:
            break;
        }
    }
}
