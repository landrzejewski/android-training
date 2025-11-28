package pl.training.runkeeper.common.components

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
import android.net.Uri
import pl.training.runkeeper.common.components.ForecastProvider.DatabaseHelper.Companion.DATE_COLUMN
import pl.training.runkeeper.common.components.ForecastProvider.DatabaseHelper.Companion.ID_COLUMN
import pl.training.runkeeper.common.components.ForecastProvider.DatabaseHelper.Companion.TABLE_NAME

class ForecastProvider : ContentProvider() {

    private var db: SQLiteDatabase? = null

    override fun onCreate(): Boolean {
        val dbHelper = DatabaseHelper(requireContext())
        db = dbHelper.writableDatabase
        return db == null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri {
        db?.insert(TABLE_NAME, "", values)?.let { id ->
            val newUrl = ContentUris.withAppendedId(CONTENT_URI, id)
            requireContext().contentResolver.notifyChange(uri, null)
            return  newUrl
        }
        throw SQLException("Insert failed for $uri")
    }

    override fun query(uri: Uri, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor? {
        val queryBuilder = SQLiteQueryBuilder()
        queryBuilder.tables = TABLE_NAME
        when(uriMatcher.match(uri)) {
            CONDITIONS_CODE -> queryBuilder.projectionMap = emptyMap<String, String>()
            CONDITION_CODE -> queryBuilder.appendWhere("$ID_COLUMN=${getId(uri)}")
        }
        val cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, DATE_COLUMN)
        cursor.setNotificationUri(requireContext().contentResolver, uri)
        return cursor
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
        val count = when(uriMatcher.match(uri)) {
            CONDITIONS_CODE -> db?.update(TABLE_NAME, values, selection, selectionArgs) ?: 0
            CONDITION_CODE -> db?.update(TABLE_NAME, values, "$ID_COLUMN=?", arrayOf(getId(uri))) ?: 0
            else -> throw IllegalStateException("Unsupported uri $uri")
        }
        requireContext().contentResolver.notifyChange(uri, null)
        return count
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        val count = when(uriMatcher.match(uri)) {
            CONDITIONS_CODE -> db?.delete(TABLE_NAME, selection, selectionArgs) ?: 0
            CONDITION_CODE -> db?.delete(TABLE_NAME, "$ID_COLUMN=?", arrayOf(getId(uri))) ?: 0
            else -> throw IllegalStateException("Unsupported uri $uri")
        }
        requireContext().contentResolver.notifyChange(uri, null)
        return count
    }

    override fun getType(uri: Uri) = when(uriMatcher.match(uri)) {
        CONDITIONS_CODE -> "vnd.android.cursor.dir/vnd.pl.training.runkeeper.conditions"
        CONDITION_CODE -> "vnd.android.cursor.dir/vnd.pl.training.runkeeper.condition"
        else -> throw IllegalStateException("Unsupported uri $uri")
    }

    private fun getId(uri: Uri) = uri.pathSegments[1]

    companion object {

        private const val PROVIDER_NAME = "pl.training.runkeeper.common.components.ForecastProvider"
        private const val URL = "content://$PROVIDER_NAME/forecast"
        private const val CONDITIONS_CODE = 1
        private const val CONDITION_CODE = 2
        private var uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(PROVIDER_NAME, "conditions", CONDITIONS_CODE)
            addURI(PROVIDER_NAME, "conditions/#", CONDITION_CODE)
        }

        val CONTENT_URI: Uri = Uri.parse(URL)

    }

    class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL(CREATE_TABLE)
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            db.execSQL(DROP_TABLE)
            onCreate(db)
        }

        companion object {

            private const val DB_NAME = "forecast-data"
            private const val DB_VERSION = 1
            const val TABLE_NAME = "forecast"
            const val ID_COLUMN = "_id"
            const val DATE_COLUMN = "date"
            const val CONDITIONS_COLUMN = "conditions"
            private const val CREATE_TABLE = "create table $TABLE_NAME ($ID_COLUMN INTEGER PRIMARY KEY AUTOINCREMENT, $DATE_COLUMN INTEGER, $CONDITIONS_COLUMN TEXT NOT NULL)"
            private const val DROP_TABLE = "drop table if exists $TABLE_NAME"

        }

    }

}