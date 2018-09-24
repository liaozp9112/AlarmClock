package cn.com.liaozp.alarmclock.tools

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.ColorSpace
import android.net.Uri
import android.os.Bundle
import cn.com.liaozp.alarmclock.AlockApp
import cn.com.liaozp.alarmclock.AlramReceiver
import cn.com.liaozp.alarmclock.SetClockActivity
import cn.com.liaozp.alarmclock.xutils.db.T_ALARM_CLOCK
import kotlinx.android.synthetic.main.content_set_clock.*
import java.util.*

class AlarmTools {

    companion object {

        private var alarmManager = AlockApp.instance().getSystemService(Context.ALARM_SERVICE) as AlarmManager;

        fun setAlarm(context :Context , model: T_ALARM_CLOCK){
            var triggerAtTime = System.currentTimeMillis();
            if (model.REPEAT_DAY== SetClockActivity.WeekDAY.Never.chnName){
                var calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, model.TIME.substring(0,model.TIME.indexOf(":")).toInt());
                calendar.set(Calendar.MINUTE,model.TIME.substring(model.TIME.indexOf(":")+1).toInt());
                calendar.set(Calendar.SECOND,0);
                var intent = Intent(context, AlramReceiver::class.java);
                intent.action = AlockApp.instance().packageName;
                intent.putExtra("ID",model.ID);
                var pi = PendingIntent.getBroadcast(context,0,intent,0);
                alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.timeInMillis,pi);
            }
        }

        fun cancelAlarm(context :Context , model: T_ALARM_CLOCK){
            if (model.REPEAT_DAY== SetClockActivity.WeekDAY.Never.chnName){
                var intent = Intent(context, AlramReceiver::class.java);
                intent.action = AlockApp.instance().packageName;
                intent.putExtra("ID",model.ID);
                var pi = PendingIntent.getBroadcast(context,0,intent,PendingIntent.FLAG_NO_CREATE);
                if(pi !=null){
                    alarmManager.cancel(pi)
                }
            }
        }
    }
}