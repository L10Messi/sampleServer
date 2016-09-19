package zgbzhyjy.sampsystem.dao;

import java.io.Serializable;

import org.springframework.stereotype.Repository;

import zgbzhyjy.sampsystem.entity.User;
import zgbzhyjy.sampsystem.util.GenericDao;

@Repository
public class UserDao extends GenericDao<User, Serializable> implements Serializable {

	private static final long serialVersionUID = 1L;
	

}
