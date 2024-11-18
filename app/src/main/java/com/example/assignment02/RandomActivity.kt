package com.example.assignment02

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class RandomActivity : AppCompatActivity()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_random)
        val number_text: TextView = findViewById(R.id.number_text)
        val number = intent.getIntExtra("number", 1) // 123
        val resultIntent = Intent()

        if (number > 0){
            val random_number = Random.nextInt(0, number)  // 0부터 99까지의 랜덤 값
            number_text.text = random_number.toString()
            resultIntent.putExtra("random_number", random_number)
            setResult(RESULT_OK, resultIntent)
        } else {
            number_text.text = number.toString()
            resultIntent.putExtra("random_number", number)
            setResult(RESULT_OK, resultIntent)
        }
    }
}