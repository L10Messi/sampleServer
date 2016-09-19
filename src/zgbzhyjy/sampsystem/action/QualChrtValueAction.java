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
import zgbzhyjy.sampsystem.entity.QualChrtValue;
import zgbzhyjy.sampsystem.service.QualChrtService;
import zgbzhyjy.sampsystem.service.QualChrtValueService;
import zgbzhyjy.sampsystem.util.DataGrid;
import zgbzhyjy.sampsystem.util.DefaultAction;
import zgbzhyjy.sampsystem.util.PageFinder;

@Controller
@RequestMapping("/qualChrtValue")
public class QualChrtValueAction extends DefaultAction {
	
	private static final Logger logger = Logger.getLogger(QualChrtValueAction.class);
	
	@Resource 
	private QualChrtValueService qualChrtValueService;
	
	@Resource
	private QualChrtService qualChrtService;
	
	/**
	 * 跳转至质量特性限定管理页面
	 * @return
	 */
	@RequestMapping(value = "/index")
	public String index() {
		return "/product/qualChrtValue";
	}

	/**
	 * 编辑保存质量特性
	 * @return 
	 */
	@RequestMapping(value="/save")
	@ResponseBody
	public Map<String, Object> saveQualChrtValue(QualChrtValue qualChrtValue,@RequestParam(value="qid") String qid){
		System.out.println("hello");
		QualChrt qualChrt=new QualChrt();
		qualChrt=this.qualChrtService.getQualChrtById(qid);
		qualChrtValue.setQualChrt(qualChrt);
		try { 
			this.qualChrtValueService.saveQualChrtValue(qualChrtValue);
		} catch (Exception e) {
			failure("维护产品质量特性限定失败！");
			logger.error("维护产品质量特性限定失败！"+e.getMessage());
			return getData();
		}
		return getData();
	}
	
	/**
	 * 通过qualChrtID,编辑保存质量特性
	 * @return 
	 */
	@RequestMapping(value="/savebyQC")
	@ResponseBody
	public Map<String, Object> saveQualChrtV(QualChrtValue qualChrtValue,@RequestParam(value="QualChrtID") String qualchrtID){
		QualChrt qualChrt=new QualChrt();
		qualChrt=this.qualChrtService.getQualChrtByQualChrtID(qualchrtID);
		qualChrtValue.setQualChrt(qualChrt);
		try { 
			this.qualChrtValueService.saveQualChrtValue(qualChrtValue);
		} catch (Exception e) {
			failure("维护产品质量特性限定失败！");
			logger.error("维护产品质量特性限定失败！"+e.getMessage());
			return getData();
		}
		return getData();
	}
	
	
	/**
	 * 质量特性列表查询
	 * @param request
	 * @param response
	 * @param dg
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/queryList", method = RequestMethod.POST)
	public void queryList(HttpServletResponse response,QualChrtValue qualChrtValue,DataGrid dg) throws Exception {
		PageFinder page = this.qualChrtValueService.queryQualChrtValueByPage(qualChrtValue, dg.getPage(), dg.getRows());
		response.getWriter().print(JSONObject.fromObject(page).toString());
	}
	
	/**
	 * 删除质量特性限定值
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "/delete")
	@ResponseBody
	public Map<String, Object> delete(String ids) {
		try {
			this.qualChrtValueService.deleteQualChrtValue(ids);
		} catch (Exception e) {
			failure("删除失败-"+e.getMessage());
			logger.error("删除失败-"+e.getMessage());
		}
		return getData();
	}
}

