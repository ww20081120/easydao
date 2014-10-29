/**
 * 
 */
package com.fccfc.easydao.excutor;

import com.fccfc.easydao.DaoException;
import com.fccfc.easydao.config.DataParam;

/**
 * <Description> <br>
 * 
 * @author 伟<br>
 * @version 1.0<br>
 * @CreateDate 2014-10-26 <br>
 * @see com.fccfc.framework.dao.support <br>
 */
public interface ISqlExcutor {

    /**
     * 查询
     * 
     * @param sql
     * @param param
     * @return
     * @throws DaoException
     */
    Object query(String sql, DataParam param);

    /**
     * 执行sql语句
     * 
     * @param sql sql
     * @param param params
     * @return Object
     * @throws DaoException DaoException
     */
    int excuteSql(String sql, DataParam param);

    /**
     * 批处理sql
     * 
     * @param sql
     * @return
     * @throws DaoException
     */
    int[] batchExcuteSql(String[] sql, DataParam param);
}
