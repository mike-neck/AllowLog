
package org.androidtec.app.allowlog;

import java.util.Calendar;
import java.util.TimeZone;

import org.androidtec.app.allowlog.container.IdHolder;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        Intent intent = getIntent();
        CharSequence amount = intent.getCharSequenceExtra("ALLOWANCE_PAYMENT");
        TextView view = (TextView) findViewById(R.id.going_to_register);
        view.setText(amount);

        for ( Controller controller : Controller.values() ) {
            View button = findViewById(controller.getHoldingId());
            button.setOnClickListener(controller);
        }
    }

    public enum Controller implements OnClickListener, IdHolder {
        CANCEL {

            @Override
            public void onClick(View v) {
                Activity activity = (Activity) v.getContext();
                activity.finish();
            }

            @Override
            public int getHoldingId() {
                return R.id.cancel;
            }

        },
        REGISTER {

            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Activity activity = (Activity) context;

                SQLiteOpenHelper openHelper = new AllowanceDatabase(context);
                SQLiteDatabase database = openHelper.getWritableDatabase();

                ContentValues contents = new ContentValues();

                Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tokyo"));
                long date = cal.getTimeInMillis();
                contents.put("LOG_DATE", date);

                TextView view = (TextView) activity.findViewById(R.id.going_to_register);
                CharSequence text = view.getText();
                long amount = Long.parseLong(text.toString());
                contents.put("AMOUNT", amount);

                database.insert("ALLOWANCE_LOG", null, contents);

                Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
                toast.show();

                activity.finish();
            }

            @Override
            public int getHoldingId() {
                return R.id.persist;
            }
        };

        @Override
        abstract public int getHoldingId();
    }
}
