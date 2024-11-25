package com.example.assignment02

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.addCallback
import androidx.fragment.app.Fragment

class RandomFragment : Fragment() {

    private var currentNumber: Int = 0
    private lateinit var number_number: TextView

    companion object {
        fun newInstance(randomNumber: Int): RandomFragment {
            val fragment = RandomFragment()
            val args = Bundle()
            args.putInt("randomNumber", randomNumber)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_random, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        number_number = view.findViewById(R.id.number_text)

        arguments?.let {
            currentNumber = it.getInt("randomNumber", 0)
        }

        currentNumber = (0..currentNumber-1).random()
        number_number.text = currentNumber.toString()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            parentFragmentManager.popBackStack()

            val countFragment = CountFragment.newInstance(currentNumber)
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, countFragment)
                .commit()
        }
    }
}
