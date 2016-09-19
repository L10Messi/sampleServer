package zgbzhyjy.sampsystem.util;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.jdbc.Work;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;


@Repository
public class GenericDao<T, PK extends Serializable> implements java.io.Serializable {

	private static final long serialVersionUID = 8418855074581831220L;
	
	@Resource(name = "sessionFactory")
	private SessionFactory sessionFactory;
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	/**
	 * 保存
	 */
	public void save(T t) {
		this.getSession().save(t);
	}

	/**
	 * 保存或者更新
	 */
	public void saveOrUpdate(T t) {
		this.getSession().saveOrUpdate(t);
	}
	
	public void refresh(T t) {
		this.getSession().refresh(t);
	}

	public void update(T t) {
		this.getSession().update(t);
	}

	public void merge(T t) {
		this.getSession().merge(t);
	}
	
	/**
	 * 根据ID查询实体
	 */
	@SuppressWarnings("unchecked")
	public T load(Serializable id,Class<T> c) {
		T t = (T) this.getSession().load(c, id);
		return t;
	}

	/**
	 * 根据ID查询实体
	 */
	@SuppressWarnings("unchecked")
	public T get(Serializable id,Class<T> c) {
		T t = (T) this.getSession().get(c, id);
		return t;
	}

	/**
	 * 是否包含
	 */
	public boolean contains(T t) {
		return this.getSession().contains(t);
	}

	/**
	 * 根据实体删除
	 */
	public void delete(T t) {
		this.getSession().delete(t);
	}

	/**
	 * 根据ID删除
	 */
	public boolean deleteById(Serializable Id,Class<T> c) {
		T t = get(Id,c);
		if (t == null) {
			return false;
		}
		delete(t);
		return true;
	}

	/**
	 * 根据集合删除
	 */
	public void deleteAll(Collection<T> entities) {
		for (Object entity : entities) {
			this.getSession().delete(entity);
		}
	}
	
	/**
	 * 执行原生的sql语句，用于新增、更新和删除
	 * 
	 * @param sqlString
	 */
	public void executeSql(String sqlString, Object... values) {
		Query query = this.getSession().createSQLQuery(sqlString);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				query.setParameter(i, values[i]);
			}
		}
		query.executeUpdate();
	}
	
	/**
	 * 将以逗号分割的字符串 分割为单个字符
	 * @author LRover
	 * @param ids
	 * */
	public String getParamFormat(String ids){
		String strIds[] = ids.split(",");
		if(strIds.length > 1){
			String id = "";
			for (String str : strIds) {
				id += "'" + str + "',";
			}
			return id.substring(0,id.length() - 1);
		}
		return ids;
	}

	/**
	 * 根据HQL语句查找唯一实体
	 * 
	 * @param hqlString
	 * @param values  不定参数的Object数组
	 *           
	 * @return 查询实体
	 */
	@SuppressWarnings("unchecked")
	public T getByHQL(String hqlString, Object... values) {
		Query query = this.getSession().createQuery(hqlString);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				query.setParameter(i, values[i]);
			}
		}
		return (T) query.uniqueResult();
	}

	/**
	 * 根据SQL语句查找唯一实体
	 * 
	 * @param sqlString
	 * @param values
	 *           
	 * @return 查询实体
	 */
	@SuppressWarnings("unchecked")
	public T getBySQL(String sqlString, Object... values) {
		Query query = this.getSession().createSQLQuery(sqlString);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				query.setParameter(i, values[i]);
			}
		}
		return (T) query.uniqueResult();
	}

	/**
	 * 根据HQL语句，得到对应的list
	 * 
	 * @param hqlString
	 * @param values
	 * @return 查询多个实体的List集合
	 */
	@SuppressWarnings("unchecked")
	public List<T> getListByHQL(String hqlString, Object... values) {
		Query query = this.getSession().createQuery(hqlString);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				query.setParameter(i, values[i]);
			}
		}
		return query.list();
	}
	
	/**
	 * 根据HQL语句，得到对应的list
	 * 可以限定查询个数
	 * @param hqlString
	 * @param values
	 * @return 查询多个实体的List集合
	 */
	@SuppressWarnings("unchecked")
	public List<T> getListByHQL(String hqlString,int n) {
		Query query = this.getSession().createQuery(hqlString);
		query.setFirstResult(0);
		query.setMaxResults(n);
		return query.list();
	}

	/**
	 * 根据SQL语句，得到对应的list
	 * 
	 * @param sqlString
	 * @param values
	 * @return 查询多个实体的List集合
	 */
	@SuppressWarnings("rawtypes")
	public List getListBySQL(String sqlString, Object... values) {
		Query query = this.getSession().createSQLQuery(sqlString);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				query.setParameter(i, values[i]);
			}
		}
		return query.list();
	}
	
	/**
	 * 返回一个map集合
	 * @param sqlString
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getMapBySQL(final String sqlString) {
		Query query = this.getSession().createSQLQuery(sqlString);
        return (List<Map<String, Object>>)query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();  
	}

	/**
	 * 根据HQL得到记录数
	 * 
	 * @param hql
	 * @param values
	 * @return 记录总数
	 */
	public Long countByHql(String hql, Object... values) {
		Query query = this.getSession().createQuery(hql);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				query.setParameter(i, values[i]);
			}
		}
		return (Long) query.uniqueResult();
	}
	
	
	/**
	 * 根据SQL得到记录数
	 * 
	 * @param sqlString
	 * @param values
	 * @return 查询多个实体的List集合
	 */
	public Long countBySql(String sql, Object... values){
		Query query = this.getSession().createSQLQuery(sql);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				query.setParameter(i, values[i]);
			}
		}
		return Long.valueOf(query.uniqueResult()+"");
	}
	
	/**
	 * 根据SQL得到记录数
	 * 
	 * @param sqlString
	 * @param values
	 * @return 查询多个实体的List集合
	 */
	public Long countBySql1(String sql, Object... values){
		sql = "SELECT COUNT(1) FROM (" + sql + ")";
		Query query = this.getSession().createSQLQuery(sql);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				query.setParameter(i, values[i]);
			}
		}
		return Long.valueOf(query.uniqueResult()+"");
	}
	
	/**
	 * 按hibernate标准查询器进行分页查询
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PageFinder<T> pagedByCriteria(Criteria criteria, int pageNo, int pageSize) {
		Long totalRows = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
		criteria.setProjection(null);
		if (totalRows == null || totalRows.intValue() < 1) {
			PageFinder finder = new PageFinder(pageNo, pageSize, 0);
			finder.setRows(new ArrayList<T>());
			return finder;
		} else {
			PageFinder finder = new PageFinder(pageNo, pageSize, totalRows.intValue());
			List<T> list = criteria.setFirstResult(finder.getStartOfPage()).setMaxResults(
					finder.getPageSize()).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
			finder.setRows(list);
			return finder;
		}
	}

	/**
	 * 用HQL语句进行修改isDel值，完成删除目的
	 * @author LRover
	 * @param hql
	 * 
	 */
	public void updateIsDel(String hql) {
		this.getSession().createQuery(hql).executeUpdate();
	}
	
	
	
	/**
	 * 按HQL方式进行分页查询
	 * 
	 * @param toPage
	 *            跳转页号
	 * @param pageSize
	 *            每页数量
	 * @param hql
	 *            查询语句
	 * @param values
	 *            参数
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PageFinder pagedByHQL(String hql, int toPage, int pageSize, Object... values) {
		String countQueryString = " select count (*) " + removeSelect(removeOrders(hql));
		List countlist = (List) this.createQuery(countQueryString, values).list();
		Long totalCount = Long.valueOf(countlist.get(0)+"");
		if (totalCount.intValue() < 1) {
			return new PageFinder(toPage, pageSize, totalCount.intValue());
		} else {
			PageFinder finder = new PageFinder(toPage, pageSize, totalCount.intValue());
			Query query = createQuery(hql, values);
			List list = query.setFirstResult(finder.getStartOfPage()).setMaxResults(finder.getPageSize())
					.list();
			finder.setRows(list);
			return finder;
		}

	}
	
	public Query createQuery(String hql, Object... values) {
		Query query = getSession().createQuery(hql);
		if (null != values && values.length > 0) {
			for (int i = 0; i < values.length; i++) {
				query.setParameter(i, values[i]);
			}
		}
		return query;
	}
	
	/**
     * 执行SQL查询(支持分页)
     * @param sql   sql语句
     * @param objects sql参数
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public PageFinder pagedBySql(String sql, int pageNo, int pageSize,Object... objects ){
        String countQueryString = " select count (*) from (" + sql +")";
        SQLQuery query = getSession().createSQLQuery(countQueryString);
        if(objects != null){
            for (int i = 0; i < objects.length; i++) {
                if(null!=objects[i] && !"".equals(objects[i])){
                    query.setParameter(i, objects[i]);
                }
            }
        }
        List countlist = query.list();
        Long totalCount = Long.valueOf(countlist.get(0)+"");
        if (totalCount.intValue() < 1) {
            return new PageFinder(pageNo, pageSize, totalCount.intValue());
        } else {
            PageFinder finder = new PageFinder(pageNo, pageSize, totalCount.intValue());
            query = getSession().createSQLQuery(sql);
            if(objects != null){
                for (int i = 0; i < objects.length; i++) {
                    if(null!=objects[i] && !"".equals(objects[i])){
                        query.setParameter(i, objects[i]);
                    }
                }
            }
            List<Object[]> list = query.setFirstResult(finder.getStartOfPage()).setMaxResults(finder.getPageSize()).list();
            finder.setRows(list);
            return finder;
        }
    }
    
    
    /**
   	 * 用于查询表中某个数据的个数
   	 * @author LRover
   	 * @param hql
   	 * @return
   	 */
    @SuppressWarnings({ "rawtypes", })
   	public Long countByHql(String hql){
    		String countQueryString = " select count (*) " + removeSelect(removeOrders(hql));
    		List countlist = (List) this.getSession().createQuery(countQueryString).list(); 
    		Long totalCount = Long.valueOf(countlist.get(0)+"");

    		return totalCount;
           
       }
    
    /**
	 * 去除hql的orderby 子句，用于pagedQuery.
	 * 
	 * @param hql
	 * @return
	 */
	protected final static String removeOrders(String hql) {
		Pattern p = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(hql);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sb, "");
		}
		m.appendTail(sb);
		return sb.toString();
	}
	
	/**
	 * 去除hql的select 子句
	 * 
	 * @param hql
	 * @return
	 */
	protected final static String removeSelect(String hql) {
		int beginPos = hql.toLowerCase().indexOf("from");
		Assert.isTrue(beginPos != -1, " hql : " + hql + " must has a keyword 'from'");
		return hql.substring(beginPos);
	}
	
	
	/**
     * 执行无返回值的存储过程
     */
    public void executeProcedure(final String procedureName,final Object... parameters) {
    	getSession().doWork(   
		new Work() {
			public void execute(Connection connection) throws SQLException {
				String call = "{ call " + procedureName + "(";
                if(parameters == null || parameters.length == 0)
                    call = call + ")}";
                else
                    call = call + StringUtils.repeat(",?", parameters.length).substring(1) + ")}";
                CallableStatement statement = connection.prepareCall(call);
                if(parameters != null && parameters.length > 0){
                    for (int i = 0; i < parameters.length; i++) {
                        Object parameter = parameters[i];
                        int index = i + 1;
                        if(parameter instanceof String)
                            statement.setString(index, (String) parameter);
                        else if(parameter instanceof Date){
                            if(parameter instanceof java.sql.Timestamp){
                                statement.setTimestamp(index, new java.sql.Timestamp(((Date)parameter).getTime()));
                            } else {
                                statement.setDate(index, new java.sql.Date(((Date)parameter).getTime()));
                            }
                        }
                        else if(parameter instanceof Integer)
                            statement.setInt(index, (Integer) parameter);
                        else if(parameter instanceof Long)
                            statement.setLong(index, (Long) parameter);
                        else
                            statement.setObject(index, parameter);
                    }
                }
                statement.execute();
                statement.close();
            }
		}); 
    }
}
