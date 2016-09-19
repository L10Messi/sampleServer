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
import zgbzhyjy.sampsystem.entity.QualChrt;
import zgbzhyjy.sampsystem.entity.SampScheme;
import zgbzhyjy.sampsystem.service.QualChrtService;
import zgbzhyjy.sampsystem.service.SampSchemeService;
import zgbzhyjy.sampsystem.util.DataGrid;
import zgbzhyjy.sampsystem.util.DefaultAction;
import zgbzhyjy.sampsystem.util.PageFinder;

@Controller
@RequestMapping(value="/sampscheme")
public class SampSchemeAction extends DefaultAction{
	private static final Logger logger = Logger.getLogger(SampSchemeAction.class);

	
	@Resource
	private SampSchemeService sampSchemeService;
	
	@Resource
	private QualChrtService qualChrtSrvice;
	
	/**
	 * 跳转到抽样计划管理页面
	 * @return
	 * */
	@RequestMapping(value="/index")
	public String index(){
		return "/sampManager/sampScheme";
	}

	/**
	 * 新增编辑抽样计划
	 */
	@RequestMapping(value="/save")
	@ResponseBody
	public Map<String, Object> saveSampScheme(SampScheme sampScheme,@RequestParam(value="qid")String qid){
		QualChrt qualChrt=new QualChrt();
		qualChrt=this.qualChrtSrvice.getQualChrtById(qid);
		sampScheme.setQualChrt(qualChrt);
		sampScheme.setProduct(qualChrt.getProduct());
		System.out.println(qualChrt.getQualChrtName()+" "+qualChrt.getProduct().getProductName());
		try {
			this.sampSchemeService.saveSampScheme(sampScheme);
		} catch (Exception e) {
			failure("维护产品失败！");
			logger.error("维护产品失败！"+e.getMessage());
			return getData();
		}
		return getData();
		
	}
	
	/**
	 * 抽样计划列表查询
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/queryList", method = RequestMethod.POST)
	public void queryList(HttpServletResponse response,SampScheme sampScheme,DataGrid dg) throws Exception {
		PageFinder page = this.sampSchemeService.querySampSchemeByPage(sampScheme,dg.getPage(), dg.getRows());
		response.getWriter().print(JSONObject.fromObject(page).toString());
	}
	
	/**
	 * 删除抽样计划
	 * @param SSid
	 */
	@RequestMapping(value = "/delete")
	@ResponseBody
	public Map<String, Object> delete(String ids) {
		try {
			this.sampSchemeService.deleteSampScheme(ids);
		} catch (Exception e) {
			failure("删除失败-"+e.getMessage());
			logger.error("删除失败-"+e.getMessage());
		}
		return getData();
	}

}
