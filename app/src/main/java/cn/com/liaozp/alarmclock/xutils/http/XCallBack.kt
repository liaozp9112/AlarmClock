package cn.com.liaozp.alarmclock.xutils.http




interface  XCallBack<T> {

    public fun onSuccess(result :T);

    public fun onError(throwable: Throwable, b: Boolean)

    public fun onCancelled(e: Exception)

    public fun onFinished()

    public fun getResult(result: String): T
}