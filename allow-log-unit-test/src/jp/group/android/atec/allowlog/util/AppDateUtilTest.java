package jp.group.android.atec.allowlog.util;

import android.test.AndroidTestCase;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * {@link AppDateUtil}に関するテスト.
 * <ul>
 *     <li>{@link jp.group.android.atec.allowlog.util.AppDateUtil#now()}が呼び出し前、呼び出し後の時刻の間に位置する数値を返すか検証する</li>
 *     <li>{@link jp.group.android.atec.allowlog.util.AppDateUtil#thisMonthFirst()}が当月の初日を返すか検証する</li>
 *     <li>{@link jp.group.android.atec.allowlog.util.AppDateUtil#thisMonthEnd()}が当月の最終日を返すか検証する</li>
 * </ul>
 */
public class AppDateUtilTest extends AndroidTestCase {

    public void testNow() {
        long from = new Date().getTime();
        long now = AppDateUtil.now();
        long to = new Date().getTime();
        boolean expr = from <= now && now <= to;
        assertTrue(expr);
    }

    public void testThisMonthFirst() {
        Calendar calendar = month1st();
        long millis = calendar.getTimeInMillis();
        assertEquals(millis, AppDateUtil.thisMonthFirst());
    }

    public void testThisMonthEnd() {
        Calendar calendar = month1st();
        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.MILLISECOND, -1);
        long millis = calendar.getTimeInMillis();
        assertEquals(millis, AppDateUtil.thisMonthEnd());
    }

    private Calendar month1st() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tokyo"));
        calendar.set(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }
}
