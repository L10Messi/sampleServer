package zgbzhyjy.sampsystem.dao;

import java.io.Serializable;

import org.springframework.stereotype.Repository;

import zgbzhyjy.sampsystem.entity.SampInspected;
import zgbzhyjy.sampsystem.util.GenericDao;

@Repository
public class SampInspectedDao extends GenericDao<SampInspected, Serializable> implements Serializable{

	/**
	 * 样本检测数据访问层
	 */
	private static final long serialVersionUID = 1L;

}
