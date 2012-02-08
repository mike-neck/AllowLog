
package jp.group.android.atec.allowlog.util;

import static java.util.Calendar.DATE;
import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MILLISECOND;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.SECOND;

import java.util.Calendar;
import java.util.TimeZone;

public class AppDateUtil {

    private static final TimeZone TIME_ZONE = TimeZone.getTimeZone("Asia/Tokyo");

    public static long now() {
        Calendar cal = Calendar.getInstance(TIME_ZONE);
        return cal.getTimeInMillis();
    }

    public static long thisMonthFirst() {
        Calendar cal = firstDate();
        return cal.getTimeInMillis();
    }

    public static long thisMonthEnd() {
        Calendar cal = firstDate();
        cal.add(MONTH, 1);
        cal.add(MILLISECOND, -1);
        return cal.getTimeInMillis();
    }

    private static Calendar firstDate() {
        Calendar cal = Calendar.getInstance(TIME_ZONE);
        cal.set(DATE, 1);
        cal.set(HOUR_OF_DAY, 0);
        cal.set(MINUTE, 0);
        cal.set(SECOND, 0);
        cal.set(MILLISECOND, 0);
        return cal;
    }
}
