/**************************************************************************************** 
 Copyright © 2003-2012 ZTEsoft Corporation. All rights reserved. Reproduction or       <br>
 transmission in whole or in part, in any form or by any means, electronic, mechanical <br>
 or otherwise, is prohibited without the prior written consent of the copyright owner. <br>
 ****************************************************************************************/
package com.fccfc.easydao;

import com.fccfc.easydao.util.CommonUtil;

/**
 * <Description> <br>
 * 
 * @author 王伟 <br>
 * @version 1.0 <br>
 * @CreateDate 2014年10月24日 <br>
 * @see com.fccfc.framework.dao <br>
 */
public class DaoException extends Exception {

    /**
     * serialVersionUID <br>
     */
    private static final long serialVersionUID = 1615477208457076971L;

    /**
     * 默认构造函数
     */
    public DaoException() {
    }

    /**
     * 默认构造函数
     */
    public DaoException(String arg0) {
        super(arg0);
    }

    /**
     * 默认构造函数
     */
    public DaoException(String arg0, Object... params) {
        this(CommonUtil.messageFormat(arg0, params));
    }

    /**
     * 默认构造函数
     */
    public DaoException(Throwable arg0) {
        super(arg0);
    }

    /**
     * 默认构造函数
     */
    public DaoException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    /**
     * @param arg0
     * @param arg1
     * @param params
     */
    public DaoException(String arg0, Throwable arg1, Object... params) {
        this(CommonUtil.messageFormat(arg0, params), arg1);
    }
}
