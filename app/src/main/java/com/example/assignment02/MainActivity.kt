package com.example.assignment02

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val isFromNotification = intent?.getBooleanExtra("Notification", false) ?: false
        val data = intent?.getIntExtra("number", 0) ?: 0

        val fragment: Fragment = if (isFromNotification) {
            // Notification을 통해 실행된 경우 FragmentB 로드 및 데이터 전달
            RandomFragment.newInstance(data)
        } else {
            // 기본적으로 FragmentA 로드
            CountFragment.newInstance(0)
        }

        // 선택된 Fragment를 동적으로 할당
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}