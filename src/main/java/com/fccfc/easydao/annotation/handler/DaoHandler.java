/**
 * 
 */
package com.fccfc.easydao.annotation.handler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ognl.Ognl;
import ognl.OgnlException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.fccfc.easydao.DaoConstants;
import com.fccfc.easydao.DaoException;
import com.fccfc.easydao.config.DataParam;
import com.fccfc.easydao.config.ParamMetadata;
import com.fccfc.easydao.excutor.ISqlExcutor;
import com.fccfc.easydao.util.BeanUtils;
import com.fccfc.easydao.util.UtilException;
import com.fccfc.easydao.util.VelocityParseFactory;
import com.fccfc.easydao.util.cache.CacheHelper;

/**
 * <Description> <br>
 * 
 * @author 王伟<br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate 2014年10月27日 <br>
 * @since V1.0<br>
 * @see com.fccfc.framework.dao.annotation.handler <br>
 */
public class DaoHandler extends AbstractAnnotationHandler implements InvocationHandler {

    private static Logger logger = Logger.getLogger(DaoHandler.class);

    private ISqlExcutor sqlExcutor;

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // Step1:判断是否是抽象方法，如果是非抽象方法，则不执行代理拦截器
        if (proxy != null && !BeanUtils.isAbstract(method)) {
            return method.invoke(proxy, args);
        }

        // Step2:判断是否是genericBaseDao方法，如果是则直接执行
        Method m = getGenericBaseDaoExcutor(method);
        if (m != null) {
            return m.invoke(super.getDaoConfig().getBaseDao(), args);
        }

        // Step3: 装载SQL模板和所需参数
        String templateSql = cacheSqlTemplate(method);

        // 执行sql的参数
        DataParam dataParam = new DataParam();

        // SQL模板参数
        Map<String, Object> sqlParamsMap = loadDaoMetaData(method, args, dataParam);

        // Step4:解析SQL模板，返回可执行SQL
        String executeSql = parseSqlTemplate(method, templateSql, sqlParamsMap);

        // Step5:组装sql参数
        installPlaceholderSqlParam(dataParam, templateSql, sqlParamsMap);

        // Step5: 执行Sql返回结果
        return excuteSql(executeSql, dataParam);
    }

    /**
     * 组装占位符参数 -> Map
     * 
     * @param dataParam
     * @param executeSql
     * @return
     * @throws DaoException
     * @throws OgnlException
     */
    private void installPlaceholderSqlParam(DataParam dataParam, String executeSql, Map<String, Object> sqlParamsMap)
        throws DaoException {
        Map<String, Object> map = new HashMap<String, Object>();
        String regEx = ":[ tnx0Bfr]*[0-9a-z.A-Z]+"; // 表示以：开头，[0-9或者.或者A-Z大小都写]的任意字符，超过一个
        Pattern pat = Pattern.compile(regEx);
        Matcher m = pat.matcher(executeSql);
        try {
            while (m.find()) {
                logger.debug(" Match [" + m.group() + "] at positions " + m.start() + "-" + (m.end() - 1));
                String ognl_key = m.group().replace(":", "").trim();
                map.put(ognl_key, Ognl.getValue(ognl_key, sqlParamsMap));
            }
        }
        catch (Exception e) {
            throw new DaoException(e);
        }
        dataParam.setParamMap(map);
    }

    /**
     * 获取所需参数
     * 
     * @param method
     * @param args
     * @param dataParam
     * @return
     * @throws DaoException
     */
    private Map<String, Object> loadDaoMetaData(Method method, Object[] args, DataParam dataParam) throws DaoException {
        Map<String, Object> paramMap;
        try {
            ParamMetadata metadata = super.cacheSqlParamMetadata(method);
            dataParam.setBeanType(metadata.getBeanType());
            dataParam.setReturnType(metadata.getReturnType());

            String[] paramNames = metadata.getParamNames();
            paramMap = new HashMap<String, Object>();
            for (int i = 0; i < args.length; i++) {
                if (i == metadata.getIndexPosition()) {
                    dataParam.setPageIndex(Integer.valueOf(args[i].toString()));
                }
                else if (i == metadata.getSizePosition()) {
                    dataParam.setPageSize(Integer.valueOf(args[i].toString()));
                }
                else if (i == metadata.getCallBackPosition()) {
                    dataParam.setCallback(args[i]);
                }
                else {
                    paramMap.put(paramNames[i], args[i]);
                }
            }

        }
        catch (Exception e) {
            throw new DaoException(e);
        }
        return paramMap;
    }

    /**
     * @param method
     * @param sql
     * @param param
     * @return
     * @throws DaoException
     */
    private Object excuteSql(String sql, DataParam param) throws DaoException {
        Object result = null;
        try { // 区分查询方法与执行方法
            if (sql.toLowerCase().startsWith(DaoConstants.SQL_SELECT_PREFIX)) {
                result = sqlExcutor.query(sql, param);
            }
            else if (sql.indexOf(DaoConstants.SQL_SPLITOR) != -1) {
                result = sqlExcutor.batchExcuteSql(StringUtils.split(sql, DaoConstants.SQL_SPLITOR), param);
            }
            else {
                result = sqlExcutor.excuteSql(sql, param);
            }
        }
        catch (Throwable e) {
            throw new DaoException(e);
        }
        return result;
    }

    /**
     * @param templateSql
     * @param sqlParamsMap
     * @return
     * @throws UtilException
     */
    private String parseSqlTemplate(Method method, String templateSql, Map<String, Object> sqlParamsMap)
        throws UtilException {
        String key = CacheHelper.buildKey(method.getDeclaringClass().getName(), BeanUtils.getMethodSignature(method));
        String executeSql = VelocityParseFactory.parse(key, templateSql, sqlParamsMap);

        // 除去无效字段，不然批量处理可能报错
        return executeSql.replaceAll("\\n", " ").replaceAll("\\t", " ").replaceAll("\\s{1,}", " ").trim();
    }

    public void setSqlExcutor(ISqlExcutor sqlExcutor) {
        this.sqlExcutor = sqlExcutor;
    }
}
