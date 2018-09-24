package cn.com.liaozp.alarmclock

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.content_set_clock.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import android.widget.EditText
import cn.com.liaozp.alarmclock.tools.AlarmTools
import cn.com.liaozp.alarmclock.tools.FileTools
import cn.com.liaozp.alarmclock.xutils.db.DbTool
import cn.com.liaozp.alarmclock.xutils.db.T_ALARM_CLOCK
import java.text.SimpleDateFormat
import java.util.*


class SetClockActivity :BasicActivity() {

    private val TYPE_ADD :String = "ADD"

    private val TYPE_UPDATE :String = "UPDATE"
    //判断是新增还是修改,默认新增ADD
    private var TYPE : String = TYPE_ADD;

    private var model = T_ALARM_CLOCK();

    //用于选择铃声后作相应的判断标记
    private val REQUEST_CODE_PICK_RINGTONE = 1
    //保存铃声的Uri的字符串形式
    private var mRingtoneUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_clock);
        initViews();
    }

    fun initViews(){
        toolbar.title = "时间设置";
        setHasBack();
        timepicker.setIs24HourView(true);
        card_repeat.setOnClickListener { alertDaySelect(); }
        card_note.setOnClickListener { alert_edit(); }
        card_sound.setOnClickListener { doPickPingtone(); }
        repeat_text.text = model.REPEAT_DAY;
        note_text.text = model.NOTE;
        sound_text.text =
                FileTools.getFileName(FileTools.getRealFilePath(this,Uri.parse(model.SOUND)),"暂无系统默认铃声");
        val bundle = intent.extras;
        TYPE = bundle.getString("TYPE")
        if(TYPE==TYPE_UPDATE){
            model = bundle.getParcelable("MODEL");
            timepicker.currentHour = model.TIME.substring(0,model.TIME.indexOf(":")).toInt()
            timepicker.currentMinute = model.TIME.substring(model.TIME.indexOf(":")+1).toInt();
            repeat_text.text = model.REPEAT_DAY;
            note_text.text = model.NOTE;
            sound_text.text= FileTools.getFileName(FileTools.getRealFilePath(this, Uri.parse(model.SOUND)));
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_set_clock, menu)
        if (TYPE==TYPE_ADD){
             menu.findItem(R.id.action_delete).setVisible(false);
             model.ID = UUID.randomUUID().toString();
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
         when (item.itemId) {
            R.id.action_confirm -> menuSaveClock();
             R.id.action_delete -> menuDeleteClock();
        }

        return true;
    }

    /***
     * 展示日期选择对话框
     */
    private fun alertDaySelect(){
        val dayList = arrayOf<CharSequence>(
                WeekDAY.Monday.chnName,WeekDAY.Tuesday.chnName,WeekDAY.Wednesday.chnName,WeekDAY.Thursday.chnName,
                WeekDAY.Friday.chnName,WeekDAY.Saturday.chnName,WeekDAY.Sunday.chnName);

        var daySelected = booleanArrayOf(false,false,false,false,false,false,false);
        if (!model.REPEAT_DAY.isNullOrBlank()){
                daySelected = booleanArrayOf(model.REPEAT_DAY.contains(WeekDAY.Monday.chnName),
                    model.REPEAT_DAY.contains(WeekDAY.Tuesday.chnName),model.REPEAT_DAY.contains(WeekDAY.Wednesday.chnName),
                    model.REPEAT_DAY.contains(WeekDAY.Thursday.chnName),model.REPEAT_DAY.contains(WeekDAY.Friday.chnName),
                    model.REPEAT_DAY.contains(WeekDAY.Saturday.chnName),model.REPEAT_DAY.contains(WeekDAY.Sunday.chnName));
        }

        var newSelected :String = "" ;
        val daySelectDialog = AlertDialog.Builder(this).setTitle("请选择重复响铃日期")
                .setMultiChoiceItems(dayList,daySelected,
                        DialogInterface.OnMultiChoiceClickListener {
                            dialog, which, isChecked ->
                        })
                .setPositiveButton("好", DialogInterface.OnClickListener {
                    dialog, which ->
                    for (i in daySelected.indices){
                        newSelected += if (daySelected[i]) dayList.get(i) else "";
                    }
                    model.REPEAT_DAY = if(newSelected.isNullOrBlank()) WeekDAY.Never.chnName else newSelected;
                    repeat_text.text  = model.REPEAT_DAY;
                })
                .setNegativeButton("取消",null);
        daySelectDialog.show();
    }

    public enum class WeekDAY(val chnName:String){
        Never("从不"),
        Monday("每周一"),
        Tuesday("每周二"),
        Wednesday("每周三"),
        Thursday("每周四"),
        Friday("每周五"),
        Saturday("每周六"),
        Sunday("每周日")
    }

    private fun alert_edit() {
        val et = EditText(this)
        et.setSingleLine(true);
        et.setText(model.NOTE)
        AlertDialog.Builder(this).setTitle("请输入标签")
                .setView(et)
                .setPositiveButton("确定") { dialogInterface, i ->
                    model.NOTE = et.text.toString();
                    note_text.text = model.NOTE;
                }.setNegativeButton("取消", null).show()
    }

    private fun doPickPingtone(){
        val intent = Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT,true);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE,"选择铃声");
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE,
                RingtoneManager.TYPE_RINGTONE);
        // Don't show 'Silent'
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false);
        val ringtoneUri: Uri;
        ringtoneUri = mRingtoneUri?:RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        // Put checkmark next to the current ringtone for this contact
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, ringtoneUri)
        // Launch!
        // startActivityForResult(intent, REQUEST_CODE_PICK_RINGTONE);
        startActivityForResult(intent, REQUEST_CODE_PICK_RINGTONE)
    }


    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        data?:return;
        try {
            val pickedUri = data.getParcelableExtra<Uri>(RingtoneManager.EXTRA_RINGTONE_PICKED_URI)
            mRingtoneUri = pickedUri;
            model.SOUND=mRingtoneUri.toString();
            sound_text.text= FileTools.getFileName(FileTools.getRealFilePath(this, Uri.parse(model.SOUND)))

         } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun menuSaveClock(){
        var selectTime = (if(timepicker.currentHour<10) "0"+timepicker.currentHour else
            ""+timepicker.currentHour)+":"+ (if(timepicker.currentMinute<10) "0"+timepicker.currentMinute else
            ""+timepicker.currentMinute)
        model.TIME = selectTime;
        model.ACTIVE = "1";
        model.UPDATE_TIME = SimpleDateFormat("yyyy-MM-dd HH:mm:ss" ).format(Date());
        DbTool.saveOrUpdate(model);
        setAlarmClock();
        finish();
    }


    private fun menuDeleteClock(){
        DbTool.delete(model as Object);
        finish();
    }


    private fun setAlarmClock(){
        AlarmTools.setAlarm(this,model);


    }
}