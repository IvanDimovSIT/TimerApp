package com.example.timer.service

import com.example.timer.model.Timer
import java.util.Date

object TimerCalculator {
    fun calculatePassedTime(timer: Timer, currentTime: Date): Long{
        return currentTime.time - timer.startTimer.time + timer.offset
    }
}