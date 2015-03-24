/**
 * 
 */
package com.fccfc.framework.core.db.support.hibernate;

import java.io.Serializable;
import java.util.List;

import com.fccfc.framework.core.db.DaoException;
import com.fccfc.framework.core.db.support.bean.BaseEntity;

/**
 * <Description> <br>
 * 
 * @author 王伟<br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate 2014年10月23日 <br>
 * @since V1.0<br>
 * @see com.fccfc.framework.dao.support <br>
 */
public interface IGenericBaseDao {

	/**
	 * 保存
	 * 
	 * @param t
	 * @throws DaoException
	 */
	void save(BaseEntity t);

	/**
	 * 根据Id查询
	 * 
	 * @param entityClass
	 * @param id
	 * @return
	 * @throws DaoException
	 */
	<T extends BaseEntity> T getById(Class<T> entityClass, final Serializable id);

	/**
	 * 根据实体查询
	 * 
	 * @param entity
	 * @return
	 * @throws DaoException
	 */
	<T extends BaseEntity> T getByEntity(T entity);

	/**
	 * 更新实体
	 * 
	 * @param entity
	 * @throws DaoException
	 */
	void update(BaseEntity entity);

	/**
	 * 删除实体
	 * 
	 * @param entity
	 * @throws DaoException
	 */
	void delete(BaseEntity entity);

	/**
	 * 根据Id删除实体
	 * 
	 * @param entityClass
	 * @param id
	 * @throws DaoException
	 */
	<T extends BaseEntity> void deleteById(Class<T> entityClass,
			final Serializable id);

	/**
	 * 根据实体查询
	 * 
	 * @param entity
	 * @return
	 * @throws DaoException
	 */
	<T extends BaseEntity> List<T> selectList(Class<T> entityClass);
}
