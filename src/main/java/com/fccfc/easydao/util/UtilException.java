/**
 * 
 */
package com.fccfc.easydao.util;

/**
 * <Description> <br>
 * 
 * @author 王伟<br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate 2014年10月23日 <br>
 * @since V1.0<br>
 * @see com.fccfc.framework.common.exception <br>
 */
public class UtilException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 2198186597374630666L;

    /**
     * 
     */
    public UtilException() {
    }

    /**
     * @param arg0
     */
    public UtilException(String arg0) {
        super(arg0);
    }

    /**
     * @param arg0
     */
    public UtilException(Throwable arg0) {
        super(arg0);
    }

    /**
     * @param arg0
     * @param arg1
     */
    public UtilException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }
}
