/**************************************************************************************** 
 Copyright © 2003-2012 ZTEsoft Corporation. All rights reserved. Reproduction or       <br>
 transmission in whole or in part, in any form or by any means, electronic, mechanical <br>
 or otherwise, is prohibited without the prior written consent of the copyright owner. <br>
 ****************************************************************************************/
package example.easydao.dao;

import java.util.List;

import com.fccfc.easydao.DaoException;
import com.fccfc.easydao.annotation.DAO;
import com.fccfc.easydao.annotation.Sql;
import com.fccfc.easydao.support.hibernate.IGenericBaseDao;

import example.easydao.model.Dept;

/**
 * <Description> <br>
 * 
 * @author 王伟 <br>
 * @version 1.0 <br>
 * @CreateDate 2014年10月28日 <br>
 * @see com.fccfc.test.dao.api <br>
 */
@DAO
public interface DeptDao extends IGenericBaseDao {

    @Sql(value = "select * from dept", bean = Dept.class)
    List<Dept> listDept() throws DaoException;
}
