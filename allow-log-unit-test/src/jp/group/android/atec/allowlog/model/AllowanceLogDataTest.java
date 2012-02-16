
package jp.group.android.atec.allowlog.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import jp.group.android.atec.allowlog.AllowanceDatabase;
import jp.group.android.atec.sf.importer.FileType;
import jp.group.android.atec.sf.unit.DatabaseTestCase;

import java.util.Date;

public class AllowanceLogDataTest extends DatabaseTestCase {

    @Override
    protected SQLiteOpenHelper createSQLiteOpenHelper() {
        return new AllowanceDatabase(getDatabaseContext());
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        importData(FileType.Yaml, "AllowanceLogDataTest");
    }
    
    public void testすべての期間の合計金額を計算() {
        SQLiteDatabase database = getSQLiteDatabase();
        long totalAmount = AllowanceLogData.getTotalAmount(database, 0, Long.MAX_VALUE);
        assertEquals("すべての金額を足したもの", 21000, totalAmount);
        
        database.close();
    }
    
    public void test期間内の合計金額を計算() {
        SQLiteDatabase database = getSQLiteDatabase();
        long totalAmount = AllowanceLogData.getTotalAmount(database, 1330000000, 1350000000);
        assertEquals("期間間の金額を足したもの", 17000, totalAmount);
        
        database.close();
    }
    
    public void test期間登録外の合計金額を計算() {
        SQLiteDatabase database = getSQLiteDatabase();
        long totalAmount = AllowanceLogData.getTotalAmount(database, 1000000000, 1319999999);
        assertEquals("期間間の金額を足したもの", 0, totalAmount);
        
        database.close();
    }
    
    public void testFromToの引数を間違えて計算() {
        SQLiteDatabase database = getSQLiteDatabase();
        long totalAmount = AllowanceLogData.getTotalAmount(database, 1350000000, 1330000000);
        assertEquals("期間間の金額を足したもの", 0, totalAmount);
        
        database.close();
    }

    public void test履歴データを取得する () {
        SQLiteDatabase database = getSQLiteDatabase();
        long now = new Date().getTime();
        Cursor cursor = AllowanceLogData.searchHistory(database, now);
        int count = cursor.getCount();
        assertEquals(5, count);

        database.close();
    }
}
