package cn.com.liaozp.alarmclock.xutils.http

open class StringCallBack:XCallBack<String> {

    override fun onSuccess(result: String) {}

    override fun onError(throwable: Throwable, b: Boolean) {}

    override fun onCancelled(e: Exception) {}

    override fun onFinished() {}

    override fun getResult(result: String): String {
      return result;
    }
}