package com.crubio.marvelcomics.screens.home.herolistitem

import com.crubio.marvelcomics.entities.Hero

interface HeroListItemViewInterface {
    fun bindHero(hero: Hero?)
}