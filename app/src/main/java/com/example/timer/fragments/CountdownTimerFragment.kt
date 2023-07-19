package com.example.timer.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.timer.R
import com.example.timer.model.CountdownTimer
import com.example.timer.model.Timer
import com.example.timer.service.TimerFormatter
import java.util.Date

class CountdownTimerFragment : Fragment, TimerUpdatable {
    private val timer: CountdownTimer
    private var view: View? = null

    constructor(timer: CountdownTimer):super(){
        this.timer = timer
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        view = inflater.inflate(R.layout.countdown_timer_layout, container, false)
        view?.findViewById<TextView>(R.id.txtCountdownTimer)?.setText(TimerFormatter.formatTimer(timer.duration*1000 - timer.offset))
        view?.findViewById<ProgressBar>(R.id.progCountdown)?.setProgress(100)

        return view
    }

    override fun updateTimer(currentTime: Date){
        if(view == null || !timer.isStarted)
            return
        val timeRemaining = calculateTimeRemaining(currentTime)
        view?.findViewById<TextView>(R.id.txtCountdownTimer)?.text = TimerFormatter.formatTimer(timeRemaining)
        view?.findViewById<ProgressBar>(R.id.progCountdown)
            ?.setProgress(100 - (timer.duration * 1000 - timeRemaining).toInt() * 100 / (timer.duration*1000).toInt()  ,false)
    }

    override fun startOrStop() {
        if(timer.isStarted){
            timer.isStarted = false
            timer.offset += (Date().time - timer.startTimer.time).toInt()
        }else{
            timer.isStarted = true
            timer.startTimer = Date()
        }
    }

    private fun calculateTimeRemaining(currentTimer: Date): Long {
        return timer.startTimer.time + timer.duration * 1000 - currentTimer.time - timer.offset
    }

    override fun getTimer(): Timer {
        return timer
    }

}