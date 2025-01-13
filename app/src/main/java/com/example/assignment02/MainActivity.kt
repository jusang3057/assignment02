package com.example.assignment02

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private var running = false
    private var timer: Job? = null
    private var time: Long = 0
    private var stime: Long = 0
    private var etime: Long = 0

    private var adapter: TimeAdapter? = null
    private var time_list = mutableListOf<String>()

    private lateinit var time_text: TextView
    private lateinit var start_text: TextView
    private lateinit var stop_text: TextView
    private lateinit var lap_text: TextView
    private lateinit var time_view: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        time_text = findViewById(R.id.timeText)
        start_text = findViewById(R.id.startText)
        stop_text = findViewById(R.id.stopText)
        lap_text = findViewById(R.id.lapText)
        time_view = findViewById(R.id.timeList)

        time_view.layoutManager = LinearLayoutManager(this)
        time_view.adapter = TimeAdapter(time_list)

        start_text.setOnClickListener {
            if (running) {
                start_text.text = "START"
                pauseTimer()
            }
            if (!running) {
                start_text.text = "PAUSE"
                startTimer()
            }
            running = !running
        }
        stop_text.setOnClickListener {
            start_text.text = "START"
            time_text.text = String.format("00:00:00")
            running = false
            pauseTimer()
            time = 0
        }
        lap_text.setOnClickListener {
            time_list.add(time_text.text.toString())
            adapter?.notifyItemInserted(time_list.size - 1) // 리스트가 업데이트된 항목만 갱신
            time_view.scrollToPosition(time_list.size - 1)
        }
    }

    private fun startTimer(){
        stime = System.currentTimeMillis()
        timer = CoroutineScope(Dispatchers.Main).launch {
            while(true){
                delay(5)
                etime = System.currentTimeMillis()
                time += etime-stime
                time_text.text = String.format("%02d:%02d:%02d", time/60000, (time%60000)/1000, (time%1000)/10)
                stime = etime
            }
        }
    }

    private fun pauseTimer(){
        timer?.cancel()
    }
}

class TimeAdapter(private val timeList: List<String>) :
    RecyclerView.Adapter<TimeAdapter.TimeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)
        return TimeViewHolder(view)
    }

    override fun onBindViewHolder(holder: TimeViewHolder, position: Int) {
        holder.bind(timeList[position])
    }

    override fun getItemCount(): Int = timeList.size

    class TimeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val time_text: TextView = itemView.findViewById(R.id.timeText)

        fun bind(time: String) {
            time_text.text = time
        }
    }
}