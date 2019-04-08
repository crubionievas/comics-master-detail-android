package com.crubio.marvelcomics.screens.hero

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.crubio.marvelcomics.R
import com.crubio.marvelcomics.entities.Hero
import com.squareup.picasso.Picasso


class HeroDetailsView(inflater: LayoutInflater, parent: ViewGroup?): HeroDetailsViewInterface {
    val rootView: View = inflater.inflate(R.layout.activity_hero_details, parent, false)
    private var name: TextView
    private var description: TextView
    private var image: ImageView

    init {
        name = rootView.findViewById(R.id.name)
        description = rootView.findViewById(R.id.description)
        image = rootView.findViewById(R.id.image)
    }

    override fun setHeroDetails(hero: Hero) {
        name.text = hero.name
        description.text = hero.description
        // Hero image
        Picasso.with(rootView.context).load(hero.getThumbnailUrl()).into(image)
    }
}