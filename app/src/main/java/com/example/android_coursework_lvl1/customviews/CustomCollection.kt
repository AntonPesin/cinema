package com.example.android_coursework_lvl1.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.android_coursework_lvl1.R

class CustomCollection @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : LinearLayout(context, attrs, defStyleAttr) {

    private var image: ImageView
    private var deleteIcon: ImageView
    private var collectionName: TextView

    init {
        LayoutInflater.from(context).inflate(R.layout.custom_collection_item, this, true)

        deleteIcon = findViewById(R.id.custom_collection_button)
        collectionName = findViewById(R.id.custom_collection_name)
        image = findViewById(R.id.custom_collection_image)
        setupListeners()
    }

    private fun setupListeners() {
        deleteIcon.setOnClickListener {
            visibility = View.GONE
        }

    }
}