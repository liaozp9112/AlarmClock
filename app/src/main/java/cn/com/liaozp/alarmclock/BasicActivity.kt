package cn.com.liaozp.alarmclock

import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.layout_toolbar.*

open class BasicActivity : AppCompatActivity() {


    open protected fun setHasBack(){
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener { finish(); }
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
    }
}