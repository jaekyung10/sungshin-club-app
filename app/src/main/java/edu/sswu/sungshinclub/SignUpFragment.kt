package edu.sswu.sungshinclub

import edu.sswu.sungshinclub.LoginActivity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment

class SignUpFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sign_up, container, false)

        val nameInput = view.findViewById<EditText>(R.id.editTextName)
        val studentIdInput = view.findViewById<EditText>(R.id.editTextStudentId)
        val passwordInput = view.findViewById<EditText>(R.id.editTextPassword)
        val submitButton = view.findViewById<TextView>(R.id.btnSubmit)
        val sharedPreferences = requireContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val goBack = view.findViewById<ImageButton>(R.id.goBack)
        goBack.setOnClickListener{
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
        }
        submitButton.setOnClickListener {
            val name = nameInput.text.toString()
            val studentId = studentIdInput.text.toString()
            val password = passwordInput.text.toString()

            if (name.isNotEmpty() && studentId.isNotEmpty() && password.isNotEmpty()) {
                if (studentId.matches(Regex("\\d{8}"))) { // 학번 8자리 확인
                    // 사용자 데이터 저장
                    val editor = sharedPreferences.edit()
                    editor.putString("$studentId:name", name)
                    editor.putString("$studentId:password", password)
                    editor.putString("currentUser", studentId) // 현재 사용자 저장
                    editor.apply()

                    Toast.makeText(context, "회원가입 성공!", Toast.LENGTH_SHORT).show()

                    // 로그인 화면으로 이동
                    val intent = Intent(activity, LoginActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                } else {
                    Toast.makeText(context, "학번은 정확히 8자리 숫자여야 합니다.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "모든 필드를 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
}
