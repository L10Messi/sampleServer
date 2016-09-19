package zgbzhyjy.sampsystem.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import zgbzhyjy.sampsystem.dao.MenuDao;
import zgbzhyjy.sampsystem.dao.RoleDao;
import zgbzhyjy.sampsystem.dao.RoleMenuDao;
import zgbzhyjy.sampsystem.entity.Menu;
import zgbzhyjy.sampsystem.entity.Role;
import zgbzhyjy.sampsystem.entity.RoleMenu;
import zgbzhyjy.sampsystem.util.PageFinder;

@Service
@Transactional
public class RoleService {

	@Autowired
	private RoleDao roleDao;
	
	@Autowired
	private MenuDao menuDao;
	
	@Autowired
	private RoleMenuDao roleMenuDao;
	
	/**
	 * 查询所有角色
	 * @param page
	 * @param rows
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageFinder<Role> queryRoleByPage(Role role,int page,int rows){
		String hql = "from Role r where 1=1 ";
		if(StringUtils.isNotBlank(role.getRoleCode())){
			hql += " and r.roleCode like '%"+role.getRoleCode()+"%'";
		}
		if(StringUtils.isNotBlank(role.getRoleName())){
			hql += " and r.roleName like '%"+role.getRoleName()+"%'";
		}
		return this.roleDao.pagedByHQL(hql, page, rows);
	}
	
	/**
	 * 根据ID查询角色
	 * @param id
	 * @return
	 */
	public Role getRoleById(String id){
		return this.roleDao.get(id, Role.class);
	}
	
	/**
	 * 删除角色
	 * @param ids
	 * @throws Exception 
	 */
	@SuppressWarnings("rawtypes")
	public void deleteRole(String ids) throws Exception{
		String roleids = "";
		String[] rids = ids.split(",");
		for (String id : rids) {
			roleids += "'" + id + "',";
		}
		roleids = roleids.substring(0,roleids.length() - 1);
		//校验有没有用户使用此角色
		String sql = "SELECT 1 FROM T_SYS_USER_ROLE WHERE ROLE_ID IN (" + roleids + ") ";
		List listcount = roleDao.getListBySQL(sql);
		if (listcount != null && listcount.size() > 0) {
			throw new Exception("有角色被用户使用，如想删除此角色，先去掉用户关联的角色");
		}
		String[] idlist = ids.split(",");
		for (String id : idlist) {
			Role u = new Role();
			u.setId(id);
			this.roleDao.delete(u);
		}
	}
	
	/**
	 * 编辑角色
	 * @param user
	 */
	public void saveRole(Role role){
		if(StringUtils.isNotBlank(role.getId())){
			this.roleDao.merge(role);
		}else{
			role.setId(null);
			this.roleDao.save(role);
		}
	}
	
	public List<Role> queryRoleByMenuIds(String menuIds){
		String hql = "select distinct r from Role r where r.id in (select rm.roleId from RoleMenu rm where rm.menuId in ("+menuIds+"))";
		return this.roleDao.getListByHQL(hql);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Map<String, Object>> roleMenuSelectTree(String roleid)throws ServiceException {
		String hql = "from Menu m order by m.menuCode";
		List<Menu> menus = this.menuDao.getListByHQL(hql);
		hql = "from RoleMenu t where t.roleId=?";
		List<RoleMenu> roleMenuList = this.roleMenuDao.getListByHQL(hql,roleid);
		if(menus != null && menus.size() > 0){
			Map<String, Map<String, Object>> tempMap = new HashMap<String, Map<String, Object>>();
			for (Menu menu : menus) {
				Map<String, Object> node = new HashMap<String, Object>();
				node.put("id", menu.getId());
				node.put("pid", menu.getPid());
				node.put("text", menu.getMenuName());
				for (RoleMenu roleMenu : roleMenuList) {
					if (roleMenu.getMenuId().equals(menu.getId())){
						node.put("checked", true);
						break;
					}
				}
				tempMap.put(menu.getId(), node);
			}
			// 再次遍历数据，为每个数据设置children，children为list
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			for (Menu menu : menus) {
				Map<String, Object> node = tempMap.get(menu.getId());
				// 用当前ID在tempMap中查找，找到了就是id的后代
				Map<String, Object> parent = (Map<String, Object>) tempMap.get(menu.getPid());
				if (parent != null) {
					List childrens = (List) parent.get("children");
					if (childrens == null) {
						childrens = new ArrayList<Map<String, Object>>();
						parent.put("children", childrens);
					}
					childrens.add(node);
				} else {
					list.add(node);
				}
			}
			return list;
		}
		return null;
	}
	
	public void saveMenuSelect(String roleid, String ids) throws ServiceException {
		// 删除该用户拥有的角色
		String sql = "delete from t_sys_role_menu where role_id=?";
		this.roleMenuDao.executeSql(sql,roleid);
		if(StringUtils.isNotBlank(ids)){
			String[] data = ids.split(",");
			for (String menuid : data) {
				RoleMenu rolemenu = new RoleMenu();
				rolemenu.setMenuId(menuid);
				rolemenu.setRoleId(roleid);
				this.roleMenuDao.save(rolemenu);
			}
		}
		
	}
}
