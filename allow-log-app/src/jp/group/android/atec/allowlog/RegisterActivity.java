
package jp.group.android.atec.allowlog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import jp.group.android.atec.allowlog.container.IdHolder;
import jp.group.android.atec.allowlog.exception.AppException;
import jp.group.android.atec.allowlog.model.AllowanceLogData;

public class RegisterActivity extends Activity {

    /** MainActivityから呼び出されるときのリクエストコード（未使用）. */
    public static final int REQUESTCODE_FROM_MAINACTIVITY = 0;

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
                activity.setResult(RESULT_CANCELED);
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
                try {
                    database.beginTransaction();

                    TextView view = (TextView) activity.findViewById(R.id.going_to_register);
                    String text = view.getText().toString();
                    AllowanceLogData.createRecord(database, text);
                    database.setTransactionSuccessful();

                    Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
                    toast.show();
                } catch ( AppException e ) {
                    Toast toast = Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT);
                    toast.show();
                } finally {
                    database.endTransaction();
                    database.close();
                }

                activity.setResult(RESULT_OK);
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
