package zgbzhyjy.sampsystem.action;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import zgbzhyjy.sampsystem.entity.User;
import zgbzhyjy.sampsystem.service.UserService;
import zgbzhyjy.sampsystem.util.DataGrid;
import zgbzhyjy.sampsystem.util.DefaultAction;
import zgbzhyjy.sampsystem.util.PageFinder;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user")
public class UserAction extends DefaultAction {
	
	private static final Logger logger = Logger.getLogger(UserAction.class);
	
	@Resource
	private UserService userService;
	
	/**
	 * 跳转至用户管理页面
	 * @return
	 */
	@RequestMapping(value = "/index")
	public String index() {
		return "/system/user";
	}
	
	/**
	 * 用户列表查询
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/queryList", method = RequestMethod.POST)
	public void queryList(HttpServletResponse response,User user,DataGrid dg) throws Exception {
		PageFinder page = this.userService.queryUserByPage(user,dg.getPage(),dg.getRows());
		response.getWriter().print(JSONObject.fromObject(page).toString());
	}
	
	/**
	 * 编辑用户
	 * @param request
	 * @param response
	 * @param cont
	 * @return
	 */
	@RequestMapping(value = "/save")
	@ResponseBody
	public Map<String, Object> save(User user) {
		try {
			this.userService.saveUser(user);
		} catch (Exception e) {
			failure("维护用户失败！");
			logger.error("维护用户失败！"+e.getMessage());
			return getData();
		}
		return getData();
	}
	
	/**
	 * 删除用户
	 * @param request
	 * @param response
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "/delete")
	@ResponseBody
	public Map<String, Object> delete(String ids) {
		try {
			this.userService.deleteUser(ids);
		} catch (Exception e) {
			failure("删除失败！");
			logger.error("删除失败"+e.getMessage());
		}
		return getData();
	}
	
	/**
	 * 用户分配角色
	 */
	@RequestMapping(value = "/userRoleSelect")
	@ResponseBody
	public List<Map<String, Object>> userRoleSelectTree(String userid) {
		try {
			return userService.userRoleSelectTree(userid);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 保存用户角色
	 */
	@RequestMapping(value = "/saveRoleSelect")
	@ResponseBody
	public Map<String, Object> saveRoleSelect(HttpServletRequest request) {
		String userid = request.getParameter("userid");
		String ids = request.getParameter("ids");
		try {
			userService.saveRoleSelect(userid,ids);
		} catch (Exception e) {
			failure("保存用户角色失败！");
			logger.error("保存用户角色失败"+e.getMessage());
		}
		return getData();
	}
	
}
