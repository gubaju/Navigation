package com.gubaju.navigation

/**
 * Created by sanjay on 9/3/17.
 */

class NavModel(private val title: String, private val drawable: Int) : com.gubaju.navigationdrawer.MenuModel {
    override fun getText(): String = title
    override fun getImageDrawable(): Int = drawable
}
