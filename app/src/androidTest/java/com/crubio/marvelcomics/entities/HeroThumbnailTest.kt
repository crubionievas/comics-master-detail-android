package com.crubio.marvelcomics.model

import org.junit.Assert.*
import android.webkit.URLUtil
import androidx.test.InstrumentationRegistry
import com.crubio.marvelcomics.entities.Hero
import com.crubio.marvelcomics.entities.Thumbnail
import com.squareup.picasso.Picasso
import org.junit.Test

class HeroThumbnailTest {

    @Test
    fun heroThumbnailFetch() {
        val name = "Adam Warlock"
        val description = "Adam Warlock is an artificially created human who was born in a cocoon at a scientific complex called The Beehive."
        val thumb = Thumbnail("http://i.annihil.us/u/prod/marvel/i/mg/a/f0/5202887448860", "jpg")
        val adamWarlock = Hero(0, name, description, thumb)

        assert(URLUtil.isValidUrl(adamWarlock.getThumbnailUrl()))

        val context = InstrumentationRegistry.getTargetContext()
        val image = Picasso.with(context).load(adamWarlock.getThumbnailUrl()).get()
        assertNotNull(image)
    }
}