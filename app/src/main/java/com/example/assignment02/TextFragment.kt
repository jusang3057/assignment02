package com.example.assignment02

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class TextFragment : Fragment() {

    companion object {
        private val colors = arrayOf(
            Color.parseColor("#FF0000"), // Red
            Color.parseColor("#FF7F00"), // Orange
            Color.parseColor("#FFFF00"), // Yellow
            Color.parseColor("#00FF00"), // Green
            Color.parseColor("#0000FF"), // Blue
            Color.parseColor("#4B0082"), // Indigo
            Color.parseColor("#8A2BE2"), // Violet
            Color.parseColor("#FFFFFF")  // White
        )

        fun newInstance(text: String, colorIndex: Int): TextFragment {
            val fragment = TextFragment()
            val args = Bundle()
            args.putString("text", text)
            args.putInt("color", colorIndex)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_text, container, false)

        val text = arguments?.getString("text") ?: ""
        val color = arguments?.getInt("color") ?: 0

        val text_view = view.findViewById<TextView>(R.id.textView)
        text_view.setBackgroundColor(colors[color])
        text_view.text = text

        return view
    }
}
