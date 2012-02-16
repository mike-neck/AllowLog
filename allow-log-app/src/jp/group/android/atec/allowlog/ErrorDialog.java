
package jp.group.android.atec.allowlog;

import jp.group.android.atec.allowlog.container.IdHolder;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

/**
 * 汎用的なエラーダイアログ.
 * 
 * @author mike
 * 
 */
public class ErrorDialog extends Activity {

    public static final String MESSAGE = "ERROR_MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.error_dialog);

        Intent intent = getIntent();
        String message = intent.getStringExtra(MESSAGE);

        TextView errorMessage = (TextView) findViewById(R.id.error_message);
        errorMessage.setText(message);

        for ( Controller controller : Controller.values() ) {
            View view = findViewById(controller.getHoldingId());
            view.setOnClickListener(controller);
        }
    }

    enum Controller implements IdHolder, OnClickListener {
        OK {

            @Override
            public int getHoldingId() {
                return R.id.dialog_ok_button;
            }

            @Override
            public void onClick(View view) {
                Activity activity = (Activity) view.getContext();
                activity.finish();
            }
        };

        @Override
        abstract public int getHoldingId();

        @Override
        abstract public void onClick(View view);
    }
}
