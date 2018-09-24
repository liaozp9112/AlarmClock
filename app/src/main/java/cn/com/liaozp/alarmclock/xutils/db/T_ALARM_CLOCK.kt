package cn.com.liaozp.alarmclock.xutils.db

import android.media.RingtoneManager
import android.os.Parcelable
import cn.com.liaozp.alarmclock.AlockApp
import kotlinx.android.parcel.Parcelize
import org.xutils.db.annotation.Column
import org.xutils.db.annotation.Table



@Parcelize
@Table(name = "T_ALARM_CLOCK")
data class T_ALARM_CLOCK(
        @Column(name = "ID",isId = true)var ID:String="",
        @Column(name = "TIME")var TIME:String="",
        @Column(name = "REPEAT_DAY")var REPEAT_DAY:String="从不",
        @Column(name = "NOTE")var NOTE:String="",
        @Column(name = "SOUND")var SOUND:String=RingtoneManager.
                getActualDefaultRingtoneUri(
                        AlockApp.instance(),RingtoneManager.TYPE_RINGTONE).toString(),
        @Column(name = "UPDATE_TIME")var UPDATE_TIME:String="",
        @Column(name = "ACTIVE")var ACTIVE:String="1"
        ) : Parcelable{


    /* @Column(name = "ID",isId = true)
     private var id:String=ID;

     @Column(name = "TIME")
     private var time:String=TIME;

     @Column(name = "REPEAT_DAY")
     private var repeat_day:String=REPEAT_DAY;

     @Column(name = "NOTE")
     private var note:String=NOTE;

     @Column(name = "SOUND")
     private var sound:String=SOUND;

     @Column(name = "UPDATE_TIME")
     private var update_time:String=UPDATE_TIME;
 */
}