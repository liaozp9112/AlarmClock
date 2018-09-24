package cn.com.liaozp.alarmclock.xutils.http

import cn.com.liaozp.alarmclock.tools.JsonTools
import org.json.JSONObject

open class JsonCallBack : XCallBack<JSONObject>{

    override fun onSuccess(result: JSONObject) {}

    override fun onError(throwable: Throwable, b: Boolean) {}

    override fun onCancelled(e: Exception) {}

    override fun onFinished() {}

    override fun getResult(result: String): JSONObject {
        return JsonTools.generateJsonObj(result);
    }
}