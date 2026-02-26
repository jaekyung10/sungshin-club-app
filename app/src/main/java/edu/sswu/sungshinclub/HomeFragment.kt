package edu.sswu.sungshinclub

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomeFragment : Fragment() {

    private lateinit var upcomingEventsRecyclerView: RecyclerView
    private lateinit var databaseHelper: EventDatabaseHelper
    private lateinit var adapter: UpcomingEventsAdapter
    private val upcomingEvents = mutableListOf<Pair<String, String>>() // 날짜와 이벤트 내용을 저장

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // SharedPreferences에서 사용자 정보 가져오기
        val sharedPreferences = requireContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val studentId = sharedPreferences.getString("currentUser", null) // 현재 사용자 학번
        val userName = sharedPreferences.getString("$studentId:name", "학생 이름") // 사용자 이름
        val selectedClub = sharedPreferences.getString("$studentId:selectedClub", null) // 선택된 동아리 정보

        // 사용자 정보 및 동아리 정보 설정
        val studentInfoTextView = view.findViewById<TextView>(R.id.studentInfo)
        studentInfoTextView.text = "$userName"

        val clubName: String
        val clubDescription: String
        if (!selectedClub.isNullOrEmpty()) {
            val splitData = selectedClub.split(":")
            clubName = splitData[0]
            clubDescription = splitData[1]
        } else {
            clubName = "동아리 이름이 없습니다."
            clubDescription = "동아리가 선택되지 않았습니다."
        }

        // 텍스트 뷰에 데이터 설정
        val homeClubNameTextView = view.findViewById<TextView>(R.id.homeClubName)
        val homeClubInfoTextView = view.findViewById<TextView>(R.id.homeClubInfo)
        homeClubNameTextView.text = clubName
        homeClubInfoTextView.text = clubDescription

        // 예정된 이벤트 RecyclerView 설정
        upcomingEventsRecyclerView = view.findViewById(R.id.upcomingEventsRecyclerView)
        databaseHelper = EventDatabaseHelper(requireContext())
        adapter = UpcomingEventsAdapter(upcomingEvents)

        upcomingEventsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        upcomingEventsRecyclerView.adapter = adapter

        loadUpcomingEvents() // 이벤트 불러오기

        return view
    }

    // 데이터베이스에서 이벤트 불러오기
    private fun loadUpcomingEvents() {
        upcomingEvents.clear()
        val dbEvents = databaseHelper.getAllUpcomingEvents() // 모든 이벤트를 가져옴
        upcomingEvents.addAll(dbEvents)
        adapter.notifyDataSetChanged()
    }

    // RecyclerView 어댑터
    private inner class UpcomingEventsAdapter(
        private val events: List<Pair<String, String>>
    ) : RecyclerView.Adapter<UpcomingEventsAdapter.EventViewHolder>() {

        inner class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val textEventDate: TextView = itemView.findViewById(R.id.textEventDate)
            val textEventContent: TextView = itemView.findViewById(R.id.textEventContent)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
            val view = layoutInflater.inflate(R.layout.item_event_home, parent, false)
            return EventViewHolder(view)
        }

        override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
            val (date, content) = events[position]
            holder.textEventDate.text = date
            holder.textEventContent.text = content
        }

        override fun getItemCount(): Int = events.size
    }
}
