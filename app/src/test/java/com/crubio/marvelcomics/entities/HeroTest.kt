package com.crubio.marvelcomics.model

import com.crubio.marvelcomics.entities.Hero
import com.crubio.marvelcomics.entities.Thumbnail
import org.junit.Assert.*
import org.junit.Test

class HeroTest {
    @Test
    fun heroObjectInstantiation() {
        val name = "Adam Warlock"
        val description = "Adam Warlock is an artificially created human who was born in a cocoon at a scientific complex called The Beehive."
        val thumb = Thumbnail("http://i.annihil.us/u/prod/marvel/i/mg/a/f0/5202887448860", "jpg")
        val adamWarlock = Hero(0, name, description, thumb)

        assertNotNull(adamWarlock)
        assertEquals(name, adamWarlock.name)
        assertEquals(description, adamWarlock.description)
        assertEquals(thumb.path, adamWarlock.thumbnail?.path)
        assertEquals(thumb.extension, adamWarlock.thumbnail?.extension)
    }
}