package com.example.timer.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.timer.R
import com.example.timer.model.CountdownTimer
import com.example.timer.model.Timer

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

    public fun getId() : Int{
        return idCounter++;
    }

    public fun addTimer(timer: Timer){
        timers.add(timers.size, timer)
        notifyItemInserted(timers.size-1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimerViewHolder {
        var view: View
        if(viewType == VIEW_TYPE_TIMER) {
            view = LayoutInflater.from(parent.context).inflate(R.layout.timer_recycler_view_item, parent, false)
        }else{
            view = LayoutInflater.from(parent.context).inflate(R.layout.timer_recycler_view_item, parent, false)
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

        if(timers[position] is Timer){
            val timer: Timer = timers[position]
            holder.itemView.findViewById<TextView>(R.id.txtTimerItem).setText("Хронометър "+timer.id.toString())
        }else{//Countdown timer
            val timer: CountdownTimer = timers[position] as CountdownTimer
            holder.itemView.findViewById<TextView>(R.id.txtTimerItem).setText("Обратно броене "+timer.id.toString())
        }
    }

    fun update(timer: Timer){
        for(i in 0..timers.size){
            if(timers[i].id == timer.id){
                timers[i] = timer
                notifyItemChanged(i)
                return
            }
        }
    }

}