
package jp.group.android.atec.allowlog;

import jp.group.android.atec.allowlog.model.AllowanceLogData;
import jp.group.android.atec.allowlog.util.AppDateUtil;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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
    }

    /**
     * {@inheritDoc}
     * <p>
     * SQLiteから金額合計を抽出し、totalに表示する.
     */
    @Override
    public void onResume() {
        super.onResume();

        long totalValue = 0;
        SQLiteDatabase database = AllowanceLogData.readableDatabase(this);
        try {
            long from = AppDateUtil.thisMonthFirst();
            long to = AppDateUtil.thisMonthEnd();
            totalValue = AllowanceLogData.getTotalAmount(database, from, to);
        } finally {
            database.close();
        }

        TextView total = (TextView) findViewById(R.id.total);
        total.setText(Long.toString(totalValue));
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
            EditText edit = (EditText) findViewById(R.id.payment);
            CharSequence amount = edit.getText();

            if ( amount.toString().trim().length() == 0 ) {
                intent = new Intent(this, ErrorDialog.class);
                intent.putExtra(ErrorDialog.MESSAGE, getString(R.string.no_amount_input));
            } else {
                intent = new Intent(this, RegisterActivity.class);
                intent.putExtra("ALLOWANCE_PAYMENT", amount);
            }

            startActivityForResult(intent, RegisterActivity.REQUESTCODE_FROM_MAINACTIVITY);
            break;
        default:
            break;
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * RegisterActivityからのレスポンスを受け取ります.
     * 
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch ( requestCode ) {
        case RegisterActivity.REQUESTCODE_FROM_MAINACTIVITY:
            if ( resultCode == RESULT_OK ) {
                // 入力欄をクリアする
                EditText edit = (EditText) findViewById(R.id.payment);
                edit.setText("");
            }
        }
    }

}
