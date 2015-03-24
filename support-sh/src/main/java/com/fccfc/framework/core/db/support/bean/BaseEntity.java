/**
 * 
 */
package com.fccfc.framework.core.db.support.bean;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;

/**
 * <Description> <br>
 * 
 * @author 王伟<br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate 2014年10月25日 <br>
 * @since V1.0<br>
 * @see com.fccfc.framework.core.bean <br>
 */
public abstract class BaseEntity implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 540091732380370744L;

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
