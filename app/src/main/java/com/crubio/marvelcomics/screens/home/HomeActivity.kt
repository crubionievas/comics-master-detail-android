package com.crubio.marvelcomics.screens.home


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import com.crubio.marvelcomics.BuildConfig
import com.crubio.marvelcomics.R
import com.crubio.marvelcomics.bus.BusProvider
import com.crubio.marvelcomics.networking.HeroesFetch
import com.crubio.marvelcomics.screens.common.controllers.BaseActivity
import com.crubio.marvelcomics.screens.hero.HeroDetailsActivity
import com.google.gson.Gson
import com.squareup.otto.Subscribe
import java.net.HttpURLConnection

class HomeActivity : BaseActivity() {

    private var homeView: HomeView? = null
    private var isResumingFromChild = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        homeView = HomeView(LayoutInflater.from(this), null)

        setContentView(homeView?.rootView)
    }

    override fun onStart() {
        super.onStart()

        BusProvider.instance.register(this)
    }

    override fun onResume() {
        super.onResume()

        if (!isResumingFromChild) {
            homeView?.showProgress()
            HeroesFetch.getHeroes()
            isResumingFromChild = false
        }
    }

    override fun onStop() {
        super.onStop()

        BusProvider.instance.unregister(this)
    }

    @Subscribe
    fun onHeroesResults(event: HeroesFetch.HeroesEvent) {
        val data = event.heroesData
        if (data != null) {
            homeView?.setHeroes(event.heroesData)
        }

        homeView?.hideProgress()
    }

    @Subscribe
    fun onHeroesError(event: HeroesFetch.ErrorEvent) {
        if (BuildConfig.DEBUG) {
            Toast.makeText(this, "Error obtaining heroes!", Toast.LENGTH_LONG).show()
        }

        when (event.errorCode) {
            HttpURLConnection.HTTP_CONFLICT,        // Missing API Key, Hash or Timestamp
            HttpURLConnection.HTTP_FORBIDDEN,
            HttpURLConnection.HTTP_UNAUTHORIZED,    // Invalid Hash
            HttpURLConnection.HTTP_BAD_METHOD ->
                if (BuildConfig.DEBUG) {
                    homeView?.showErrorAlert(getString(R.string.networking_known_error_message, event.errorCode, event.errorMessage))
                }
            else ->
                if (BuildConfig.DEBUG) {
                    homeView?.showErrorAlert(getString(R.string.networking_error_message, event.errorCode, event.errorMessage))
                }
        }

        homeView?.hideProgress()
    }

    @Subscribe
    fun onHeroesFailure(event: HeroesFetch.FailureEvent) {
        if (BuildConfig.DEBUG) {
            Toast.makeText(this, "Failure obtaining heroes!", Toast.LENGTH_LONG).show()
            homeView?.showErrorAlert(getString(R.string.networking_failure_error_message, event.message))
        }

        homeView?.hideProgress()
    }

    @Subscribe
    fun onHeroClicked(event: HomeView.HeroClickedEvent) {
        // Send hero to detail activity
        val intent = Intent(this, HeroDetailsActivity::class.java)
        val heroJson = Gson().toJson(event.hero)
        intent.putExtra("heroJson", heroJson)
        startActivity(intent)
        overridePendingTransition(R.anim.activity_slide_in, R.anim.activity_slide_out)

        // This will avoid reload of heroes list when coming back
        isResumingFromChild = true
    }

}
