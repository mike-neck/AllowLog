
package org.androidtec.app.allowlog;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class AllowanceDatabase extends SQLiteOpenHelper {

	private static final String DATABASE = "allowlog.db";

	private static final int DATABASE_VERSION = 1;

	public AllowanceDatabase(Context context) {
		super(context, DATABASE, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		StringBuilder builder = new StringBuilder();
		String sql = builder.append("CREATE TABLE ").append("ALLOWANCE_LOG").append("( ").append(BaseColumns._ID)
		        .append(" INTEGER PRIMARY KEY AUTOINCREMENT,").append("LOG_DATE INTEGER NOT NULL,")
		        .append("AMOUNT INTEGER NOT NULL);").toString();
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int olderVersion, int newVersion) {
		StringBuilder builder = new StringBuilder();
		String sql = builder.append("DROP TABLE ").append("IF EXISTS ").append("ALLOWANCE_LOG").toString();
		db.execSQL(sql);
		onCreate(db);
	}

}
