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
import zgbzhyjy.sampsystem.entity.Client;
import zgbzhyjy.sampsystem.service.ClientService;
import zgbzhyjy.sampsystem.util.DataGrid;
import zgbzhyjy.sampsystem.util.DefaultAction;
import zgbzhyjy.sampsystem.util.PageFinder;

@Controller
@RequestMapping(value="/client")
public class ClientAction extends DefaultAction{
	private static final Logger logger = Logger.getLogger(ClientAction.class);

	@Resource
	private ClientService clientService;
	
	/**
	 * 跳转至客户端管理页面
	 * @return
	 */
	@RequestMapping(value="/index")
	public String index(){
		return "/system/client";
	}
	
	
	/**
	 * 新增编辑客户端
	 * @return 
	 */
	@RequestMapping(value="/save")
	@ResponseBody
	public Map<String, Object> saveClient(Client client){
		try {
			this.clientService.saveClient(client);
		} catch (Exception e) {
			failure("维护客户端失败！");
			logger.error("维护客户端失败！"+e.getMessage());
			return getData();
		}
		return getData();
		
	}
	
	/**
	 * 客户端列表查询
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/queryList", method = RequestMethod.POST)
	public void queryList(HttpServletResponse response,Client client,DataGrid dg) throws Exception {
		PageFinder page = this.clientService.queryClientByPage(client, dg.getPage(), dg.getRows());
		response.getWriter().print(JSONObject.fromObject(page).toString());
	}
	
	
	/**
	 * 删除客户端
	 * @param request
	 * @param response
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "/delete")
	@ResponseBody
	public Map<String, Object> delete(String ids) {
		try {
			this.clientService.deleteClient(ids);
		} catch (Exception e) {
			failure("删除客户端失败-"+e.getMessage());
			logger.error("删除客户端失败-"+e.getMessage());
		}
		return getData();
	}

}
