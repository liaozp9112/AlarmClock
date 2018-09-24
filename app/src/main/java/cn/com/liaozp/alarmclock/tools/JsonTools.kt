package cn.com.liaozp.alarmclock.tools

import org.json.JSONObject
import org.json.JSONArray
import org.json.JSONException



class JsonTools {

    companion object {
        /**
         * 生成JSONObject
         *
         * @param jsonStr
         * @return
         */
        fun generateJsonObj(jsonStr: String): JSONObject {
            var jsonStr = jsonStr
            var jsonObject= JSONObject()
            if (!jsonStr.isNullOrBlank()) {
                try {
                    val start = jsonStr.indexOf("{")
                    val end = jsonStr.lastIndexOf("}")
                    jsonStr = jsonStr.substring(start, end + 1)
                    jsonObject = JSONObject(jsonStr)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
            return jsonObject
        }

        /**
         * 生成JSONArray
         *
         * @param jsonStr
         * @return
         */
        fun generateJsonArr(jsonStr: String): JSONArray {
            var jsonStr = jsonStr
            var jsonObject = JSONArray()
            if (!jsonStr.isNullOrBlank()) {
                try {
                    val start = jsonStr.indexOf("[")
                    val end = jsonStr.lastIndexOf("]")
                    jsonStr = jsonStr.substring(start, end + 1)
                    jsonObject = JSONArray(jsonStr)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
            return jsonObject
        }


        /**
         * 获取Json的String属性
         *
         * @param object
         * @param key
         * @return
         */
        fun getJsonString(`object`: JSONObject, key: String): String {
            var value = ""
            if (contain(`object`, key)) {
                try {
                    value = `object`.getString(key)
                } catch (e: Exception) {
                }

            }
            return value
        }

        /**
         * 判断JSON包含指定KEY
         *
         * @param json
         * @param key
         * @return
         */
        fun contain(json: JSONObject, key: String): Boolean {
            return json.has(key)
        }

        /**
         * 将Map集合转换成JSONOject
         *
         * @param map
         * @return JSONObject
         */
        fun swapMapToJSONObject(map: Map<String, Any>): JSONObject {
            val obj = JSONObject()
            val iter = map.keys.iterator()
            while (iter.hasNext()) {
                try {
                    val key = iter.next()
                    obj.put(key, if (map[key] != null) map[key].toString() else "")
                } catch (e: JSONException) {

                    e.printStackTrace()
                }

            }
            return obj

        }

        /**
         * 将一个JSONObject对象转换为一个HashMap
         *
         * @param jsonObj
         * @return Map
         */
        fun swapJsonToMap(jsonObj: JSONObject?): Map<String, Any> {
            val map = HashMap<String, Any>()
            if (jsonObj != null) {
                val keys = jsonObj.keys()
                while (keys.hasNext()) {
                    val key = keys.next()
                    map[key] = getJsonString(jsonObj, key)
                }
            }
            return map
        }

        /**
         * 将一个value放进JSONObject中
         *
         * @param
         * @return
         */
        fun put(jsonObj: JSONObject, key: String, value: Any): JSONObject {
            try {
                jsonObj.put(key, value)
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            return jsonObj
        }

        /**
         *
         * @param jsonObj
         * @param key
         * @return
         */
        fun getJsonObject(jsonObj: JSONObject, key: String): JSONObject? {
            var json: JSONObject? = null
            try {
                json = jsonObj.getJSONObject(key)
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            return json
        }


        /**
         * 将一个JSONArray对象转换为一个ArrayList<HashMap></HashMap><String></String>, Object>>集合
         * @param jsonArr
         * @return
         */
        fun parserJsonArrToMapList(jsonArr: JSONArray): List<Map<String, Any>> {
            val arrayList = ArrayList<Map<String, Any>>()
            try {
                for (i in 0 until jsonArr.length()) {
                    arrayList.add(parserJsonToMap(jsonArr.getJSONObject(i)))
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            return arrayList
        }

        /**
         * 将一个jsonArrStr字符串转换为一个ArrayList<HashMap></HashMap><String></String>, Object>>集合
         * @param jsonArrStr
         * @return
         */
        fun parserJsonArrToMapList(jsonArrStr: String): List<Map<String, Any>>? {
            var arrayList: List<Map<String, Any>>? = null
            try {
                arrayList = parserJsonArrToMapList(parseJsonArray(jsonArrStr))
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return arrayList
        }

        /**
         * 将Map集合转换成JSONOject
         * @param  map
         * @return JSONObject
         */
        fun parserMapToJSONObject(map: Map<String, Any>): JSONObject {
            val obj = JSONObject()
            val iter = map.keys.iterator()
            while (iter.hasNext()) {
                try {
                    val key = iter.next()
                    obj.put(key, if (map[key] != null) map[key].toString() else "")
                } catch (e: JSONException) {

                    e.printStackTrace()
                }

            }
            return obj

        }

        /**
         * 生成JSONArray
         * @param jsonStr
         * @return
         */
        fun parseJsonArray(jsonStr: String): JSONArray {
            var jsonStr = jsonStr
            var jsonObject = JSONArray()
            if (!jsonStr.isNullOrBlank()) {
                try {
                    val index = jsonStr.indexOf("[")
                    val end = jsonStr.lastIndexOf("]")
                    jsonStr = jsonStr.substring(index, end + 1)
                    jsonObject = JSONArray(jsonStr)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
            return jsonObject
        }

        /**
         * 将List 转换成JSONArray
         * @param  listMap
         * @return JSONArray
         */
        fun parseListToJSONArray(listMap: List<Map<String, Any>>): JSONArray {
            val array = JSONArray()
            for (i in listMap.indices) {
                val map = listMap[i]
                val jsonObject = parserMapToJSONObject(map)
                array.put(jsonObject)
            }
            return array
        }


        /**
         * 将一个JSONObject对象转换为一个HashMap
         * @param jsonObj
         * @return
         */
        fun parserJsonToMap(jsonObj: JSONObject?): Map<String, Any> {
            val params = HashMap<String, Any>()
            if (jsonObj != null) {
                val keys = jsonObj.keys()
                while (keys.hasNext()) {
                    val keyt = keys.next() as String
                    params[keyt] = getJsonString(jsonObj, keyt)
                }
            }
            return params
        }

        /**
         * 从json对象中取json数组
         * @param jsonObj
         * @param key
         * @return
         */
        fun getJArrFromJObj(jsonObj: JSONObject, key: String): JSONArray? {
            var arr: JSONArray? = null
            try {
                arr = jsonObj.getJSONArray(key)
            } catch (e: Exception) {

            }

            return arr
        }


        /**
         * 从jsonarr数组中取json
         * @param arr
         * @param index
         * @return
         */
        fun getJsonFromJArr(arr: JSONArray, index: Int): JSONObject? {
            var `object`: JSONObject? = null
            try {
                `object` = arr.getJSONObject(index)
            } catch (e: Exception) {

            }

            return `object`
        }


        /**
         * 获取Json的String属性
         *
         * @param object
         * @param key
         * @return
         */
        fun getJsonInteger(`object`: JSONObject, key: String): Int {
            var value = 0

            if (contain(`object`, key)) {
                try {
                    value = Integer.valueOf(`object`.getString(key))
                } catch (e: Exception) {
                }

            }
            return value
        }
    }
}