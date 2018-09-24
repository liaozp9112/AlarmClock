package cn.com.liaozp.alarmclock.xutils.http

import android.app.ProgressDialog
import android.content.Context
import org.xutils.http.RequestParams
import org.xutils.x
import org.xutils.common.Callback
import org.jetbrains.anko.longToast


class HttpUtil @JvmOverloads constructor(
        val context: Context?=null,val progressText: String ="正在加载中,请稍候...") {

    private var TAG :String;

    private lateinit var dialog :ProgressDialog ;

    init {
        TAG = "HttpUtil";
        onPreInvoke();
    }

    private fun onPreInvoke(){
        if (context != null && !progressText.isNullOrBlank()){
            dialog = ProgressDialog(context);
            dialog.setMessage(progressText);
        }
    }

    private fun onPostInvoke(){
        if (dialog!=null && dialog.isShowing){
            dialog.dismiss();
        }
    }

    private fun showProgressDialog(){
        if(dialog!=null&&!dialog.isShowing()){
            dialog.show();
        }
    }

    /**
     *
     * @param params
     * @param callBack
     */
    public fun <T> invokePost(params: RequestParams, callBack: XCallBack<T>) :HttpUtil {
        showProgressDialog();
        x.http().post(params, object : Callback.CommonCallback<String> {
            override fun onSuccess(result: String) {
                val successResult  = callBack.getResult(result)
                callBack.onSuccess(successResult)
            }

            override fun onError(throwable: Throwable, b: Boolean) {
                context?.longToast("请求失败,Error:" + throwable.message)
                callBack.onError(throwable, b)
            }

            override fun onCancelled(e: Callback.CancelledException) {
                callBack.onCancelled(e)
            }

            override fun onFinished() {
                onPostInvoke()
                callBack.onFinished()
            }
        })
        return this;
    }

}