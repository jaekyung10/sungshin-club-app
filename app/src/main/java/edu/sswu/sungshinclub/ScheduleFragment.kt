package edu.sswu.sungshinclub

import android.app.AlertDialog
import android.content.ContentValues
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment

class ScheduleFragment : Fragment(R.layout.fragment_schedule) {

    private lateinit var calendarView: CalendarView
    private lateinit var eventListView: ListView
    private lateinit var adapter: EventAdapter
    private lateinit var databaseHelper: EventDatabaseHelper
    private var events = mutableListOf<String>()
    private var selectedDate: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 뷰 초기화
        calendarView = view.findViewById(R.id.scheduleCalendar)
        eventListView = view.findViewById(R.id.eventListView)
        val addEventButton: Button = view.findViewById(R.id.addEventButton)

        // 데이터베이스 초기화
        databaseHelper = EventDatabaseHelper(requireContext())

        // 커스텀 어댑터 초기화
        adapter = EventAdapter(events)
        eventListView.adapter = adapter

        // 캘린더 날짜 선택 리스너
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            selectedDate = "$year-${month + 1}-$dayOfMonth"
            Toast.makeText(requireContext(), "Selected date: $selectedDate", Toast.LENGTH_SHORT).show()
            loadEvents(selectedDate) // 선택한 날짜의 이벤트 불러오기
        }

        // Add Event 버튼 클릭 리스너
        addEventButton.setOnClickListener {
            if (selectedDate.isNotEmpty()) {
                showAddEventDialog()
            } else {
                Toast.makeText(requireContext(), "Please select a date first!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // 이벤트 추가 대화상자
    private fun showAddEventDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_event, null)
        val editTextEvent = dialogView.findViewById<EditText>(R.id.editTextEvent)

        AlertDialog.Builder(requireContext())
            .setTitle("Add Event")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val eventText = editTextEvent.text.toString()
                if (eventText.isNotEmpty()) {
                    databaseHelper.addEvent(selectedDate, eventText) // 이벤트 저장
                    loadEvents(selectedDate) // 저장 후 이벤트 다시 불러오기
                    Toast.makeText(requireContext(), "Event added", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Event cannot be empty", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    // 이벤트 수정 대화상자
    private fun showEditEventDialog(oldEvent: String) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_event, null)
        val editTextEvent = dialogView.findViewById<EditText>(R.id.editTextEvent)
        editTextEvent.setText(oldEvent) // 기존 이벤트 내용으로 초기화

        AlertDialog.Builder(requireContext())
            .setTitle("Edit Event")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val newEvent = editTextEvent.text.toString()
                if (newEvent.isNotEmpty()) {
                    updateEvent(oldEvent, newEvent) // 이벤트 수정
                    Toast.makeText(requireContext(), "Event updated", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Event cannot be empty", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    // 이벤트 수정 메서드
    private fun updateEvent(oldEvent: String, newEvent: String) {
        val db = databaseHelper.writableDatabase
        val values = ContentValues()
        values.put(EventDatabaseHelper.COLUMN_EVENT, newEvent)

        db.update(
            EventDatabaseHelper.TABLE_EVENTS,
            values,
            "${EventDatabaseHelper.COLUMN_EVENT} = ? AND ${EventDatabaseHelper.COLUMN_DATE} = ?",
            arrayOf(oldEvent, selectedDate)
        )
        loadEvents(selectedDate) // 수정 후 이벤트 다시 불러오기
    }

    // 이벤트 삭제 대화상자
    private fun showDeleteEventDialog(event: String) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Event")
            .setMessage("Are you sure you want to delete this event?")
            .setPositiveButton("Yes") { _, _ ->
                deleteEvent(event) // 이벤트 삭제
                Toast.makeText(requireContext(), "Event deleted", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("No", null)
            .show()
    }

    // 이벤트 삭제 메서드
    private fun deleteEvent(event: String) {
        databaseHelper.writableDatabase.delete(
            EventDatabaseHelper.TABLE_EVENTS,
            "${EventDatabaseHelper.COLUMN_EVENT} = ? AND ${EventDatabaseHelper.COLUMN_DATE} = ?",
            arrayOf(event, selectedDate)
        )
        loadEvents(selectedDate) // 삭제 후 이벤트 다시 불러오기
    }

    // 날짜에 해당하는 이벤트 불러오기
    private fun loadEvents(date: String) {
        events.clear()
        events.addAll(databaseHelper.getEventsByDate(date)) // 데이터베이스에서 이벤트 불러오기
        adapter.notifyDataSetChanged()
    }

    // 커스텀 어댑터 클래스
    private inner class EventAdapter(
        private val events: MutableList<String>
    ) : BaseAdapter() {

        override fun getCount(): Int = events.size

        override fun getItem(position: Int): Any = events[position]

        override fun getItemId(position: Int): Long = position.toLong()

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view = convertView ?: LayoutInflater.from(requireContext())
                .inflate(R.layout.item_event, parent, false)

            val textEvent = view.findViewById<TextView>(R.id.textEvent)
            val btnDeleteEvent = view.findViewById<Button>(R.id.btnDeleteEvent)
            val btnEditEvent = view.findViewById<Button>(R.id.btnEditEvent)

            val event = events[position]
            textEvent.text = event

            // 삭제 버튼 클릭 리스너
            btnDeleteEvent.setOnClickListener {
                showDeleteEventDialog(event)
            }

            // 수정 버튼 클릭 리스너
            btnEditEvent.setOnClickListener {
                showEditEventDialog(event)
            }

            return view
        }
    }
}
