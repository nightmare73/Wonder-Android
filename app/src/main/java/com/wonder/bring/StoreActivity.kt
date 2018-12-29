package com.wonder.bring

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.FrameLayout

class StoreActivity : AppCompatActivity() {

    private var pg_btn: Button? = null
    private var photo_btn: Button? = null


    private val FRAGMENT1 = 1
    private val FRAGMENT2 = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store)

//        pg_btn = view.findViewById(R.id.fm_pg_btn) as Button
//        photo_btn = view.findViewById(R.id.fm_photo_btn) as Button
//
//        mypick_fragment = view.findViewById(R.id.mypick_fragment) as FrameLayout
//
//        callFragment(FRAGMENT1)
//
//
//        pg_btn!!.setOnClickListener(this)
//        photo_btn!!.setOnClickListener(this)
//
//
//        return view
    }

    fun onClick(v: View) {
//        when (v.id) {
//            R.id.fm_pg_btn ->
//
//                callFragment(FRAGMENT1)
//            R.id.fm_photo_btn -> {
//                photo_btn!!.setTextColor(Color.parseColor("#ffffff"))
//                pg_btn!!.setTextColor(Color.parseColor("#A5A5A5"))
//                photo_btn!!.setBackgroundColor(Color.parseColor("#000000"))
//                pg_btn!!.setBackgroundColor(Color.parseColor("#e9e9e9"))
//                callFragment(FRAGMENT2)
//            }
//        }
    }


    private fun callFragment(frament_no: Int) {

        // 프래그먼트 사용을 위해
        val transaction = fragmentManager.beginTransaction()

        when (frament_no) {
            1 -> {
                // '프래그먼트1' 호출
//                pg_btn!!.setTextColor(Color.parseColor("#ffffff"))
//                photo_btn!!.setTextColor(Color.parseColor("#A5A5A5"))
//                pg_btn!!.setBackgroundColor(Color.parseColor("#000000"))
//                photo_btn!!.setBackgroundColor(Color.parseColor("#e9e9e9"))
//                val mypick_pg_fragment = MyPickPhotographerFragment()
//                transaction.replace(R.id.mypick_fragment, mypick_pg_fragment)
//                transaction.commit()
            }

            2 -> {
//                // '프래그먼트2' 호출
//                val mypick_photo_fragment = MyPickPhotoFragment()
//                transaction.replace(R.id.mypick_fragment, mypick_photo_fragment)
//                transaction.commit()
            }
        }

    }

}
