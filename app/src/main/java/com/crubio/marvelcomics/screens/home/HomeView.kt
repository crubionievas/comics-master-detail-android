package com.crubio.marvelcomics.screens.home

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.ProgressBar
import com.crubio.marvelcomics.R
import com.crubio.marvelcomics.bus.BusProvider
import com.crubio.marvelcomics.entities.Hero
import com.crubio.marvelcomics.entities.HeroesData

class HomeView(inflater: LayoutInflater, parent: ViewGroup?) : HomeViewInterface {
    val rootView = inflater.inflate(R.layout.activity_home, parent, false)
    private var heroesRecyclerView: RecyclerView? = null
    private var heroesRecyclerAdapter: HeroesRecyclerAdapter? = null
    private var heroesData: HeroesData? = null
    private var heroesList: MutableList<Hero> = mutableListOf()
    private var noResultsView: View? = null
    private var progressBar: ProgressBar? = null
    private var alertDialog: AlertDialog? = null

    class HeroClickedEvent(val hero: Hero)

    init {
        heroesRecyclerView = findView(R.id.heroes_list)
        noResultsView = findView(R.id.no_results)

        // Use a linear layout manager
        heroesRecyclerView?.layoutManager = LinearLayoutManager(rootView.context)

        // Specify an adapter
        heroesRecyclerAdapter = HeroesRecyclerAdapter(heroesList, noResultsView, heroesRecyclerView)
        heroesRecyclerView?.adapter = heroesRecyclerAdapter

        heroesRecyclerView?.addOnItemTouchListener(
            RecyclerTouchListener(rootView.context, heroesRecyclerView,
                object : ClickListener {
                    override fun onClick(view: View, position: Int) {
                        //Toast.makeText(rootView.context, "Hero item clicked", Toast.LENGTH_SHORT).show();

                        // Send Hero to activity: IndexOutOfBoundsException protection
                        if (position >= 0 && position < heroesList.size) {
                            BusProvider.instance.post(HeroClickedEvent(heroesList.get(position)))
                        }
                    }

                    override fun onLongClick(view: View, position: Int) {}
                })
        )

        progressBar = findView(R.id.request_progress)
    }

    fun dismissAlert() {
        alertDialog?.dismiss()
        alertDialog = null
    }

    protected fun finalize() {
        dismissAlert()
    }

    protected fun <T : View> findView(id: Int): T {
        return rootView.findViewById(id)
    }

    override fun setHeroes(data: HeroesData) {
        heroesData = data

        // Update heroes list
        val list = heroesData?.results
        if (list != null) {
            heroesList.clear()
            heroesList.addAll(list)
        }

        // Update recycler view
        heroesRecyclerAdapter?.setHeroes(heroesData)
        heroesRecyclerAdapter?.notifyDataSetChanged()
        heroesRecyclerAdapter?.setEmptyViewVisibility()
    }

    override fun showProgress() {
        showProgress(true, heroesRecyclerView)
    }

    override fun hideProgress() {
        showProgress(false, heroesRecyclerView)
    }

    /**
     * Shows the progress UI and hides the view and vice versa.
     */
    private fun showProgress(show: Boolean, view: View?) {
        val shortAnimTime = rootView.resources.getInteger(android.R.integer.config_shortAnimTime)

        view?.visibility = if (show) View.INVISIBLE else View.VISIBLE
        view?.animate()?.setDuration(shortAnimTime.toLong())?.alpha((if (show) 0 else 1).toFloat())
            ?.setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    view.visibility = if (show) View.INVISIBLE else View.VISIBLE
                }
            })

        progressBar?.visibility = if (show) View.VISIBLE else View.GONE
        progressBar?.animate()?.setDuration(shortAnimTime.toLong())?.alpha((if (show) 1 else 0).toFloat())
            ?.setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    progressBar?.visibility = if (show) View.VISIBLE else View.GONE
                }
            })

    }

    fun showErrorAlert(error: String) {
        alertDialog = AlertDialog.Builder(rootView.context).create()
        alertDialog?.setMessage(error)
        alertDialog?.setButton(
            DialogInterface.BUTTON_NEUTRAL, rootView.resources.getString(R.string.accept)
        ) { dialog, _ -> dialog.dismiss() }
        alertDialog?.show()
    }

    internal interface ClickListener {
        fun onClick(view: View, position: Int)
        fun onLongClick(view: View, position: Int)
    }

    private inner class RecyclerTouchListener constructor(context: Context, recycleView: RecyclerView?,
        private val clickListener: ClickListener?) : RecyclerView.OnItemTouchListener {
        private val gestureDetector: GestureDetector

        init {
            gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
                override fun onSingleTapUp(e: MotionEvent): Boolean {
                    return true
                }

                override fun onLongPress(e: MotionEvent) {
                    val child = recycleView?.findChildViewUnder(e.x, e.y)
                    if (child != null) {
                        val childAdapter = recycleView.getChildAdapterPosition(child)
                        clickListener?.onLongClick(child, childAdapter)
                    }
                }
            })
        }

        override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
            val child = rv.findChildViewUnder(e.x, e.y)
            if (child != null && gestureDetector.onTouchEvent(e)) {
                clickListener?.onClick(child, rv.getChildAdapterPosition(child))
            }

            return false
        }

        override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}

        override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
    }
}