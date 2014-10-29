/**************************************************************************************** 
 Copyright © 2003-2012 ZTEsoft Corporation. All rights reserved. Reproduction or       <br>
 transmission in whole or in part, in any form or by any means, electronic, mechanical <br>
 or otherwise, is prohibited without the prior written consent of the copyright owner. <br>
 ****************************************************************************************/
package example.easydao.service;

import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.hibernate.transform.ResultTransformer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import example.easydao.dao.EmpDao;
import example.easydao.model.Dept;

/**
 * <Description> <br>
 * 
 * @author 王伟 <br>
 * @version 1.0 <br>
 * @CreateDate 2014年10月29日 <br>
 * @see example.easydao.service <br>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
    "classpath:/META-INF/spring/*.xml"
})
@Transactional
public class EmpService {

    @Resource
    private EmpDao empDao;

    @Test
    public void test() {
        try {
            Dept dept = new Dept();
            dept.setDeptno(30);

            System.out.println("------------------");
            System.out.println(empDao.queryEmp(dept).size());
            System.out.println("------------------");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void test2() {
        try {
            System.out.println("------------------");
            System.out.println(empDao.listCount(new ResultTransformer() {
                public Object transformTuple(Object[] tuple, String[] aliases) {
                    return Integer.parseInt(tuple[0].toString());
                }
                public List transformList(List collection) {
                    return collection;
                }
            }));
            System.out.println("------------------");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
