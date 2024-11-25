package com.example.assignment02

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

class CountFragment : Fragment() {

    private lateinit var numberText: TextView
    private var currentNumber: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_count, container, false)

        numberText = view.findViewById(R.id.number_text)
        val countButton = view.findViewById<Button>(R.id.count_btn)
        val toastButton = view.findViewById<Button>(R.id.toast_btn)
        val randomButton = view.findViewById<Button>(R.id.random_btn)

        currentNumber = arguments?.getInt("count") ?: 0
        numberText.text = currentNumber.toString()

        countButton.setOnClickListener {
            currentNumber++
            numberText.text = currentNumber.toString()
        }
        toastButton.setOnClickListener {
            showAlertDialog()
        }
        randomButton.setOnClickListener {
            val randomFragment = RandomFragment.newInstance(currentNumber)
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, randomFragment)
                .addToBackStack(null)
                .commit()
        }

        return view
    }

    private fun showAlertDialog() {
        // Fragment에서는 requireContext()를 사용하여 컨텍스트를 가져옴
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("ToastButton")
            .setMessage("초기화/Toast/취소")

            // Positive 버튼 설정
            .setPositiveButton("positive") { dialog, which ->
                currentNumber = 0
                numberText.text = currentNumber.toString()
            }

            // Neutral 버튼 설정
            .setNeutralButton("neutral") { dialog, which ->
                Toast.makeText(requireContext(), "Number: $currentNumber", Toast.LENGTH_SHORT).show()
            }

            // Negative 버튼 설정
            .setNegativeButton("negative") { dialog, which ->
            }

            // 다이얼로그 생성 및 표시
            .create()
            .show()
    }

    companion object {
        fun newInstance(count: Int): CountFragment {
            val fragment = CountFragment()
            val args = Bundle()
            args.putInt("count", count)
            fragment.arguments = args
            return fragment
        }
    }
}
