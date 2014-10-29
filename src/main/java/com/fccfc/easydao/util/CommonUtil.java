/**************************************************************************************** 
 Copyright © 2003-2012 ZTEsoft Corporation. All rights reserved. Reproduction or       <br>
 transmission in whole or in part, in any form or by any means, electronic, mechanical <br>
 or otherwise, is prohibited without the prior written consent of the copyright owner. <br>
 ****************************************************************************************/
package com.fccfc.easydao.util;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * <Description> <br>
 * 
 * @author 王伟 <br>
 * @version 1.0 <br>
 * @CreateDate 2014年10月23日 <br>
 * @see com.fccfc.framework.common.utils <br>
 */
public final class CommonUtil {

    /**
     * Description: 字符串是为NULL或为空<br>
     * 
     * @author 王伟 <br>
     * @param str
     * @return <br>
     */
    public static boolean isEmpty(String str) {
        return StringUtils.isEmpty(str);
    }

    /**
     * Description:字符串不为NULL也不为空 <br>
     * 
     * @author 王伟 <br>
     * @param str
     * @return <br>
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 判断数组是否为NULL或为空
     * 
     * @param t
     * @return
     */
    public static <T> boolean isEmpty(T[] t) {
        return t == null || t.length == 0;
    }

    /**
     * 判断数组不为NULL也不为空
     * 
     * @param t
     * @return
     */
    public static <T> boolean isNotEmpty(T[] t) {
        return !isEmpty(t);
    }

    /**
     * Description: 集合是否为NULL或为空<br>
     * 
     * @author 王伟 <br>
     * @param col <br>
     */
    public static boolean isEmpty(Collection<?> col) {
        return col == null || col.isEmpty();
    }

    /**
     * Description:集合不为NULL也不为空 <br>
     * 
     * @author 王伟 <br>
     * @param col
     * @return <br>
     */
    public static boolean isNotEmpty(Collection<?> col) {
        return !isEmpty(col);
    }

    /**
     * Description: map是否为NULL或为空<br>
     * 
     * @author 王伟 <br>
     * @param col <br>
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    /**
     * Description:map不为NULL也不为空 <br>
     * 
     * @author 王伟 <br>
     * @param col
     * @return <br>
     */
    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    /**
     * Description: <br>
     * 
     * @author 王伟 <br>
     * @param str
     * @return <br>
     */
    public static String lowerCaseFirstChar(String str) {
        if (isNotEmpty(str)) {
            char firstChar = str.charAt(0);
            if (Character.isUpperCase(firstChar)) {
                StringBuilder sb = new StringBuilder(str);
                sb.setCharAt(0, Character.toLowerCase(firstChar));
                str = sb.toString();
            }
        }
        return str;
    }

    /**
     * 消息格式化
     * 
     * @param message message
     * @param params params
     * @return String
     */
    public static String messageFormat(String message, Object... params) {
        return isNotEmpty(params) ? MessageFormat.format(message, params) : message;
    }
}
