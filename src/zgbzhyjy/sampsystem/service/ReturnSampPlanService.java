package zgbzhyjy.sampsystem.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import zgbzhyjy.sampsystem.dao.ReturnSampPlanDao;
import zgbzhyjy.sampsystem.entity.ReturnSampPlan;
import zgbzhyjy.sampsystem.entity.SampTask;
import zgbzhyjy.sampsystem.util.PageFinder;

@Service
@Transactional
public class ReturnSampPlanService {

	@Autowired
	private SampInspectedService sampleInspectedService;

	@Autowired
	private InvokeWSService invokeWSService;

	@Autowired
	private ReturnSampPlanDao rspd;
	
	@Autowired
	private SampTaskService sampTaskService;

	/**
	 * 查询列表
	 * 
	 * @param ReturnSampPlan
	 */
	@SuppressWarnings("unchecked")
	public PageFinder<ReturnSampPlan> queryReturnSampPlanByPage(ReturnSampPlan returnSampPlan, int page, int rows) {
		String hql = "from ReturnSampPlan s where 1=1 ";
		return this.rspd.pagedByHQL(hql, page, rows);
	}
	
	
	/**
	 * 查询ReturnSampPlan
	 * @author LRover
	 * @param id
	 * */
	public ReturnSampPlan queryReturnSampPlanByid(String id){
		return this.rspd.get(id, ReturnSampPlan.class);
	}
	
	/**
	 * 查询ReturnSampPlan
	 * @author LRover
	 * @param sql
	 * */
	public List<ReturnSampPlan> queryListReturnSampPlan(String sql,int n){
		
		return this.rspd.getListByHQL(sql,n);
	}

	/**
	 * 查表得出匹配的抽样方案
	 * 
	 * @author LRover
	 * @param int initStringency,(int)lotSize,(int)il,(double)aql,(int)normalPlanType,(int)
	 *            tightenedPlanType,(int)reducedPlanType,(int)needReduced
	 */
	public void handleReturnSamplan(SampTask sampTask) {

		int LotSize=sampTask.getSampScheme().getLotSize();
		int IL=sampTask.getSampScheme().getIL();
		double AQL=sampTask.getSampScheme().getAQL();
		int NPlanType=sampTask.getSampScheme().getNPlanType();
		int TPlanType=sampTask.getSampScheme().getTPlanType();
		int RPlanType=sampTask.getSampScheme().getRPlanType();
		int needReduced=sampTask.getSampScheme().getNeedReduced();

		// WebService上面要求的参数类型为String型
		Object[] params = { LotSize + "", IL + "", AQL + "", NPlanType + "", TPlanType + "", RPlanType + "",
				needReduced + "" };
		String[] paramsName = { "s_lotSize", "s_il", "s_aql", "s_normalPlanType", "s_tightenedPlanType",
				"s_reducedPlanType", "s_needReduced" };
		
		
		String WebServiceURL = this.invokeWSService.getWS("TestSwitchScore").getWebServiceURL();
		String strSamplingPlan = this.invokeWSService.invokeWS(WebServiceURL, "CreateSamplingPlan", params, paramsName);
		System.out.println(strSamplingPlan);
		ReturnSampPlan rsp=saveStringRS(sampTask,strSamplingPlan);
				
		//为客服端创建流水号
		this.sampleInspectedService.SetserilID(rsp.getSampTask().getSampScheme().getSampSchemeID(),
				rsp.getSampTask().getSampScheme().getProduct().getProductID(),
				rsp.getSampTask().getSampScheme().getQualChrt().getQualChrtID(), rsp.getSampTask().getSampTaskID(),
				rsp.getSampTask().getClient().getClientID(), rsp.getLotNo(),rsp.getSampleSize(),
				rsp.getSampTask().getSampScheme().getProduct().getProductName(),
				rsp.getSampTask().getSampScheme().getQualChrt().getQualChrtName());
	
	}

	/**
	 * 将得到的抽样计划信息进行保存
	 */
//	public void saveReturnSampPlan(int LotSize, int InspectionLevel, double AQL, int NPlanType, int TPlanType,
//			int RPlanType, int needReduced, String SampSchemeID, int sampleNo, String SampTaskID, String ProductID,
//			String QualChrtID, int initStringency, int LotNo, String ClientID) {
//		// 获取抽样任务实体类
//		SampTask sampTask = new SampTask();
//		sampTask = this.sampTaskService.querySampTask(SampTaskID);
//
//		// WebService上面要求的参数类型为String型
//		Object[] params = { LotSize + "", InspectionLevel + "", AQL + "", NPlanType + "", TPlanType + "",
//				RPlanType + "", needReduced + "" };
//		String[] paramsName = { "s_lotSize", "s_il", "s_aql", "s_normalPlanType", "s_tightenedPlanType",
//				"s_reducedPlanType", "s_needReduced" };
//
//		String WebServiceURL = this.invokeWSService.getWS("TestSwitchScore").getWebServiceURL();
//		// 得到查表后的抽样计划
//		String strSamplingPlan = this.invokeWSService.invokeWS(WebServiceURL, "CreateSamplingPlan", params, paramsName);
//
//		System.out.println(strSamplingPlan);
//		// 将返回的信息（抽查的个数及相关信息）进行保存
//		String[] org = strSamplingPlan.split("\\|");
//
//		ReturnSampPlan rsp = new ReturnSampPlan();
//		// 根据参数进行赋值
//		rsp.setSampTask(sampTask);
//
//		rsp.setNeedReduced(Integer.parseInt(org[0]));
//		rsp.setSSMethod(Integer.parseInt(org[1]));
//		rsp.setACJ(Integer.parseInt(org[2]));
//
//		rsp.setNormalPlanType(Integer.parseInt(org[3]));
//		rsp.setNormalCharacterCode(org[4]);
//		rsp.setNormalSampleSize(org[5]);
//		rsp.setNormalAc(org[6]);
//		rsp.setNormalRe(org[7]);
//
//		rsp.setTightenedPlanType(Integer.parseInt(org[8]));
//		rsp.setTightenedCharacterCode(org[9]);
//		rsp.setTightenedSampleSize(org[10]);
//		rsp.setTightenedAc(org[11]);
//		rsp.setTightenedRe(org[12]);
//
//		rsp.setReducedPlanType(Integer.parseInt(org[13]));
//		rsp.setReducedCharacterCode(org[14]);
//		rsp.setReducedSampleSize(org[15]);
//		rsp.setReducedAc(org[16]);
//		rsp.setReducedRe(org[17]);
//
//		rsp.setLotNo(LotNo);
//		rsp.setSampleNo(sampleNo);
//
//		// 把返回的信息进行保存
//		this.rspd.save(rsp);
//
//		// 获取第一次检验的样本数,与sampleNo
//		int sampleSize = 0;
//		if (initStringency == 0) {
//			sampleSize = Integer.parseInt(org[5].split(",")[sampleNo]);
//		} else if (initStringency == 1) {
//			sampleSize = Integer.parseInt(org[10].split(",")[sampleNo]);
//		} else {
//			sampleSize = Integer.parseInt(org[15].split(",")[sampleNo]);
//		}
//
//		// 将检验计划信息进行整合保存
//		insertSampInspectedNeeded(rsp.getRSPid(), rsp.getSampTask(), sampleNo, sampleSize, LotNo);
//		System.out.println("开始计算流水号");
//		// 计算流水号，并根据客户端，独立进行保存
//		System.out.println(rsp.getSampTask().getSampScheme().getSampSchemeID() + "-"
//				+ rsp.getSampTask().getSampScheme().getProduct().getProductID() + "-"
//				+ rsp.getSampTask().getSampScheme().getQualChrt().getQualChrtID() + "-"
//				+ rsp.getSampTask().getSampTaskID() + "-" + rsp.getSampTask().getClient().getClientID() + "-" + LotNo
//				+ "-" + sampleNo + "-" + sampleSize);
//		this.sampleInspectedService.SetserilID(rsp.getSampTask().getSampScheme().getSampSchemeID(),
//				rsp.getSampTask().getSampScheme().getProduct().getProductID(),
//				rsp.getSampTask().getSampScheme().getQualChrt().getQualChrtID(), rsp.getSampTask().getSampTaskID(),
//				rsp.getSampTask().getClient().getClientID(), LotNo, sampleNo, sampleSize,
//				rsp.getSampTask().getSampScheme().getProduct().getProductName(),
//				rsp.getSampTask().getSampScheme().getQualChrt().getQualChrtName());
//	}

//	/**
//	 * 将抽样计划信息进行保存
//	 * 
//	 */
//	public void insertSampInspectedNeeded(String rspId, SampTask sampTask, int sampleNo, int sampleSize, int lotNo) {
//		SampInspectedNeed sin = new SampInspectedNeed();
//		sin.setRSPid(rspId);
//		sin.setClientID(sampTask.getClient().getClientID());
//		sin.setProductID(sampTask.getSampScheme().getProduct().getProductID());
//		sin.setProductName(sampTask.getSampScheme().getProduct().getProductName());
//		sin.setQualChrtID(sampTask.getSampScheme().getQualChrt().getQualChrtID());
//		sin.setQualChrtName(sampTask.getSampScheme().getQualChrt().getQualChrtName());
//		sin.setSampSchemeID(sampTask.getSampScheme().getSampSchemeID());
//		sin.setSampTaskID(sampTask.getSampTaskID());
//		sin.setSampleNo(sampleNo);
//		sin.setLotNo(lotNo);
//		sin.setSampleCount(sampleSize);
//		this.sinsd.save(sin);
//	}
	
	/**
	 * 将 WebService 返回的字符串 存储到 ReturnSampPlan 类
	 * @param sampTask,String
	 * */
	public ReturnSampPlan saveStringRS(SampTask sampTask,String strSamplingPlan){
		
		String[] org = strSamplingPlan.split("\\|");
		
		ReturnSampPlan rsp = new ReturnSampPlan();
		rsp.setSampTask(sampTask);
		rsp.setLotNo(sampTask.getLotNo());
		
		rsp.setNeedReduced(Integer.parseInt(org[0]));
		rsp.setSSMethod(Integer.parseInt(org[1]));
		rsp.setACJ(Integer.parseInt(org[2]));

		rsp.setNormalPlanType(Integer.parseInt(org[3]));
		rsp.setNormalCharacterCode(org[4]);
		rsp.setNormalSampleSize(org[5]);
		rsp.setNormalAc(org[6]);
		rsp.setNormalRe(org[7]);

		rsp.setTightenedPlanType(Integer.parseInt(org[8]));
		rsp.setTightenedCharacterCode(org[9]);
		rsp.setTightenedSampleSize(org[10]);
		rsp.setTightenedAc(org[11]);
		rsp.setTightenedRe(org[12]);

		rsp.setReducedPlanType(Integer.parseInt(org[13]));
		rsp.setReducedCharacterCode(org[14]);
		rsp.setReducedSampleSize(org[15]);
		rsp.setReducedAc(org[16]);
		rsp.setReducedRe(org[17]);
		
//		修改抽样值的默认值
		rsp.setInspectionResult(-1);
		
		int initStringency=sampTask.getSampScheme().getInitStringency();
		if (initStringency == 0) {
			rsp.setSampleSize(Integer.parseInt(org[5].split(",")[0]));
		} else if (initStringency == 1) {
			rsp.setSampleSize(Integer.parseInt(org[10].split(",")[0]));
		} else {
			rsp.setSampleSize(Integer.parseInt(org[15].split(",")[0]));
		}
		
		this.rspd.save(rsp);
		return rsp;
	}
	/**
	 * 根据抽样任务和批号得到匹配的抽样方案
	 * @author LRover
	 * @param sampTaskID,lotNo
	 * 
	 * */
	public ReturnSampPlan queryReturnSampPlan(String sampTaskID,int lotNo){
		SampTask st=this.sampTaskService.queryBySamTaskID(sampTaskID);
		
		String hql = "from ReturnSampPlan rs  where 1=1";
		hql += " and rs.sampTask = '" + st.getSTid()+"'" ;
		hql += " and rs.lotNo='"+lotNo+"'";
		return this.rspd.getByHQL(hql);
	}
	
	/**
	 * 更新ReturnSampPlan信息
	 * @author LRover
	 * @param
	 * @return
	 * */
	public void update(ReturnSampPlan rs){
		this.rspd.update(rs);
	}
	
	/**
	 * 更新检测的数量
	 * @author LRover
	 * @param
	 * @return
	 * */
	public void updateInspectionCount(ReturnSampPlan rs,int inspectionResult){
		rs.setInspectedCount(rs.getInspectedCount()+1);
		
		if(inspectionResult==1){
			rs.setAcceptCount(rs.getAcceptCount()+1);
		}else{
			rs.setRefuseCount(rs.getRefuseCount()+1);
		}
		this.rspd.update(rs);
	}
	
	/**
	 * 调用WebService完成检验结果的处理
	 * @author LRover
	 * 
	 * @param	returnSampPlan 
	 * @param	refusedCount	近5批的不合格数
	 * @param	InspectedResult	近5批的判定结果
	 * @param 	RefusedLotSum 累计的拒收批数
	 * 
	 * */
	public String handleInspectionResule(ReturnSampPlan rs,String refusedCount,String InspectedResult,int RefusedLotSum){
		
		String[] paramsName = { "s_lotNo", "s_sampleNo", "s_currStringency", "s_i_needReduced",
				"s_i_ssMethod", "s_switchScore", "s_acj", "s_normalPlanType", "normalCharacterCode",
				"normalSampleSize", "normalAc", "normalRe", "s_tightenedPlanType", "tightenedCharacterCode",
				"tightenedSampleSize", "tightenedAc", "tightenedRe", "s_reducedPlanType",
				"reducedCharacterCode", "reducedSampleSize", "reducedAc", "reducedRe", "s_nonconformities",
				"s_acceptLot", "s_reLotCumNum" };
		
		int currStringency=rs.getSampTask().getSampScheme().getInitStringency();
		Object[] params = { Integer.toString(rs.getLotNo()),Integer.toString(rs.getSampleSize()),Integer.toString(currStringency),Integer.toString(rs.getNeedReduced()),
				Integer.toString(rs.getSSMethod()),Integer.toString(rs.getSwitchScore()),Integer.toString(rs.getACJ()),Integer.toString(rs.getNormalPlanType()),rs.getNormalCharacterCode(),
				rs.getNormalSampleSize(),rs.getNormalAc(),rs.getNormalRe(),Integer.toString(rs.getTightenedPlanType()),rs.getTightenedCharacterCode(),
				rs.getTightenedSampleSize(),rs.getTightenedAc(),rs.getTightenedRe(),Integer.toString(rs.getReducedPlanType()),
				rs.getReducedCharacterCode(),rs.getReducedSampleSize(),rs.getReducedAc(),rs.getReducedRe(),refusedCount,
				InspectedResult,Integer.toString(RefusedLotSum)};
		
		String WebServiceURL = this.invokeWSService.getWS("TestSwitchScore").getWebServiceURL();
		String str = this.invokeWSService.invokeWS(WebServiceURL, "ProcessInspectionResult", params, paramsName);
		return str;
				
	}
	
}
