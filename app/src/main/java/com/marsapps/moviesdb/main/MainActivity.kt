package com.marsapps.moviesdb.main

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputLayout
import com.marsapps.moviesdb.R
import com.marsapps.moviesdb.dp2px
import com.marsapps.moviesdb.impl.OnWatchListListener
import com.marsapps.moviesdb.main.viewModel.MainViewModel
import com.marsapps.moviesdb.pagerAdapter.SectionsPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val model: MainViewModel by viewModel()
    private var watchListListener: OnWatchListListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun onDestroy() {
        watchListListener = null
        model.closeConnection()
        super.onDestroy()
    }

    private fun initView() {
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        view_pager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(view_pager)
        tabs.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.white))
        tabs.setTabTextColors(ContextCompat.getColor(this, R.color.colorPrimaryLight), ContextCompat.getColor(this, R.color.white))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        super.onCreateOptionsMenu(menu)

        return true
    }

    /**
     * SearchView should be only in Movies list.
     */
    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.getItem(0)?.isVisible = tabs.selectedTabPosition == 0
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.action_item_add -> {
                showAddListAlertDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun setOnWatchListListener(listener: OnWatchListListener) {
        watchListListener = listener
    }

    fun showLoading(show: Boolean) {
        main_progress.visibility = if (show)
            View.VISIBLE
        else
            View.GONE

        enableScreen(!show)
    }

    private fun enableScreen(enable: Boolean) {
        dim_view.visibility = if (enable) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            View.GONE
        }
        else {
            window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            View.VISIBLE
        }
    }

    private fun showAddListAlertDialog() {
        showKeyboard(true)

        val builder = AlertDialog.Builder(this, R.style.CustomDialogTheme)
        val textInputLayout = TextInputLayout(this)
        val editText = EditText(this)

        editText.setTextColor(ContextCompat.getColor(this, R.color.white))

        // Textview in dialog has margin 14dp and padding 5dp by default.
        textInputLayout.setPadding(19.dp2px, 0, 19.dp2px, 0)
        textInputLayout.addView(editText)

        builder.setView(textInputLayout)
        builder.setTitle(getString(R.string.new_watchlist))

        builder.setPositiveButton(getString(R.string.ok)) { _, _ ->
            editText.text.toString().apply {
                val add = model.addCustomWatchlist(this)

                if (add)
                    watchListListener?.onWatchListAdded(this)
            }
        }

        builder.setNegativeButton(getString(R.string.cancel)) { _, _ -> }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()

        alertDialog.setOnDismissListener {
            showKeyboard(false)
        }

        listOf(AlertDialog.BUTTON_POSITIVE, AlertDialog.BUTTON_NEGATIVE).forEach {
            alertDialog.getButton(it).setTextColor(ContextCompat.getColor(this, R.color.colorAccent))
        }
    }

    // Show keyboard
    private fun showKeyboard(show: Boolean) {
        if (show)
            (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).toggleSoftInput(
                InputMethodManager.SHOW_FORCED, 0)
        else
            (getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager).toggleSoftInput(
                InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
    }
}