package com.crubio.marvelcomics.screens.home

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.crubio.marvelcomics.entities.Hero
import com.crubio.marvelcomics.entities.HeroesData
import com.crubio.marvelcomics.screens.home.herolistitem.HeroListItemView

class HeroesRecyclerAdapter(private var heroes: MutableList<Hero>?, private var noResultsView: View?,
                            private val heroesRecyclerView: RecyclerView?) : RecyclerView.Adapter<HeroesRecyclerAdapter.ViewHolder>() {

    class ViewHolder(view: HeroListItemView) : RecyclerView.ViewHolder(view.rootView) {
        var heroListItemView: HeroListItemView? = null

        init {
            heroListItemView = view
        }
    }

    fun setHeroes(heroesData: HeroesData?) {
        heroes = heroesData?.results
    }

    fun setEmptyViewVisibility() {
        if (itemCount == 0) {
            noResultsView?.visibility = View.VISIBLE
            heroesRecyclerView?.visibility = View.GONE
        } else {
            noResultsView?.visibility = View.GONE
            heroesRecyclerView?.visibility = View.VISIBLE
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeroesRecyclerAdapter.ViewHolder {
        val heroListItemView = HeroListItemView(LayoutInflater.from(parent.context), parent)

        return ViewHolder(heroListItemView)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Get element from heroes list at this position
        val hero = heroes?.get(position)
        holder.heroListItemView?.bindHero(hero)
    }

    // Return the size of your list (invoked by the layout manager)
    override fun getItemCount(): Int {
        return heroes?.size ?: 0
    }
}
