package com.gubaju.navigationdrawer

import android.content.Context
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.item_menu.view.*
import java.io.Serializable

/**
 * Created by sanjay on 9/3/17.
 */

class MenuItem : FrameLayout, Serializable {

    var icon: Int? = null
        set(value) {
            if (value != null)
                itemMenuThumb.setImageDrawable(ContextCompat.getDrawable(context, value))
        }

    var title: String? = null
        set(value) {
            if (value != null)
                itemMenuTitle.text = value
        }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleRes: Int) : super(context, attrs, defStyleRes)

    init {
        LayoutInflater.from(context).inflate(R.layout.item_menu, this, true)
        itemMenuThumb.setOnClickListener { Log.e("Clicked", "Item") }
    }

}