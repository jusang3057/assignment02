package com.example.assignment02

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    val nameList = mutableListOf<String>()

    lateinit var name_text: EditText
    lateinit var recycler_view: RecyclerView
    lateinit var nameAdapter: NameAdapter
    lateinit var add_button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        name_text = findViewById(R.id.name_text)
        recycler_view = findViewById(R.id.recycler_view)
        nameAdapter = NameAdapter(nameList) { position ->showDeleteDialog(position)}
        add_button = findViewById(R.id.add_button)

        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = nameAdapter

        add_button.setOnClickListener {
            nameList.add(name_text.text.toString())
            nameAdapter.notifyItemInserted(nameList.size - 1)
            name_text.setText("")
        }
    }

    private fun showDeleteDialog(position: Int) {
        AlertDialog.Builder(this)
            .setTitle("이름 목록 삭제하기")
            .setMessage("이름 목록을 삭제해보자.")
            .setPositiveButton("확인") { dialog, _ ->
                showEditDialog(position)
                dialog.dismiss()
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }
    private fun showEditDialog(position: Int) {
        val editText = EditText(this).apply {
            hint = "수정할 이름을 입력하세요."
            setText("")
        }

        AlertDialog.Builder(this)
            .setTitle(" ")
            .setView(editText)
            .setPositiveButton("확인") { _, _ ->
                nameList[position] = editText.text.toString()
                nameAdapter.notifyItemChanged(position)
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }
}
