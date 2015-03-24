package com.ztesoft.zsmart.easydao.support;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.exception.ExceptionHandler;
import com.ztesoft.zsmart.core.jdbc.BaseDAO;
import com.ztesoft.zsmart.core.jdbc.BlobWrapper;
import com.ztesoft.zsmart.core.jdbc.ClobWrapper;
import com.ztesoft.zsmart.core.jdbc.LobHelper;
import com.ztesoft.zsmart.core.jdbc.ParamArray;
import com.ztesoft.zsmart.core.jdbc.ParamMap;
import com.ztesoft.zsmart.core.jdbc.rowset.RowSetMapper;
import com.ztesoft.zsmart.core.jdbc.rowset.RowSetOperator;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.utils.BoHelper;
import com.ztesoft.zsmart.core.utils.ZSmartLogger;

/**
 * 针对数据库查询再做一层业务封装,将查询结果集自动映射成Java Bean
 * <p>
 * 为了提高业务代码的清晰性,业务内部不鼓励使用DynamicDict对象作为参数传递,鼓励使用自己定义的SimpleObject;<br>
 * 为了省去get\set的代码工作量,这里提供统一的映射方法将查询结果集自动转成指定的简单对象;
 * <p>
 * Copyright: Copyright (c) 2011
 * <p>
 * Company: ztesoft
 * <p>
 * Created Date: 2011-11-22
 * 
 * @author: zhang.wenqing
 * @version: R13
 * @Task：
 * @Reason：
 */
public class BusiBaseDAO extends BaseDAO {

    /** IGNORE_FIELD_SIGN */
    private static final String IGNORE_FIELD_SIGN = "$";
    
    
    /** logger */
    private static ZSmartLogger logger = ZSmartLogger.getLogger(BusiBaseDAO.class);

    /**
     * 通用查询方法:将查询的结果集合以简单对象LIST的方式返回
     * <p>
     * 当查询结果为空时返回一个空List; 当查询结果为一条或者多条记录时返回一个List集合
     * <p>
     * 该方法适用场景<br>
     * 1.查询结果在业务层还需要进一步增加或者删除元素<br>
     * 2.查询返回对象属性均为简单属性<br>
     * 
     * @param querySql 查询SQL
     * @param queryParam 查询SQL参数
     * @param objClass 返回对象原型
     * @return objList 返回对象LIST
     * @throws BaseAppException <br>
     */
    public List<Object> queryListToSimpObj(String querySql, ParamArray queryParam, final Class<?> objClass)
        throws BaseAppException {
        return query(querySql, queryParam, null, new RowSetMapper<List<Object>>() {
            public List<Object> mapRows(RowSetOperator op, ResultSet rs, int colNum, Object para) throws SQLException,
                BaseAppException {
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnCount = rsmd.getColumnCount();
                List<Object> objList = new ArrayList<Object>();

                try {
                    while (rs.next()) {
                        objList.add(toSimpleObj(rsmd, rs, columnCount, objClass));
                    }
                }
                catch (Exception e) {
                    ExceptionHandler.publish("queryListToSimpObj has exception:", e);
                }

                return objList;
            }
        });
    }

    /**
     * 
     * Description: <br> 
     *  
     * @author jiao.yang<br>
     * @taskId <br>
     * @param <T>
     * @param querySql querySql
     * @param queryParam querySql
     * @param objClass objClass
     * @param <T> T
     * @return List
     * @throws BaseAppException <br>
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> queryListToSimpObj3(String querySql, ParamArray queryParam, final Class<?> objClass)
        throws BaseAppException {
        return query(querySql, queryParam, null, new RowSetMapper<List<T>>() {
            public List<T> mapRows(RowSetOperator op, ResultSet rs, int colNum, Object para) throws SQLException,
                BaseAppException {
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnCount = rsmd.getColumnCount();
                List<T> objList = new ArrayList<T>();

                try {
                    while (rs.next()) {
                        objList.add((T) toSimpleObj(rsmd, rs, columnCount, objClass));
                    }
                }
                catch (Exception e) {
                    ExceptionHandler.publish("queryListToSimpObj has exception:", e);
                }

                return objList;
            }
        });
    }

    /**
     * 
     * Description: <br> 
     *  
     * @author jiao.yang<br>
     * @taskId <br>
     * @param <T>
     * @param querySql querySql
     * @param queryParam queryParam
     * @param objClass objClass
     * @param <T> T
     * @return List
     * @throws BaseAppException <br>
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> queryListToSimpObj3(String querySql, ParamMap queryParam, final Class<?> objClass)
        throws BaseAppException {
        return query(querySql, queryParam, null, null, new RowSetMapper<List<T>>() {
            public List<T> mapRows(RowSetOperator op, ResultSet rs, int colNum, Object para) throws SQLException,
                BaseAppException {
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnCount = rsmd.getColumnCount();
                List<T> objList = new ArrayList<T>();

                try {
                    while (rs.next()) {
                        objList.add((T) toSimpleObj(rsmd, rs, columnCount, objClass));
                    }
                }
                catch (Exception e) {
                    ExceptionHandler.publish("queryListToSimpObj has exception:", e);
                }

                return objList;
            }
        });
    }

    /**
     * 通用查询方法:将查询的结果集合以简单对象LIST的方式返回
     * <p>
     * 当查询结果为空时返回一个空List; 当查询结果为一条或者多条记录时返回一个List集合
     * <p>
     * 该方法适用场景<br>
     * 1.查询结果在业务层还需要进一步增加或者删除元素<br>
     * 2.查询返回对象属性均为简单属性<br>
     * 
     * @param querySql 查询SQL
     * @param queryParam 查询SQL参数
     * @param objClass 返回对象原型
     * @return objList 返回对象LIST
     * @throws BaseAppException <br>
     */
    public List<Object> queryListToSimpObj(String querySql, ParamMap queryParam, final Class<?> objClass)
        throws BaseAppException {
        return query(querySql, queryParam, null, null, new RowSetMapper<List<Object>>() {
            public List<Object> mapRows(RowSetOperator op, ResultSet rs, int colNum, Object para) throws SQLException,
                BaseAppException {
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnCount = rsmd.getColumnCount();
                List<Object> objList = new ArrayList<Object>();

                try {
                    while (rs.next()) {
                        objList.add(toSimpleObj(rsmd, rs, columnCount, objClass));
                    }
                }
                catch (Exception e) {
                    ExceptionHandler.publish("queryListToSimpObj has exception:", e);
                }

                return objList;
            }
        });
    }

    /**
     * 通用查询方法:将查询的结果集合以简单对象的LIST方式返回
     * <p>
     * 该方法用来查询返回一个对象List,并且该集合里面的对象有可能会存在一个子对象List,如查询商品以及商品属性列表
     * <p>
     * 查询SQL在编写时,需要在定义P_C_SEPARATOR列将主表与子对象从表的列分开
     * <p>
     * 该方法适用场景<br>
     * 1.查询对象的子对象LIST有且只有一个<br>
     * 2.查询结果在业务层还需要进一步增加或者删除元素<br>
     * 
     * @param querySql 查询SQL
     * @param queryParam 查询参数
     * @param objClass 返回对象原型
     * @param childObjClass 返回对象子对象原型
     * @param primaryKey 查询对象主键
     * @param childFieldName 返回对象子对象属性名
     * @return ret 返回对象LIST
     * @throws BaseAppException <br>
     */
    public List<Object> queryListToSimpObj2(String querySql, ParamArray queryParam, final Class<?> objClass,
        final Class<?> childObjClass, final String primaryKey, final String childFieldName) throws BaseAppException {
        return query(querySql, queryParam, null, new RowSetMapper<List<Object>>() {
            public List<Object> mapRows(RowSetOperator op, ResultSet rs, int colNum, Object para) throws SQLException,
                BaseAppException {
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnCount = rsmd.getColumnCount();
                List<Object> ret = new ArrayList<Object>();
                Object tempObj = null;
                List<Object> childTempList = null;

                String temp = null;

                try {
                    while (rs.next()) {
                        String primaryKeyValue = rs.getString(primaryKey);

                        if (temp == null) {
                            childTempList = new ArrayList<Object>();
                            temp = primaryKeyValue;
                            tempObj = toSimpleObj2(rsmd, rs, columnCount, objClass, childObjClass, childFieldName,
                                true, null, childTempList);
                            ret.add(tempObj);
                        }
                        else if (!temp.equals(primaryKeyValue)) {
                            childTempList = new ArrayList<Object>();
                            ret.add(toSimpleObj2(rsmd, rs, columnCount, objClass, childObjClass, childFieldName, true,
                                null, childTempList));
                            temp = primaryKeyValue;
                        }
                        else {
                            toSimpleObj2(rsmd, rs, columnCount, objClass, childObjClass, childFieldName, false,
                                tempObj, childTempList);
                        }
                    }
                }
                catch (Exception e) {
                    ExceptionHandler.publish("queryListToSimpObj2 has exception:", e);
                }

                return ret;
            }
        });
    }

    /**
     * 通用查询方法:将查询的结果集合以简单对象的LIST方式返回
     * <p>
     * 该方法用来查询返回一个对象List,并且该集合里面的对象有可能会存在一个子对象List,如查询商品以及商品属性列表
     * <p>
     * 查询SQL在编写时,需要在定义P_C_SEPARATOR列将主表与子对象从表的列分开
     * <p>
     * 该方法适用场景<br>
     * 1.查询对象的子对象LIST有且只有一个<br>
     * 2.查询结果在业务层还需要进一步增加或者删除元素<br>
     * 
     * @param querySql 查询SQL
     * @param queryParam 查询参数
     * @param objClass 返回对象原型
     * @param childObjClass 返回对象子对象原型
     * @param primaryKey 查询对象主键
     * @param childFieldName 返回对象子对象属性名
     * @return ret 返回对象LIST
     * @throws BaseAppException <br>
     */
    public List<Object> queryListToSimpObj2(String querySql, ParamMap queryParam, final Class<?> objClass,
        final Class<?> childObjClass, final String primaryKey, final String childFieldName) throws BaseAppException {
        return query(querySql, queryParam, null, null, new RowSetMapper<List<Object>>() {
            public List<Object> mapRows(RowSetOperator op, ResultSet rs, int colNum, Object para) throws SQLException,
                BaseAppException {
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnCount = rsmd.getColumnCount();
                List<Object> ret = new ArrayList<Object>();
                Object tempObj = null;
                List<Object> childTempList = null;

                String temp = null;

                try {
                    while (rs.next()) {
                        String primaryKeyValue = rs.getString(primaryKey);

                        if (temp == null) {
                            childTempList = new ArrayList<Object>();
                            temp = primaryKeyValue;
                            tempObj = toSimpleObj2(rsmd, rs, columnCount, objClass, childObjClass, childFieldName,
                                true, null, childTempList);
                            ret.add(tempObj);
                        }
                        else if (!temp.equals(primaryKeyValue)) {
                            childTempList = new ArrayList<Object>();
                            ret.add(toSimpleObj2(rsmd, rs, columnCount, objClass, childObjClass, childFieldName, true,
                                null, childTempList));
                            temp = primaryKeyValue;
                        }
                        else {
                            toSimpleObj2(rsmd, rs, columnCount, objClass, childObjClass, childFieldName, false,
                                tempObj, childTempList);
                        }
                    }
                }
                catch (Exception e) {
                    ExceptionHandler.publish("queryListToSimpObj2 has exception:", e);
                }

                return ret;
            }
        });
    }

    /**
     * 通用查询方法:将查询的结果集合以简单对象的方式返回
     * <p>
     * 该方法用来查询返回一个对象,并且该对象有可能会存在一个子对象List,如查询商品以及商品属性列表
     * <p>
     * 查询SQL在编写时,需要在定义P_C_SEPARATOR列将主表与子对象从表的列分开
     * <p>
     * 该方法适用场景<br>
     * 1.查询结果总数确定 为0-1条<br>
     * 2.查询对象的子对象LIST有且只有一个<br>
     * 
     * @param querySql 查询SQL
     * @param queryParam 查询参数
     * @param objClass 返回对象原型
     * @param childObjClass 返回对象子对象原型
     * @param childFieldName 返回对象子对象属性名
     * @return tempObj 返回对象
     * @throws BaseAppException <br>
     */
    public Object queryToSimpObj2(String querySql, ParamArray queryParam, final Class<?> objClass,
        final Class<?> childObjClass, final String childFieldName) throws BaseAppException {
        return query(querySql, queryParam, null, new RowSetMapper<Object>() {
            public Object mapRows(RowSetOperator op, ResultSet rs, int colNum, Object para) throws SQLException,
                BaseAppException {
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnCount = rsmd.getColumnCount();
                Object tempObj = null;
                List<Object> childTempList = null;

                try {
                    int i = 0;

                    while (rs.next()) {
                        if (i == 0) {
                            childTempList = new ArrayList<Object>();
                            tempObj = toSimpleObj2(rsmd, rs, columnCount, objClass, childObjClass, childFieldName,
                                true, null, childTempList);
                        }
                        else {
                            toSimpleObj2(rsmd, rs, columnCount, objClass, childObjClass, childFieldName, false,
                                tempObj, childTempList);
                        }

                        i++;
                    }
                }
                catch (Exception e) {
                    ExceptionHandler.publish("queryToSimpObj2 has exception:", e);
                }

                return tempObj;
            }
        });
    }

    /**
     * 通用查询方法:将查询的结果集合以简单对象的方式返回
     * <p>
     * 该方法用来查询返回一个对象,并且该对象有可能会存在一个子对象List,如查询商品以及商品属性列表
     * <p>
     * 查询SQL在编写时,需要在定义P_C_SEPARATOR列将主表与子对象从表的列分开
     * <p>
     * 该方法适用场景<br>
     * 1.查询结果总数确定 为0-1条<br>
     * 2.查询对象的子对象LIST有且只有一个<br>
     * 
     * @param querySql 查询SQL
     * @param queryParam 查询参数
     * @param objClass 返回对象原型
     * @param childObjClass 返回对象子对象原型
     * @param childFieldName 返回对象子对象属性名
     * @return tempObj 返回对象
     * @throws BaseAppException <br>
     */
    public Object queryToSimpObj2(String querySql, ParamMap queryParam, final Class<?> objClass,
        final Class<?> childObjClass, final String childFieldName) throws BaseAppException {
        return query(querySql, queryParam, null, null, new RowSetMapper<Object>() {
            public Object mapRows(RowSetOperator op, ResultSet rs, int colNum, Object para) throws SQLException,
                BaseAppException {
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnCount = rsmd.getColumnCount();
                Object tempObj = null;
                List<Object> childTempList = null;

                try {
                    int i = 0;

                    while (rs.next()) {
                        if (i == 0) {
                            childTempList = new ArrayList<Object>();
                            tempObj = toSimpleObj2(rsmd, rs, columnCount, objClass, childObjClass, childFieldName,
                                true, null, childTempList);
                        }
                        else {
                            toSimpleObj2(rsmd, rs, columnCount, objClass, childObjClass, childFieldName, false,
                                tempObj, childTempList);
                        }

                        i++;
                    }
                }
                catch (Exception e) {
                    ExceptionHandler.publish("queryToSimpObj2 has exception:", e);
                }

                return tempObj;
            }
        });
    }

    /**
     * 通用查询方法:将查询的结果集合以简单对象的方式返回
     * <p>
     * 当查询结果为空时返回NULL
     * <p>
     * 该方法适用场景<br>
     * 1.查询结果总数确定 为0-1条<br>
     * 2.查询返回对象属性均为简单属性<br>
     * 
     * @param querySql querySql
     * @param queryParam queryParam
     * @param objClass objClass
     * @param <T> T
     * @return T
     * @throws BaseAppException <br>
     */
    @SuppressWarnings("unchecked")
    public <T> T queryToSimpObj(String querySql, ParamArray queryParam, final Class<?> objClass)
        throws BaseAppException {
        return query(querySql, queryParam, null, new RowSetMapper<T>() {
            public T mapRows(RowSetOperator op, ResultSet rs, int colNum, Object para) throws SQLException,
                BaseAppException {
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnCount = rsmd.getColumnCount();

                try {
                    if (rs.next()) {
                        return (T) toSimpleObj(rsmd, rs, columnCount, objClass);
                    }
                }
                catch (Exception e) {
                    ExceptionHandler.publish("queryToSimpObj has exception:", e);
                }

                return null;
            }
        });
    }

    /**
     * 通用查询方法:将查询的结果集合以简单对象的方式返回
     * <p>
     * 当查询结果为空时返回NULL
     * <p>
     * 该方法适用场景<br>
     * 1.查询结果总数确定 为0-1条<br>
     * 2.查询返回对象属性均为简单属性<br>
     * 
     * @param querySql querySql
     * @param queryParam queryParam
     * @param objClass objClass
     * @param <T> T
     * @return T
     * @throws BaseAppException <br>
     */
    @SuppressWarnings("unchecked")
    public <T> T queryToSimpObj(String querySql, ParamMap queryParam, final Class<?> objClass) throws BaseAppException {
        return query(querySql, queryParam, null, null, new RowSetMapper<T>() {
            public T mapRows(RowSetOperator op, ResultSet rs, int colNum, Object para) throws SQLException,
                BaseAppException {
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnCount = rsmd.getColumnCount();

                try {
                    if (rs.next()) {
                        return (T) toSimpleObj(rsmd, rs, columnCount, objClass);
                    }
                }
                catch (Exception e) {
                    ExceptionHandler.publish("queryToSimpObj has exception:", e);
                }

                return null;
            }
        });
    }

    /**
     * 通用查询方法:将查询的结果集合以简单对象的方式返回<br>
     * <p>
     * 当查询结果为空时返回一个空数组<br>
     * 当查询结果为一条或者多条记录时返回一个简单对象的Array<br>
     * <p>
     * 该方法适用场景<br>
     * 1.查询返回对象在业务层中不需要再做新增删除元素操作<br>
     * 2.查询返回对象属性均为简单属性<br>
     * 
     * @param qrySql 查询SQL
     * @param qryParam 查询参数
     * @param objClass 返回的简单对象 数组的元素原型
     * @return qryObj 返回的简单对象 数组
     * @param <T> T
     * @throws BaseAppException <br>
     */
    @SuppressWarnings("unchecked")
    public <T> T qryToArrayObj(String qrySql, ParamArray qryParam, final Class<?> objClass) throws BaseAppException {
        return query(qrySql, qryParam, null, new RowSetMapper<T>() {
            public T mapRows(RowSetOperator op, ResultSet rs, int colNum, Object para) throws SQLException,
                BaseAppException {
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnCount = rsmd.getColumnCount();
                List<Object> objList = new ArrayList<Object>();

                try {
                    while (rs.next()) {
                        objList.add(toSimpleObj(rsmd, rs, columnCount, objClass));
                    }
                }
                catch (Exception e) {
                    ExceptionHandler.publish("queryListToSimpObj has exception:", e);
                }

                Object qryObj = Array.newInstance(objClass, objList.size());

                for (int i = 0; i < objList.size(); i++) {
                    Array.set(qryObj, i, objList.get(i));
                }

                return (T) qryObj;
            }
        });
    }

    /**
     * 通用查询方法:将查询的结果集合以简单对象的方式返回<br>
     * <p>
     * 当查询结果为空时返回一个空数组<br>
     * 当查询结果为一条或者多条记录时返回一个简单对象的Array<br>
     * <p>
     * 该方法适用场景<br>
     * 1.查询返回对象在业务层中不需要再做新增删除元素操作<br>
     * 2.查询返回对象属性均为简单属性<br>
     * 
     * @param qrySql 查询SQL
     * @param qryParam 查询参数
     * @param objClass 返回的简单对象 数组的元素原型
     * @return qryObj 返回的简单对象 数组
     * @param <T> T
     * @throws BaseAppException <br>
     */
    @SuppressWarnings("unchecked")
    public <T> T qryToArrayObj(String qrySql, ParamMap qryParam, final Class<?> objClass) throws BaseAppException {
        return query(qrySql, qryParam, null, null, new RowSetMapper<T>() {
            public T mapRows(RowSetOperator op, ResultSet rs, int colNum, Object para) throws SQLException,
                BaseAppException {
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnCount = rsmd.getColumnCount();
                List<Object> objList = new ArrayList<Object>();

                try {
                    while (rs.next()) {
                        objList.add(toSimpleObj(rsmd, rs, columnCount, objClass));
                    }
                }
                catch (Exception e) {
                    ExceptionHandler.publish("queryListToSimpObj has exception:", e);
                }

                Object qryObj = Array.newInstance(objClass, objList.size());

                for (int i = 0; i < objList.size(); i++) {
                    Array.set(qryObj, i, objList.get(i));
                }

                return (T) qryObj;
            }
        });
    }

    /**
     * 通用查询方法:将查询的结果集合以简单对象的方式返回<br>
     * <p>
     * 当查询结果为空时返回一个空数组<br>
     * 当查询结果为一条或者多条记录时返回一个简单对象的Array<br>
     * <p>
     * 该方法适用场景<br>
     * 1.查询返回对象在业务层中不需要再做新增删除元素操作<br>
     * 2.查询返回对象属性均为简单属性<br>
     * 
     * @param querySql 查询SQL
     * @param queryParam 查询参数
     * @param objClass 返回的简单对象 数组的元素原型
     * @return qryObj 返回的简单对象 数组
     * @param childObjClass childObjClass
     * @param primaryKey primaryKey
     * @param childFieldName childFieldName
     * @throws BaseAppException <br>
     */
    public Object qryToArrayObj2(String querySql, ParamArray queryParam, final Class<?> objClass,
        final Class<?> childObjClass, final String primaryKey, final String childFieldName) throws BaseAppException {
        return query(querySql, queryParam, null, new RowSetMapper<Object>() {
            public Object mapRows(RowSetOperator op, ResultSet rs, int colNum, Object para) throws SQLException,
                BaseAppException {
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnCount = rsmd.getColumnCount();
                List<Object> retList = new ArrayList<Object>();
                Object tempObj = null;
                List<Object> childTempList = null;
                String temp = null;
                int index = 0;

                while (rs.next()) {
                    String primaryKeyValue = rs.getString(primaryKey);

                    if (temp == null) {
                        childTempList = new ArrayList<Object>();
                        temp = primaryKeyValue;
                        tempObj = toSimpleObj2(rsmd, rs, columnCount, objClass, childObjClass, childFieldName, true,
                            null, childTempList);
                        retList.add(tempObj);
                        index++;
                    }
                    else if (!temp.equals(primaryKeyValue)) {
                        childTempList = new ArrayList<Object>();
                        retList.add(toSimpleObj2(rsmd, rs, columnCount, objClass, childObjClass, childFieldName, true,
                            null, childTempList));
                        temp = primaryKeyValue;
                        index++;
                    }
                    else {
                        toSimpleObj2(rsmd, rs, columnCount, objClass, childObjClass, childFieldName, false, tempObj,
                            childTempList);
                    }
                }

                Object ret = Array.newInstance(objClass, index);

                for (int i = 0; i < index; i++) {
                    Array.set(ret, index, retList.get(i));
                }

                return ret;
            }
        });
    }

    /**
     * 通用查询方法:将查询的结果集合以简单对象的方式返回<br>
     * <p>
     * 当查询结果为空时返回一个空数组<br>
     * 当查询结果为一条或者多条记录时返回一个简单对象的Array<br>
     * <p>
     * 该方法适用场景<br>
     * 1.查询返回对象在业务层中不需要再做新增删除元素操作<br>
     * 2.查询返回对象属性均为简单属性<br>
     * 
     * @param querySql 查询SQL
     * @param queryParam 查询参数
     * @param objClass 返回的简单对象 数组的元素原型
     * @param  childObjClass childObjClass
     * @param primaryKey primaryKey
     * @param childFieldName childFieldName
     * @return qryObj 返回的简单对象 数组
     * @throws BaseAppException <br>
     */ 
    public Object qryToArrayObj2(String querySql, ParamMap queryParam, final Class<?> objClass,
        final Class<?> childObjClass, final String primaryKey, final String childFieldName) throws BaseAppException {
        return query(querySql, queryParam, null, null, new RowSetMapper<Object>() {
            public Object mapRows(RowSetOperator op, ResultSet rs, int colNum, Object para) throws SQLException,
                BaseAppException {
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnCount = rsmd.getColumnCount();
                List<Object> retList = new ArrayList<Object>();
                Object tempObj = null;
                List<Object> childTempList = null;
                String temp = null;
                int index = 0;

                while (rs.next()) {
                    String primaryKeyValue = rs.getString(primaryKey);

                    if (temp == null) {
                        childTempList = new ArrayList<Object>();
                        temp = primaryKeyValue;
                        tempObj = toSimpleObj2(rsmd, rs, columnCount, objClass, childObjClass, childFieldName, true,
                            null, childTempList);
                        retList.add(tempObj);
                        index++;
                    }
                    else if (!temp.equals(primaryKeyValue)) {
                        childTempList = new ArrayList<Object>();
                        retList.add(toSimpleObj2(rsmd, rs, columnCount, objClass, childObjClass, childFieldName, true,
                            null, childTempList));
                        temp = primaryKeyValue;
                        index++;
                    }
                    else {
                        toSimpleObj2(rsmd, rs, columnCount, objClass, childObjClass, childFieldName, false, tempObj,
                            childTempList);
                    }
                }

                Object ret = Array.newInstance(objClass, index);

                for (int i = 0; i < index; i++) {
                    Array.set(ret, index, retList.get(i));
                }

                return ret;
            }
        });
    }

    /**
     * Description: mapRows<br>
     * 
     * @author jiao.yang<br>
     * @taskId <br>
     * @param rsmd rsmd<br>
     * @param rs rs
     * @param columnCount columnCount
     * @param retObj retObj
     * @return Object<br>
     * @throws BaseAppException <br>
     */
    public Object toSimpleObj(ResultSetMetaData rsmd, ResultSet rs, int columnCount, Class<?> retObj)
        throws BaseAppException {
        Object obj = null;

        if (BoHelper.isSimpleType(retObj)) {
            obj = toBaseSimpleObj(rs, retObj);
        }
        else {
            try {
                obj = retObj.newInstance();
            }
            catch (Exception e) {
                ExceptionHandler.publish("toSimpleObj Has Exception", e);
            }

            Map<String, Object> parentMap = new HashMap<String, Object>();

            for (int i = 1; i <= columnCount; i++) {
                String columnName = null;
                try {
                    columnName = rsmd.getColumnName(i).toLowerCase();

                    // 不理会带$的字段
                    if (columnName.indexOf(IGNORE_FIELD_SIGN) != -1) {
                        continue;
                    }
                }
                catch (SQLException e) {
                    ExceptionHandler.publish("toSimpleObj Has Exception", e);
                }

                if (columnName.indexOf("#") != -1) {
                    String[] columnNames = columnName.split("#");
                    String columnName0 = columnNames[0];
                    String columnName1 = columnNames[1];

                    Object parentObj = parentMap.get(columnName0);
                    if (parentObj == null) {
                        parentObj = genObj(columnName0, obj, retObj);
                        parentMap.put(columnName0, parentObj);
                    }

                    setObjAttr(columnName1, rs, i, parentObj, parentObj.getClass());
                }
                else {
                    setObjAttr(columnName, rs, i, obj, retObj);
                }
            }
        }

        return obj;
    }

    /**
     * Description: mapRows<br>
     * 
     * @author jiao.yang<br>
     * @taskId <br>
     * @param rs rs<br>
     * @param retObj retObj
     * @return Object<br>
     */
    private Object toBaseSimpleObj(ResultSet rs, Class<?> retObj) {
        Object obj = null;

        if (String.class.equals(retObj)) {
            try {
                obj = rs.getString(1);
            }
            catch (SQLException e) {
                logger.error(e);
            }
        }
        else if (Long.class.equals(retObj)) {
            try {
                obj = rs.getLong(1);
            }
            catch (SQLException e) {
                logger.error(e);
            }
        }
        else if (Date.class.equals(retObj)) {
            try {
                obj = rs.getDate(1);
            }
            catch (SQLException e) {
                logger.error(e);
            }
        }
        else if (int.class.equals(retObj)) {
            try {
                obj = rs.getInt(1);
            }
            catch (SQLException e) {
                logger.error(e);
            }
        }

        return obj;
    }

    /**
     * 更新操作
     * 
     * @param updateSql 该SQL的通配符类型必须为"?"
     * @param args SQL参数 参数类型必须为基础类型 如：long/Long/String/Date等，顺序与SQL中的"?"保持一致
     * @return int
     * @throws BaseAppException <br>
     */
    public int updateObject(String updateSql, Object... args) throws BaseAppException {
        return this.executeUpdate(updateSql, getParamArray(args));
    }

    /**
     * 更新操作
     * 
     * @param updateSql 该SQL的通配符类型必须为":PARAM_NAME"
     * @param args SQL参数对象(DTO)
     * @return int
     * @throws BaseAppException <br>
     */
    public int updateObject(String updateSql, Object args) throws BaseAppException {
        if (BoHelper.isSimpleType(args.getClass())) {
            return this.executeUpdate(updateSql, getParamArray(args));
        }
        else {
            return this.executeUpdate(updateSql, getParamMap(args));
        }
    }

    /**
     * Description: updateObject<br>
     * 
     * @author jiao.yang<br>
     * @taskId <br>
     * @param updateSql updateSql<br>
     * @return int<br>
     * @throws BaseAppException <br>
     */
    public int updateObject(String updateSql) throws BaseAppException {
        return this.executeUpdate(updateSql, new ParamArray());
    }

    /**
     * 查询操作
     * 
     * @param selectSql 该SQL的通配符类型必须为"?"
     * @param retClass 返回简单对象原型
     * @param args SQL参数 参数类型必须为基础类型 如：long/Long/String/Date等，顺序与SQL中的"?"保持一致
     * @param <T> T
     * @return Object 简单对象. 无记录时返回null
     * @throws BaseAppException <br>
     */
    @SuppressWarnings("unchecked")
    public <T> T selectObject(String selectSql, Class<?> retClass, Object... args) throws BaseAppException {
        return (T) this.queryToSimpObj(selectSql, getParamArray(args), retClass);
    }

    /**
     * 查询操作
     * 
     * @param selectSql 该SQL的通配符类型必须为"?"
     * @param retClass 返回简单对象原型
     * @param args SQL参数 参数类型必须为基础类型 如：long/String/Date等
     * @param <T> T
     * @return Object Array对象。无记录时返回空的数组
     * @throws BaseAppException <br>
     */
    @SuppressWarnings("unchecked")
    public <T> T selectArray(String selectSql, Class<?> retClass, Object... args) throws BaseAppException {
        return (T) this.qryToArrayObj(selectSql, getParamArray(args), retClass);
    }

    /**
     * 查询操作
     * 
     * @param selectSql 该SQL的通配符类型必须为":PARAM_NAME"
     * @param retClass 返回简单对象原型
     * @param args SQL参数对象(DTO)
     * @param <T> T
     * @return Object 简单对象 无记录时返回null
     * @throws BaseAppException <br>
     */
    @SuppressWarnings("unchecked")
    public <T> T selectObject(String selectSql, Class<?> retClass, Object args) throws BaseAppException {
        if (BoHelper.isSimpleType(args.getClass())) {
            return (T) this.queryToSimpObj(selectSql, getParamArray(args), retClass);
        }
        else {
            return (T) this.queryToSimpObj(selectSql, getParamMap(args), retClass);
        }
    }

    /**
     * 查询操作
     * 
     * @param selectSql 该SQL的通配符类型必须为":PARAM_NAME"
     * @param retClass 返回简单对象原型
     * @param args SQL参数对象
     * @param <T> T
     * @return Object Array对象。无记录时返回空的数组
     * @throws BaseAppException <br>
     */
    @SuppressWarnings("unchecked")
    public <T> T selectArray(String selectSql, Class<?> retClass, Object args) throws BaseAppException {
        if (BoHelper.isSimpleType(args.getClass())) {
            return (T) this.qryToArrayObj(selectSql, getParamArray(args), retClass);
        }
        else {
            return (T) this.qryToArrayObj(selectSql, getParamMap(args), retClass);
        }
    }

    /**
     * Description: updateObject<br>
     * 
     * @author jiao.yang<br>
     * @taskId <br>
     * @param selectSql selectSql<br>
     * @param retClass retClass
     * @param args args
     * @param <T> T
     * @return int<br>
     * @throws BaseAppException <br>
     */
    public <T> List<T> selectList(String selectSql, Class<?> retClass, Object... args) throws BaseAppException {
        return this.queryListToSimpObj3(selectSql, getParamArray(args), retClass);
    }

    /**
     * Description: updateObject<br>
     * 
     * @author jiao.yang<br>
     * @taskId <br>
     * @param selectSql selectSql<br>
     * @param retClass retClass
     * @param <T> T
     * @param args args
     * @return int<br>
     * @throws BaseAppException <br>
     */
    public <T> List<T> selectList(String selectSql, Class<?> retClass, Object args) throws BaseAppException {
        if (BoHelper.isSimpleType(args.getClass())) {
            return this.queryListToSimpObj3(selectSql, getParamArray(args), retClass);
        }
        else {
            return this.queryListToSimpObj3(selectSql, getParamMap(args), retClass);
        }
    }

    /**
     * Description: updateObject<br>
     * 
     * @author jiao.yang<br>
     * @taskId <br>
     * @param selectSql selectSql<br>
     * @param retClass retClass
     * @param <T> T
     * @return int<br>
     * @throws BaseAppException <br>
     */
    public <T> List<T> selectList(String selectSql, Class<?> retClass) throws BaseAppException {
        return this.queryListToSimpObj3(selectSql, new ParamArray(), retClass);
    }

    
    /**
     * Description: updateObject<br>
     * 
     * @author jiao.yang<br>
     * @taskId <br>
     * @param selectSql selectSql<br>
     * @param retClass retClass
     * @param <T> T
     * @return  int<br>
     * @throws BaseAppException <br>
     */
    @SuppressWarnings("unchecked")
    public <T> T selectObject(String selectSql, Class<?> retClass) throws BaseAppException {
        return (T) this.queryToSimpObj(selectSql, new ParamArray(), retClass);
    }

    /**
     * Description: updateObject<br>
     * 
     * @author jiao.yang<br>
     * @taskId <br>
     * @param selectSql selectSql<br>
     * @param retClass retClass<br>
     * @param <T> T
     * @return  T<br>
     * @throws BaseAppException <br>
     */
    @SuppressWarnings("unchecked")
    public <T> T selectArray(String selectSql, Class<?> retClass) throws BaseAppException {
        return (T) this.qryToArrayObj(selectSql, new ParamArray(), retClass);
    }

    /**
     * Description: getParamMap<br>
     * 
     * @author jiao.yang<br>
     * @taskId <br>
     * @param obj obj<br>
     * @return ParamMap<br>
     * @throws BaseAppException <br>
     */
    private ParamMap getParamMap(Object obj) throws BaseAppException {
        ParamMap paramMap = new ParamMap();

        if (obj != null) {
            Class<?> objClass = obj.getClass();

            if (DynamicDict.class.equals(objClass)) {
                DynamicDict dictObj = (DynamicDict) obj;
                HashMap<String, Object> map = dictObj.valueMap;
                Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, Object> entry = iterator.next();
                    Object objValue = entry.getValue();
                    Class<?> objValueClass = objValue.getClass();

                    if (!BoHelper.isSimpleType(objValueClass) || boolean.class.equals(objValueClass)) {
                        continue;
                    }

                    setParamMap(paramMap, entry.getKey(), objValue, objValueClass);
                }
            }
            else if (ParamMap.class.equals(objClass)) {
                paramMap = (ParamMap) obj;
            }
            else {
                List<Field> list = this.getAllField(objClass);
                for (Field field : list) {
                    Class<?> fileType = field.getType();
                    
                    // 对于static类型的属性，忽略不处理，因为一般的dto中不会定义static类型的属性
                    // modified by Zhang.Jinhua0027000268，TaskId：483949
                    int modifier = field.getModifiers();
                    if (Modifier.isStatic(modifier)) {
                        continue;
                    }
                    
                    if (!this.isSimpleType(fileType) || boolean.class.equals(fileType)) {
                        continue;
                    }

                    String fieldName = field.getName();
                    String paramName = BoHelper.firstLetterUpperToSlash(fieldName);
                    Object paramValue = "";
                    try {
                        Method method = getMethodByName(objClass, "get" + fieldName);
                        paramValue = method.invoke(obj);
                    }
                    catch (Exception e) {
                        ExceptionHandler.publish("business dao getParamMap has exception:" + e);
                    }

                    setParamMap(paramMap, paramName, paramValue, fileType);
                }
            }
        }

        return paramMap;
    }

    /**
     * Description: getParamMap<br>
     * 
     * @author jiao.yang<br>
     * @taskId <br>
     * @param paramMap paramMap<br>
     * @param paramName paramName<br>
     * @param paramValue paramValue<br>
     * @param fileType fileType<br>
     * @throws BaseAppException <br>
     */
    private void setParamMap(ParamMap paramMap, String paramName, Object paramValue, Class<?> fileType)
        throws BaseAppException {
        if (long.class.equals(fileType) || Long.class.equals(fileType)) {
            paramMap.set(paramName, (Long) paramValue);
        }
        else if (String.class.equals(fileType)) {
            paramMap.set(paramName, (String) paramValue);
        }
        else if (java.sql.Date.class.equals(fileType)) {
            paramMap.set(paramName, (Date) paramValue);
        }
        else if (java.util.Date.class.equals(fileType)) {
            paramMap.set(paramName, (java.util.Date) paramValue);
        }
        else if (BigDecimal.class.equals(fileType)) {
            paramMap.set(paramName, (BigDecimal) paramValue);
        }
        else if (int.class.equals(fileType) || Integer.class.equals(fileType)) {
            paramMap.set(paramName, (Integer) paramValue);
        }
        else if (BlobWrapper.class.equals(fileType)) {
            paramMap.set(paramName, (BlobWrapper) paramValue);
        }
        else if (ClobWrapper.class.equals(fileType)) {
            paramMap.set(paramName, (ClobWrapper) paramValue);
        }
        else {
            ExceptionHandler.publish("business dao do not support this param type:" + fileType);
        }
    }

    /**
     * Description: getParamMap<br>
     * 
     * @author jiao.yang<br>
     * @taskId <br>
     * @param args args<br>
     * @return ParamMap<br>
     * @throws BaseAppException <br>
     */
    private ParamArray getParamArray(Object... args) throws BaseAppException {
        ParamArray paramArray = new ParamArray();

        if (args != null) {
            for (Object obj : args) {
                Class<?> objClass = obj.getClass();

                if (!BoHelper.isSimpleType(objClass)) {
                    continue;
                }

                if (long.class.equals(objClass) || Long.class.equals(objClass)) {
                    paramArray.set("", (Long) obj);
                }
                else if (String.class.equals(objClass)) {
                    paramArray.set("", (String) obj);
                }
                else if (java.sql.Date.class.equals(objClass)) {
                    paramArray.set("", (Date) obj);
                }
                else if (java.util.Date.class.equals(objClass)) {
                    paramArray.set("", (java.util.Date) obj);
                }
                else if (BigDecimal.class.equals(objClass)) {
                    paramArray.set("", (BigDecimal) obj);
                }
                else if (int.class.equals(objClass) || Integer.class.equals(objClass)) {
                    paramArray.set("", (Integer) obj);
                }
                else {
                    ExceptionHandler.publish("business dao do not support this param type:" + objClass);
                }
            }
        }

        return paramArray;
    }

    /**
     * 将查询结果转换成指定的简单对象
     * <p>
     * 简单对象的属性类型必须与数据库字段类型一致 为了遵守Java Bean的默认规范在转成时会自动去掉数据库字段"_"标记并且方法的每个首字母都会大写
     * 
     * @param rsmd 结果集元数据
     * @param rs 结果集
     * @param columnCount 结果集列数
     * @param retObj 指定的简单对象Class对象
     * @param childRetObj childRetObj
     * @param childKey childKey
     * @param isNewObj isNewObj
     * @param oldObj oldObj
     * @param childList childList
     * @return Object
     * @throws BaseAppException <br>
     */
    public Object toSimpleObj2(ResultSetMetaData rsmd, ResultSet rs, int columnCount, Class<?> retObj,
        Class<?> childRetObj, String childKey, boolean isNewObj, Object oldObj, List<Object> childList)
        throws BaseAppException {
        Object obj = null;
        Object childObj = null;
        try {
            childObj = childRetObj.newInstance();

            if (isNewObj) {
                obj = retObj.newInstance();
                String childListMethodName = "set" + Character.toUpperCase(childKey.charAt(0)) + childKey.substring(1);
                Field childField = retObj.getDeclaredField(childKey);
                Class<?> childFieldType = childField.getType();
                if (childFieldType.equals(List.class)) {
                    Method callMethod = retObj.getMethod(childListMethodName, List.class);
                    callMethod.invoke(obj, childList);
                }
                else {
                    ExceptionHandler.publish("the type of the childList must be List");
                }
            }
            else {
                obj = oldObj;
            }
        }
        catch (Exception e) {
            ExceptionHandler.publish("toSimpleObj2 Has Exception", e);
        }

        boolean childColumBegin = false;

        for (int i = 1; i <= columnCount; i++) {
            // 简单对象的set方法名
            StringBuffer methodNameBuff = new StringBuffer();
            // 简单对象的变量名
            StringBuffer fieldNameBuff = new StringBuffer();

            String columnName = null;
            try {
                columnName = rsmd.getColumnName(i).toLowerCase();
            }
            catch (SQLException e) {
                ExceptionHandler.publish("toSimpleObj2 Has Exception", e);
            }

            methodNameBuff.append("set");

            if (!isNewObj && !childColumBegin && !"p_c_separator".equals(columnName)) {
                continue;
            }

            // 在SQL查询时父子之间手动加一个分隔符
            if ("p_c_separator".equals(columnName)) {
                childColumBegin = true;
                continue;
            }
            else if (columnName.indexOf("_") != -1) {
                // 每个"_"字符需要过滤掉,并且"_"之后的首字母为大写
                String[] columnArr = columnName.split("_");

                for (int j = 0; j < columnArr.length; j++) {
                    methodNameBuff.append(Character.toUpperCase(columnArr[j].charAt(0)));
                    methodNameBuff.append(columnArr[j].substring(1));

                    if (j == 0) {
                        fieldNameBuff.append(columnArr[j]);
                    }
                    else {
                        fieldNameBuff.append(Character.toUpperCase(columnArr[j].charAt(0)));
                        fieldNameBuff.append(columnArr[j].substring(1));
                    }
                }
            }
            else {
                methodNameBuff.append(Character.toUpperCase(columnName.charAt(0)));
                methodNameBuff.append(columnName.substring(1));

                fieldNameBuff.append(columnName);
            }

            Field field = null;
            Method callMethod = null;
            if (childColumBegin) {
                // 构造属性是为了读取属性的类型
                try {
                    field = childRetObj.getDeclaredField(fieldNameBuff.toString());
                }
                catch (SecurityException e) {
                    ExceptionHandler.publish("toSimpleObj2 Has Exception", e);
                }
                catch (NoSuchFieldException e) {
                    Class<?> superClass = childRetObj.getSuperclass();
                    if (superClass.equals(Object.class) || superClass.getDeclaredFields().length == 0) {
                        ExceptionHandler.publish("toSimpleObj2 Has Exception", e);
                    }

                    callSurperMethod(fieldNameBuff.toString(), superClass, methodNameBuff.toString(), childObj, rs, i);
                    continue;
                }
                try {
                    callMethod = childRetObj.getMethod(methodNameBuff.toString(), field.getType());
                    callMethod(callMethod, field.getType(), childObj, rs, i);
                }
                catch (Exception e1) {
                    ExceptionHandler.publish("toSimpleObj2 Has Exception", e1);
                }
            }
            else {
                // 构造属性是为了读取属性的类型
                try {
                    field = retObj.getDeclaredField(fieldNameBuff.toString());
                }
                catch (SecurityException e) {
                    ExceptionHandler.publish("toSimpleObj2 Has Exception", e);
                }
                catch (NoSuchFieldException e) {
                    Class<?> superClass = retObj.getSuperclass();
                    if (superClass.equals(Object.class) || superClass.getDeclaredFields().length == 0) {
                        ExceptionHandler.publish("toSimpleObj2 Has Exception", e);
                    }
                    callSurperMethod(fieldNameBuff.toString(), superClass, methodNameBuff.toString(), obj, rs, i);
                    continue;
                }

                try {
                    callMethod = retObj.getMethod(methodNameBuff.toString(), field.getType());
                    callMethod(callMethod, field.getType(), obj, rs, i);
                }
                catch (Exception e) {
                    ExceptionHandler.publish("toSimpleObj2 Has Exception", e);
                }
            }
        }

        childList.add(childObj);

        return obj;
    }

    /**
     * Description: getParamMap<br>
     * 
     * @author jiao.yang<br>
     * @taskId <br>
     * @param callMethod callMethod<br>
     * @param fieldType fieldType
     * @param obj obj
     * @param rs rs
     * @param i i
     * @throws BaseAppException <br>
     */
    private void callMethod(Method callMethod, Class<?> fieldType, Object obj, ResultSet rs, int i)
        throws BaseAppException {
        try {
            if (rs.getObject(i) == null) {
                return;
            }

            if (long.class.equals(fieldType) || Long.class.equals(fieldType)) {
                callMethod.invoke(obj, rs.getLong(i));
            }
            else if (String.class.equals(fieldType)) {
                callMethod.invoke(obj, rs.getString(i));
            }
            else if (java.sql.Timestamp.class.equals(fieldType)) {
                callMethod.invoke(obj, rs.getTimestamp(i));
            }
            else if (java.sql.Date.class.equals(fieldType) || java.util.Date.class.equals(fieldType)) {
                // callMethod.invoke(obj, rs.getDate(i));
                Timestamp t = rs.getTimestamp(i);
                if (t != null) {
                    callMethod.invoke(obj, new Date(t.getTime()));
                }
            }
            else if (int.class.equals(fieldType) || Integer.class.equals(fieldType)) {
                callMethod.invoke(obj, rs.getInt(i));
            }
            else if (float.class.equals(fieldType) || Float.class.equals(fieldType)) {
                callMethod.invoke(obj, rs.getFloat(i));
            }
            else if (double.class.equals(fieldType) || Double.class.equals(fieldType)) {
                callMethod.invoke(obj, rs.getDouble(i));
            }
            else if (Blob.class.equals(fieldType)) {
                callMethod.invoke(obj, rs.getBlob(i));
            }
            else if (Clob.class.equals(fieldType)) {
                callMethod.invoke(obj, rs.getClob(i));
            }
            else if (ClobWrapper.class.equals(fieldType)) {
                callMethod.invoke(obj, new ClobWrapper(LobHelper.getClobString(rs, i)));
            }
            else if (BlobWrapper.class.equals(fieldType)) {
                callMethod.invoke(obj, new BlobWrapper(LobHelper.getBlobBytes(rs, i)));
            }
        }
        catch (Exception e) {
            ExceptionHandler.publish("callMethod Has Exception", e);
        }
    }

    /**
     * 
     * Description: <br> 
     *  
     * @author jiao.yang<br>
     * @taskId <br>
     * @param fieldName fieldName
     * @param superClass superClass
     * @param methodName methodName
     * @param obj obj
     * @param rs rs
     * @param i i
     * @throws BaseAppException <br>
     */
    private void callSurperMethod(String fieldName, Class<?> superClass, String methodName, Object obj, ResultSet rs,
        int i) throws BaseAppException {
        Field field = null;
        Method method = null;

        try {
            field = superClass.getDeclaredField(fieldName);
            method = superClass.getMethod(methodName, field.getType());
            callMethod(method, field.getType(), obj, rs, i);
        }
        catch (SecurityException e) {
            ExceptionHandler.publish("callSurperMethod Has Exception", e);
        }
        catch (NoSuchFieldException e) {
            superClass = superClass.getSuperclass();
            if (superClass.equals(Object.class) || superClass.getDeclaredFields().length == 0) {
                ExceptionHandler.publish("callSurperMethod Has Exception", e);
            }

            callSurperMethod(fieldName, superClass, methodName, obj, rs, i);
        }
        catch (NoSuchMethodException e) {
            ExceptionHandler.publish("callSurperMethod Has Exception", e);
        }
    }

    /**
     * 
     * Description: <br> 
     *  
     * @author jiao.yang<br>
     * @taskId <br>
     * @param columnName columnName
     * @param obj obj
     * @param retObj  retObj
     * @return Object
     * @throws BaseAppException <br>
     */
    private Object genObj(String columnName, Object obj, Class<?> retObj) throws BaseAppException {
        Object parentObj = null;

        // 简单对象的set方法名
        StringBuffer methodNameBuff = new StringBuffer();
        // 简单对象的变量名
        StringBuffer fieldNameBuff = new StringBuffer();

        // 设置methodNameBuff\fieldNameBuff
        setBuffer(columnName, methodNameBuff, fieldNameBuff);

        // 构造属性是为了读取属性的类型
        Field field = null;
        try {
            field = retObj.getDeclaredField(fieldNameBuff.toString());
            parentObj = field.getType().newInstance();
        }
        catch (NoSuchFieldException e) {
            Class<?> superClass = retObj.getSuperclass();
            if (superClass.equals(Object.class) || superClass.getDeclaredFields().length == 0) {
                ExceptionHandler.publish("toSimpleObj2 Has Exception", e);
            }

            callSurperMethod3(columnName, superClass, methodNameBuff.toString(), obj, parentObj);
            return parentObj;
        }
        catch (Exception e) {
            ExceptionHandler.publish("genParentObjAttrValue Has Exception, columnName:" + columnName, e);
        }

        Method callMethod = null;
        try {
            callMethod = retObj.getMethod(methodNameBuff.toString(), field.getType());
            callMethod.invoke(obj, parentObj);
        }
        catch (Exception e) {
            ExceptionHandler.publish("toSimpleObj Has Exception", e);
        }

        return parentObj;
    }

    /**
     * 
     * Description: <br> 
     *  
     * @author jiao.yang<br>
     * @taskId <br> 
     * @param fieldName fieldName
     * @param superClass superClass
     * @param methodName methodName
     * @param obj obj
     * @param parentObj parentObj
     * @throws BaseAppException <br>
     */
    private void callSurperMethod3(String fieldName, Class<?> superClass, String methodName, Object obj,
        Object parentObj) throws BaseAppException {
        Field field = null;
        Method method = null;

        try {
            field = superClass.getDeclaredField(fieldName);
            method = superClass.getMethod(methodName, field.getType());
            method.invoke(obj, parentObj);
        }
        catch (NoSuchFieldException e) {
            superClass = superClass.getSuperclass();
            if (superClass.equals(Object.class) || superClass.getDeclaredFields().length == 0) {
                ExceptionHandler.publish("callSurperMethod Has Exception", e);
            }

            callSurperMethod3(fieldName, superClass, methodName, obj, parentObj);
        }
        catch (Exception e) {
            ExceptionHandler.publish("callSurperMethod3 Has Exception", e);
        }
    }

    /**
     * Description: setBuffer<br>
     * 
     * @author jiao.yang<br>
     * @taskId <br>
     * @param columnName columnName<br>
     * @param methodNameBuff methodNameBuff<br>
     * @param fieldNameBuff fieldNameBuff<br>
     */
    private void setBuffer(String columnName, StringBuffer methodNameBuff, StringBuffer fieldNameBuff) {
        methodNameBuff.append("set");

        if (columnName.indexOf("_") != -1) {
            // 每个"_"字符需要过滤掉,并且"_"之后的首字母为大写
            String[] columnArr = columnName.split("_");

            for (int j = 0; j < columnArr.length; j++) {
                methodNameBuff.append(Character.toUpperCase(columnArr[j].charAt(0)));
                methodNameBuff.append(columnArr[j].substring(1));

                if (j == 0) {
                    fieldNameBuff.append(columnArr[j]);
                }
                else {
                    fieldNameBuff.append(Character.toUpperCase(columnArr[j].charAt(0)));
                    fieldNameBuff.append(columnArr[j].substring(1));
                }
            }
        }
        else {
            methodNameBuff.append(Character.toUpperCase(columnName.charAt(0)));
            methodNameBuff.append(columnName.substring(1));

            fieldNameBuff.append(columnName);
        }
    }

    /**
     * 
     * Description: <br> 
     *  
     * @author jiao.yang<br>
     * @taskId <br>
     * @param columnName columnName
     * @param rs  rs
     * @param i i
     * @param obj obj
     * @param retObj retObj
     * @throws BaseAppException <br>
     */
    private void setObjAttr(String columnName, ResultSet rs, int i, Object obj, Class<?> retObj)
        throws BaseAppException {
        // 简单对象的set方法名
        StringBuffer methodNameBuff = new StringBuffer();
        // 简单对象的变量名
        StringBuffer fieldNameBuff = new StringBuffer();

        // 设置methodNameBuff\fieldNameBuff
        setBuffer(columnName, methodNameBuff, fieldNameBuff);

        // 构造属性是为了读取属性的类型
        Field field = null;
        try {
            field = retObj.getDeclaredField(fieldNameBuff.toString());
        }
        catch (SecurityException e) {
            ExceptionHandler.publish("toSimpleObj Has Exception", e);
        }
        catch (NoSuchFieldException e) {
            Class<?> superClass = retObj.getSuperclass();
            if (superClass.equals(Object.class) || superClass.getDeclaredFields().length == 0) {
                ExceptionHandler.publish("toSimpleObj2 Has Exception", e);
            }

            callSurperMethod(fieldNameBuff.toString(), superClass, methodNameBuff.toString(), obj, rs, i);
            return;
        }

        Method callMethod = null;
        try {
            callMethod = retObj.getMethod(methodNameBuff.toString(), field.getType());
        }
        catch (SecurityException e) {
            ExceptionHandler.publish("toSimpleObj Has Exception", e);
        }
        catch (NoSuchMethodException e) {
            ExceptionHandler.publish("toSimpleObj Has Exception", e);
        }

        try {
            callMethod(callMethod, field.getType(), obj, rs, i);
        }
        catch (Exception e) {
            ExceptionHandler.publish("toSimpleObj Has Exception", e);
        }
    }

    /**
     * Description:
     * 
     * @param fieldType <br/>
     * @return boolean <br/>
     */
    private boolean isSimpleType(Class<?> fieldType) {
        if (fieldType.isPrimitive()) {
            return true;
        }
        else if (fieldType.equals(Integer.class) || fieldType.equals(Long.class) || fieldType.equals(String.class)
            || fieldType.equals(Boolean.class) || fieldType.equals(BlobWrapper.class)
            || fieldType.equals(ClobWrapper.class) || fieldType.equals(java.sql.Date.class)
            || fieldType.equals(java.util.Date.class)) {
            return true;
        }

        return false;
    }

    /**
     * 
     * Description: <br> 
     *  
     * @author jiao.yang<br>
     * @taskId <br>
     * @param clazz clazz
     * @return List<br>
     */
    private List<Field> getAllField(Class<?> clazz) {
        List<Field> list = new ArrayList<Field>();
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                list.add(field);
            }
        }
        return list;
    }

    /**
     * 
     * Description: <br> 
     *  
     * @author jiao.yang<br>
     * @taskId <br>
     * @param clazz clazz
     * @param name name
     * @return Method
     * @throws BaseAppException <br>
     */
    private Method getMethodByName(Class<?> clazz, String name) throws BaseAppException {
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                if (name.equalsIgnoreCase(method.getName())) {
                    return method;
                }
            }
        }
        ExceptionHandler.publish("getMethodByName Has Exception");
        return null;
    }

}
