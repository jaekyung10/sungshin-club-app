package edu.sswu.sungshinclub

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class ShortcutFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_shortcut, container, false)

        // 성신여대 포털
        val sungshinPortal = view.findViewById<TextView>(R.id.sungshinPortal)
        sungshinPortal.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://portal.sungshin.ac.kr"))
            startActivity(intent)
        }

        // 성신여대 홈페이지
        val sungshinHomePage = view.findViewById<TextView>(R.id.sungshinHomePage)
        sungshinHomePage.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.sungshin.ac.kr"))
            startActivity(intent)
        }

        // 성신여대 LMS
        val sungshinLMS = view.findViewById<TextView>(R.id.sungshinLMS)
        sungshinLMS.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://lms.sungshin.ac.kr"))
            startActivity(intent)
        }

        return view
    }
}
