package zgbzhyjy.sampsystem.dao;

import java.io.Serializable;

import org.springframework.stereotype.Repository;

import zgbzhyjy.sampsystem.entity.Client;
import zgbzhyjy.sampsystem.util.GenericDao;

@Repository
public class ClientDao extends GenericDao<Client, Serializable> implements Serializable{

	/**
	 * 客户端的数据访问层
	 */
	private static final long serialVersionUID = 1L;

}
