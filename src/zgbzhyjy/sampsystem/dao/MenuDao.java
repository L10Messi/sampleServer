package zgbzhyjy.sampsystem.dao;

import java.io.Serializable;

import org.springframework.stereotype.Repository;

import zgbzhyjy.sampsystem.entity.Menu;
import zgbzhyjy.sampsystem.util.GenericDao;

@Repository
public class MenuDao extends GenericDao<Menu, Serializable> implements Serializable {

	private static final long serialVersionUID = 1L;

}
