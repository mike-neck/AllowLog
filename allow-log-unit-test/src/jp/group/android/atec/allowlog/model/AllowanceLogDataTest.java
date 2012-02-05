
package jp.group.android.atec.allowlog.model;

import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

import jp.group.android.atec.allowlog.AllowanceDatabase;

public class AllowanceLogDataTest extends AndroidTestCase {

    private AllowanceDatabase helper;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        helper = new AllowanceDatabase(new RenamingDelegatingContext(getContext(), "test_"));

        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            db.execSQL("INSERT INTO ALLOWANCE_LOG(LOG_DATE, AMOUNT) VALUES(1320000000, 1000);");
            db.execSQL("INSERT INTO ALLOWANCE_LOG(LOG_DATE, AMOUNT) VALUES(1330000000, 5000);");
            db.execSQL("INSERT INTO ALLOWANCE_LOG(LOG_DATE, AMOUNT) VALUES(1340000000, 10000);");
            db.execSQL("INSERT INTO ALLOWANCE_LOG(LOG_DATE, AMOUNT) VALUES(1350000000, 2000);");
            db.execSQL("INSERT INTO ALLOWANCE_LOG(LOG_DATE, AMOUNT) VALUES(1360000000, 3000);");
        } finally {
            db.close();
        }
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        
        helper.close();
    }
    
    public void testすべての期間の合計金額を計算() {
        SQLiteDatabase database = helper.getReadableDatabase();
        long totalAmount = AllowanceLogData.getTotalAmount(database, 0, Long.MAX_VALUE);
        assertEquals("すべての金額を足したもの", 21000, totalAmount);
        
        database.close();
    }
    
    public void test期間内の合計金額を計算() {
        SQLiteDatabase database = helper.getReadableDatabase();
        long totalAmount = AllowanceLogData.getTotalAmount(database, 1330000000, 1350000000);
        assertEquals("期間間の金額を足したもの", 17000, totalAmount);
        
        database.close();
    }
    
    public void test期間登録外の合計金額を計算() {
        SQLiteDatabase database = helper.getReadableDatabase();
        long totalAmount = AllowanceLogData.getTotalAmount(database, 1000000000, 1319999999);
        assertEquals("期間間の金額を足したもの", 0, totalAmount);
        
        database.close();
    }
    
    public void testFromToの引数を間違えて計算() {
        SQLiteDatabase database = helper.getReadableDatabase();
        long totalAmount = AllowanceLogData.getTotalAmount(database, 1350000000, 1330000000);
        assertEquals("期間間の金額を足したもの", 0, totalAmount);
        
        database.close();
    }
}
