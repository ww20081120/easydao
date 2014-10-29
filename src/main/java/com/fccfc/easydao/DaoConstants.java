/**************************************************************************************** 
 Copyright © 2003-2012 ZTEsoft Corporation. All rights reserved. Reproduction or       <br>
 transmission in whole or in part, in any form or by any means, electronic, mechanical <br>
 or otherwise, is prohibited without the prior written consent of the copyright owner. <br>
 ****************************************************************************************/
package com.fccfc.easydao;

/**
 * <Description> <br>
 * 
 * @author 王伟 <br>
 * @version 1.0 <br>
 * @CreateDate 2014年10月23日 <br>
 * @see com.fccfc.framework.common.constant <br>
 */
public interface DaoConstants {
    /** 分隔符 */
    String SPLITOR = ",";

    /** 路径分割符 */
    String PATH_SPLITOR = "/";

    /** SQL语句分隔符 */
    String SQL_SPLITOR = ";";

    /** 下划线 */
    char UNDERLINE = '_';

    /** 空白 */
    String BLANK = "";

    /** 默认编码 */
    String DEFAULT_CHARSET = "utf-8";

    /** NULL */
    String NULL = "NULL";

    /** pageIndex */
    String PAGE_INDEX = "pageIndex";

    /** pageSize */
    String PAGE_SIZE = "pageSize";

    /** 查询sql开始语句 */
    String SQL_SELECT_PREFIX = "select";
}
