package com.inter.explore


import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.ViewModel
import javax.inject.Inject


class ExploreViewModel @Inject constructor() : ViewModel(),
    ServiceConnection {


    fun testing() {


    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        TODO("Not yet implemented")
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        TODO("Not yet implemented")
    }


}