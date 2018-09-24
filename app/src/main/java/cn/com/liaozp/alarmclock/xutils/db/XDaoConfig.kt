package cn.com.liaozp.alarmclock.xutils.db

import android.text.TextUtils
import org.xutils.DbManager
import org.xutils.db.sqlite.SqlInfoBuilder
import org.xutils.db.table.DbBase
import org.xutils.db.table.TableEntity
import org.xutils.ex.DbException
import org.xutils.x
import java.io.File


class XDaoConfig {

    companion object {

        private val DB_VERSION = 1

        private val DB_PATH = "/data/data/cn.com.liaozp.alarmclock.xutils.db/database"

        private val DB_NAME = "alarmClock.db"

        private var daoConfig: DbManager.DaoConfig?=null;
        public fun getDaoConfig(): DbManager.DaoConfig?{
            if (daoConfig == null) {
                initDbConfig()

            }
            return daoConfig
        }

        private fun initDbConfig() {
            daoConfig = DbManager.DaoConfig()
                    .setDbName(DB_NAME)//设置数据库的名称
                    .setAllowTransaction(true)//设置是否允许事务，默认true
                    .setDbDir(File(DB_PATH)) // 设置数据库的存放路径, 默认存储在app的私有目录(数据库默认存储在/data/data/你的应用程序/database/xxx.db)
                    .setDbVersion(DB_VERSION)//设置数据库的版本号
                    .setTableCreateListener { _db, table ->

                        if (table.name == Tables.T_ALARM_CLOCK.name){
                            println("${table.name} 创建成功");
                        }
                    }
                    //设置数据库打开的监听
                    .setDbOpenListener { db ->
                        // 开启WAL, 对写入加速提升巨大
                        db.database.enableWriteAheadLogging()
                    }
                    //设置数据库更新的监听
                    .setDbUpgradeListener { db, oldVersion, newVersion ->
                        // TODO: ...
                        // db.addColumn(...);
                        // db.dropTable(...);
                        // ...
                        // or
                        // db.dropDb();
                    }
            createAllTables();
        }

        private fun createAllTables(){
            for (table in Tables.values()){
                val cl = Class.forName(table.tablepath);
                createTableIfNotExist(x.getDb(daoConfig).getTable(cl));
            }
        }


        @Throws(DbException::class)
        protected fun createTableIfNotExist(table: TableEntity<*>) {
            if (!table.tableIsExist()) {
                synchronized(table.javaClass) {
                    if (!table.tableIsExist()) {
                        val sqlInfo = SqlInfoBuilder.buildCreateTableSqlInfo(table)
                        x.getDb(daoConfig).execNonQuery(sqlInfo)
                        val execAfterTableCreated = table.onCreated
                        if (!TextUtils.isEmpty(execAfterTableCreated)) {
                            x.getDb(daoConfig).execNonQuery(execAfterTableCreated)
                        }
                    }
                }
            }
        }

    }


}