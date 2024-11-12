package com.example.assignment02

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val number_text: TextView = findViewById(R.id.number_text)
        val count_btn: TextView = findViewById(R.id.count_btn)
        val toast_btn: TextView = findViewById(R.id.toast_btn)
        val random_btn: TextView = findViewById(R.id.random_btn)
        val intent = Intent(this, RandomActivity::class.java)

        val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK)
                Toast.makeText(this, "OK", Toast.LENGTH_SHORT).show()
            else
                Toast.makeText(this, "오류", Toast.LENGTH_SHORT).show()
        }

        count_btn.setOnClickListener {
            number_text.text = (number_text.text.toString().toInt()+1).toString()
        }
        toast_btn.setOnClickListener {
            Toast.makeText(this, "숫자 : "+number_text.text, Toast.LENGTH_LONG).show()
        }
        random_btn.setOnClickListener {
            intent.putExtra("number", number_text.text.toString().toInt())
            resultLauncher.launch(intent)
        }
    }
}