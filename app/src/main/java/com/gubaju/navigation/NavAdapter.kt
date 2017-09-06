package com.gubaju.navigation

import android.content.Context
import android.widget.FrameLayout
import com.gubaju.navigationdrawer.MenuItem
import com.gubaju.navigationdrawer.TopNavigationAdapter


/**
 * Created by sanjay on 9/3/17.
 */
class NavAdapter(private val context: Context, items: List<NavModel>) : TopNavigationAdapter<NavModel>(items) {

    override val items: List<NavModel>
        get() = super.items

    override fun createView(position: Int, item: NavModel): MenuItem {
        val i = MenuItem(context)
        i.icon = item.getImageDrawable()
        i.title = item.getText()
        i.layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT)
        return i
    }
}
