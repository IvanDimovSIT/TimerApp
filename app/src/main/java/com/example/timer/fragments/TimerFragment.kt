package com.example.timer.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.timer.R
import com.example.timer.model.Timer
import com.example.timer.service.TimerFormatter
import java.util.Date


class TimerFragment : Fragment, TimerUpdatable {
    private val timer: Timer
    private var view: View? = null

    constructor(timer: Timer):super(){
        this.timer = timer
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        view = inflater.inflate(R.layout.timer_layout, container, false)
        view?.findViewById<TextView>(R.id.txtTimer)?.setText(TimerFormatter.formatTimer(timer.offset.toLong()))

        return view
    }

    override fun updateTimer(currentTime: Date){
        if(view == null || !timer.isStarted)
            return
        view?.findViewById<TextView>(R.id.txtTimer)?.text  = TimerFormatter.formatTimer(currentTime.time - timer.startTimer.time + timer.offset)
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

    override fun getTimer(): Timer {
        return timer
    }

}