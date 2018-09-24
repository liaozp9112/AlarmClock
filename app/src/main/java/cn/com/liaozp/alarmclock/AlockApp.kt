package cn.com.liaozp.alarmclock

import android.app.Application
import cn.com.liaozp.alarmclock.xutils.db.DbTool
import cn.com.liaozp.alarmclock.xutils.db.T_ALARM_CLOCK
import org.xutils.x
import kotlin.properties.Delegates

class AlockApp : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this;
        x.Ext.init(instance);
        x.Ext.setDebug(false);
    }

    companion object {
        private var instance : AlockApp by Delegates.notNull();
        fun instance() = instance;
    }
}