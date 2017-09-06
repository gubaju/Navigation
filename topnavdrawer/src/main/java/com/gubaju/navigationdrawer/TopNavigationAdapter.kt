package com.gubaju.navigationdrawer

/**
 * Created by sanjay on 9/3/17.
 */

abstract class TopNavigationAdapter<T>(open val items: List<T>) {
    abstract fun createView(position: Int, item: T): MenuItem
}
