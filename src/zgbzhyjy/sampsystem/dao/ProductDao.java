package zgbzhyjy.sampsystem.dao;

import java.io.Serializable;

import org.springframework.stereotype.Repository;

import zgbzhyjy.sampsystem.entity.Product;
import zgbzhyjy.sampsystem.util.GenericDao;

@Repository
public class ProductDao extends GenericDao<Product, Serializable> implements Serializable{

	/**
	 * 产品的数据访问层
	 */
	private static final long serialVersionUID = 1L;
	
}
