package com.gubaju.navigationdrawer

import android.content.Context
import android.content.res.Configuration
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import org.jetbrains.anko.configuration

/**
 * Created by sanjay on 9/3/17.
 */

class ExpandedNavigationView : ViewGroup {

    private var spanVertical: Int = 3
    private var spanHorizontal: Int = 5
    private var prevItem: View? = null
    private var prevX: Int? = null
    private var prevY: Int? = null
    private var prevHeight = 0
    private var startX = 0f
    private var startY = 0f

    private var margin: Int = dpToPx(getDimen(R.dimen.margin_medium))
    private val filters: LinkedHashMap<MenuItem, Coordinates> = LinkedHashMap()

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleRes: Int) : super(context, attrs, defStyleRes)

    override fun onLayout(p0: Boolean, p1: Int, p2: Int, p3: Int, p4: Int) {
        if (!filters.isEmpty()) {
            for (i in 0 until childCount) {
                val child: View = getChildAt(i)
                val coordinates: Coordinates? = filters[child]

                if (coordinates != null) {
                    child.layout(coordinates.x, coordinates.y, coordinates.x + child.measuredWidth, coordinates.y + child.measuredHeight)
                }

            }
        }
    }

    private fun canPlaceOnTheSameLine(filterItem: View): Boolean {
        if (prevItem != null) {
            val occupiedWidth: Int = prevX!! + prevItem!!.measuredWidth + margin + filterItem.measuredWidth
            return occupiedWidth <= measuredWidth
        }

        return false
    }

    private fun calculateDesiredHeight(): Int {
        var height: Int = prevHeight

        if (filters.isEmpty()) {
            for (i in 0 until childCount) {
                val child: MenuItem = getChildAt(i) as MenuItem

                child.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)

                if (prevItem == null) {
                    prevX = margin
                    prevY = margin
                    height = child.measuredHeight + margin
                } else if (canPlaceOnTheSameLine(child)) {
                    prevX = prevX!! + prevItem!!.measuredWidth + margin / 2
                } else {
                    prevX = margin
                    prevY = prevY!! + prevItem!!.measuredHeight + margin / 2
                    height += child.measuredHeight + margin / 2
                }

                prevItem = child

                if (filters.size < childCount) {
                    filters.put(child, Coordinates(prevX!!, prevY!!))
                }
            }

            height = if (height > 0) height + margin else 0
            prevHeight = height
        }

        return prevHeight
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {


        for (i in 0 until childCount) {
            val child: MenuItem = getChildAt(i) as MenuItem
            val thumb = child.findViewById(R.id.itemMenuThumb)

            val params = thumb.layoutParams as LayoutParams
            params.width = (calculateSize(widthMeasureSpec, LayoutParams.MATCH_PARENT) /
                    if (context.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                        spanVertical
                    } else {
                        spanHorizontal
                    }) - margin
            thumb.layoutParams = params
        }

        setMeasuredDimension(calculateSize(widthMeasureSpec, LayoutParams.MATCH_PARENT),
                calculateSize(heightMeasureSpec, calculateDesiredHeight()))
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = event.x
                startY = event.y
            }
            MotionEvent.ACTION_MOVE -> {
                if (event.y - startY < -20) {
                    startX = 0f
                    startY = 0f
                }
            }
        }

        return true
    }
}