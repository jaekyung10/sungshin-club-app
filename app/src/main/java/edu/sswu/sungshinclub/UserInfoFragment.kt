package edu.sswu.sungshinclub

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment

class UserInfoFragment : Fragment(R.layout.fragment_user_info) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = requireContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val studentId = sharedPreferences.getString("currentUser", null)
        val userName = sharedPreferences.getString("$studentId:name", "학생 이름")

        // 사용자 이름 표시
        val userNameTextView = view.findViewById<TextView>(R.id.userNameTextView)
        userNameTextView.text = userName

        // 로그아웃 버튼
        val logoutButton = view.findViewById<Button>(R.id.logoutButton)
        logoutButton.setOnClickListener {
            val editor = sharedPreferences.edit()
            editor.putBoolean("isLoggedIn", false)
            editor.putString("currentUser", null)
            editor.apply()

            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

        // 동아리 등록하기
        val addClubTextView = view.findViewById<TextView>(R.id.addClub)
        addClubTextView.setOnClickListener {
            val dialogLayout = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_club, null)
            val clubNameInput = dialogLayout.findViewById<EditText>(R.id.clubNameInput)
            val clubDescriptionInput = dialogLayout.findViewById<EditText>(R.id.clubDescriptionInput)

            val dialog = AlertDialog.Builder(requireContext())
                .setTitle("동아리 등록")
                .setView(dialogLayout)
                .setPositiveButton("등록") { _, _ ->
                    val clubName = clubNameInput.text.toString()
                    val clubDescription = clubDescriptionInput.text.toString()
                    if (clubName.isNotEmpty() && clubDescription.isNotEmpty()) {
                        val editor = sharedPreferences.edit()
                        val clubList = sharedPreferences.getStringSet("$studentId:clubs", mutableSetOf()) ?: mutableSetOf()
                        clubList.add("$clubName:$clubDescription")
                        editor.putStringSet("$studentId:clubs", clubList)
                        editor.apply()
                        Toast.makeText(requireContext(), "$clubName 동아리가 등록되었습니다.", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "모든 필드를 입력하세요.", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("취소", null)
                .create()
            dialog.show()
        }

        // 동아리 선택하기
        val selectClubTextView = view.findViewById<TextView>(R.id.selectClub)
        selectClubTextView.setOnClickListener {
            val clubList = sharedPreferences.getStringSet("$studentId:clubs", null)
            if (clubList.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "등록된 동아리가 없습니다.", Toast.LENGTH_SHORT).show()
            } else {
                val clubArray = clubList.map { it.split(":")[0] }.toTypedArray()
                val dialog = AlertDialog.Builder(requireContext())
                    .setTitle("동아리 선택")
                    .setItems(clubArray) { _, which ->
                        val selectedClub = clubList.first { it.startsWith(clubArray[which]) }
                        val (clubName, clubDescription) = selectedClub.split(":")
                        val editor = sharedPreferences.edit()
                        editor.putString("$studentId:selectedClub", selectedClub)
                        editor.apply()
                        Toast.makeText(requireContext(), "$clubName 동아리가 선택되었습니다", Toast.LENGTH_SHORT).show()
                    }
                    .create()
                dialog.show()
            }
        }
    }
}
