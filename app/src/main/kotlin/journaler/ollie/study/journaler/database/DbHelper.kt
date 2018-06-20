package journaler.ollie.study.journaler.database

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import journaler.ollie.study.journaler.Journaler

class DbHelper(dbName: String, version: Int) : SQLiteOpenHelper(
    Journaler.ctx, dbName, null, version
) {

    private val tag = "DbHelper"

    companion object {
        val ID: String = "_id"
        val TABLE_TODOS = "todos"
        val TABLE_NOTES = "notes"
        val COLUMN_TITLE: String = "title"
        val COLUMN_MESSAGE: String = "message"
        val COLUMN_SCHEDULEED: String ="scheduled"
        val COLUMN_LOCATION_LATITUDE: String = "latitude"
        val COLUMN_LOCATION_LONGITUDE: String = "longitude"
    }

    private val createTableNotes = """
        CREATE TABLE if not exists $TABLE_NOTES
        (
            $ID integer PRIMARY KEY autoincrement,
            $COLUMN_TITLE text,
            $COLUMN_MESSAGE text,
            $COLUMN_LOCATION_LATITUDE real,
            $COLUMN_LOCATION_LONGITUDE real
        )
    """

    private val createTableTodos = """
        CREATE TABLE if not exists $TABLE_TODOS
        (
            $ID integer PRIMARY KEY autoincrement,
            $COLUMN_TITLE text,
            $COLUMN_MESSAGE text,
            $COLUMN_SCHEDULEED integer,
            $COLUMN_LOCATION_LATITUDE real,
            $COLUMN_LOCATION_LONGITUDE real
        )
    """

    override fun onCreate(db: SQLiteDatabase?) {
        Log.d(tag, "Database [ CREATING ]")
        db?.execSQL(createTableNotes)
        db?.execSQL(createTableTodos)
        Log.d(tag, "Database [ CREATED ]")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }
}