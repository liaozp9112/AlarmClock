package cn.com.liaozp.alarmclock.xutils.http

import org.xutils.http.RequestParams

class HttpParams(var action :String,val defaultURL :Boolean=true) : RequestParams() {

    init {
        if(defaultURL) action+=action;
        addBaseParam();
    }

    private fun addBaseParam(){

    }

}