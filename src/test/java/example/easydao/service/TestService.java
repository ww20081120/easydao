/**************************************************************************************** 
 Copyright © 2003-2012 ZTEsoft Corporation. All rights reserved. Reproduction or       <br>
 transmission in whole or in part, in any form or by any means, electronic, mechanical <br>
 or otherwise, is prohibited without the prior written consent of the copyright owner. <br>
 ****************************************************************************************/
package example.easydao.service;

import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fccfc.easydao.DaoException;

import example.easydao.dao.DataDao;
import example.easydao.dao.DeptDao;
import example.easydao.model.Dept;

/**
 * <Description> <br>
 * 
 * @author 王伟 <br>
 * @version 1.0 <br>
 * @CreateDate 2014年10月27日 <br>
 * @see com.fccfc.test.dao.service <br>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
    "classpath:/META-INF/spring/*.xml"
})
@Transactional
public class TestService {

    @Resource
    private DataDao dataDao;

    @Resource
    private DeptDao deptDao;

    @Before
    public void init() throws DaoException {
        dataDao.drop();
        dataDao.create();
    }

    @Test
    public void listDept() throws DaoException {
        List<Dept> list = deptDao.listDept();
        for (Dept dept : list) {
            System.out.println(dept);
        }
    }
}
