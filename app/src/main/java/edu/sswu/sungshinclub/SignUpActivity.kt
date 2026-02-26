package edu.sswu.sungshinclub

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up) // 올바른 레이아웃 설정

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, SignUpFragment()) // 동적으로 프래그먼트 추가
                .commit()
        }
    }
}
