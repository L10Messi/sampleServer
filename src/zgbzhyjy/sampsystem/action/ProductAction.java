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
import zgbzhyjy.sampsystem.entity.Product;
import zgbzhyjy.sampsystem.service.ProductService;
import zgbzhyjy.sampsystem.util.DataGrid;
import zgbzhyjy.sampsystem.util.DefaultAction;
import zgbzhyjy.sampsystem.util.ExcelUtil;
import zgbzhyjy.sampsystem.util.PageFinder;

@Controller
@RequestMapping("/product")
public class ProductAction extends DefaultAction{
	
	private static final Logger logger = Logger.getLogger(ProductAction.class);
	
	@Resource 
	private ProductService productService;
	
	/**
	 * 跳转至产品管理页面
	 * @return
	 */
	@RequestMapping(value = "/index")
	public String index() {
		return "/product/product";
	}
	
	
	/**
	 * 新增或者更新产品
	 * @param product
	 */
	@RequestMapping(value="/save")
	@ResponseBody
	public Map<String, Object> saveProduct(Product product){
		try {
			String userid=getUserId();
			this.productService.saveProduct(product,userid);
		} catch (Exception e) {
			failure("维护产品失败！");
			logger.error("维护产品失败！"+e.getMessage());
			return getData();
		}
		return getData();
	}
	
	/**
	 * 产品列表查询
	 * @param request
	 * @param response
	 * @param dg
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/queryList", method = RequestMethod.POST)
	public void queryList(HttpServletResponse response,Product product,DataGrid dg) throws Exception {
		PageFinder page = this.productService.queryProductByPage(product, dg.getPage(), dg.getRows());
		response.getWriter().print(JSONObject.fromObject(page).toString());
	}
	
	/**
	 * 产品记录导出
	 * @throws Exception 
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/toExcel", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> toExcel(Product product,DataGrid dg) {
		PageFinder page = this.productService.queryProductByPage(product, dg.getPage(), dg.getRows());
		String json=JSONObject.fromObject(page).toString();
		JSONObject jsonObject = JSONObject.fromObject(json);
		try {
			ExcelUtil.createExcel("ProductList",jsonObject);
		} catch (Exception e) {
			failure("导出失败-"+e.getMessage());
			logger.error("导出失败-"+e.getMessage());
		}
		return getData();
	}
	
//	/**
//	 * 产品查询测试
//	 * @param request
//	 * @param response
//	 * @param dg
//	 * @throws Exception
//	 */
//	@RequestMapping(value = "/queryProduct", method = RequestMethod.POST)
//	public void queryProduct(HttpServletResponse response,@RequestParam(value="ProductID")String productID) throws Exception {
//		Product p = this.productService.queryProduct(productID);
//		
//		JSONObject json =new JSONObject();
//		json.put("id", p.getProductID());
//		json.put("text", p.getProductName());
//		
//		
//		response.getWriter().print(json.toString());
//	}
	
	/**
	 * 删除产品
	 * @param request
	 * @param response
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "/delete")
	@ResponseBody
	public Map<String, Object> delete(String ids) {
		try {
			this.productService.deleteProduct(ids);
		} catch (Exception e) {
			failure("删除产品失败-"+e.getMessage());
			logger.error("删除产品失败-"+e.getMessage());
		}
		return getData();
	}
	
}
