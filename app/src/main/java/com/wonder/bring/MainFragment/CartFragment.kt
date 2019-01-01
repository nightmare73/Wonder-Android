package com.wonder.bring.MainFragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wonder.bring.LoginProcess.LoginActivity

import com.wonder.bring.R
import com.wonder.bring.db.SharedPreferenceController
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast


class CartFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {





        return inflater.inflate(R.layout.fragment_cart, container, false)
    }


}
