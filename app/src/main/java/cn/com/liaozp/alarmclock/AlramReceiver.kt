package cn.com.liaozp.alarmclock

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import cn.com.liaozp.alarmclock.xutils.db.T_ALARM_CLOCK
import org.jetbrains.anko.startActivity

class AlramReceiver :BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent!!.action == AlockApp.instance().packageName){
            var intentContent = Intent(context,ContentActivity::class.java);
            intentContent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
            intentContent.putExtra("ID",intent.getStringExtra("ID"))
            context!!.startActivity(intentContent);
        }
    }
}