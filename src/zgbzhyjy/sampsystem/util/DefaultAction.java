package zgbzhyjy.sampsystem.util;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import zgbzhyjy.sampsystem.entity.User;

/**
 * 控制器Action的父类
 */
@Controller
public class DefaultAction {
	private Map<String, Object> data = new HashMap<String, Object>();
	
	protected void failure(String msg){
		data.put("success", Boolean.FALSE);
		data.put("msg", msg);
		data.put("init", Boolean.TRUE);
	}
	
	public Map<String, Object> getData() {
		Boolean init = (Boolean)data.get("init");
		if (init == null || init == false){
			data.put("success", Boolean.TRUE);
		}
		data.put("init", Boolean.FALSE);
		return data;
	}
	
	public HttpSession getSession() {
		ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		return attrs.getRequest().getSession();
	}
	
	/**
	 * 获取执行操作的用户User
	 * @return userid
	 * */
	public String getUserId(){
		User user = (User) getSession().getAttribute("user");
		return user.getId();
	}
}
