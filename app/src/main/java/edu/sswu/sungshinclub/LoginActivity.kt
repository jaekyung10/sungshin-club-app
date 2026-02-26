package edu.sswu.sungshinclub

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 로그인 상태 확인
        val sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        if (isLoggedIn) {
            // 로그인 상태가 유지되어 있으면 HomeActivity로 이동
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        setContentView(R.layout.activity_login)

        val studentIdInput = findViewById<EditText>(R.id.editStudentId)
        val passwordInput = findViewById<EditText>(R.id.editPassword)
        val loginButton = findViewById<TextView>(R.id.btnLogin)
        val signUpButton = findViewById<TextView>(R.id.btnSignUp)

        signUpButton.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        loginButton.setOnClickListener {
            val studentId = studentIdInput.text.toString()
            val password = passwordInput.text.toString()

            if (studentId.isNotEmpty() && password.isNotEmpty()) {
                val storedPassword = sharedPreferences.getString("$studentId:password", null)
                if (storedPassword != null && storedPassword == password) {
                    // 로그인 성공 시 상태 저장
                    val editor = sharedPreferences.edit()
                    editor.putBoolean("isLoggedIn", true)
                    editor.putString("currentUser", studentId) // 현재 사용자 학번 저장
                    editor.apply()

                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "학번 또는 비밀번호가 잘못되었습니다.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "학번과 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
