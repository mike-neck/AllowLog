
package jp.group.android.atec.allowlog.util;

import java.util.Calendar;
import java.util.TimeZone;

public class AppDateUtil {

    public static long now() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tokyo"));
        return cal.getTimeInMillis();
    }
}
