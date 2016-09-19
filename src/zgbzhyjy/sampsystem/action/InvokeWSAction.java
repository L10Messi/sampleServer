package zgbzhyjy.sampsystem.action;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.sf.json.JSONObject;
import zgbzhyjy.sampsystem.entity.WebService;
import zgbzhyjy.sampsystem.service.InvokeWSService;
import zgbzhyjy.sampsystem.util.DataGrid;
import zgbzhyjy.sampsystem.util.DefaultAction;
import zgbzhyjy.sampsystem.util.PageFinder;

@Controller
@RequestMapping(value="/webService")
public class InvokeWSAction extends DefaultAction{
	private static final Logger logger = Logger.getLogger(InvokeWSAction.class);
	
	@Resource
	private InvokeWSService invokeWSService;
	
	/**
	 * 跳转至WebService配置管理页面
	 * @return
	 */
	@RequestMapping(value="/index")
	public String index(){
		return "/system/webService";
	}

	/**
	 * 更新WebService信息
	 * @return 
	 */
	@RequestMapping(value="/save")
	@ResponseBody
	public Map<String, Object> updateClient(WebService webService){
		try {
			this.invokeWSService.updateInvokeWS(webService);
		} catch (Exception e) {
			failure("更新信息失败！");
			logger.error("更新信息失败！"+e.getMessage());
			return getData();
		}
		return getData();
		
	}
	
	/**
	 * WebService列表查询
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/queryList", method = RequestMethod.POST)
	public void queryList(HttpServletResponse response,WebService webService,DataGrid dg) throws Exception {
		PageFinder page = this.invokeWSService.queryInvokeWSByPage(webService,dg.getPage(), dg.getRows());
		response.getWriter().print(JSONObject.fromObject(page).toString());
	}
}
