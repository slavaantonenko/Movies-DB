package com.marsapps.moviesdb.base

import android.os.Handler
import androidx.fragment.app.Fragment
import com.marsapps.moviesdb.Constants.WebView.TIMEOUT
import com.marsapps.moviesdb.main.MainActivity
import com.marsapps.moviesdb.openYoutube

abstract class BaseFragment : Fragment() {

    abstract fun initView()

    abstract fun initListeners()

    /**
     * Open youtube with relevant movie in webview. The screen becomes disabled.
     * There is a timeout of 10 seconds to open the webview after that the screen will be enabled.
     */
    fun openYoutube(title: String) {
        val runnable = Runnable { (requireActivity() as MainActivity).showLoading(false) }
        val handler = Handler()
        handler.postDelayed(runnable, TIMEOUT)

        (requireActivity() as MainActivity).showLoading(true)

        title.openYoutube(requireContext(), title) {
            (requireActivity() as MainActivity).showLoading(false)
            handler.removeCallbacks(runnable)
        }
    }
}