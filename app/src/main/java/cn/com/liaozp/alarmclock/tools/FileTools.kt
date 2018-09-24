package cn.com.liaozp.alarmclock.tools

import android.provider.MediaStore
import android.content.ContentResolver
import android.content.Context
import android.net.Uri


class FileTools {

    companion object {
        fun getRealFilePath(context: Context, uri: Uri?): String {
            if (null == uri) return ""
            val scheme = uri!!.getScheme()
            var data: String = "";
            if (scheme == null)
                data = uri!!.getPath()
            else if (ContentResolver.SCHEME_FILE == scheme) {
                data = uri!!.getPath()
            } else if (ContentResolver.SCHEME_CONTENT == scheme) {
                val cursor = context.getContentResolver().query(uri, arrayOf(MediaStore.Images.ImageColumns.DATA), null, null, null)
                if (null != cursor) {
                    if (cursor!!.moveToFirst()) {
                        val index = cursor!!.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                        if (index > -1) {
                            data = cursor!!.getString(index)
                        }
                    }
                    cursor!!.close()
                }
            }
            return data
        }

        fun getFileName(filename: String,defaultName:String=""):String{
            var start :Int = filename.lastIndexOf("/");
            var end :Int = filename.lastIndexOf(".");
            if(start!=-1&& end!=-1){
                return filename.substring(start+1,end);
            }else{
                return defaultName;
            }

        }
    }



}