package jp.group.android.atec.allowlog;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;

/**
 * @author: mike
 * @since: 12/02/17
 */
public class ErrorDialogTest extends ActivityInstrumentationTestCase2<ErrorDialog> {

    private static final Class<ErrorDialog> CLASS = ErrorDialog.class;

    private Instrumentation instrumentation;

    public ErrorDialogTest() {
        super(CLASS);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        setActivityInitialTouchMode(false);
        instrumentation = getInstrumentation();
    }

    public void testメッセージあってるよね () {
        Intent intent = new Intent(instrumentation.getTargetContext(), CLASS);
        intent.putExtra(ErrorDialog.MESSAGE, "test message");
        setActivityIntent(intent);

        Activity activity = getActivity();
        TextView view = (TextView) activity.findViewById(R.id.error_message);
        assertEquals("test message", view.getText());
    }
}
