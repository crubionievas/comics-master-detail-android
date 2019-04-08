package com.crubio.marvelcomics.screens.hero

import android.os.Bundle
import android.view.LayoutInflater
import com.crubio.marvelcomics.R
import com.crubio.marvelcomics.entities.Hero
import com.crubio.marvelcomics.screens.common.controllers.BaseActivity
import com.google.gson.Gson

class HeroDetailsActivity : BaseActivity() {

    private var heroDetailsView: HeroDetailsView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val hero = Gson().fromJson(intent.getStringExtra("heroJson"), Hero::class.java)
        heroDetailsView = HeroDetailsView(LayoutInflater.from(this), null)
        heroDetailsView?.setHeroDetails(hero)

        setContentView(heroDetailsView?.rootView)
    }

    override fun onBackPressed() {
        finish()
        overridePendingTransition(R.anim.activity_back_slide_in, R.anim.activity_back_slide_out)
    }
}
