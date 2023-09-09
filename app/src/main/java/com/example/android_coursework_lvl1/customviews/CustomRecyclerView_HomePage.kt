package com.example.android_coursework_lvl1.customviews

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.android_coursework_lvl1.R


const val CATEGORY_KEY = "category"
class CustomView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : LinearLayout(context, attrs, defStyleAttr) {

    private val recyclerView: RecyclerView
    private val allTextView: TextView
    private val titleTextView: TextView

    private var showAllButton: Boolean = false
    private var maxItemCount: Int = 20

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


    fun getAllMovies(categoryName: String) {
        allTextView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(CATEGORY_KEY, categoryName)
            findNavController().navigate(R.id.action_homepage_to_allMovies, bundle)
        }
    }

}
