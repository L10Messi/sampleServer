package zgbzhyjy.sampsystem.action;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import zgbzhyjy.sampsystem.entity.User;
import zgbzhyjy.sampsystem.service.MenuService;
import zgbzhyjy.sampsystem.service.UserService;
import zgbzhyjy.sampsystem.util.DefaultAction;

@Controller
@RequestMapping("/sysCommon")
public class SysCommonAction extends DefaultAction {
	
	@Resource
	private UserService userService;
	
	@Resource
	private MenuService menuService;
	
	/**
	 * 跳转至登陆页
	 * @return
	 */
	@RequestMapping(value = "/login")
	public String login() {
		return "/sampLogin";
	}
	/**
	 * 用户验证登录
	 */
	@RequestMapping(value = "/loginCheck.do")
	@ResponseBody
	public Map<String, Object> login(HttpServletResponse response,String loginname,String loginpwd) {
		User user = this.userService.getUser(loginname,loginpwd);
		if(user != null){
			if("1".equals(user.getState())){
				failure("此用户已被禁用，请联系管理员！");
			}else{
				Cookie cookieuser = new Cookie("loginname",loginname); 
			    cookieuser.setPath("/") ;
			    response.addCookie(cookieuser); 
				getSession().setAttribute("user",user);
			}
		}else{
			failure("用户名或密码错误！");
		}
		return getData();
	}
	
	/**
	 * 用户验证登录
	 */
	@RequestMapping(value = "/loginJeso")
	public String loginJeso(String userid) {
		User user = this.userService.getUser(userid);
		if(user != null){
			getSession().setAttribute("user",user);
			return "/sampIndex";
		}
		return "/sampLogin";
	}
	
	/**
	 * 跳转至主界面
	 * @return
	 */
	@RequestMapping(value = "/main")
	public String main() {
		return "/sampIndex";
	}
	
	/**
	 * 获取用户对应角色菜单
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/menutree")
	@ResponseBody
	public List<Map<String, Object>> getUserMenuTree(HttpSession session) {
		User user = (User) session.getAttribute("user");
		try {
			return menuService.getMenuTree(user);
		} catch (ServiceException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 退出登陆，清空内存用户信息
	 */
	@RequestMapping(value = "/logout")
	@ResponseBody
	public void  logout(HttpSession session) {
		session.invalidate();
	}
	
	
	@RequestMapping(value = "/updatePassword")
	@ResponseBody
	public Map<String, Object> updatePassword(HttpSession session,String password,String password_new1,String password_new2) {
		User user = (User) session.getAttribute("user");
		if(!password.equals(user.getLoginpwd())){
			failure("原密码错误！");
			return getData();
		}
		if(!password_new1.equals(password_new2)){
			failure("新密码与确认密码不相同！");
			return getData();
		}
		this.userService.udpatePassword(user.getId(), password_new2);
		return getData();
	}
}
