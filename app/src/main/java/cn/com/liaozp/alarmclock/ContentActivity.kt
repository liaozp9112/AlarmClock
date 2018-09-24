package cn.com.liaozp.alarmclock

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.support.annotation.UiThread
import android.view.View
import cn.com.liaozp.alarmclock.tools.AlarmTools
import cn.com.liaozp.alarmclock.xutils.db.DbTool
import cn.com.liaozp.alarmclock.xutils.db.T_ALARM_CLOCK
import kotlinx.android.synthetic.main.layout_content.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.File
import java.net.URI


class ContentActivity : BasicActivity() {
    var  mMediaPlayer = MediaPlayer();
    private var model = T_ALARM_CLOCK();
    private var id :String ="";
    //
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_content)
        initViews();
    }

    private fun initViews(){
        id = intent.getStringExtra("ID")
        toolbar.title="It's time "
        doAsync {
            model = DbTool.getDbManager().selector(T_ALARM_CLOCK::class.java).
                    where("ID","=",id).findFirst();
           uiThread {
               if (model==null) return@uiThread
               note_text.text = model.NOTE;
               stop_rt.visibility= View.VISIBLE;
               if (model.ACTIVE == "1"){
                   initMediaPlayer();
                   stop_rt.setOnClickListener {
                       if (mMediaPlayer.isPlaying){
                           mMediaPlayer.stop()
                       }
                       finish();
                   }
               }
           }
        }
    }

    private fun initMediaPlayer() {
        try {
            mMediaPlayer.setDataSource(this@ContentActivity, Uri.parse(model.SOUND));
            mMediaPlayer.prepare()
            mMediaPlayer.start()
            mMediaPlayer.setOnCompletionListener {
                mMediaPlayer.start()
                mMediaPlayer.isLooping =true;
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        resetActiveType();
        mMediaPlayer.release();
    }

    private fun resetActiveType(){
        if(model!=null && model.REPEAT_DAY== SetClockActivity.WeekDAY.Never.chnName){
            model.ACTIVE="0"
            DbTool.saveOrUpdate(model);
            AlarmTools.cancelAlarm(this,model);
        }
    }
}