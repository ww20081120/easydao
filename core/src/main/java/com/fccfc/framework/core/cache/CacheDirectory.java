/**************************************************************************************** 
 Copyright © 2003-2012 ZTEsoft Corporation. All rights reserved. Reproduction or       <br>
 transmission in whole or in part, in any form or by any means, electronic, mechanical <br>
 or otherwise, is prohibited without the prior written consent of the copyright owner. <br>
 ****************************************************************************************/
package com.fccfc.framework.core.cache;

/**
 * <Description> <br>
 * 
 * @author 王伟 <br>
 * @version 1.0 <br>
 * @CreateDate 2014年10月24日 <br>
 * @see com.fccfc.easydao.util.cache <br>
 */
public interface CacheDirectory {

	/** cache root 目录 */
	String CACHE_ROOT = "/CACHE";

	/** sql模板目录 */
	String SQL_DIR = "/SQL_TEMPLATE";

	/** dao 参数目录 */
	String SQL_PARAM_DIR = "/SQL_PARAM";
}
