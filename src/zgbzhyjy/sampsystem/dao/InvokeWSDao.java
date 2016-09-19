package zgbzhyjy.sampsystem.dao;

import java.io.Serializable;

import org.springframework.stereotype.Repository;

import zgbzhyjy.sampsystem.entity.WebService;
import zgbzhyjy.sampsystem.util.GenericDao;

@Repository
public class InvokeWSDao extends GenericDao<WebService, Serializable>{

	/**
	 * 调用WebService的数据访问层
	 */
	private static final long serialVersionUID = 1L;

}
