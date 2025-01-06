package com.example.assignment02

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.BatteryManager
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ListFragment : Fragment() {

    private val data = mutableListOf<Item>()
    private lateinit var adapter: Adapter
    private lateinit var name: TextView
    private lateinit var time: TextView
    private lateinit var energy: LinearLayout

    companion object {
        fun newInstance(): ListFragment {
            return ListFragment()
        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_list, container, false)

        name = view.findViewById(R.id.name)
        time = view.findViewById(R.id.time)
        energy = view.findViewById(R.id.energe)

        // 배터리에 따라 배경색 변경하기!
        val battery = getBatteryData(requireContext()) // 0~100(%)
        energy.backgroundTintList = ColorStateList.valueOf(ColorUtils.blendARGB(Color.parseColor("#00B0FF"), Color.parseColor("#FFFFFF"), battery / 100f))

        val recycler_list: RecyclerView = view.findViewById(R.id.recycler_list)
        recycler_list.layoutManager = LinearLayoutManager(requireContext())
        adapter = Adapter(data, name, time)
        recycler_list.adapter = adapter

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        ) {
            openFragment()
        }
        else if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.FOREGROUND_SERVICE) != PackageManager.PERMISSION_GRANTED
        ) {
            openFragment()
        }
        else {
            // 권한이 있음
            getMusicFilesData()
        }

        return view
    }

    fun getBatteryData(context: Context): Int {
        val batteryManager = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        return batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
    }

    fun getMusicFilesData() {
        val contentResolver: ContentResolver = requireContext().contentResolver
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATA
        )

        val cursor = contentResolver.query(uri, projection, null, null, null)

        cursor?.let {
            val titleIndex = it.getColumnIndex(MediaStore.Audio.Media.TITLE)
            val artistIndex = it.getColumnIndex(MediaStore.Audio.Media.ARTIST)
            val durationIndex = it.getColumnIndex(MediaStore.Audio.Media.DURATION)
            val dataIndex = it.getColumnIndex(MediaStore.Audio.Media.DATA)

            while (it.moveToNext()) {
                val item = Item(it.getString(titleIndex) ?: "Unknown Title", it.getString(artistIndex) ?: "Unknown Artist", it.getLong(durationIndex) ?: 0, it.getString(dataIndex) ?: "")
                data.add(item)
            }
            adapter.notifyDataSetChanged()
            it.close()
        } ?: run {
            Log.e("MediaStore", "오디오 파일을 찾을 수 없습니다.")
        }
    }

    fun openFragment() {
        val fragment_authority = AuthorityFragment.newInstance()
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment_authority)
            .addToBackStack(null)
            .commit()
    }

    class Adapter(private val data: MutableList<Item>, private var name_view:TextView, private var time_view:TextView) : RecyclerView.Adapter<Adapter.ViewHolder>() {

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val title: TextView = itemView.findViewById(R.id.title)
            val artist: TextView = itemView.findViewById(R.id.artist)
            val duration: TextView = itemView.findViewById(R.id.duration)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_list_item, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = data[position]
            holder.title.text = item.title
            holder.artist.text = item.artist
            val m = (item.duration/60000).toString()
            val s = (item.duration/1000 %60).toString()
            holder.duration.text =  "$m:$s"

            holder.itemView.setOnClickListener {
                name_view.text = item.title
                time_view.text = holder.duration.text
                val intent = Intent(holder.itemView.context, PlayMusic::class.java).apply {
                    putExtra("uri", item.uri)
                }
                ContextCompat.startForegroundService(holder.itemView.context, intent)
            }
        }

        override fun getItemCount(): Int = data.size
    }

    data class Item(val title: String, val artist: String, val duration: Long, val uri: String)
}
