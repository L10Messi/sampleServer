package zgbzhyjy.sampsystem.action;


import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import zgbzhyjy.sampsystem.entity.SampInspected;
import zgbzhyjy.sampsystem.service.SampInspectedService;
import zgbzhyjy.sampsystem.util.DataGrid;
import zgbzhyjy.sampsystem.util.DefaultAction;
import zgbzhyjy.sampsystem.util.ExcelUtil;
import zgbzhyjy.sampsystem.util.PageFinder;

@Controller
@RequestMapping(value="/sampinspected")
public class SampInspectedAction extends DefaultAction{
	private static final Logger logger = Logger.getLogger(SampInspectedAction.class);
	
	@Resource
	private SampInspectedService sampInspectedService;
	
	/**
	 * 跳转到样本检测记录页面
	 * */
	@RequestMapping(value="/index")
	public String index(){
		return "/sampManager/sampInspected";
	}

	/**
	 * 样本检测记录列表查询
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/queryList", method = RequestMethod.POST)
	public void queryList(HttpServletResponse response,SampInspected sampInspected,DataGrid dg) throws Exception {
		PageFinder page = this.sampInspectedService.querySampInspectedByPage(sampInspected,dg.getPage(), dg.getRows());
		response.getWriter().print(JSONObject.fromObject(page).toString());
	}
	
	/**
	 * 查询样本检测记录的商品名
	 * @return 
	 * @return 
	 * @throws Exception 
	 */
	@RequestMapping(value = "/getProductName")
	@ResponseBody 
	public void getProductName(HttpServletResponse response) throws Exception{
		List<Map<String, Object>> rs=this.sampInspectedService.getProductName();
		JSONArray json = new JSONArray();
		for (int i = 0; i < rs.size(); i++) {
			JSONObject jso = new JSONObject();
			jso.put("id", rs.get(i).get("ProductID"));
			jso.put("text", rs.get(i).get("ProductName"));
			json.add(jso);
		}
		response.getWriter().print(json.toString());
		
//		JSONArray jso = new JSONArray();
//		System.out.println(rs);
//		for (SampInspected si : rs) {
//			JSONObject json = new JSONObject();
////			json.put("id", si.getClientID());
//			json.put("text", si.getProductID());
//			jso.add(json);
//		}
//		System.out.println(jso.toString());
//		response.getWriter().print(jso.toString());
////		return "hello";
		
	}
	/**
	 * 查询样本检测记录的质量特性名
	 * @return 
	 * @throws IOException 
	 */
	@RequestMapping(value = "/getQualChrtName", method = RequestMethod.POST)
	@ResponseBody
	public void getQualChrtName(@RequestParam(value="ProductID") String ProductID,HttpServletResponse response) throws Exception{
//		ResultSet rs=this.sampInspectedService.getQualChrtName(ProductID);
//		ResultSetToJson rstj=new ResultSetToJson();
//		System.out.println(rstj.resultSetToJson(rs));
//		response.getWriter().print(rstj.resultSetToJson(rs));
		List<Map<String,Object>> rs=this.sampInspectedService.getQualChrtName(ProductID);
		JSONArray jso=new JSONArray();
		for(int i=0;i < rs.size();i++){
			JSONObject json =new JSONObject();
			json.put("id", rs.get(i).get("QualChrtID"));
			json.put("text", rs.get(i).get("QualChrtName"));
			jso.add(json);
		}
		response.getWriter().println(jso.toString());
	}

	/**
	 * 查询样本检测记录的质量特性名
	 * @return 
	 * @throws IOException 
	 */
	@RequestMapping(value = "/getClientID", method = RequestMethod.POST)
	@ResponseBody
	public void getClientID(@RequestParam(value="QualChrt") String QualChrtID,HttpServletResponse response) throws Exception{
//		ResultSet rs=this.sampInspectedService.getQualChrtName(ProductID);
//		ResultSetToJson rstj=new ResultSetToJson();
//		System.out.println(rstj.resultSetToJson(rs));
//		response.getWriter().print(rstj.resultSetToJson(rs));
		List<Map<String,Object>> rs=this.sampInspectedService.getClientID(QualChrtID);
		JSONArray jso=new JSONArray();
		for(int i=0;i < rs.size();i++){
			JSONObject json =new JSONObject();
			json.put("id", rs.get(i).get("ClientID"));
			json.put("text", rs.get(i).get("ClientID"));
			jso.add(json);
		}
		response.getWriter().println(jso.toString());
	}
	
	/**
	 * 样本检测记录导出
	 * @throws Exception 
	 */
	@RequestMapping(value = "/toExcel", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> toExcel(SampInspected sampInspected,DataGrid dg) {
		try {
			String[] title={"主键标示码","流水号","抽样计划编号","产品ID","产品名称","质量特性ID","抽样任务编号",
					"检测的客户端编号","批号","样本号","检测状态","检测序列","检测结果","检测结果详情"};
			List<SampInspected> rs=this.sampInspectedService.querySampInspectedByPage(sampInspected);
			ExcelUtil.exportExcel("SampInspectedList",title , rs);
		} catch (Exception e) {
			failure("导出失败-"+e.getMessage());
			logger.error("导出失败-"+e.getMessage());
		}
		return getData();
	}
	
	/**
	 * 查询样本检测记录的商品名
	 * @return 
	 * @return 
	 * @throws Exception 
	 */
	@RequestMapping(value = "/getProductNameByid")
	@ResponseBody 
	public void getProductNameByid(HttpServletResponse response) throws Exception{
		List<Map<String, Object>> rs=this.sampInspectedService.getProductName();
		JSONArray json = new JSONArray();
		for (int i = 0; i < rs.size(); i++) {
			JSONObject jso = new JSONObject();
			jso.put("id", rs.get(i).get("pid"));
			jso.put("text", rs.get(i).get("ProductName"));
			json.add(jso);
		}
		response.getWriter().print(json.toString());
	}
	
}
