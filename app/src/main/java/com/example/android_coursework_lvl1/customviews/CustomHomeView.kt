package com.example.android_coursework_lvl1.customviews

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.android_coursework_lvl1.R


const val CATEGORY_KEY = "category"

class CustomView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : RelativeLayout(context, attrs, defStyleAttr) {

    private val recyclerView: RecyclerView
    private val allTextView: TextView
    private val titleTextView: TextView
    init {
        LayoutInflater.from(context).inflate(R.layout.custom_view_layout, this, true)

        recyclerView = findViewById(R.id.custom_rv)
        allTextView = findViewById(R.id.custom_all_text)
        titleTextView = findViewById(R.id.custom_title_text)
    }

    fun setRecyclerViewAdapter(adapter: RecyclerView.Adapter<*>) {
        recyclerView.adapter = adapter
    }

    fun setTitleText(text: String) {
        titleTextView.text = text
    }

    fun check(adapter: RecyclerView.Adapter<*>) {
        if (adapter.itemCount == 0){
            recyclerView.visibility = View.INVISIBLE
            allTextView.visibility = View.INVISIBLE
            titleTextView.visibility = View.INVISIBLE
        }
        if (adapter.itemCount >= 20) {
            recyclerView.visibility = View.VISIBLE
            allTextView.visibility = View.VISIBLE
            titleTextView.visibility = View.VISIBLE
        } else
            allTextView.visibility = View.INVISIBLE
    }


    fun getAllMovies(categoryName: String) {
        allTextView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(CATEGORY_KEY, categoryName)
            findNavController().navigate(R.id.action_homepage_to_allMovies, bundle)
        }
    }

}
