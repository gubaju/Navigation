package com.gubaju.navigationdrawer

import java.io.Serializable

/**
 * Created by sanjay on 9/3/17.
 */
interface MenuModel : Serializable {
    fun getText(): String
    fun getImageDrawable(): Int
}
