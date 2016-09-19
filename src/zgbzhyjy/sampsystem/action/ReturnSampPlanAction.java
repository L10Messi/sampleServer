package zgbzhyjy.sampsystem.action;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import net.sf.json.JSONObject;
import zgbzhyjy.sampsystem.entity.ReturnSampPlan;
import zgbzhyjy.sampsystem.service.ReturnSampPlanService;
import zgbzhyjy.sampsystem.util.DataGrid;
import zgbzhyjy.sampsystem.util.DefaultAction;
import zgbzhyjy.sampsystem.util.PageFinder;


@Controller
@RequestMapping(value="/returnSampPlan")
public class ReturnSampPlanAction extends DefaultAction{
	@Resource
	private ReturnSampPlanService rsps;

	/**
	 * 跳转到样本检测记录页面
	 * */
	@RequestMapping(value="/index")
	public String index(){
		return "/sampManager/returnSampPlan";
	}
	
	/**
	 * 样本检测记录列表查询
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/queryList", method = RequestMethod.POST)
	public void queryList(HttpServletResponse response,ReturnSampPlan returnSampPlan,DataGrid dg) throws Exception {
		PageFinder page = this.rsps.queryReturnSampPlanByPage(returnSampPlan,dg.getPage(), dg.getRows());
		response.getWriter().print(JSONObject.fromObject(page).toString());
	}
	
//	/**
//	 * 样本检测记录列表查询
//	 */
//	@SuppressWarnings("rawtypes")
//	@RequestMapping(value = "/toExcel", method = RequestMethod.GET)
//	public void toExcel(HttpServletResponse response,ReturnSampPlan returnSampPlan,DataGrid dg) throws Exception {
//		PageFinder page = this.rsps.queryReturnSampPlanByPage(returnSampPlan,dg.getPage(), dg.getRows());
//		String json=JSONObject.fromObject(page).toString();
//		JSONObject jsonObject = JSONObject.fromObject(json);
//		
//		
//		String NowDate=DateUtil.getMachingCurrentTime("yyyyMMdd");
//		
//		int i=1;
//		String fileName="/Users/LRover/Downloads/"+"ReturnSampPlan_"+NowDate+"_"+i+".xls";
//		i++;
//		
//		ExcelUtil.createExcel(fileName,jsonObject);
//		response.getWriter().print(JSONObject.fromObject(page).toString());
//	}
//	
	
}
