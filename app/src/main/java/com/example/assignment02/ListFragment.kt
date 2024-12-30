package com.example.assignment02

import android.Manifest
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class ListFragment : Fragment() {

    private val data = mutableListOf<Item>()
    private lateinit var adapter: Adapter

    companion object {
        fun newInstance(): ListFragment {
            return ListFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_list, container, false)

        val recycler_list: RecyclerView = view.findViewById(R.id.recycler_list)
        recycler_list.layoutManager = LinearLayoutManager(requireContext())
        adapter = Adapter(data)
        recycler_list.adapter = adapter

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        ) {
            // 권한이 있음 -> 음악 파일 추가
            getMusicFilesData()
        } else {
            // 권한이 없음
            openFragment()
        }

        return view
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
                Log.d("File Path", "음악 파일 경로: ${it.getString(dataIndex)}")

                val item = Item(it.getString(titleIndex) ?: "Unknown Title", it.getString(artistIndex) ?: "Unknown Artist", it.getLong(durationIndex) ?: 0)
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

    class Adapter(private val data: MutableList<Item>) : RecyclerView.Adapter<Adapter.ViewHolder>() {

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
        }

        override fun getItemCount(): Int = data.size
    }

    data class Item(val title: String, val artist: String, val duration: Long)

}
