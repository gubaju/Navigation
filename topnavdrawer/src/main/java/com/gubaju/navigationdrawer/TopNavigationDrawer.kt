package com.gubaju.navigationdrawer

import android.animation.ValueAnimator
import android.content.Context
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

    private val ANIMATION_DURATION_OPEN: Long = 200
    private val ANIMATION_DURATION_CLOSE: Long = 400
    private var isCollapsed: Boolean = false
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

        ValueAnimator.ofFloat(0f, ANIMATION_DURATION_OPEN.toFloat())
                .setDuration(ANIMATION_DURATION_OPEN)
                .apply {
                    addUpdateListener {
                        isBusy = true

                        val ratio = it.animatedValue as Float / ANIMATION_DURATION_OPEN
                        viewTopNavExpandedFrame.translationY = -viewTopNavExpandedFrame.height.toFloat() * (1 - ratio)
                        viewTopNavExpandedNavigationView.translationY = -viewTopNavExpandedFrame.height.toFloat() * (1 - ratio)

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

        ValueAnimator.ofFloat(0f, ANIMATION_DURATION_CLOSE.toFloat())
                .setDuration(ANIMATION_DURATION_CLOSE)
                .apply {
                    addUpdateListener {
                        isBusy = true

                        val ratio = it.animatedValue as Float / ANIMATION_DURATION_CLOSE
                        viewTopNavExpandedFrame.translationY = ratio * (-measuredHeight)
                        viewTopNavExpandedNavigationView.translationY = ratio * (-measuredHeight)

                        if (ratio == 1f) {
                            isBusy = false
                        }
                    }
                }.start()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        for (i in 0 until childCount) {
            val child = getChildAt(i) as View

            val holder = child.findViewById(R.id.viewTopNavExpandedFrame)
            val view = child.findViewById(R.id.viewTopNavExpandedNavigationView)

            val params = view.layoutParams as FrameLayout.LayoutParams
            holder.layoutParams = params
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }
}
