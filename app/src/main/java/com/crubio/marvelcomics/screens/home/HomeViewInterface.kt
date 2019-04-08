package com.crubio.marvelcomics.screens.home

import com.crubio.marvelcomics.entities.HeroesData

interface HomeViewInterface {
    fun setHeroes(heroesData: HeroesData)

    fun showProgress()

    fun hideProgress()
}
