/**
 * 
 */
package com.fccfc.easydao.support.hibernate;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;

import com.fccfc.easydao.DaoException;
import com.fccfc.easydao.config.DataParam;
import com.fccfc.easydao.excutor.ISqlExcutor;
import com.fccfc.easydao.util.CommonUtil;

/**
 * <Description> <br>
 * 
 * @author 伟<br>
 * @version 1.0<br>
 * @CreateDate 2014-10-26 <br>
 * @see com.fccfc.framework.dao.support.hibernate <br>
 */
public class BaseHibernateDao implements IGenericBaseDao, ISqlExcutor {

    /** sessionFactory */
    private SessionFactory sessionFactory;

    /*
     * (non-Javadoc)
     * @see com.fccfc.framework.dao.support.SqlExcutor#query(java.lang.String, java.util.Map)
     */
    @SuppressWarnings("unchecked")
    public Object query(final String sql, final DataParam param) {
        Session session = sessionFactory.getCurrentSession();

        SQLQuery query = session.createSQLQuery(sql);

        // step1:设置参数
        Map<String, Object> paramMap = param.getParamMap();
        if (CommonUtil.isNotEmpty(paramMap)) {
            for (Entry<String, Object> entry : paramMap.entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
            }
        }

        // step2:设置分页
        if (param.getPageIndex() != -1 && param.getPageSize() != -1) {
            query.setFirstResult((param.getPageIndex() - 1) * param.getPageSize());
            query.setMaxResults(param.getPageSize());
        }

        // step3:设置返回值类型
        final Object callBack = param.getCallback();
        if (callBack != null && callBack instanceof ResultTransformer) {
            query.setResultTransformer((ResultTransformer) callBack);
        }
        else if (param.getBeanType().equals(Map.class)) {
            query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        }
        else if (Serializable.class.isAssignableFrom(param.getBeanType())) {
            query.setResultTransformer(new AutoResultTransformer((Class<? extends Serializable>) param.getBeanType()));
        }

        return List.class.isAssignableFrom(param.getReturnType()) || Object.class.equals(param.getReturnType()) ? query
            .list() : query.uniqueResult();
    }

    /*
     * (non-Javadoc)
     * @see com.fccfc.framework.dao.support.SqlExcutor#excuteSql(java.lang.String, java.util.Map)
     */
    public int excuteSql(final String sql, final DataParam param) {
        Session session = sessionFactory.getCurrentSession();
        SQLQuery query = session.createSQLQuery(sql);
        Map<String, Object> paramMap = param.getParamMap();
        if (CommonUtil.isNotEmpty(paramMap)) {
            for (Entry<String, Object> entry : paramMap.entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
            }
        }
        return query.executeUpdate();
    }

    /*
     * (non-Javadoc)
     * @see com.fccfc.framework.dao.support.SqlExcutor#batchExcuteSql(java.lang.String[])
     */
    public int[] batchExcuteSql(final String[] sqls, final DataParam param) {
        Session session = sessionFactory.getCurrentSession();
        int[] result = new int[sqls.length];
        SQLQuery query;
        Map<String, Object> paramMap = param.getParamMap();
        for (int i = 0; i < sqls.length; i++) {
            query = session.createSQLQuery(sqls[i]);
            if (CommonUtil.isNotEmpty(paramMap)) {
                for (Entry<String, Object> entry : paramMap.entrySet()) {
                    query.setParameter(entry.getKey(), entry.getValue());
                }
            }
            result[i] = query.executeUpdate();
        }
        return result;
    }

    /*
     * (non-Javadoc)
     * @see com.fccfc.framework.dao.support.IGenericBaseDao#save(java.lang.Object)
     */
    public void save(Serializable t) throws DaoException {
        try {
            Session session = sessionFactory.getCurrentSession();
            session.save(t);
        }
        catch (Exception e) {
            throw new DaoException(e);
        }
    }

    /*
     * (non-Javadoc)
     * @see com.fccfc.framework.dao.support.IGenericBaseDao#getById(java.lang.Class, java.io.Serializable)
     */
    @SuppressWarnings("unchecked")
    public <T extends Serializable> T getById(Class<T> entityClass, Serializable id) throws DaoException {
        try {
            Session session = sessionFactory.getCurrentSession();
            return (T) session.get(entityClass, id);
        }
        catch (Exception e) {
            throw new DaoException(e);
        }
    }

    /*
     * (non-Javadoc)
     * @see com.fccfc.framework.dao.support.IGenericBaseDao#getByEntity(java.lang.Object)
     */
    public <T extends Serializable> T getByEntity(T entity) throws DaoException {
        try {
            List<T> list = selectList(entity);
            return CommonUtil.isEmpty(list) ? null : list.get(0);
        }
        catch (Exception e) {
            throw new DaoException(e);
        }
    }

    /*
     * (non-Javadoc)
     * @see com.fccfc.framework.dao.support.IGenericBaseDao#update(java.lang.Object)
     */
    public void update(Serializable entity) throws DaoException {
        try {
            Session session = sessionFactory.getCurrentSession();
            session.update(entity);
        }
        catch (Exception e) {
            throw new DaoException(e);
        }
    }

    /*
     * (non-Javadoc)
     * @see com.fccfc.framework.dao.support.IGenericBaseDao#delete(java.lang.Object)
     */
    public void delete(Serializable entity) throws DaoException {
        try {
            Session session = sessionFactory.getCurrentSession();
            session.delete(entity);
        }
        catch (Exception e) {
            throw new DaoException(e);
        }
    }

    /*
     * (non-Javadoc)
     * @see com.fccfc.framework.dao.support.IGenericBaseDao#deleteById(java.lang.Class, java.io.Serializable)
     */
    public <T extends Serializable> void deleteById(Class<T> entityClass, Serializable id) throws DaoException {
        try {
            delete(getById(entityClass, id));
        }
        catch (Exception e) {
            throw new DaoException(e);
        }
    }

    /*
     * (non-Javadoc)
     * @see com.fccfc.framework.dao.support.IGenericBaseDao#selectList(java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    public <T extends Serializable> List<T> selectList(T entity) throws DaoException {
        try {
            Session session = sessionFactory.getCurrentSession();
            Criteria executableCriteria = session.createCriteria(entity.getClass());
            executableCriteria.add(Example.create(entity));

            return executableCriteria.list();
        }
        catch (Exception e) {
            throw new DaoException(e);
        }
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}
