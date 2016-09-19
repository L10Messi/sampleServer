package zgbzhyjy.sampsystem.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import zgbzhyjy.sampsystem.dao.MenuDao;
import zgbzhyjy.sampsystem.entity.Menu;
import zgbzhyjy.sampsystem.entity.Role;
import zgbzhyjy.sampsystem.entity.User;
import zgbzhyjy.sampsystem.util.PageFinder;

@Service
@Transactional
public class MenuService {

	@Autowired
	private MenuDao menuDao;
	
	@Autowired
	private RoleService roleService;
	
	/**
	 * 获取用户对应菜单
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getMenuTree(User user){
			String hql = "";
			if(user == null){
				hql = "from Menu m order by m.menuCode";
			}else{
				if("admin".equals(user.getLoginname())){
					hql = "from Menu m order by m.menuCode";
				}else{
					hql = "select distinct m from Menu m,RoleMenu r where m.id = r.menuId and r.roleId in (select u.roleId from UserRole u where u.userId = '"+user.getId()+"') order by m.menuCode";
				}
			}
			List<Menu> menus = this.menuDao.getListByHQL(hql);
			Map<String, Map<String, Object>> tempMap = new HashMap<String, Map<String, Object>>();
			// 再次遍历数据，为每个数据设置children，children为list
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			if(menus != null && menus.size() > 0){
				for (Menu menu : menus) {
					Map<String, Object> node = new HashMap<String, Object>();
					node.put("id", menu.getId());
					node.put("pid", menu.getPid());
					node.put("text", menu.getMenuName());
					node.put("url", menu.getUrl());
					tempMap.put(menu.getId(), node);
				}
				for (Menu menu : menus) {
					Map<String, Object> node = tempMap.get(menu.getId());
					// 用当前ID在tempMap中查找，找到了就是id的后代
					Map<String, Object> parent = (Map<String, Object>) tempMap.get(menu.getPid());
					if (parent != null) {
						List<Map<String, Object>> childrens = (List<Map<String, Object>>) parent.get("children");
						if (childrens == null) {
							childrens = new ArrayList<Map<String, Object>>();
							parent.put("children", childrens);
						}
						childrens.add(node);
					} else {
						list.add(node);
					}
				}
			}
			return list;
	}
	
	/**
	 * 查询菜单列表
	 * @param page
	 * @param rows
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageFinder<Menu> queryMenuList(String id,int page,int rows){
		String hql = "from Menu t where 1=1 ";
		if(StringUtils.isNotBlank(id)){
			hql += " and t.pid = '"+id+"' ";
		}else{
			hql += " and t.pid is null ";
		}
		hql += " order by t.menuCode";
		return this.menuDao.pagedByHQL(hql, page, rows);
	}
	
	/**
	 * 编辑菜单
	 */
	public void saveMenu(Menu menu){
		if(StringUtils.isNotBlank(menu.getId())){
			this.menuDao.merge(menu);
		}else{
			menu.setId(null);
			this.menuDao.save(menu);
		}
	}
	
	/**
	 * 删除菜单
	 * @param ids
	 * @throws Exception 
	 */
	@SuppressWarnings("rawtypes")
	public void deleteMenu(String menuids) throws Exception{
		String mids = "";
		String[] ids = menuids.split(",");
		for (String id : ids) {
			mids += "'" + id + "',";
		}
		mids = mids.substring(0,mids.length() - 1);
		List<Role> roles = roleService.queryRoleByMenuIds(mids);
		if(roles != null && roles.size() > 0){
			String rolename = "";
			for (Role role : roles) {
				rolename += role.getRoleName() + ",";
			}
			rolename = rolename.substring(0, rolename.length() - 1);
			throw new Exception("角色："+rolename+"已使用菜单，如想删除此菜单，先去掉角色关联的菜单");
		}else{
			String sql="select 1 from t_sys_menu where pid in (" + mids + ")";
			List list = menuDao.getListBySQL(sql);
			if (list.size() > 0) {
				throw new Exception("有子菜单的节点，不能删除");
			}
			sql = "delete from t_sys_menu where id in ("+mids+")";
			this.menuDao.executeSql(sql);
		}
	}
}
