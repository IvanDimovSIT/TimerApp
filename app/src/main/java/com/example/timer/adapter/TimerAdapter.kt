package com.example.timer.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.timer.R
import com.example.timer.model.CountdownTimer
import com.example.timer.model.Timer
import com.example.timer.service.CountdownTimerCalculator
import com.example.timer.service.TimerCalculator
import com.example.timer.service.TimerFormatter
import java.util.Date

class TimerAdapter(private val observer: TimerAdapterSelectTimerObserver) : RecyclerView.Adapter<TimerAdapter.TimerViewHolder>() {
    class TimerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    interface TimerAdapterSelectTimerObserver{
        fun timerSelected(timer: Timer)
    }

    private val timers: MutableList<Timer> = ArrayList<Timer>()
    private var idCounter: Int = 1

    companion object {
        private const val VIEW_TYPE_TIMER = 1
        private const val VIEW_TYPE_COUNTDOWN_TIMER = 2
    }

    override fun getItemViewType(position: Int): Int {
        val timer = timers[position]
        return if (timer is CountdownTimer) {
            VIEW_TYPE_COUNTDOWN_TIMER
        } else {
            VIEW_TYPE_TIMER
        }
    }

    fun getId() : Int{
        return idCounter++
    }

    fun addTimer(timer: Timer){
        timers.add(timers.size, timer)
        notifyItemInserted(timers.size-1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimerViewHolder {
        val view: View
        if(viewType == VIEW_TYPE_TIMER) {
            view = LayoutInflater.from(parent.context).inflate(R.layout.timer_recycler_view_item, parent, false)
            view.findViewById<TextView>(R.id.txtItemTimePassed).text = "00:00"
        }else{
            view = LayoutInflater.from(parent.context).inflate(R.layout.countdown_timer_recycler_view_item, parent, false)
        }

        return TimerViewHolder(view)
    }

    override fun getItemCount(): Int {
        return timers.size
    }

    override fun onBindViewHolder(holder: TimerViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            observer.timerSelected(timers[position])
        }

        if(timers[position] is CountdownTimer){
            val timer: CountdownTimer = timers[position] as CountdownTimer
            val timeRemaining = CountdownTimerCalculator.calculateRemainingTime(timer, Date())
            holder.itemView.findViewById<TextView>(R.id.txtCoutndownTimerItem).setText("Обратно броене "+timer.id.toString())
            holder.itemView.findViewById<TextView>(R.id.txtItemTimeRemaining).setText(
                TimerFormatter.formatTimer(
                    timeRemaining
                )
            )
            holder.itemView.findViewById<ProgressBar>(R.id.progCountdownItem).progress = 0
        }else{
            val timer: Timer = timers[position]
            holder.itemView.findViewById<TextView>(R.id.txtTimerItem).setText("Хронометър "+timer.id.toString())
            holder.itemView.findViewById<TextView>(R.id.txtItemTimePassed).setText(TimerFormatter.formatTimer(TimerCalculator.calculatePassedTime(timer, Date())))
        }
    }

    fun remove(timer: Timer){
        for(i in 0 until timers.size){
            if(timers[i].id != timer.id)
                continue
            timers.removeAt(i)
            notifyItemRemoved(i)
            break
        }
    }

    fun update(timer: Timer){
        for(i in 0..timers.size){
            if(timers[i].id == timer.id){
                timers[i] = timer
                return
            }
        }
    }

    fun updateTimersTime(currentTime: Date, recyclerView: RecyclerView){
        for(i in 0 until timers.size){
            if(!timers[i].isStarted)
                continue

            val view : View = recyclerView.findViewHolderForAdapterPosition(i)?.itemView!!
            if(timers[i] is CountdownTimer){
                val timeRemaining : Long =
                    CountdownTimerCalculator.calculateRemainingTime(timers[i] as CountdownTimer, currentTime)
                if(timeRemaining < 0)
                    continue


                view.findViewById<TextView>(R.id.txtItemTimeRemaining).text =
                    TimerFormatter.formatTimer(timeRemaining)
                view.findViewById<ProgressBar>(R.id.progCountdownItem).progress =
                    ((timeRemaining*100)/((timers[i] as CountdownTimer).duration * 1000 * 100 )).toInt()
            }else{
                view.findViewById<TextView>(R.id.txtItemTimePassed).text =
                    TimerFormatter.formatTimer(TimerCalculator.calculatePassedTime(timers[i], currentTime))
            }
        }
    }

}