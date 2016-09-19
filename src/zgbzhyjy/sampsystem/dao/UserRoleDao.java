package zgbzhyjy.sampsystem.dao;

import java.io.Serializable;

import org.springframework.stereotype.Repository;

import zgbzhyjy.sampsystem.entity.UserRole;
import zgbzhyjy.sampsystem.util.GenericDao;

@Repository
public class UserRoleDao extends GenericDao<UserRole, Serializable> implements Serializable {

	private static final long serialVersionUID = 1L;

}
