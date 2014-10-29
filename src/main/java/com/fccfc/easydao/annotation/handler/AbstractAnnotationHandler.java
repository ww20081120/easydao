/**************************************************************************************** 
 Copyright © 2003-2012 ZTEsoft Corporation. All rights reserved. Reproduction or       <br>
 transmission in whole or in part, in any form or by any means, electronic, mechanical <br>
 or otherwise, is prohibited without the prior written consent of the copyright owner. <br>
 ****************************************************************************************/
package com.fccfc.easydao.annotation.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.fccfc.easydao.DaoConstants;
import com.fccfc.easydao.DaoException;
import com.fccfc.easydao.annotation.Param;
import com.fccfc.easydao.annotation.Sql;
import com.fccfc.easydao.config.DaoConfig;
import com.fccfc.easydao.config.ParamMetadata;
import com.fccfc.easydao.util.BeanUtils;
import com.fccfc.easydao.util.CommonUtil;
import com.fccfc.easydao.util.cache.CacheException;
import com.fccfc.easydao.util.cache.CacheHelper;

/**
 * <Description> <br>
 * 
 * @author 王伟 <br>
 * @version 1.0 <br>
 * @CreateDate 2014年10月24日 <br>
 * @see com.fccfc.framework.dao.annotation.handler <br>
 */
public class AbstractAnnotationHandler {

    /** sql文件后缀 */
    private static final String SQL_SUFFIX = ".sql";

    /** sql 包名 */
    private static final String SQL_PACKAGE = "/sql";

    /** IGenericBaseDao 的方法签名 */
    private static Map<String, Method> genericBaseDaoMethodMap;

    private static Logger logger = Logger.getLogger(AbstractAnnotationHandler.class);

    private DaoConfig daoConfig;

    /**
     * 默认构造函数
     */
    public AbstractAnnotationHandler() {
    }

    /**
     * 获取IGenericBaseDao执行方法
     * 
     * @param method
     * @return
     */
    protected Method getGenericBaseDaoExcutor(Method method) {
        Method result = null;
        Object obj = daoConfig.getBaseDao();
        if (obj != null) {
            if (genericBaseDaoMethodMap == null) {
                genericBaseDaoMethodMap = new HashMap<String, Method>();
                Method[] methods = obj.getClass().getDeclaredMethods();
                for (Method m : methods) {
                    genericBaseDaoMethodMap.put(BeanUtils.getMethodSignature(m), m);
                }
            }
            String methodSignature = BeanUtils.getMethodSignature(method);
            if (genericBaseDaoMethodMap.containsKey(methodSignature)) {
                result = genericBaseDaoMethodMap.get(methodSignature);
            }
        }

        return result;
    }

    /**
     * 检查sql路径
     * 
     * @param method
     * @param path
     * @return
     * @throws InitializationException
     */
    private String checkSqlPath(Method method, String path) throws DaoException {
        StringBuilder sb = new StringBuilder();
        if (CommonUtil.isNotEmpty(path)) {
            sb.append(path);
        }
        else {
            sb.append(method.getDeclaringClass().getName().replace(".", DaoConstants.PATH_SPLITOR))
                .append(DaoConstants.UNDERLINE).append(method.getName()).append(SQL_SUFFIX);
        }

        URL sqlFileUrl = this.getClass().getClassLoader().getResource(sb.toString());
        if (sqlFileUrl == null) {
            String dbType = "." + daoConfig.getDbType();
            sb.insert(sb.lastIndexOf(SQL_SUFFIX), dbType);
            sqlFileUrl = this.getClass().getClassLoader().getResource(sb.toString());
            if (sqlFileUrl == null) {
                sb.insert(sb.lastIndexOf(DaoConstants.PATH_SPLITOR), SQL_PACKAGE);
                sqlFileUrl = this.getClass().getClassLoader().getResource(sb.toString());
                if (sqlFileUrl == null) {
                    sb.replace(sb.lastIndexOf(dbType), sb.lastIndexOf(SQL_SUFFIX), DaoConstants.BLANK);
                    sqlFileUrl = this.getClass().getClassLoader().getResource(sb.toString());
                    if (sqlFileUrl == null) {
                        throw new DaoException("初始化sql失败，未找到{0}#{1}的sql", method.getDeclaringClass().getName(),
                            method.getName());
                    }
                }
            }
        }

        BufferedReader reader = null;
        String sqlPath = sb.toString();
        try {
            InputStream in = this.getClass().getClassLoader().getResourceAsStream(sqlPath);
            reader = new BufferedReader(new InputStreamReader(in, DaoConstants.DEFAULT_CHARSET));
            sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(StringUtils.trim(line)).append('\n');
            }
            return sb.toString();
        }
        catch (Exception e) {
            throw new DaoException("读取sql文件失败,路径[{0}]", sqlPath);
        }
        finally {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (IOException e) {
                    logger.warn("", e);
                }
            }
        }
    }

    /**
     * @param method
     * @return
     * @throws InitializationException
     */
    protected ParamMetadata cacheSqlParamMetadata(Method method) throws DaoException {
        String key = CacheHelper.buildKey(method.getDeclaringClass().getName(), BeanUtils.getMethodSignature(method));
        ParamMetadata metadata = null;
        try {
            metadata = daoConfig.isCache() ? CacheHelper.getSqlParamMetedata(key) : null;
            if (metadata == null) {
                Class<?>[] typeClazz = method.getParameterTypes();
                Annotation[][] parameterAnnotations = method.getParameterAnnotations();
                metadata = new ParamMetadata(parameterAnnotations.length);

                // 设置返回转换类型
                metadata.setBeanType(method.isAnnotationPresent(Sql.class) ? method.getAnnotation(Sql.class).bean()
                    : Map.class);

                // 设置返回类型
                metadata.setReturnType(method.getReturnType());

                String[] paramNames = null;
                for (int i = 0; i < parameterAnnotations.length; i++) {
                    Annotation[] pmAnnotaions = parameterAnnotations[i];

                    // 如果类型为resultCallback
                    if (daoConfig.getCallBackType() != null
                        && daoConfig.getCallBackType().isAssignableFrom(typeClazz[i])) {
                        if (metadata.getCallBackPosition() != -1) {
                            throw new DaoException("Clazz[{0}] Method[{1}]含有多个ResultCallback参数", method
                                .getDeclaringClass().getName(), method.getName());
                        }
                        metadata.setCallBackPosition(i);
                        continue;
                    }

                    // 如果没有注解参数名称，则获取参数变量名称为key
                    if (pmAnnotaions.length == 0) {
                        if (paramNames == null) {
                            paramNames = BeanUtils.getMethodParamNames(method);
                        }
                        metadata.setParamName(i, paramNames[i]);
                    }
                    else {
                        // 判断是否含有Param注解
                        String name = DaoConstants.NULL;
                        for (Annotation annotation : pmAnnotaions) {
                            if (annotation instanceof Param) {
                                Param p = (Param) annotation;
                                name = p.value();
                                if (Param.pageIndex.equals(name)) {
                                    metadata.setIndexPosition(i);
                                }
                                else if (Param.pageSize.equals(name)) {
                                    metadata.setSizePosition(i);
                                }
                                metadata.setParamName(i, name);
                                break;
                            }
                        }

                        // 如果没有Param注解，还是需要自动获取变量名称
                        if (CommonUtil.isEmpty(name)) {
                            if (paramNames == null) {
                                paramNames = BeanUtils.getMethodParamNames(method);
                            }
                            metadata.setParamName(i, paramNames[i]);
                        }
                    }
                }
                if ((metadata.getIndexPosition() == -1 && metadata.getSizePosition() != -1)
                    || (metadata.getIndexPosition() != -1 && metadata.getSizePosition() == -1)) {
                    throw new DaoException("Clazz[{0}] Method[{1}]中Pagesize 和 PageIndex 必须同时设置", method
                        .getDeclaringClass().getName(), method.getName());
                }
                if (daoConfig.isCache()) {
                    CacheHelper.putSqlParamMetedata(key, metadata);
                }
            }
        }
        catch (Exception e) {
            throw new DaoException(e);
        }
        return metadata;
    }

    /**
     * Description: <br>
     * 
     * @author 王伟 <br>
     * @param method
     * @throws InitializationException InitializationException
     */
    protected String cacheSqlTemplate(Method method) throws DaoException {
        String key = CacheHelper.buildKey(method.getDeclaringClass().getName(), BeanUtils.getMethodSignature(method));
        String templateSql = null;
        try {
            templateSql = daoConfig.isCache() ? CacheHelper.getSqlTemplate(key) : null;
            if (CommonUtil.isEmpty(templateSql)) {
                String path = null;

                // 获取方法的SQL标签
                if (method.isAnnotationPresent(Sql.class)) {
                    Sql sql = method.getAnnotation(Sql.class);
                    templateSql = sql.value();
                    path = sql.path();
                }

                if (CommonUtil.isEmpty(templateSql)) {
                    templateSql = checkSqlPath(method, path);
                }
                if (daoConfig.isCache()) {
                    CacheHelper.putSqlTemplate(key, templateSql);
                }
            }
        }
        catch (CacheException e) {
            throw new DaoException(e);
        }
        return StringUtils.trim(templateSql);
    }

    /**
     * @return the daoConfig
     */
    public DaoConfig getDaoConfig() {
        return daoConfig;
    }

    /**
     * @param daoConfig the daoConfig to set
     */
    public void setDaoConfig(DaoConfig daoConfig) {
        this.daoConfig = daoConfig;
    }
}
