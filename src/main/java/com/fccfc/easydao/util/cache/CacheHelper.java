/**************************************************************************************** 
 Copyright © 2003-2012 ZTEsoft Corporation. All rights reserved. Reproduction or       <br>
 transmission in whole or in part, in any form or by any means, electronic, mechanical <br>
 or otherwise, is prohibited without the prior written consent of the copyright owner. <br>
 ****************************************************************************************/
package com.fccfc.easydao.util.cache;

import java.io.Serializable;

import com.fccfc.easydao.DaoConstants;
import com.fccfc.easydao.config.ParamMetadata;
import com.fccfc.easydao.util.cache.simple.SimpleCache;

/**
 * <Description> <br>
 * 
 * @author 王伟 <br>
 * @version 1.0 <br>
 * @CreateDate 2014年10月24日 <br>
 * @see com.fccfc.easydao.util.cache <br>
 */
public final class CacheHelper {

    /** cache */
    private static ICache<Serializable> cache;

    /** 缓存sql模板 */
    public static void putSqlTemplate(String sqlSignature, String sqlTemplete) throws CacheException {
        getCache().putValue(CacheDirectory.SQL_DIR, sqlSignature, sqlTemplete);
    }

    /** 获取sql模板 */
    public static String getSqlTemplate(String sqlSignature) throws CacheException {
        return (String) getCache().getValue(CacheDirectory.SQL_DIR, sqlSignature);
    }

    /** 缓存sql参数 */
    public static void putSqlParamMetedata(String sqlSignature, ParamMetadata metaData) throws CacheException {
        getCache().putValue(CacheDirectory.SQL_PARAM_DIR, sqlSignature, metaData);
    }

    /** 获取sql参数 */
    public static ParamMetadata getSqlParamMetedata(String sqlSignature) throws CacheException {
        return (ParamMetadata) getCache().getValue(CacheDirectory.SQL_PARAM_DIR, sqlSignature);
    }

    /**
     * Description: <br>
     * 
     * @author 王伟 <br>
     * @param paths
     * @return <br>
     */
    public static String buildKey(String... paths) {
        StringBuilder sb = new StringBuilder();
        for (String path : paths) {
            sb.append(DaoConstants.PATH_SPLITOR).append(path);
        }
        return sb.toString();
    }

    public static ICache<Serializable> getCache() {
        if (cache == null) {
            cache = new SimpleCache<Serializable>();
        }
        return cache;
    }

    public static void setCache(ICache<Serializable> cache) {
        CacheHelper.cache = cache;
    }
}
