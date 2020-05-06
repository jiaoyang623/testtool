package guru.ioio.tool;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.util.Log;

public class TestProvider extends ContentProvider {
    public TestProvider() {
        Log.i("TestProvider", "()");
    }

    @Override
    public boolean onCreate() {
        Log.i("TestProvider", "onCreate");
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        MatrixCursor c = new MatrixCursor(new String[]{"key", "value"});
        c.addRow(new String[]{"encrypt", "b"});
        return c;
    }

    @Override
    public String getType(Uri uri) {
        return "test";
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
