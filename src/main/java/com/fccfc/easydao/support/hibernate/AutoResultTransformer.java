/**************************************************************************************** 
 Copyright © 2003-2012 ZTEsoft Corporation. All rights reserved. Reproduction or       <br>
 transmission in whole or in part, in any form or by any means, electronic, mechanical <br>
 or otherwise, is prohibited without the prior written consent of the copyright owner. <br>
 ****************************************************************************************/
package com.fccfc.easydao.support.hibernate;

import java.io.Serializable;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.property.ChainedPropertyAccessor;
import org.hibernate.property.PropertyAccessor;
import org.hibernate.property.PropertyAccessorFactory;
import org.hibernate.property.Setter;
import org.hibernate.transform.ResultTransformer;

import com.fccfc.easydao.util.BeanUtils;

/**
 * <Description> <br>
 * 
 * @author 王伟 <br>
 * @version 1.0 <br>
 * @CreateDate 2014年10月28日 <br>
 * @see com.fccfc.framework.dao.support.hibernate <br>
 */
public class AutoResultTransformer implements ResultTransformer {

    /**
     * serialVersionUID <br>
     */
    private static final long serialVersionUID = 7131196081465940115L;

    private final Class<? extends Serializable> resultClass;

    private PropertyAccessor propertyAccessor;

    private Setter[] setters;

    /**
     * 默认构造函数
     */
    public AutoResultTransformer(Class<? extends Serializable> resultClass) {
        if (resultClass == null) {
            throw new IllegalArgumentException("resultClass cannot be null");
        }
        this.resultClass = resultClass;
        propertyAccessor = new ChainedPropertyAccessor(new PropertyAccessor[] {
            PropertyAccessorFactory.getPropertyAccessor(resultClass, null),
            PropertyAccessorFactory.getPropertyAccessor("field")
        });
    }

    // 结果转换时，HIBERNATE调用此方法
    public Object transformTuple(Object[] tuple, String[] aliases) {
        Object result;

        try {
            if (setters == null) {// 首先初始化，取得目标POJO类的所有SETTER方法
                setters = new Setter[aliases.length];
                for (int i = 0; i < aliases.length; i++) {
                    String alias = aliases[i];
                    if (alias != null) {
                        setters[i] = getSetterByColumnName(alias);
                    }
                }
            }
            result = resultClass.newInstance();

            // 这里使用SETTER方法填充POJO对象
            for (int i = 0; i < aliases.length; i++) {
                if (setters[i] != null) {
                    setters[i].set(result, tuple[i], null);
                }
            }
        }
        catch (InstantiationException e) {
            throw new HibernateException("Could not instantiate resultclass: " + resultClass.getName());
        }
        catch (IllegalAccessException e) {
            throw new HibernateException("Could not instantiate resultclass: " + resultClass.getName());
        }
        return result;
    }

    // 根据数据库字段名在POJO查找JAVA属性名，参数就是数据库字段名，如：USER_ID
    private Setter getSetterByColumnName(String alias) {
        return propertyAccessor.getSetter(resultClass, BeanUtils.toCamelCase(alias));
    }

    /**
     * @see org.hibernate.transform.ResultTransformer#transformList(java.util.List)
     */
    @SuppressWarnings("rawtypes")
    @Override
    public List transformList(List collection) {
        return collection;
    }
}
