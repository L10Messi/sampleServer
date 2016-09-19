package zgbzhyjy.sampsystem.action;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import zgbzhyjy.sampsystem.entity.Menu;
import zgbzhyjy.sampsystem.service.MenuService;
import zgbzhyjy.sampsystem.util.DataGrid;
import zgbzhyjy.sampsystem.util.DefaultAction;
import zgbzhyjy.sampsystem.util.PageFinder;

import org.apache.log4j.Logger;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/menu")
public class MenuAction extends DefaultAction {
	
	private static final Logger logger = Logger.getLogger(MenuAction.class);
	
	@Resource
	private MenuService menuService;
	
	/**
	 * 跳转至菜单管理页面
	 * @return
	 */
	@RequestMapping(value = "/index")
	public String index() {
		return "/system/menu";
	}
	
	/**
	 * 获取用户对应角色菜单
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/menutreeAll")
	@ResponseBody
	public List<Map<String, Object>> menutreeAll() {
		try {
			return menuService.getMenuTree(null);
		} catch (ServiceException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 菜单列表查询
	 * @param request
	 * @param response
	 * @param dg
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/queryList")
	public void queryList(HttpServletResponse response,String id,DataGrid dg) throws Exception {
		PageFinder page = this.menuService.queryMenuList(id,dg.getPage(),dg.getRows());
		response.getWriter().print(JSONObject.fromObject(page).toString());
	}
	
	/**
	 * 编辑菜单
	 * @return
	 */
	@RequestMapping(value = "/save")
	@ResponseBody
	public Map<String, Object> save(Menu menu) {
		try {
			this.menuService.saveMenu(menu);
		} catch (Exception e) {
			failure("维护菜单失败！");
			logger.error("维护菜单失败！"+e.getMessage());
			return getData();
		}
		return getData();
	}
	
	
	/**
	 * 删除菜单
	 * @param request
	 * @param response
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "/delete")
	@ResponseBody
	public Map<String, Object> delete(String ids) {
		try {
			this.menuService.deleteMenu(ids);
		} catch (Exception e) {
			failure("删除失败"+e.getMessage());
			logger.error("删除失败-"+e.getMessage());
			return getData();
		}
		return getData();
	}
}
