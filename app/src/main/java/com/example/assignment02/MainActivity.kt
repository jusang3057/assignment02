package com.example.assignment02

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottom_nav: BottomNavigationView
    private lateinit var view_page: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        view_page = findViewById(R.id.viewPage)
        bottom_nav = findViewById(R.id.bottomNav)
        view_page.adapter = ColorAdapter(this)

        bottom_nav.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.color -> {
                    view_page.adapter = ColorAdapter(this)
                }
                R.id.number -> {
                    view_page.adapter = NumberAdapter(this)
                }
                R.id.alphabet -> {
                    view_page.adapter = AlphabetAdapter(this)
                }
            }
            true
        }
    }
    class ColorAdapter(fragment: MainActivity) : FragmentStateAdapter(fragment) {
        private val pageCount = 7
        override fun getItemCount(): Int = pageCount
        override fun createFragment(position: Int): Fragment {
            return TextFragment.newInstance("", position)
        }
    }
    class NumberAdapter(fragment: MainActivity) : FragmentStateAdapter(fragment) {
        private val pageCount = Int.MAX_VALUE
        override fun getItemCount(): Int = pageCount
        override fun createFragment(position: Int): Fragment {
            return TextFragment.newInstance((position+1).toString(), 7)
        }
    }
    class AlphabetAdapter(fragment: MainActivity) : FragmentStateAdapter(fragment) {
        private val pageCount = 26
        override fun getItemCount(): Int = pageCount
        override fun createFragment(position: Int): Fragment {
            return TextFragment.newInstance(('a'+position).toString(), 7)
        }
    }
}

