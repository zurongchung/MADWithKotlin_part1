package journaler.ollie.study.journaler.database

import android.content.ContentValues
import android.location.Location
import android.util.Log
import journaler.ollie.study.journaler.model.Note
import journaler.ollie.study.journaler.model.Todo

object Db {

    private val tag = "Db"
    private val version = 1
    private val name = "students"

    val NOTE = object : Crud<Note> {

        override fun insert(what: Note): Long {
            val inserted = insert(listOf(what))
            if (!inserted.isEmpty()) return inserted[0]
            return 0
        }
        override fun insert(what: Collection<Note>): List<Long> {
            val db = DbHelper(name, version).writableDatabase
            db.beginTransaction()
            var inserted = 0
            val items = mutableListOf<Long>()
            what.forEach { item ->
                val values = ContentValues()
                val table = DbHelper.TABLE_NOTES

                values.put(DbHelper.COLUMN_TITLE, item.title)
                values.put(DbHelper.COLUMN_MESSAGE, item.message)
                values.put(DbHelper.COLUMN_LOCATION_LATITUDE, item.location.latitude)
                values.put(DbHelper.COLUMN_LOCATION_LONGITUDE, item.location.longitude)
                val id = db.insert(table, null, values)
                if (id > 0) {
                    items.add(id)
                    Log.v(tag, "Entry ID assigned [ $id ]")
                    inserted++
                }

            }
            val success = inserted == what.size
            if (success) {
                db.setTransactionSuccessful()
            } else {
                items.clear()
            }
            db.endTransaction()
            db.close()
            return items

        }

        override fun update(what: Note) = update(listOf(what))
        override fun update(what: Collection<Note>): Int {
            val db = DbHelper(name, version).writableDatabase
            db.beginTransaction()
            var updated = 0
            what.forEach { item ->
                val values = ContentValues()
                val table = DbHelper.TABLE_NOTES

                values.put(DbHelper.COLUMN_TITLE, item.title)
                values.put(DbHelper.COLUMN_MESSAGE, item.message)
                values.put(DbHelper.COLUMN_LOCATION_LATITUDE, item.location.latitude)
                values.put(DbHelper.COLUMN_LOCATION_LONGITUDE, item.location.longitude)

                db.update(table, values, "_id = ?", arrayOf(item.id.toString()))
                updated++
            }
            val result = updated == what.size
            if (result) {
                db.setTransactionSuccessful()
            } else {
                updated = 0
            }
            db.endTransaction()
            db.close()
            return updated
        }

        override fun delete(what: Note) = delete(listOf(what))
        override fun delete(what: Collection<Note>): Int {
            val db = DbHelper(name, version).writableDatabase
            db.beginTransaction()
            val ids = StringBuilder()
            
            what.forEachIndexed { index, item ->
                ids.append(item.id.toString())
                if (index < what.size - 1) ids.append(", ")
            }

            val table = DbHelper.TABLE_NOTES
            val statement = db.compileStatement(
                "DELETE FROM $table WHERE ${DbHelper.ID} IN ($ids);")
            val count = statement.executeUpdateDelete()
            val success = count > 0
            if (success) {
                db.setTransactionSuccessful()
                Log.i(tag, "DELETE [ SUCCESS ][ $count ][ $statement ]")
            } else {
                Log.w(tag, "DELETE [ FAILED ][ $statement ]")
            }
            db.endTransaction()
            db.close()
            return count
        }

        override fun select(args: Pair<String, String>) = select(listOf(args))
        override fun select(args: Collection<Pair<String, String>>): List<Note> {
            val db = DbHelper(name, version).writableDatabase
            val selection = StringBuilder()
            val selectionArgs = mutableListOf<String>()

            args.forEach { arg ->
                selection.append("${args.first()} == ?")
                selectionArgs.add(arg.second)
            }

            val result = mutableListOf<Note>()

            val cursor = db.query(
                true,
                DbHelper.TABLE_NOTES,
                null,
                selection.toString(),
                selectionArgs.toTypedArray(),
                null, null, null, null
            )

            while (cursor.moveToNext()) {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(DbHelper.ID))
                val titleIdx = cursor.getColumnIndexOrThrow(DbHelper.COLUMN_TITLE)
                val title = cursor.getString(titleIdx)
                val messageIdx = cursor.getColumnIndexOrThrow(DbHelper.COLUMN_MESSAGE)
                val message = cursor.getString(messageIdx)
                val latitudeIdx = cursor.getColumnIndexOrThrow(DbHelper.COLUMN_LOCATION_LATITUDE)
                val latitude = cursor.getDouble(latitudeIdx)
                val longitudeIdx = cursor.getColumnIndexOrThrow(DbHelper.COLUMN_LOCATION_LONGITUDE)
                val longitude = cursor.getDouble(longitudeIdx)

                val location = Location("")
                location.latitude = latitude
                location.longitude = longitude

                val note = Note(title, message, location)
                note.id = id
                result.add(note)
            }
            cursor.close()
            return result
        }

        override fun selectAll(): List<Note> {
            val db = DbHelper(name, version).writableDatabase
            val result = mutableListOf<Note>()

            val cursor = db.query(
                true,
                DbHelper.TABLE_NOTES,
                null, null, null,
                null, null, null, null
            )

            while (cursor.moveToNext()) {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(DbHelper.ID))
                val titleIdx = cursor.getColumnIndexOrThrow(DbHelper.COLUMN_TITLE)
                val title = cursor.getString(titleIdx)
                val messageIdx = cursor.getColumnIndexOrThrow(DbHelper.COLUMN_MESSAGE)
                val message = cursor.getString(messageIdx)
                val latitudeIdx = cursor.getColumnIndexOrThrow(DbHelper.COLUMN_LOCATION_LATITUDE)
                val latitude = cursor.getDouble(latitudeIdx)
                val longitudeIdx = cursor.getColumnIndexOrThrow(DbHelper.COLUMN_LOCATION_LONGITUDE)
                val longitude = cursor.getDouble(longitudeIdx)

                val location = Location("")
                location.latitude = latitude
                location.longitude = longitude

                val note = Note(title, message, location)
                note.id = id
                result.add(note)
            }
            cursor.close()
            return result

        }

    }

    val TODO = object : Crud<Todo> {

        override fun insert(what: Todo): Long {
            val inserted = insert(listOf(what))
            if (!inserted.isEmpty()) return inserted[0]
            return 0
        }
        override fun insert(what: Collection<Todo>): List<Long> {
            val db = DbHelper(name, version).writableDatabase
            db.beginTransaction()
            var inserted = 0
            val items = mutableListOf<Long>()

            what.forEach{ item ->
                val values = ContentValues()
                val table = DbHelper.TABLE_TODOS

                values.put(DbHelper.COLUMN_TITLE, item.title)
                values.put(DbHelper.COLUMN_MESSAGE, item.message)
                values.put(DbHelper.COLUMN_LOCATION_LATITUDE, item.location.latitude)
                values.put(DbHelper.COLUMN_LOCATION_LONGITUDE, item.location.longitude)
                values.put(DbHelper.COLUMN_SCHEDULEED, item.scheduledFor)

                val id = db.insert(table, null, values)
                if (id > 0){
                    item.id = id
                    Log.v(tag, "Entry ID assigned [ $id ]")
                    inserted++
                }
            }
            val success = inserted == what.size
            if (success) {
                db.setTransactionSuccessful()
            } else {
                items.clear()
            }
            db.endTransaction()
            db.close()
            return items
        }

        override fun update(what: Todo) = update(listOf(what))
        override fun update(what: Collection<Todo>): Int {
            val db = DbHelper(name, version).writableDatabase
            db.beginTransaction()
            var updated = 0

            what.forEach { item ->
                val values = ContentValues()
                val table = DbHelper.TABLE_TODOS

                values.put(DbHelper.COLUMN_TITLE, item.title)
                values.put(DbHelper.COLUMN_MESSAGE, item.message)
                values.put(DbHelper.COLUMN_LOCATION_LATITUDE, item.location.latitude)
                values.put(DbHelper.COLUMN_LOCATION_LONGITUDE, item.location.longitude)
                values.put(DbHelper.COLUMN_SCHEDULEED, item.scheduledFor)

                db.update(table, values, "_id = ?", arrayOf(item.id.toString()))
                updated++
            }
            val result = updated == what.size
            if (result) {
                db.beginTransaction()
            } else {
                updated = 0
            }
            db.endTransaction()
            db.close()
            return updated
        }

        override fun delete(what: Todo) = delete(listOf(what))
        override fun delete(what: Collection<Todo>): Int {
            val db = DbHelper(name, version).writableDatabase
            db.beginTransaction()
            val ids = StringBuilder()

            what.forEachIndexed { index, item ->
                ids.append(item.id.toString())
                if (index < what.size - 1) ids.append(", ")
            }

            val table = DbHelper.TABLE_TODOS
            val statement = db.compileStatement(
                "DELETE $table WHERE ${DbHelper.ID} IN ($ids);")

            val count = statement.executeUpdateDelete()
            val success = count > 0
            if (success) {
                db.setTransactionSuccessful()
                Log.i(tag, "DELETE [ SUCCESS ][ $count ][ $statement ]")
            } else {
                Log.w(tag, "DELETE [ FAILED ][ $statement ]")
            }
            db.endTransaction()
            db.close()
            return count

        }

        override fun select(args: Pair<String, String>) = select(listOf(args))
        override fun select(args: Collection<Pair<String, String>>): List<Todo> {
            val db = DbHelper(name, version).writableDatabase
            val selection = StringBuilder()
            val selectionArgs = mutableListOf<String>()

            args.forEach { arg ->
                selection.append("${args.first()} == ?")
                selectionArgs.add(arg.second)
            }

            val result = mutableListOf<Todo>()

            val cursor = db.query(
                true,
                DbHelper.TABLE_NOTES,
                null,
                selection.toString(),
                selectionArgs.toTypedArray(),
                null, null, null, null
            )

            while (cursor.moveToNext()) {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(DbHelper.ID))
                val titleIdx = cursor.getColumnIndexOrThrow(DbHelper.COLUMN_TITLE)
                val title = cursor.getString(titleIdx)
                val messageIdx = cursor.getColumnIndexOrThrow(DbHelper.COLUMN_MESSAGE)
                val message = cursor.getString(messageIdx)
                val latitudeIdx = cursor.getColumnIndexOrThrow(DbHelper.COLUMN_LOCATION_LATITUDE)
                val latitude = cursor.getDouble(latitudeIdx)
                val longitudeIdx = cursor.getColumnIndexOrThrow(DbHelper.COLUMN_LOCATION_LONGITUDE)
                val longitude = cursor.getDouble(longitudeIdx)
                val scheduleForIdx = cursor.getColumnIndexOrThrow(DbHelper.COLUMN_SCHEDULEED)
                val scheduleFor = cursor.getLong(scheduleForIdx)

                val location = Location("")
                location.latitude = latitude
                location.longitude = longitude

                val todo = Todo(title, message, location, scheduleFor)
                todo.id = id
                result.add(todo)
            }
            cursor.close()
            return result
        }
        override fun selectAll(): List<Todo> {
            val db = DbHelper(name, version).writableDatabase
            val result = mutableListOf<Todo>()

            val cursor = db.query(
                true,
                DbHelper.TABLE_TODOS,
                null, null, null,
                null, null, null, null
            )

            while (cursor.moveToNext()) {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(DbHelper.ID))
                val titleIdx = cursor.getColumnIndexOrThrow(DbHelper.COLUMN_TITLE)
                val title = cursor.getString(titleIdx)
                val messageIdx = cursor.getColumnIndexOrThrow(DbHelper.COLUMN_MESSAGE)
                val message = cursor.getString(messageIdx)
                val latitudeIdx = cursor.getColumnIndexOrThrow(DbHelper.COLUMN_LOCATION_LATITUDE)
                val latitude = cursor.getDouble(latitudeIdx)
                val longitudeIdx = cursor.getColumnIndexOrThrow(DbHelper.COLUMN_LOCATION_LONGITUDE)
                val longitude = cursor.getDouble(longitudeIdx)
                val scheduleForIdx = cursor.getColumnIndexOrThrow(DbHelper.COLUMN_SCHEDULEED)
                val scheduleFor = cursor.getLong(scheduleForIdx)

                val location = Location("")
                location.latitude = latitude
                location.longitude = longitude

                val todo = Todo(title, message, location, scheduleFor)
                todo.id = id
                result.add(todo)
            }
            cursor.close()
            return result
        }
    }
}