package edu.sswu.sungshinclub

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class EventDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "events.db"
        private const val DATABASE_VERSION = 1

        const val TABLE_EVENTS = "events"
        const val COLUMN_ID = "id"
        const val COLUMN_DATE = "date"
        const val COLUMN_EVENT = "event"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_EVENTS_TABLE = ("CREATE TABLE $TABLE_EVENTS ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "$COLUMN_DATE TEXT, "
                + "$COLUMN_EVENT TEXT)")
        db.execSQL(CREATE_EVENTS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_EVENTS")
        onCreate(db)
    }

    // 이벤트 저장
    fun addEvent(date: String, event: String) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_DATE, date)
        values.put(COLUMN_EVENT, event)

        db.insert(TABLE_EVENTS, null, values)
        db.close()
    }

    // 날짜별 이벤트 불러오기
    fun getEventsByDate(date: String): List<String> {
        val eventList = mutableListOf<String>()
        val db = this.readableDatabase

        val cursor = db.query(
            TABLE_EVENTS,
            arrayOf(COLUMN_EVENT),
            "$COLUMN_DATE = ?",
            arrayOf(date),
            null, null, null
        )

        if (cursor != null) {
            while (cursor.moveToNext()) {
                eventList.add(cursor.getString(0))
            }
            cursor.close()
        }
        db.close()
        return eventList
    }

    fun getAllUpcomingEvents(): List<Pair<String, String>> {
        val events = mutableListOf<Pair<String, String>>()
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_EVENTS, arrayOf(COLUMN_DATE, COLUMN_EVENT),
            null, null, null, null, "$COLUMN_DATE ASC"
        )

        while (cursor.moveToNext()) {
            val date = cursor.getString(0)
            val content = cursor.getString(1)
            events.add(Pair(date, content))
        }
        cursor.close()
        db.close()
        return events
    }

}
