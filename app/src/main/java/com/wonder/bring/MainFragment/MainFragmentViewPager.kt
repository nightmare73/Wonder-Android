package com.wonder.bring.MainFragment

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent

/**
 * Created by maetel.leejh on 2017-10-25.
 */

class MainFragmentViewPager(context: Context, attrs: AttributeSet) : ViewPager(context, attrs) {

//    private var enabled: Boolean

    private var enable : Boolean

    init { this.enable = true }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (this.enable) {
            super.onTouchEvent(event)

        } else false

    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return if (this.enable) {
            super.onInterceptTouchEvent(event)
        } else false

    }

    fun setPagingEnabled(enabled: Boolean) {
        this.enable = enabled
    }
}