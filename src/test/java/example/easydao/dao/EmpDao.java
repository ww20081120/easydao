/**************************************************************************************** 
 Copyright © 2003-2012 ZTEsoft Corporation. All rights reserved. Reproduction or       <br>
 transmission in whole or in part, in any form or by any means, electronic, mechanical <br>
 or otherwise, is prohibited without the prior written consent of the copyright owner. <br>
 ****************************************************************************************/
package example.easydao.dao;

import java.util.List;

import org.hibernate.transform.ResultTransformer;

import com.fccfc.easydao.annotation.DAO;
import com.fccfc.easydao.annotation.Param;
import com.fccfc.easydao.annotation.Sql;

import example.easydao.model.Dept;
import example.easydao.model.Emp;

/**
 * <Description> <br>
 * 
 * @author 王伟 <br>
 * @version 1.0 <br>
 * @CreateDate 2014年10月29日 <br>
 * @see example.easydao.dao <br>
 */
@DAO
public interface EmpDao {
    
    @Sql(bean=Emp.class)
    List<Emp> queryEmp(@Param("dept")Dept dept);
    
    @Sql("select count(*) from emp")
    int listCount(ResultTransformer transformer);
}
