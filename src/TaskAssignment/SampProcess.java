package TaskAssignment;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import bean.VsamplingTask;
import zgbzhyjy.sampsystem.entity.QualChrt;
import zgbzhyjy.sampsystem.entity.QualChrtValue;
import zgbzhyjy.sampsystem.entity.ReturnSampPlan;
import zgbzhyjy.sampsystem.entity.SampTask;
import zgbzhyjy.sampsystem.service.InvokeWSService;
import zgbzhyjy.sampsystem.service.QualChrtService;
import zgbzhyjy.sampsystem.service.QualChrtValueService;
import zgbzhyjy.sampsystem.service.ReturnSampPlanService;
import zgbzhyjy.sampsystem.service.SampInspectedService;
import zgbzhyjy.sampsystem.service.SampTaskService;

/**
 * 处理从客户端接收的数据
 */
public class SampProcess {

	ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
	InvokeWSService invokeWSService = ctx.getBean(InvokeWSService.class);
	ReturnSampPlanService rsps = ctx.getBean(ReturnSampPlanService.class);
	SampInspectedService sis = ctx.getBean(SampInspectedService.class);
	SampTaskService sampTaskService=ctx.getBean(SampTaskService.class);
	QualChrtService qualChrtService = ctx.getBean(QualChrtService.class);
	QualChrtValueService qualChrtValueService = ctx.getBean(QualChrtValueService.class);

	/**
	 * 接收客户端的字符串，并进行终端提交数据的处理
	 * 
	 * @author LRover
	 * 
	 */
	public void handleResult(ArrayList<?> vSamples, String sampTaskID, String productID, String qualChrtID, int lotNo,
			int sampleNo, String samplingPlanID, String userID, String dataReceived) {
		// 1.获取质量特性值的信息
		QualChrtValue qu = queryQualChrtValue(qualChrtID);
		ReturnSampPlan rs = this.rsps.queryReturnSampPlan(sampTaskID, lotNo);
		// 2.对接收的结果字符串进行处理
		System.out.println(dataReceived);
		String ss[] = dataReceived.split("#");
		// 3.通过循环完成向终端提交检测结果
		for (int i=0,j = 0; i<vSamples.size()&j < ss.length; i++,j++) {
			String inspectionResultDetail = ss[j];
			String serialID=((VsamplingTask)(vSamples.get(i))).getSerialID();
			saveQualChrtResult(serialID, sampTaskID, productID, qualChrtID, lotNo, sampleNo, samplingPlanID, userID,
					inspectionResultDetail, qu, rs);
		}
	}

	/**
	 * 终端提交检测结果，提交一个产品的一个特性，如果有N个产品，则此特性的检测结果需提交N次
	 * 
	 * @author LRover
	 */
	public String saveQualChrtResult(String serialID, String taskID, String productID, String qualChrtID, int lotNo,
			int sampleNo, String samplingPlanID, String userID, String inspectionResultDetail,
			QualChrtValue qualChrtValue, ReturnSampPlan returnSampPlan) {

		int inspectionResult = 0;
		// 1.对结果进行判断
		// 合格为1，不合格为2
		if (inspectionResultDetail != null) {
			inspectionResult = CheckResult(inspectionResultDetail, qualChrtValue);

			// 2.将终端提交的检测结果和检测细节进行存储,并更改检测状态
			this.sis.updateInspectedResult(inspectionResultDetail, inspectionResult, serialID);
			
			// 3.检测数量增加 1,同步更新 接收 和 拒绝 数量
			this.rsps.updateInspectionCount(returnSampPlan, inspectionResult);
//			// 4.判断样本数是否已经达到样本量
			ReturnSampPlan rs = this.rsps.queryReturnSampPlanByid(returnSampPlan.getRSPid());
			if (rs.getSampleSize() == rs.getInspectedCount()) {
				// 如果相等，判定该批是否接收，并创建下一批的抽样方案
				System.out.println("判断是否接收");
				// 1.查找最近5批的数据
				String hql_rs_5 = "FROM ReturnSampPlan rs where ";
				hql_rs_5 += " rs.sampTask='" + rs.getSampTask().getSTid() + "'";
				hql_rs_5 += "order by lotNo desc";
				List<ReturnSampPlan> rs_list = this.rsps.queryListReturnSampPlan(hql_rs_5,5);

				// a.近5批的不合格数
				String refusedCount = "";
				// b.近5批的判定结果
				String InspectedResult = "";
				// c.累计拒收的数量
				int RefusedLotSum = 0;

				//拼接字符串
				for (int i = 0; i < rs_list.size(); i++) {
					if (i>0 || i<rs_list.size()-1) {
						refusedCount += rs_list.get(i).getRefuseCount() + ",";
						InspectedResult += rs_list.get(i).getInspectionResult() + ",";
					}else{
						refusedCount += rs_list.get(i).getRefuseCount();
						InspectedResult += rs_list.get(i).getInspectionResult();
					}
					if (rs_list.get(i).getInspectionResult() == 0) {
						RefusedLotSum += 1;
					}
				}
				// 调用WebService 进行计算
				String str = this.rsps.handleInspectionResule(rs, refusedCount, InspectedResult,
						RefusedLotSum);
				System.out.println("hello,webserice结束");
				
				String[] str_s = str.split(",");
				
				if (str_s.length == 1) { 
					// 数组的长度为1，暂停检验，拒收，检验结果为0
					rs.setInspectionResult(0);
					this.rsps.update(rs);
					System.out.println("拒绝接收");
				} else{
					// 下次要检验的批次
					int next_lotNo = Integer.parseInt(str_s[1]);
					int next_Stringency = (str_s.length == 5) ? Integer.parseInt(str_s[5]):rs.getSampTask().getSampScheme().getInitStringency();
					
					System.out.println(str_s[0]);
					
					if (str_s[0] == "0") { 
						// 无法得出结果，检验下一样本，此时数组长度为4,检验结果为拒收
						rs.setInspectionResult(0);
						this.rsps.update(rs);
						System.out.println("无法得出结果，继续");
					} else {
						rs.setInspectionResult(Integer.parseInt(str_s[4]));
						this.rsps.update(rs);
						
						System.out.println("修改sampTask的批号");
						//修改sampTask的批号
						
						SampTask st=rs.getSampTask();
						st.setLotNo(next_lotNo);
						this.sampTaskService.saveSampTask(st);
						
						//创建新的抽样方案
						this.rsps.handleReturnSamplan(st);
						
					}
				}
				
			} else if(rs.getSampleSize() < rs.getInspectedCount()){
				System.out.println("检测还没有结束，继续检测");
			}else{
				System.out.println("检测数量不相符，继续检验");
			}
		} else {
			System.out.println("检测结果为空");
		}
	 return "没完成";
}

	/**
	 * 通过 QualChrtID 得到 QualChrtvalue
	 * 
	 * @author LRover
	 * @since 2016.7.28
	 * @param QualChrtID
	 * @return QualChrtValue
	 */
	public QualChrtValue queryQualChrtValue(String QualChrtID) {
		QualChrt qualChrt = this.qualChrtService.getQualChrtByQualChrtID(QualChrtID);
		QualChrtValue qualChrtValue = this.qualChrtValueService.getQualChrtValueByqid(qualChrt.getQid());
		return qualChrtValue;
	}

	/**
	 * 对结果进行匹配
	 * 
	 * @author LRover
	 * @since 2016.7.28
	 */
	public int CheckResult(String InspectionResultDetail, QualChrtValue qualChrtValue) {
		boolean result = false;
		String value = qualChrtValue.getQualChrtValue();
		String symbol = qualChrtValue.getQualChrtValSymbol();

		if (symbol.equals("01")) {
			result = (Integer.parseInt(value) == Integer.parseInt(InspectionResultDetail));
		}
		if (symbol.equals("02")) {
			result = (Integer.parseInt(InspectionResultDetail) < Integer.parseInt(value));
		}
		if (symbol.equals("03")) {
			result = (Integer.parseInt(InspectionResultDetail) > Integer.parseInt(value));
		}
		if (symbol.equals("04")) {
			result = (Integer.parseInt(InspectionResultDetail) <= Integer.parseInt(value));
		}
		if (symbol.equals("05")) {
			result = (Integer.parseInt(InspectionResultDetail) >= Integer.parseInt(value));
		}

		if (result) {
			return 1; // 合格
		} else {
			return 2; // 不合格
		}
	}

	/**
	 * 
	 */

	// // 上传一终端所检验出的不合格数
	// public void updateRejectedCount(String taskID, String productID, String
	// qualChrtID, int count, int lotNo,
	// int sampleNo, String sampSchemeID) {
	// String queryFive = "SampTaskID='" + taskID + "' and ProductID='" +
	// productID + "' and QualChrtID='" + qualChrtID
	// + "' and LotNo=" + lotNo + " and SampleNo='" + sampleNo + "' and
	// SampSchemeID='" + sampSchemeID + "'";
	// String sql = "update SampInspectedNeeded set RefuseCount = " + count + "
	// where " + queryFive;
	// SQLHelper.ExecSql(sql);
	// }

	// // 获取该样本中被接受的样本数量
	// public int getAccpetedCount(String samplingPlanID, String productID,
	// String qualChrtID, int lotNo, int sampleNo,
	// String taskID) {
	// String queryWhere = "SampTaskID='" + taskID + "' and ProductID='" +
	// productID + "' and QualChrtID='"
	// + qualChrtID + "' and SampSchemeID='" + samplingPlanID + "' and LotNo=" +
	// lotNo + " and SampleNo="
	// + sampleNo;
	// int accCount = 0;
	// String sql = "select count(*) AcceptedCount, 1 as ID from
	// SampInspectedInfo where " + queryWhere
	// + " and InspectionResult=1";
	// System.out.println(sql);
	// try {
	// ResultSet rs = SQLHelper.getResultSet(sql);
	// while (rs.next()) {
	// accCount = Integer.parseInt(rs.getString("AcceptedCount"));
	// }
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return accCount;
	// }

	// // 获取该样本中被拒绝的样本数量
	// public int getRejectedCount(String sampSchemeID, String productID, String
	// qualChrtID, int lotNo, int sampleNo,
	// String taskID) {
	// String queryWhere = "SampTaskID='" + taskID + "' and ProductID='" +
	// productID + "' and QualChrtID='"
	// + qualChrtID + "' and SampSchemeID='" + sampSchemeID + "' and LotNo=" +
	// lotNo + " and SampleNo="
	// + sampleNo;
	// int rejCount = 0;
	// String sql = "select count(*) RejectedCount, 1 as ID from
	// SampInspectedInfo where " + queryWhere
	// + " and InspectionResult=0";
	// try {
	// ResultSet rs = SQLHelper.getResultSet(sql);
	// while (rs.next()) {
	// rejCount = Integer.parseInt(rs.getString("RejectedCount"));
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return rejCount;
	// }
	//
}
