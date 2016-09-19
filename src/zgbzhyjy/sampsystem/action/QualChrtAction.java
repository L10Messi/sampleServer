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
import zgbzhyjy.sampsystem.entity.Product;
import zgbzhyjy.sampsystem.entity.QualChrt;
import zgbzhyjy.sampsystem.service.ProductService;
import zgbzhyjy.sampsystem.service.QualChrtService;
import zgbzhyjy.sampsystem.util.DataGrid;
import zgbzhyjy.sampsystem.util.DefaultAction;
import zgbzhyjy.sampsystem.util.PageFinder;

@Controller
@RequestMapping("/qualChrt")
public class QualChrtAction extends DefaultAction{
	
	private static final Logger logger = Logger.getLogger(QualChrtAction.class);
	
	@Resource 
	private QualChrtService qualChrtService;
	
	@Resource 
	private ProductService productService;
	/**
	 * 跳转至质量特性管理页面(WQ)
	 * @return
	 */
	@RequestMapping(value = "/index")
	public String index() {
		return "/product/qualChrt";
	}
	
	/**
	 * 编辑保存质量特性
	 * @return 
	 */
	@RequestMapping(value="/save")
	@ResponseBody
	public Map<String, Object> saveQualChrt(QualChrt qualChrt,@RequestParam(value="pid") String pid){
		Product product=new Product();
		product=this.productService.queryProduct(pid);
		qualChrt.setProduct(product);
		try {
			this.qualChrtService.saveQualChrt(qualChrt);
		} catch (Exception e) {
			failure("维护产品质量特性失败！");
			logger.error("维护产品质量特性失败！"+e.getMessage());
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
	public void queryList(HttpServletResponse response,QualChrt qualChrt,DataGrid dg) throws Exception {
		PageFinder page = this.qualChrtService.queryQualChrtByPage(qualChrt, dg.getPage(), dg.getRows());
		response.getWriter().print(JSONObject.fromObject(page).toString());
	}
	
	
	/**
	 * 指定产品ID(pid)对质量特性列表查询
	 * 实现在sampScheme选择产品和质量特性时的动态变化。
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/getQualChrtList", method = RequestMethod.POST)
	public void getQualChrtList(@RequestParam(value="pid") String pid,HttpServletResponse response,DataGrid dg) throws Exception {
		Product product=new Product();
		product=this.productService.queryProduct(pid);
		QualChrt qualChrt=new QualChrt();
		qualChrt.setProduct(product);;
		PageFinder page = this.qualChrtService.queryQualChrtByPage(qualChrt, dg.getPage(), dg.getRows());
		response.getWriter().print(JSONObject.fromObject(page).toString());
	}
	
	/**
	 * 删除质量特性
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "/delete")
	@ResponseBody
	public Map<String, Object> delete(String ids) {
		try {
			this.qualChrtService.deleteQualChrt(ids);
		} catch (Exception e) {
			failure("删除失败-"+e.getMessage());
			logger.error("删除失败-"+e.getMessage());
		}
		return getData();
	}
	
	/**
	 * 实现页面上质量特性ID自动生成
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "/qualchrtId")
	@ResponseBody
	public String qualchrtId(String ids) {
		String qualChrtID=null;
		try {
			qualChrtID=this.qualChrtService.CalQualChrtID(ids);
		} catch (Exception e) {
			failure("质量特性ID生成失败-"+e.getMessage());
			logger.error("质量特性ID生成失败-"+e.getMessage());
		}
		return qualChrtID;
	}
	
}

