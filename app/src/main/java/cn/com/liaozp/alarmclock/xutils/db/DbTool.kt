package cn.com.liaozp.alarmclock.xutils.db

import org.xutils.ex.DbException
import org.xutils.DbManager
import org.xutils.x
import org.xutils.db.Selector;


class DbTool {

    companion object {

        private val dbManager = x.getDb(XDaoConfig.getDaoConfig())


        fun getDbManager(): DbManager {
            return dbManager
        }

        fun saveOrUpdate(`object`: Any) {
            try {
                dbManager.saveOrUpdate(`object`)
            } catch (e: DbException) {
               e.printStackTrace();
            }

        }


        fun save(`object`: Any) {
            try {
                dbManager.save(`object`)
            } catch (e: DbException) {
                e.printStackTrace();
            }

        }

        fun <T> findAll(entityType: Class<T>): List<T> {
            try {
                return dbManager.findAll(entityType)
            } catch (e: DbException) {
                e.printStackTrace();
            }
            return emptyList();
        }

        fun delete(entity:Object){
            try {
                 dbManager.delete(entity);
            } catch (e: DbException) {
                e.printStackTrace();
            }
        }


        /* fun <T> find(selector: Selector<T>, orderby:String): ArrayList<T>? {
             var list: ArrayList<T>? = null
             try {
                 dbManager.selector(T::class.java).orderBy(orderby).findAll()

                 list = ArrayList(selector.findAll());
             } catch (e: DbException) {
                 e.printStackTrace();
             }
             return list
         }*/
    }
}