/**************************************************************************************** 
 Copyright © 2003-2012 ZTEsoft Corporation. All rights reserved. Reproduction or       <br>
 transmission in whole or in part, in any form or by any means, electronic, mechanical <br>
 or otherwise, is prohibited without the prior written consent of the copyright owner. <br>
 ****************************************************************************************/
package com.ztesoft.zsmart.easydao.support;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import com.fccfc.framework.core.db.annotation.handler.DaoHandler;
import com.fccfc.framework.core.db.config.DaoConfig;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.jdbc.JdbcUtil;
import com.ztesoft.zsmart.core.jdbc.RowSetFormatter;
import com.ztesoft.zsmart.core.jdbc.ds.DbIdentifier;

/**
 * <Description> <br>
 * 
 * @author 王伟 <br>
 * @version 1.0 <br>
 * @CreateDate 2015年3月19日 <br>
 * @see com.ztesoft.zsmart.easydao.support <br>
 */
public class EasyDaoFactory {

    /** cacheMap */
    private static Map<DbIdentifier, Map<Class<?>, Object>> cacheMap = new HashMap<DbIdentifier, Map<Class<?>, Object>>();

    /**
     * Description: getDaoInstance<br>
     * 
     * @author wang.wei297<br>
     * @taskId <br>
     * @param clazz clazz
     * @return T
     * @throws BaseAppException BaseAppException<br>
     */
    public static <T> T getDaoInstance(Class<T> clazz) throws BaseAppException {
        return getDaoInstance(clazz, JdbcUtil.getDbIdentifier());
    }

    /**
     * Description: getDaoInstance<br>
     * 
     * @author wang.wei297<br>
     * @taskId <br>
     * @param clazz clazz
     * @param dbID dbID
     * @return T
     * @throws BaseAppException BaseAppException<br>
     */
    @SuppressWarnings("unchecked")
    public static <T> T getDaoInstance(Class<T> clazz, DbIdentifier dbID) throws BaseAppException {
        Map<Class<?>, Object> map = cacheMap.get(dbID);
        if (map == null) {
            map = new HashMap<Class<?>, Object>();
            cacheMap.put(dbID, map);
        }

        Object result = map.get(clazz);
        if (result == null) {
            SqlExcutor excutor = new SqlExcutor();
            excutor.setConnection(dbID);

            DaoConfig config = new DaoConfig();
            config.setBaseDao(excutor);
            config.setCache(true);
            config.setDbType(dbID.getDialect().getDialectName());
            config.setCallBackType(RowSetFormatter.class);

            DaoHandler daoHandler = new DaoHandler();
            daoHandler.setDaoConfig(config);
            daoHandler.setSqlExcutor(excutor);

            try {
                result = Proxy.newProxyInstance(clazz.getClassLoader(), new Class[] {
                    clazz
                }, daoHandler);
            }
            catch (Exception e) {
                throw new BaseAppException(e.getMessage(), e);
            }
            map.put(clazz, result);
        }
        return (T) result;
    }

}
