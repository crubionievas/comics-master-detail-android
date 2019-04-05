package com.crubio.marvelcomics.bus

import com.squareup.otto.Bus
import com.squareup.otto.ThreadEnforcer

object BusProvider {
    val instance = Bus(ThreadEnforcer.ANY)
}