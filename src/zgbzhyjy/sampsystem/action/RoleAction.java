package zgbzhyjy.sampsystem.action;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import zgbzhyjy.sampsystem.entity.Role;
import zgbzhyjy.sampsystem.service.RoleService;
import zgbzhyjy.sampsystem.util.DataGrid;
import zgbzhyjy.sampsystem.util.DefaultAction;
import zgbzhyjy.sampsystem.util.PageFinder;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/role")
public class RoleAction extends DefaultAction {
	
	private static final Logger logger = Logger.getLogger(RoleAction.class);
	
	@Resource
	private RoleService roleService;
	
	/**
	 * 跳转至角色管理页面
	 * @return
	 */
	@RequestMapping(value = "/index")
	public String index() {
		return "/system/role";
	}
	
	/**
	 * 角色列表查询
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/queryList", method = RequestMethod.POST)
	public void queryList(HttpServletResponse response,Role role,DataGrid dg) throws Exception {
		PageFinder page = this.roleService.queryRoleByPage(role,dg.getPage(),dg.getRows());
		response.getWriter().print(JSONObject.fromObject(page).toString());
	}
	
	/**
	 * 编辑角色
	 * @param request
	 * @param response
	 * @param cont
	 * @return
	 */
	@RequestMapping(value = "/save")
	@ResponseBody
	public Map<String, Object> save(Role role) {
		try {
			this.roleService.saveRole(role);
		} catch (Exception e) {
			failure("维护角色失败！");
			logger.error("维护角色失败！"+e.getMessage());
			return getData();
		}
		return getData();
	}
	
	/**
	 * 删除角色
	 * @param request
	 * @param response
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "/delete")
	@ResponseBody
	public Map<String, Object> delete(String ids) {
		try {
			this.roleService.deleteRole(ids);
		} catch (Exception e) {
			failure("删除失败-"+e.getMessage());
			logger.error("删除失败-"+e.getMessage());
		}
		return getData();
	}
	
	/**
	 * 角色分配菜单树
	 */
	@RequestMapping(value = "/roleMenuSelect")
	@ResponseBody
	public List<Map<String, Object>> roleMenuSelect(String roleid) {
		try {
			return roleService.roleMenuSelectTree(roleid);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 保存角色的菜单
	 */
	@RequestMapping(value = "/saveMenuSelect")
	@ResponseBody
	public Map<String, Object> saveMenuSelect(HttpServletRequest request) {
		String roleid = request.getParameter("roleid");
		String ids = request.getParameter("ids");
		try {
			roleService.saveMenuSelect(roleid,ids);
		} catch (Exception e) {
			failure(e.getMessage());
			logger.error(e.getMessage());
		}
		return getData();
	}
	
}
