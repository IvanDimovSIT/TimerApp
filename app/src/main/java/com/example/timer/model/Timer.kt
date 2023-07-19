package com.example.timer.model

import java.util.Date

open class Timer(public val id:Int, public var startTimer: Date, public var offset: Int = 0, public var isStarted: Boolean = false)
