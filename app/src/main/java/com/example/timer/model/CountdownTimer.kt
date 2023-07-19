package com.example.timer.model

import java.util.Date

class CountdownTimer(public val duration: Long, id:Int, startTimer: Date): Timer(id, startTimer)
