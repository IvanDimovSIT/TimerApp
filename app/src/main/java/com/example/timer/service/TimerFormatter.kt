package com.example.timer.service

import java.text.SimpleDateFormat
import java.util.Date

object TimerFormatter {
    fun formatTimer(time: Long): String{
        return  SimpleDateFormat("mm:ss").format(Date(time))
    }
}