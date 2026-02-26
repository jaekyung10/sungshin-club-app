package edu.sswu.sungshinclub

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // 기본 프래그먼트 설정
        if (savedInstanceState == null) {
            loadFragment(HomeFragment())
        }

        // BottomNavigationView 설정
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> loadFragment(HomeFragment())
                R.id.nav_schedule -> loadFragment(ScheduleFragment())
                R.id.nav_community -> loadFragment(ShortcutFragment())
            }
            true
        }

        // userInfoIcon 클릭 이벤트 처리
        val userInfoIcon = findViewById<ImageView>(R.id.userInfoIcon)
        userInfoIcon.setOnClickListener {
            loadFragment(UserInfoFragment())
        }
    }

    // 프래그먼트를 교체하는 함수
    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
