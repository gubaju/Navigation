package com.gubaju.navigationdrawer

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.Toolbar
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.view_top_nav.view.*

/**
 * Created by sanjay on 9/3/17.
 */
class TopNavigationDrawer<T : MenuModel> : FrameLayout {

    var adapter: TopNavigationAdapter<T>? = null

    private var toolbarTitle: String? = null
    private var toolbarIcon: Drawable? = null

    private var toolbar: Toolbar? = null
    private val ANIMATION_FAST: Long = 200
    private val ANIMATION_SLOW: Long = 400
    private var isCollapsed: Boolean = true
    private var isBusy: Boolean = false
    private val menuList: LinkedHashMap<MenuItem, T> = LinkedHashMap()

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        LayoutInflater.from(context).inflate(R.layout.view_top_nav, this, true)
    }

    fun build() {

        if (!validate()) return
        menuList.clear()
        viewTopNavExpandedNavigationView.post {
            adapter?.items?.forEachIndexed { i, item ->
                val view: MenuItem = adapter?.createView(i, item)!!
                viewTopNavExpandedNavigationView.addView(view)
                menuList.put(view, item)
            }
        }

    }

    private fun validate(): Boolean = adapter != null && adapter?.items != null && !adapter?.items?.isEmpty()!!

    fun setToolbar(activityMainToolbar: Toolbar) {
        this.toolbar = activityMainToolbar

        // Get current title
        toolbarTitle = toolbar?.title.toString()
        toolbarIcon = toolbar?.navigationIcon

        // Collapsed
        viewTopNavExpandedHolder.translationY = -1000f
        viewTopNavExpandedNavigationView.translationY = -1000f
    }

    // Toggle between expanded and collapsed states
    fun toggle() {
        if (!isBusy) {
            if (isCollapsed) {
                expand()
            } else {
                collapse()
            }
        }
    }

    // Expanded View
    private fun expand() {
        // Change collapse status
        isCollapsed = false

        ValueAnimator.ofFloat(0f, ANIMATION_FAST.toFloat())
                .setDuration(ANIMATION_FAST)
                .apply {
                    addUpdateListener {
                        isBusy = true

                        val ratio = it.animatedValue as Float / ANIMATION_FAST
                        viewTopNavExpandedHolder.translationY = -viewTopNavExpandedHolder.height.toFloat() * (1 - ratio)
                        viewTopNavExpandedNavigationView.translationY = -viewTopNavExpandedHolder.height.toFloat() * (1 - ratio)

                        // Animate toolbar icon
                        toolbar?.setTitle(R.string.title_menu)
                        toolbar?.navigationIcon = ContextCompat.getDrawable(context, R.drawable.ic_close)

                        if (ratio == 1f) {
                            isBusy = false
                        }
                    }
                }.start()
    }

    // Collapsed View
    private fun collapse() {
        // Change collapse status
        isCollapsed = true

        ValueAnimator.ofFloat(0f, ANIMATION_SLOW.toFloat())
                .setDuration(ANIMATION_SLOW)
                .apply {
                    addUpdateListener {
                        isBusy = true

                        val ratio = it.animatedValue as Float / ANIMATION_SLOW
                        viewTopNavExpandedHolder.translationY = ratio * (-measuredHeight)
                        viewTopNavExpandedNavigationView.translationY = ratio * (-measuredHeight)

                        // Animate toolbar icon
                        toolbar?.title = toolbarTitle
                        toolbar?.navigationIcon = toolbarIcon

                        if (ratio == 1f) {
                            isBusy = false
                        }
                    }
                }.start()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        for (i in 0 until childCount) {
            val child = getChildAt(i) as View

            val holder = child.findViewById(R.id.viewTopNavExpandedHolder)
            val view = child.findViewById(R.id.viewTopNavExpandedNavigationView)

            val params = view.layoutParams as FrameLayout.LayoutParams
            holder.layoutParams = params
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

}
