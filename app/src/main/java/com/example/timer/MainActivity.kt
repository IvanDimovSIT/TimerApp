package com.example.timer

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.timer.adapter.TimerAdapter
import com.example.timer.dialog.CountdownTimerDialog
import com.example.timer.fragments.CountdownTimerFragment
import com.example.timer.fragments.TimerFragment
import com.example.timer.fragments.TimerUpdatable
import com.example.timer.model.CountdownTimer
import com.example.timer.model.Timer
import com.example.timer.ui.theme.TimerTheme
import java.util.Date

class MainActivity : FragmentActivity() {
    private val timerAdapter: TimerAdapter = TimerAdapter(object : TimerAdapter.TimerAdapterSelectTimerObserver{
        override fun timerSelected(timer: Timer) {
            runOnUiThread {
                if(timer is CountdownTimer)
                    loadCountdownTimerView(timer)
                else
                    loadTimerView(timer)
            }
        }
    })
    private var timerUpdatable: TimerUpdatable? = null

    private fun addTimerButtonListener(){
        findViewById<Button>(R.id.btnAddTimer).setOnClickListener {
            val id = timerAdapter.getId()
            val timer = Timer(id, Date())
            timerAdapter.addTimer(timer)
            loadTimerView(timer)
        }
    }

    private fun loadTimerView(timer: Timer){
        val fragment = TimerFragment(timer)
        timerUpdatable = fragment

        supportFragmentManager.beginTransaction().replace(R.id.frmTimer, fragment as Fragment).commit()
        (timerUpdatable as TimerUpdatable).updateTimer(Date())
    }

    private fun loadCountdownTimerView(countdownTimer: CountdownTimer){
        val fragment = CountdownTimerFragment(countdownTimer)
        timerUpdatable = fragment

        supportFragmentManager.beginTransaction().replace(R.id.frmTimer, fragment as Fragment).commit()
        (timerUpdatable as TimerUpdatable).updateTimer(Date())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_layout)

        val recyclerView = findViewById<RecyclerView>(R.id.rclTimerList)
        recyclerView.adapter = timerAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        addTimerButtonListener()
        addStartButtonListener()
        addCountdownButtonListener()

        Thread(object : Runnable{
            override fun run() {
                while (true){
                    Thread.sleep(1000)
                    if(timerUpdatable == null)
                        continue
                    runOnUiThread {
                        timerUpdatable!!.updateTimer(Date())
                    }
                }
            }
        }).start()
    }

    private fun addCountdownButtonListener() {
        findViewById<Button>(R.id.btnAddCountdown).setOnClickListener {
            val dialog = CountdownTimerDialog(object : CountdownTimerDialog.CountdownTimerDialogObserver{
                override fun onAdded(timer: CountdownTimer) {
                    timerAdapter.addTimer(timer)
                    loadCountdownTimerView(timer)
                }

                override fun getId(): Int {
                    return timerAdapter.getId()
                }

            })
            dialog.show(supportFragmentManager, "AddCountdown")
        }
    }

    private fun addStartButtonListener() {
        findViewById<Button>(R.id.btnStart).setOnClickListener {
            if(timerUpdatable == null)
                return@setOnClickListener

            timerUpdatable!!.startOrStop()
            timerAdapter.update(timerUpdatable!!.getTimer())
        }
    }
}
