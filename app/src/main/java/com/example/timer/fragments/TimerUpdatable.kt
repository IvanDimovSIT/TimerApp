package com.example.timer.fragments

import com.example.timer.model.Timer
import java.util.Date

interface TimerUpdatable {
    fun updateTimer(currentTime: Date)
    fun startOrStop()
    fun getTimer() : Timer
}