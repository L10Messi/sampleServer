package zgbzhyjy.sampsystem.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import zgbzhyjy.sampsystem.dao.RoleDao;
import zgbzhyjy.sampsystem.dao.UserDao;
import zgbzhyjy.sampsystem.dao.UserRoleDao;
import zgbzhyjy.sampsystem.entity.Role;
import zgbzhyjy.sampsystem.entity.User;
import zgbzhyjy.sampsystem.entity.UserRole;
import zgbzhyjy.sampsystem.util.PageFinder;

@Service
@Transactional
public class UserService {

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private RoleDao roleDao;
	
	@Autowired
	private UserRoleDao userRoleDao;
	
	/**
	 * 根据用户名密码查询用户
	 * @param loginname
	 * @param loginpwd
	 * @return
	 */
	public User getUser(String loginname,String loginpwd){
		return userDao.getByHQL("from User where loginname = ? and loginpwd = ?", loginname,loginpwd);
	}
	
	/**
	 * 根据用户名密码查询用户
	 * @param loginname
	 * @param loginpwd
	 * @return
	 */
	public User getUser(String loginname){
		return userDao.getByHQL("from User where loginname = ?", loginname);
	}
	
	/**
	 * 查询所有用户
	 * @param page
	 * @param rows
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageFinder<User> queryUserByPage(User user,int page,int rows){
		String hql = "from User u where 1=1 and u.loginname <> 'admin' ";
		if(StringUtils.isNotBlank(user.getLoginname())){
			hql += " and u.loginname like '%"+user.getLoginname()+"%'";
		}
		if(StringUtils.isNotBlank(user.getName())){
			hql += " and u.name like '%"+user.getName()+"%'";
		}
		if(StringUtils.isNotBlank(user.getState())){
			hql += " and u.state = '"+user.getState()+"'";
		}
		return this.userDao.pagedByHQL(hql, page, rows);
	}
	
	/**
	 * 根据ID查询用户
	 * @param id
	 * @return
	 */
	public User getUserById(String id){
		return this.userDao.get(id, User.class);
	}
	
	/**
	 * 删除用户
	 * @param ids
	 */
	public void deleteUser(String ids){
		String[] idlist = ids.split(",");
		for (String id : idlist) {
			User u = new User();
			u.setId(id);
			this.userDao.delete(u);
		}
		String userids = "";
		for (String id : idlist) {
			userids += "'" + id + "',";
		}
		userids = userids.substring(0,userids.length() - 1);
		String sql = "delete from t_sys_user_role where user_id in ("+userids+")";
		this.userDao.executeSql(sql);
	}
	/**
	 * 编辑用户
	 * @param user
	 */
	public void saveUser(User user){
		if(StringUtils.isNotBlank(user.getId())){
			User u = this.getUserById(user.getId());
			u.setLoginname(user.getLoginname());
			u.setName(user.getName());
			u.setState(user.getState());
			this.userDao.merge(u);
		}else{
			user.setLoginpwd("1");
			this.userDao.save(user);
		}
	}
	
	
	public List<Map<String, Object>> userRoleSelectTree(String userid){
		String hql = "from Role m order by m.roleCode";
		List<Role> roles = this.roleDao.getListByHQL(hql);
		hql = "from UserRole t where t.userId=?";
		List<UserRole> userRoleList = this.userRoleDao.getListByHQL(hql,userid);
		if(roles != null && roles.size() > 0){
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			for (Role role : roles) {
				Map<String, Object> node = new HashMap<String, Object>();
				node.put("id", role.getId());
				node.put("text", role.getRoleName());
				for (UserRole userRole : userRoleList) {
					if (userRole.getRoleId().equals(role.getId())){
						node.put("checked", true);
						break;
					}
				}
				list.add(node);
			}
			return list;
		}
		return null;
	}
	
	public void saveRoleSelect(String userid, String ids){
		// 删除该用户拥有的角色
		String sql = "delete from t_sys_user_role where user_id=?";
		this.userRoleDao.executeSql(sql,userid);
		if(StringUtils.isNotBlank(ids)){
			String[] array = ids.split(",");
			for (String roleid : array) {
				UserRole userRole = new UserRole();
				userRole.setRoleId(roleid);
				userRole.setUserId(userid);
				this.userRoleDao.save(userRole);
			}
		}
		
	}
	
	
	public void udpatePassword(String id,String password){
		User user = this.getUserById(id);
		user.setLoginpwd(password);
		this.userDao.update(user);
	}
}
