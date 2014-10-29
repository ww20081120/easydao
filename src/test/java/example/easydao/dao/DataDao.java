/**
 * 
 */
package example.easydao.dao;

import com.fccfc.easydao.DaoException;
import com.fccfc.easydao.annotation.DAO;

/**
 * <Description> <br>
 * 
 * @author 王伟<br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate 2014年10月25日 <br>
 * @since V1.0<br>
 * @see com.fccfc.test.dao.api <br>
 */
@DAO
public interface DataDao {

    int[] create() throws DaoException;

    int[] drop() throws DaoException;
}
