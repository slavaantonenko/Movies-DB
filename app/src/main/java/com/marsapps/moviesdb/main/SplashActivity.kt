package com.marsapps.moviesdb.main

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.gms.maps.model.LatLng
import com.marsapps.moviesdb.R
import com.marsapps.moviesdb.checkPermissionGranted
import com.marsapps.moviesdb.getAddressFromLocation
import com.marsapps.moviesdb.main.viewModel.SplashViewModel
import com.marsapps.moviesdb.requestLocationPermission
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashActivity : AppCompatActivity(), LocationListener {

    private val model: SplashViewModel by viewModel()
    private lateinit var locationManager: LocationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (checkPermissionGranted(android.Manifest.permission.ACCESS_FINE_LOCATION))
            initLocation()
        else
            requestLocationPermission()
    }

    /**
     * Checking permission in onCreate
     */
    @SuppressLint("MissingPermission")
    private fun initLocation() {
        locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, this, Looper.getMainLooper())
    }

    override fun onDestroy() {
        model.closeConnections()
        locationManager.removeUpdates(this)
        super.onDestroy()
    }

    // Wait for API response, we got response go to Main Activity
    private fun listenToResults() {
        model.getResults().observe(this, Observer {
            if (it)
                goToMainActivity()
        })
    }

    private fun loadGenres() {
        listenToResults()

        model.getGenres()?.observe(this, Observer {
            if (it.isNotEmpty()) {
                model.updateGenres(it)
                loadMovies()
            }
            else {
                model.getGenresFromAPI()
            }
        })
    }

    private fun loadMovies() {
        model.getMovies()?.observe(this, Observer {
            if (it.isNotEmpty()) {
                model.updateMovies(it)
                goToMainActivity()
            }
            else {
                model.getMoviesFromAPI()
            }
        })
    }

    private fun goToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onLocationChanged(location: Location?) {
        location?.let {
            val countryCode = LatLng(it.latitude, it.longitude).getAddressFromLocation(this)?.countryCode
            countryCode?.let { model.updateCountryCode(countryCode) }
        }

        loadGenres()
    }

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {}

    override fun onProviderEnabled(p0: String?) {}

    override fun onProviderDisabled(p0: String?) {}

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (permissions[0]) {
            android.Manifest.permission.ACCESS_FINE_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    model.resetMovies()
                    initLocation()
                }
                else {
                    loadGenres()
                }
            }
            else -> {}
        }
    }
}