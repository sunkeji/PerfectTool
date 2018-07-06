package com.wheel.perfect.api.downapi;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.skj.perfect.DaoMaster;
import com.skj.perfect.DaoSession;
import com.skj.perfect.DownApkInfoDao;
import com.wheel.perfect.MyApplication;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * ClassName:	DbDownUtil
 * Function:	${TODO} greenDao的运用封装
 * Reason:	${TODO} ADD REASON(可选)
 * Date:	2018/6/26 17:21
 *
 * @author 孙科技
 * @version ${TODO}
 * @see
 * @since JDK 1.8
 */
public class DbDownUtil {
    private static DbDownUtil db;
    private Context context;
    private final static String dbName = "tests_db";
    private DaoMaster.DevOpenHelper openHelper;

    /**
     * 初始化greenDao
     */
    public DbDownUtil() {
        this.context = MyApplication.mContext;
        openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);

    }

    /**
     * 单例
     *
     * @return
     */
    public static DbDownUtil getInstance() {
        if (db == null) {
            synchronized (DbDownUtil.class) {
                if (db == null)
                    db = new DbDownUtil();
            }
        }
        return db;
    }

    /**
     * 获取可读数据库
     */
    private SQLiteDatabase getReadableDatabase() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
        }
        SQLiteDatabase db = openHelper.getReadableDatabase();
        return db;
    }

    /**
     * 获取可写数据库
     */
    private SQLiteDatabase getWritableDatabase() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
        }
        SQLiteDatabase db = openHelper.getWritableDatabase();
        return db;
    }

    /**
     * 针对操作类DownApkInfo，保存
     *
     * @param info
     */
    public void save(DownApkInfo info) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        DownApkInfoDao downInfoDao = daoSession.getDownApkInfoDao();
        downInfoDao.insert(info);
    }

    /**
     * 针对操作类DownApkInfo，更新
     *
     * @param info
     */
    public void update(DownApkInfo info) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        DownApkInfoDao downInfoDao = daoSession.getDownApkInfoDao();
        downInfoDao.update(info);
    }

    /**
     * 针对操作类DownApkInfo，删除
     *
     * @param info
     */
    public void delete(DownApkInfo info) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        DownApkInfoDao downInfoDao = daoSession.getDownApkInfoDao();
        downInfoDao.delete(info);
    }

    /**
     * 针对操作类DownApkInfo，通过键名查询
     *
     * @param id
     */
    public DownApkInfo query(long id) {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        DownApkInfoDao downInfoDao = daoSession.getDownApkInfoDao();
        QueryBuilder<DownApkInfo> qb = downInfoDao.queryBuilder();
        qb.where(DownApkInfoDao.Properties.Id.eq(id));
        List<DownApkInfo> list = qb.list();
        if (list.isEmpty()) {
            return null;
        } else {
            return list.get(0);
        }
    }

    /**
     * 查询所有的数据
     *
     * @return
     */
    public List<DownApkInfo> queryAllList() {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        DownApkInfoDao downInfoDao = daoSession.getDownApkInfoDao();
        QueryBuilder<DownApkInfo> qb = downInfoDao.queryBuilder();
        return qb.list();
    }
}
