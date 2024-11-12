package com.example.assignment02

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class RandomActivity : AppCompatActivity()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_random)
        val intent = Intent()
        val number = intent.getIntExtra("number", 0)

        // 어떤 작업 후 RandomActivity 종료
        val random_number = Random.nextInt(0, number)  // 0부터 99까지의 랜덤 값
        intent.putExtra("random_number", random_number)
        setResult(RESULT_OK, intent)
    }
}