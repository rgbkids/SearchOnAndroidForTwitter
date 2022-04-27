package biz.r8b.twitter.basic;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class _DBHelper extends SQLiteOpenHelper {

	private String dbName;
	private int version;
	private String tableName;
	private String[] columns;

	public String getDbName() {
		return dbName;
	}

	public int getVersion() {
		return version;
	}

	public String getTableName() {
		return tableName;
	}

	public String[] getColumns() {
		return columns;
	}

	private _DBHelper(Context context, String dbName, int version, String tableName, String[] columns) {
		super(context, dbName, null, version);

		this.dbName = dbName;
		this.version = version;
		this.tableName = tableName;
		this.columns = columns;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table if not exists " + tableName + "(id integer primary key autoincrement, screen_name text, type txt, value txt, insdate text)");
	}

	public void onCreate() {
		this.onCreate(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("drop table if exists " + tableName);
		this.onCreate(db);
	}

	public void onUpgrade(int oldVersion, int newVersion) {
		this.onUpgrade(db, oldVersion, newVersion);
	}

	private static SQLiteDatabase db;
	private static _DBHelper helper;

	public static _DBHelper getInstance(Context context, String dbName, int version, String tableName, String[] columns) {
		helper = new _DBHelper(context, dbName, version, tableName, columns);
		db = helper.getWritableDatabase();
		return helper;
	}

	public void insert(ContentValues values) {
		db.insert(tableName, "", values);
	}

	public void update(ContentValues values) {
//		if (db.update(tableName, values, "code="+values.get("code"), null) == 0) {
		if (db.update(tableName, values, "screen_name='" + values.get("screen_name") + "' and type='" + values.get("type") + "'", null) == 0) {
			db.insert(tableName, "", values);
		}
	}

	public void delete(ContentValues values) {
//		db.delete(tableName, "code="+values.get("code"), null);
	}

	public String getValue(String selection) {
//		// select
//        Cursor c = null;
//        try {
//            c = db.query(tableName, columns, selection, null, null, null, null);
//            if (c.getCount() > 0) {
//            	c.moveToFirst();
//            	String res = c.getString(2);
//            	return res;
//            }
//        }
//        catch (Exception e) {
//        }
//        finally {
//        	if (c != null) c.close();
//        }

        return null;
	}

	public ArrayList<HashMap<String, Object>> getAll() {
		ArrayList<HashMap<String, Object>> res = null;

		// select
        Cursor c = null;
        try {
            c = db.query(tableName, columns, null, null, null, null, "insdate desc");

            if (c != null && c.getCount() != 0) {
	            c.moveToFirst();
	            CharSequence[] list = new CharSequence[c.getCount()];
	            for (int i = 0; i < list.length; i++) {
	            	if (res == null) res = new ArrayList<HashMap<String, Object>>();

	            	HashMap<String, Object> map = new HashMap<String, Object>();


	//                list[i] = c.getString(0);
	//                res += "id=" +  c.getString(0) + ", code=" + c.getString(1) + " cap=" + c.getBlob(2) + " insdate=" + c.getString(3) + "\n";

	            	map.put("id",          c.getString(0));
	            	map.put("screen_name", c.getString(1));
	            	map.put("type",        c.getString(2));
	            	map.put("value",       c.getString(3));
	            	map.put("insdate",     c.getString(4));

	            	res.add(map);

	                c.moveToNext();
	            }
            }
        }
        catch (Exception e) {
        	throw new RuntimeException(e);
        }
        finally {
        	if (c != null) c.close();
        }

		return res;
	}

	public ArrayList<HashMap<String, Object>> getAll(String selection, String orderBy) {
		ArrayList<HashMap<String, Object>> res = null;

		// select
        Cursor c = null;
        try {
            c = db.query(tableName, columns, selection, null, null, null, orderBy);

            if (c != null && c.getCount() != 0) {
	            c.moveToFirst();
	            CharSequence[] list = new CharSequence[c.getCount()];
	            for (int i = 0; i < list.length; i++) {
	            	if (res == null) res = new ArrayList<HashMap<String, Object>>();

	            	HashMap<String, Object> map = new HashMap<String, Object>();


	//                list[i] = c.getString(0);
	//                res += "id=" +  c.getString(0) + ", code=" + c.getString(1) + " cap=" + c.getBlob(2) + " insdate=" + c.getString(3) + "\n";

	            	map.put("id",          c.getString(0));
	            	map.put("screen_name", c.getString(1));
	            	map.put("type",        c.getString(2));
	            	map.put("value",       c.getString(3));
	            	map.put("insdate",     c.getString(4));

	            	res.add(map);

	                c.moveToNext();
	            }
            }
        }
        catch (Exception e) {
        	throw new RuntimeException(e);
        }
        finally {
        	if (c != null) c.close();
        }

		return res;
	}

	@Override
	public String toString() {
		String res = "";

//		// select
//        Cursor c = null;
//        try {
//            c = db.query(tableName, columns, null, null, null, null, null);
//
//            c.moveToFirst();
//            CharSequence[] list = new CharSequence[c.getCount()];
//            for (int i = 0; i < list.length; i++) {
//                list[i] = c.getString(0);
//                res += "id=" +  c.getString(0) + ", code=" + c.getString(1) + " cap=" + c.getBlob(2) + " insdate=" + c.getString(3) + "\n";
//                c.moveToNext();
//            }
//        }
//        catch (Exception e) {
//        }
//        finally {
//        	if (c != null) c.close();
//        }

		return res;
	}
}