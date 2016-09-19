package zgbzhyjy.sampsystem.action;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import net.sf.json.JSONObject;
import zgbzhyjy.sampsystem.entity.Client;
import zgbzhyjy.sampsystem.entity.SampScheme;
import zgbzhyjy.sampsystem.entity.SampTask;
import zgbzhyjy.sampsystem.service.ClientService;
import zgbzhyjy.sampsystem.service.SampSchemeService;
import zgbzhyjy.sampsystem.service.SampTaskService;
import zgbzhyjy.sampsystem.util.DataGrid;
import zgbzhyjy.sampsystem.util.DefaultAction;
import zgbzhyjy.sampsystem.util.PageFinder;

@Controller
@RequestMapping(value="/samptask")
public class SampTaskAction extends DefaultAction {
	
	private static final Logger logger = Logger.getLogger(SampTaskAction.class);

	@Resource
	private SampTaskService sampTaskService;
	
	@Resource
	private ClientService clientService;
	
	@Resource
	private SampSchemeService sampSchemeService;
	
	
	
	
	/**
	 * 跳转到抽样计划管理页面
	 * @return
	 * */
	@RequestMapping(value="/index")
	public String index(){
		return "/sampManager/sampTask";
	}

	/**
	 * 新增编辑抽样任务 
	 * @return 
	 */
	@RequestMapping(value="/save")
	@ResponseBody
	public Map<String, Object> saveSampScheme(SampTask sampTask,@RequestParam(value="cid")String cid,@RequestParam(value="SSid")String SSid){
		SampScheme sampScheme=this.sampSchemeService.querySampScheme(SSid);
		Client client=this.clientService.queryClient(cid);
		sampTask.setClient(client);
		sampTask.setSampScheme(sampScheme);
		try {
			this.sampTaskService.saveSampTask(sampTask);
		} catch (Exception e) {
			failure("维护失败！");
			logger.error("维护失败！"+e.getMessage());
			return getData();
		}
		return getData();
		
	}
	
	/**
	 * 抽样任务列表查询
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/queryList", method = RequestMethod.POST)
	public void queryList(HttpServletResponse response,SampTask sampTask,DataGrid dg) throws Exception {
		PageFinder page = this.sampTaskService.querySampTaskByPage(sampTask,dg.getPage(), dg.getRows());
		response.getWriter().print(JSONObject.fromObject(page).toString());
	}
	
	/**
	 * 删除抽样任务
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "/delete")
	@ResponseBody
	public Map<String, Object> delete(String ids) {
		try {
			this.sampTaskService.deleteSampTask(ids);;
		} catch (Exception e) {
			failure("删除失败-"+e.getMessage());
			logger.error("删除失败-"+e.getMessage());
		}
		return getData();
	}
	
	/**
	 * 开始抽样任务
	 */
	@RequestMapping(value = "/begin")
	@ResponseBody
	public  Map<String, Object> beginSampTask(String ids) {
		try {
			this.sampTaskService.SampTaskBegin(ids);
		} catch (Exception e) {
			failure("开始任务失败-"+e.getMessage());
			logger.error("开始任务失败-"+e.getMessage());
		}
		return getData();
	}

	
}
