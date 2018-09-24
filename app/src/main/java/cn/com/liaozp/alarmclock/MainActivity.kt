package cn.com.liaozp.alarmclock

import android.app.ProgressDialog
import android.database.DatabaseUtils
import android.os.Bundle
import android.support.v7.widget.CardView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import cn.com.liaozp.alarmclock.xutils.db.DbTool
import cn.com.liaozp.alarmclock.xutils.db.T_ALARM_CLOCK
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import android.widget.TextView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Switch
import android.widget.Toast
import cn.com.liaozp.alarmclock.xutils.db.Tables
import org.jetbrains.anko.*
import org.xutils.db.table.DbBase


class MainActivity : BasicActivity() {

    private  var clockList = ArrayList<T_ALARM_CLOCK>();

    private lateinit var dialog : ProgressDialog;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            startActivity<SetClockActivity>(
                Pair("TYPE" , "ADD")
            );
        }

        recyclerView.layoutManager = LinearLayoutManager(this);

    }

    override fun onResume() {
        super.onResume()
        loadData();
    }



    internal inner class ClockListAdapter(val mDatas:List<T_ALARM_CLOCK>) :
            RecyclerView.Adapter<ClockListAdapter.MyViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            var viewHolder= MyViewHolder(LayoutInflater.from(
                    this@MainActivity).inflate(R.layout.layout_item_clolklist, parent,
                    false))

            return viewHolder;
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder?.time_tv?.text = mDatas.get(position).TIME;
            holder?.day_tv?.text = mDatas.get(position).REPEAT_DAY;
            holder?.note_tv?.text = mDatas.get(position).NOTE;
            holder?.clock_sw.isChecked =  if(mDatas.get(position).ACTIVE =="1" )
                 true else false;
            holder?.rootView.setOnClickListener {
                startActivity<SetClockActivity>(
                        Pair("TYPE" , "UPDATE"),
                        Pair("MODEL" , mDatas.get(position))
                );

            }
        }

        override fun getItemCount(): Int {
            return mDatas.size;
        }

        internal inner class MyViewHolder(view: View) : ViewHolder(view) {

            var time_tv: TextView
            var day_tv : TextView
            var note_tv :TextView
            var clock_sw :Switch
            var rootView : CardView
            init {
                time_tv = view.findViewById(R.id.time_text) as TextView
                day_tv = view.findViewById(R.id.day_text) as TextView
                note_tv = view.findViewById(R.id.note_text) as TextView
                clock_sw = view.findViewById(R.id.clock_switch) as Switch
                rootView = view.findViewById(R.id.rootView) as CardView
            }
        }


    }



    private fun loadData(){
        dialog = indeterminateProgressDialog("正在加载数据");
        doAsync {
            clockList.clear();
            clockList.addAll(DbTool.getDbManager().selector(T_ALARM_CLOCK::class.java)
                    .orderBy("UPDATE_TIME",true).findAll())
            uiThread {
                if(recyclerView.adapter == null)
                    recyclerView.adapter = ClockListAdapter(clockList);
                else
                    recyclerView.adapter.notifyDataSetChanged();

                if(dialog.isShowing) dialog.dismiss();
            }

        }
    }
}
