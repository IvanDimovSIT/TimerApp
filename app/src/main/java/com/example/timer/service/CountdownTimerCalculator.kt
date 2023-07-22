package com.example.timer.service

import com.example.timer.model.CountdownTimer
import java.util.Date

object CountdownTimerCalculator {
    fun calculateRemainingTime(countdownTimer: CountdownTimer, currentTime: Date) : Long{
        return countdownTimer.startTimer.time + countdownTimer.duration * 1000 - currentTime.time - countdownTimer.offset
    }
}