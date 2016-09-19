package zgbzhyjy.sampsystem.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import zgbzhyjy.sampsystem.entity.ReturnSampPlan;
import zgbzhyjy.sampsystem.entity.SampInspected;
import zgbzhyjy.sampsystem.service.ReturnSampPlanService;
import zgbzhyjy.sampsystem.service.SampInspectedService;
import zgbzhyjy.sampsystem.util.DefaultAction;

@Controller
@RequestMapping(value = "/sampAnly")
public class SampAnlyAction extends DefaultAction {

	@Autowired
	private SampInspectedService sampInspectedService;

	@Autowired
	private ReturnSampPlanService returnSampPlanService;

	/**
	 * 跳转到样本分析页面
	 */
	@RequestMapping(value = "/index")
	public String index() {
		return "/anly/sampAnly";
	}

	/**
	 * 跳转到数据处理页面
	 */
	@RequestMapping(value = "/index_one")
	public String index_one() {
		return "/anly/sampAnly1";
	}

	@RequestMapping(value = "/queryJson")
	@ResponseBody
	public void query(@RequestParam(value = "ProductID") String productID,
			@RequestParam(value = "QualChrtID") String qualChrtID, @RequestParam(value = "ClientID") String clientID,
			HttpServletResponse response) throws Exception {
		System.out.println(productID + "" + qualChrtID + "" + clientID);
		SampInspected sa = new SampInspected();
		sa.setProductID(productID);
		sa.setQualChrtID(qualChrtID);
		sa.setClientID(clientID);

		List<SampInspected> list = this.sampInspectedService.querySampInspectedByPage(sa);

		// //图例
		// List<Map<String, Object>>
		// legendlist=this.sampInspectedService.getlegend(sa); //由产品名称和批次组成
		List<String> legend = new ArrayList<String>();
		// for (int i = 0; i < legendlist.size()-1; i++) {
		// String
		// st=legendlist.get(i).get("ProductName")+"-"+legendlist.get(i).get("QualChrtID")+"-"+legendlist.get(i).get("LotNo");
		// legend.add(st);
		// }
		//
		// 种类
		List<Integer> category = new ArrayList<Integer>();

		// 构建Series
		JSONArray series = new JSONArray();

		// 得到data
		List<String> datalist = new ArrayList<String>();
		legend.add(list.get(0).getProductName());

		for (int i = 0; i < list.size(); i++) {
			category.add(list.get(i).getInspectionSequence());
			datalist.add(list.get(i).getInspectionResultDetail());

			for (int j = 0; j < legend.size(); j++) {
				if (!list.get(i).getProductName().equals(legend.get(j))) {
					legend.add(list.get(i).getProductName());
				}
			}

			if (i < list.size() - 1 && list.get(i).getLotNo() != list.get(i + 1).getLotNo()) {
				JSONObject jso = new JSONObject();
				jso.put("name", list.get(i).getProductName());
				jso.put("type", "line");
				jso.put("data", datalist);
				series.add(jso);

				category.clear();
				datalist.clear();
			}
		}

		JSONObject json = new JSONObject();
		json.put("legend", legend);
		json.put("category", category);
		json.put("series", series);
		System.out.println(json.toString());
		response.getWriter().print(json.toString());

	}

	@RequestMapping(value = "/qualified")
	@ResponseBody
	public void qualified(@RequestParam(value = "ProductID") String productID,
			@RequestParam(value = "QualChrtID") String qualChrtID, @RequestParam(value = "ClientID") String clientID,
			HttpServletResponse response) throws Exception {
		System.out.println(productID + "" + qualChrtID + "" + clientID);

		SampInspected sa = new SampInspected();
		sa.setProductID(productID);
		sa.setQualChrtID(qualChrtID);
		sa.setClientID(clientID);

		List<SampInspected> list = this.sampInspectedService.querySampInspectedByPage(sa);

		String[] legend = { "合格", "不合格", "未检测" };

		// //种类
		// List<Integer> category=new ArrayList<Integer>();
		int[] categories = { 1, 2 };

		int length = list.get(list.size() - 1).getLotNo() + 1;
		int[] refuse = new int[length];
		int[] accept = new int[length];
		int[] ni = new int[length];

		int r = 0, a = 0, n = 0;
		for (int i = 0; i < list.size(); i++) {
			// 统计各个的值
			if (list.get(i).getInspectionResultDetail().equals("null")) {
				n += 1;
			} else {
				if (list.get(i).getInspectionResult().equals("2")) {
					r += 1;
				} else {
					a += 1;
				}
			}

			// 一批检测结束
			if (i < list.size() - 1 && list.get(i).getLotNo() != list.get(i + 1).getLotNo()) {
				int N = list.get(i).getLotNo();
				refuse[N] = r;
				accept[N] = a;
				ni[N] = n;
				r = 0;
				a = 0;
				n = 0;
			} else if (i == list.size() - 1) {
				int N = list.get(i).getLotNo();
				refuse[N] = r;
				accept[N] = a;
				ni[N] = n;
			}
		}

		// 构建Series
		JSONArray series = new JSONArray();
		// 合格
		JSONObject jso1 = new JSONObject();
		jso1.put("name", legend[0]);
		jso1.put("data", accept);

		// 不合格
		JSONObject jso2 = new JSONObject();
		jso2.put("name", legend[1]);
		jso2.put("data", refuse);

		// 未检测
		JSONObject jso3 = new JSONObject();
		jso3.put("name", legend[2]);
		jso3.put("data", ni);
		series.add(jso1);
		series.add(jso2);
		series.add(jso3);

		JSONObject json = new JSONObject();
		json.put("legend", legend);
		json.put("categories", categories);
		json.put("series", series);
		System.out.println(json.toString());
		response.getWriter().print(json.toString());

	}
}
