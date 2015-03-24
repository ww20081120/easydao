/**************************************************************************************** 
 Copyright © 2003-2012 ZTEsoft Corporation. All rights reserved. Reproduction or       <br>
 transmission in whole or in part, in any form or by any means, electronic, mechanical <br>
 or otherwise, is prohibited without the prior written consent of the copyright owner. <br>
 ****************************************************************************************/
package com.ztesoft.zsmart.easydao.support;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.fccfc.framework.core.db.config.DataParam;
import com.fccfc.framework.core.db.executor.ISqlExcutor;
import com.fccfc.framework.core.utils.CommonUtil;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.jdbc.ParamArray;
import com.ztesoft.zsmart.core.jdbc.ParamMap;
import com.ztesoft.zsmart.core.jdbc.rowset.BOMapper;
import com.ztesoft.zsmart.core.jdbc.rowset.RowSetMapper;
import com.ztesoft.zsmart.core.jdbc.rowset.RowSetOperator;
import com.ztesoft.zsmart.core.service.DynamicDict;

/**
 * <Description> <br>
 * 
 * @author 王伟 <br>
 * @version 1.0 <br>
 * @CreateDate 2015年3月19日 <br>
 * @see com.ztesoft.zsmart.easydao.support <br>
 */
public class SqlExcutor extends BusiBaseDAO implements ISqlExcutor {

    /**
     * Description: <br>
     * 
     * @author wang.wei297<br>
     * @taskId <br>
     * @param arg0
     * @param arg1
     * @return <br>
     */
    public int[] batchExcuteSql(String[] arg0, DataParam arg1) {
        int[] array = new int[arg0.length];
        for (int i = 0; i < arg0.length; i++) {
            array[i] = excuteSql(arg0[i], arg1);
        }
        return array;
    }

    /**
     * Description: <br>
     * 
     * @author wang.wei297<br>
     * @taskId <br>
     * @param arg0
     * @param arg1
     * @return <br>
     */
    public int excuteSql(String sql, DataParam param) {
        try {
            return super.executeUpdate(sql, getParamMap(param.getParamMap()));
        }
        catch (BaseAppException e) {
            throw new RuntimeException(e);
        }
    }

    private ParamMap getParamMap(Map<String, Object> map) {
        ParamMap paramMap = new ParamMap();
        if (CommonUtil.isNotEmpty(map)) {
            for (Entry<String, Object> entry : map.entrySet()) {
                paramMap.set(entry.getKey(), entry.getValue());
            }
        }
        return paramMap;
    }

    /**
     * Description: <br>
     * 
     * @author wang.wei297<br>
     * @taskId <br>
     * @param arg0
     * @param arg1
     * @return <br>
     */
    public Object query(String sql, DataParam dataParam) {
        Object result = null;

        ParamMap pm = getParamMap(dataParam.getParamMap());
        try {
            if (dataParam.getPageIndex() != -1 && dataParam.getPageSize() != -1) {
                long count = queryCount(sql, pm);
                sql = getPagedSql(sql, dataParam.getPageIndex(), dataParam.getPageSize(), count);
            }

            Object callBack = dataParam.getCallback();
            if (callBack != null && callBack instanceof RowSetMapper) {
                result = super.query(sql, pm, null, null, (RowSetMapper<?>) callBack);
            }
            else {
                Class<?> beanType = dataParam.getBeanType();
                Class<?> returnType = dataParam.getReturnType();

                if (Serializable.class.equals(beanType)) {
                    BOMapper mapper = BOMapper.getInstance();
                    List<DynamicDict> dictList = super.query(sql, pm, null, null, mapper);
                    if (returnType.isArray()) {
                        result = dictList.toArray();
                    }
                    else if (List.class.isAssignableFrom(returnType)) {
                        result = dictList;
                    }
                    else {
                        result = CommonUtil.isEmpty(dictList) ? null : dictList.get(0);
                    }
                }
                else if (returnType.isArray()) {
                    result = super.selectArray(sql, beanType, pm);
                }
                else if (List.class.isAssignableFrom(returnType)) {
                    result = super.selectList(sql, beanType, pm);
                }
                else {
                    result = super.queryToSimpObj(sql, pm, beanType);
                }
            }
        }
        catch (BaseAppException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    /**
     * Description: getPagedSql<br>
     * 
     * @author wang.wei297<br>
     * @taskId 561799<br>
     * @param sql sql
     * @param currentPage currentPage
     * @param pageSize pageSize
     * @param totalCount totalCount
     * @return sql<br>
     */
    public String getPagedSql(String sql, int currentPage, int pageSize, Long totalCount) {
        int start = 1 + (currentPage - 1) * pageSize;
        long end = currentPage * pageSize;
        end = end > totalCount ? totalCount : end;
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT").append(sql.trim().substring(6, sql.toUpperCase().indexOf("FROM"))).append("\t\n");
        sb.append("FROM (SELECT A00__.*, ROWNUM ROW_NUM").append("\t\n");
        sb.append("FROM (").append(sql).append(") A00__").append("\t\n");
        sb.append("WHERE ROWNUM <= ").append(end).append(")").append("\t\n");
        sb.append("WHERE ROW_NUM >= ").append(start);
        return sb.toString();
    }

    /**
     * Description: queryCount<br>
     * 
     * @author wang.wei297<br>
     * @taskId <br>
     * @param sql sql
     * @param obj obj
     * @return Long
     * @throws BaseAppException BaseAppException<br>
     */
    public Long queryCount(String sql, Object obj) throws BaseAppException {
        RowSetMapper<Long> rowSetMapper = new RowSetMapper<Long>() {
            public Long mapRows(RowSetOperator rowsetoperator, ResultSet resultset, int i, Object obj)
                throws SQLException, BaseAppException {
                Long retCnt = 0L;
                if (resultset.next()) {
                    retCnt = rowsetoperator.getLong(resultset, 1);
                }
                return retCnt;
            }
        };

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT COUNT(*) FROM (").append(sql).append(')');

        Long totalCount = 0L;
        if (obj instanceof ParamArray) {
            totalCount = super.query(sb.toString(), (ParamArray) obj, null, rowSetMapper);
        }
        else if (obj instanceof ParamMap) {
            totalCount = super.query(sb.toString(), (ParamMap) obj, null, null, rowSetMapper);
        }

        return totalCount;
    }
}
