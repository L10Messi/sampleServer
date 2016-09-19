package TaskAssignment;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import zgbzhyjy.sampsystem.service.InvokeWSService;
import zgbzhyjy.sampsystem.service.ReturnSampPlanService;
import zgbzhyjy.sampsystem.service.SampInspectedService;
import zgbzhyjy.sampsystem.util.SQLHelper;

public class SamplingProcess {

	ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
	InvokeWSService invokeWSService = ctx.getBean(InvokeWSService.class);
	ReturnSampPlanService rsps = ctx.getBean(ReturnSampPlanService.class);
	SampInspectedService sis=ctx.getBean(SampInspectedService.class);

	// 给客户端传送需检验的质量特性信息(WQ)
	public ResultSet sendInspectionInfoByClient(String clientID) {
		String sql = "Select * from vsamplingtask  where";
		sql += " InspectedBy='" + clientID + "'";
		sql += "and InspectionStatus = 0 order by SampleNo ";
		ResultSet rs = SQLHelper.getResultSet(sql);

		return rs;
	}

	// 获取服务器端的质量特性信息，并进行比较，返回匹配结果
	public int CheckResult(String InspectionResultDetail, String QualChrtID) {
		boolean result = false;
		// 获取数据库里的质量特征信息
		String sql = "Select qv.QualChrtValue,qv.QualChrtValSymbol from QualChrtValueInfo qv,QualChrtInfo q where q.QualChrtID='"
				+ QualChrtID + "' and qv.qid=q.qid";
		sql += " and qv.isDel=0 and q.isDel=0";
		System.out.println(sql);
		ResultSet rs = SQLHelper.getResultSet(sql);
		try {
			while (rs.next()) {
				String qualChrtValue = rs.getString("QualChrtValue");
				String value = rs.getString("QualChrtValSymbol");

				if (value.equals("01")) {
					result = (Integer.parseInt(qualChrtValue) == Integer.parseInt(InspectionResultDetail));
				}
				if (value.equals("02")) {
					result = (Integer.parseInt(InspectionResultDetail) < Integer.parseInt(qualChrtValue));
				}
				if (value.equals("03")) {
					result = (Integer.parseInt(InspectionResultDetail) > Integer.parseInt(qualChrtValue));
				}
				if (value.equals("04")) {
					result = (Integer.parseInt(InspectionResultDetail) <= Integer.parseInt(qualChrtValue));
				}
				if (value.equals("05")) {
					result = (Integer.parseInt(InspectionResultDetail) >= Integer.parseInt(qualChrtValue));
				}
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (result) {
			return 1;
		} else {
			return 2;
		}
	}

	// 上传一终端所检验出的不合格数
	public void updateRejectedCount(String taskID, String productID, String qualChrtID, int count, int lotNo,
			int sampleNo, String sampSchemeID) {
		String queryFive = "SampTaskID='" + taskID + "' and ProductID='" + productID + "' and QualChrtID='" + qualChrtID
				+ "' and LotNo=" + lotNo + " and SampleNo='" + sampleNo + "' and SampSchemeID='" + sampSchemeID + "'";
		String sql = "update SampInspectedNeeded set RefuseCount = " + count + " where " + queryFive;
		SQLHelper.ExecSql(sql);
	}

	/**
	 * 终端提交检测结果，提交一个产品的一个特性，如果有N个产品，则此特性的检测结果需提交N次
	 */
	public String saveQualChrtResult(String serialID, String taskID, String productID, String qualChrtID, int lotNo,
			int sampleNo, String samplingPlanID, String userID, String inspectionResultDetail) {

		System.out.println(serialID + "-" + taskID + "-" + "-" + productID + "-" + qualChrtID + "-" + lotNo + "-"
				+ sampleNo + "-" + samplingPlanID + "-" + userID + "-" + inspectionResultDetail);

		// 1.对结果进行判断
		// 合格为1，不合格为0
		int inspectionResult = CheckResult(inspectionResultDetail, qualChrtID);

		
		// 2.将终端提交的检测结果和检测细节进行存储,并更改检测状态
		this.sis.updateInspectedResult(inspectionResultDetail, inspectionResult, serialID);
//		String sqlUpdChrt = "update SampInspectedInfo set InspectionResultDetail='" + inspectionResultDetail
//				+ "', InspectionResult = '" + inspectionResult + "',InspectionStatus=1  where serialID='" + serialID
//				+ "'"; // status =2该特性检测完毕
//		System.out.println(sqlUpdChrt);
//		SQLHelper.ExecSql(sqlUpdChrt);

		String queryWhere = "sin.SampTaskID='" + taskID + "' and sin.ProductID='" + productID + "' and sin.QualChrtID='"
				+ qualChrtID + "' and sin.SampSchemeID='" + samplingPlanID + "' and sin.LotNo=" + lotNo
				+ " and sampleNo=" + sampleNo;
		String queryFive = "SampTaskID='" + taskID + "' and ProductID='" + productID + "' and QualChrtID='" + qualChrtID
				+ "' and SampSchemeID='" + samplingPlanID + "' and lotNo=" + lotNo;

		// 更新SampInspectedNeed中的检测数量
//		this.sins.updateInspectionCount(taskID, samplingPlanID, sampleNo);

		// 更新已拒绝的样本数，20151219需求更改
//		if (inspectionResult == 0) {
//			this.sins.updateRefuseCount(taskID, samplingPlanID, sampleNo);
//		}

		String sqlGetQual = "select qn.*, qp.* from SampInspectedNeeded qn left join ReturnedSamplingPlan qp on";
		sqlGetQual += " qn.RSPid=qp.RSPid where ";
		sqlGetQual += " qn.SampTaskID='" + taskID + "' and qn.ProductID='" + productID + "' and qn.QualChrtID='"
				+ qualChrtID + "' and qn.SampSchemeID='" + samplingPlanID + "' and qn.LotNo=" + lotNo
				+ " and qn.SampleNo=" + sampleNo;
		try {
			// 得到近N(0<=N<=5)次的检验结果
			// String sqllatestfiveResult = "select InspectionResult from
			// SampInspectedNeeded where " + queryFive ;
			// sqllatestfiveResult += " order by SampleNo asc limit
			// 5";SampInspectedInfo
			String sqllatestfiveResult = "select InspectionResult from SampInspectedNeeded where " + queryFive;
			sqllatestfiveResult += " order by SampleNo asc limit 5";

			String s_acceptLot = "";
			ResultSet rsFive = SQLHelper.getResultSet(sqllatestfiveResult);
			int rejectedCount = 0;
			int acceptedCount = 0;
			while (rsFive.next()) {
				// 计算近几次的接收次数和拒绝次数
				s_acceptLot += rsFive.getString("InspectionResult") + ","; // 1-接收，0-拒收
				if (rsFive.getString("InspectionResult") == "0") {
					rejectedCount++;
				}
				if (rsFive.getString("InspectionResult") == "1")
					acceptedCount++;
			}
			s_acceptLot = (s_acceptLot.length() == 0) ? "" : s_acceptLot.substring(0, s_acceptLot.length() - 1);
			System.out.println("大boss" + sqlGetQual);
			ResultSet rs = SQLHelper.getResultSet(sqlGetQual);
			System.out.println(sqlGetQual);
			while (rs.next()) {
				// 得出该质量特性合格与不合格的数量
				int sampleSize = Integer.parseInt(rs.getString("SampleCount"));
				int inspectedCount = (rs.getString("InspectedCount").isEmpty()) ? 0
						: Integer.parseInt(rs.getString("InspectedCount"));
				System.out.println("SampleSize为：" + sampleSize + "----" + "inspectedCount为：" + inspectedCount);
				if (sampleSize == inspectedCount) {
					// 该特性检测完毕，执行跳转
					Object[] params = { Integer.toString(lotNo), Integer.toString(sampleNo),
							rs.getString("initStringency"), rs.getString("NeedReduced"), rs.getString("SSMethod"),
							rs.getString("SwitchScore"), rs.getString("ACJ"), // ACJ
							rs.getString("NormalPlanType"), rs.getString("NormalCharacterCode"),
							rs.getString("NormalSampleSize"), rs.getString("NormalAC"), rs.getString("NormalRe"),
							rs.getString("TightenedPlanType"), rs.getString("TightenedCharacterCode"),
							rs.getString("TightenedSampleSize"), rs.getString("TightenedAC"),
							rs.getString("TightenedRe"), rs.getString("ReducedPlanType"),
							rs.getString("ReducedCharacterCode"), rs.getString("ReducedSampleSize"),
							rs.getString("ReducedAC"), rs.getString("ReducedRe"), Integer.toString(rejectedCount),
							(s_acceptLot.equals("null") || s_acceptLot.equals("")) ? "1" : s_acceptLot, // 近5次判定结果
							Integer.toString(rejectedCount)// 累计拒收的批数
					};
					String[] paramsName = { "s_lotNo", "s_sampleNo", "s_currStringency", "s_i_needReduced",
							"s_i_ssMethod", "s_switchScore", "s_acj", "s_normalPlanType", "normalCharacterCode",
							"normalSampleSize", "normalAc", "normalRe", "s_tightenedPlanType", "tightenedCharacterCode",
							"tightenedSampleSize", "tightenedAc", "tightenedRe", "s_reducedPlanType",
							"reducedCharacterCode", "reducedSampleSize", "reducedAc", "reducedRe", "s_nonconformities",
							"s_acceptLot", "s_reLotCumNum" };
					for (Object o : params) {
						System.out.print(String.valueOf(o) + "--");
					}

					System.out.println("00000000000");

					// 根据WebService计算转移计算得分方法
					// String WebServiceURL =
					// invokeWSService.getWS("TestSwitchScore").getWebServiceURL();
					// String retVal =
					// invokeWSService.invokeWSScore(WebServiceURL,"ProcessInspectionResult",params,paramsName);
					String retVal = "1,1,0,10,1,0";
					String[] vals = retVal.split(",");
					String sqlUpd = null;
					if (vals.length == 1) { // 数组的长度为1，暂停检验，拒收，检验结果为0
						sqlUpd = "udpate SampInspectedNeeded sin set sin.InspectionResult=0 where " + queryWhere;
						SQLHelper.ExecSql(sqlUpd);
						System.out.println("拒绝接收");
					} else {
						int nextSampleNo = sampleNo;
						int nextStringency = (vals.length == 5) ? Integer.parseInt(vals[5])
								: Integer.parseInt(rs.getString("initStringency"));
						if (vals[0] == "0") { // 无法得出结果，检验下一样本，此时数组长度为4,检验结果为拒收
							sqlUpd = "update SampInspectedNeeded sin set sin.InspectionResult=0 where " + queryWhere;
							SQLHelper.ExecSql(sqlUpd);
						} else {
							sqlUpd = "update SampInspectedNeeded sin set sin.InspectionResult=" + vals[4] + " where "
									+ queryWhere;
							nextSampleNo += 1;
							System.out.println(sqlUpd);
							SQLHelper.ExecSql(sqlUpd);
						}

						int totalSampleNo = sampleNo; // 该有几次检验
						if (rs.getString("initStringency").equals("0")) { // 当前为正常检验，判断是一次、二次、多次、全检验
							System.out.println("我是被逼的");
							if (rs.getString("NormalPlanType").equals("1")) {
								totalSampleNo = 2;
							} else if (rs.getString("NormalPlanType").equals("2")
									|| rs.getString("NormalPlanType").equals("3")) {
								totalSampleNo = 5;
							}
						} else if (rs.getString("initStringency").equals("1")) { // 当前为加严检验，判断是一次、二次、多次、全检验
							if (rs.getString("TightenedPlanType").equals("1")) {
								totalSampleNo = 2;
							} else if (rs.getString("TightenedPlanType").equals("2")
									|| rs.getString("TightenedPlanType").equals("3")) {
								totalSampleNo = 5;
							}
						} else {
							if (rs.getString("ReducedPlanType").equals("1")) {
								totalSampleNo = 2;
							} else if (rs.getString("ReducedPlanType").equals("2")
									|| rs.getString("ReducedPlanType").equals("3")) {
								totalSampleNo = 5;
							}
						}
						System.out.println(sampleNo);
						System.out.println(totalSampleNo);
						if (sampleNo < totalSampleNo) {
							// sampleNo +=1;
							int nextSampleSize = Integer.parseInt(vals[3]);// 下一批次的样本量
							nextSampleNo = Integer.parseInt(vals[2]);
							int nextLotNo = Integer.parseInt(vals[1]);
							System.out.println(nextSampleNo);
							// rsps.saveReturnSampPlan(Integer.parseInt(rs.getString("LotSize")),
							// Integer.parseInt(rs.getString("InspectionLevel")),
							// Double.parseDouble(rs.getString("AQL")),
							// Integer.parseInt(rs.getString("NormalPlanType")),
							// Integer.parseInt(rs.getString("tightenedPlanType")),
							// Integer.parseInt(rs.getString("reducedPlanType")),
							// Integer.parseInt(rs.getString("needReduced")),
							// rs.getString("SamplingPlanID"),
							// nextSampleNo, taskID, productID, qualChrtID,
							// nextStringency, nextLotNo,
							// rs.getString("InspectedBy"));
							// rsps.saveReturnSampPlan(rs.getInt("LotSize"),
							// rs.getInt("InspectionLevel"),
							// Double.parseDouble(rs.getString("AQL")),
							// rs.getInt("NormalPlanType"),
							// rs.getInt("TightenedPlanType"),
							// rs.getInt("ReducedPlanType"),
							// rs.getInt("NeedReduced"), samplingPlanID,
							// nextSampleNo, taskID, productID, qualChrtID,
							// nextStringency, nextLotNo,
							// rs.getString("ClientID"));
							System.out.println("已经重新创建抽样计划表");
							return Integer.toString(nextSampleNo);
						} else {
							System.out.println("检测结束");
						}
					}
				} else {
					System.out.println("检测样本数量不匹配");
					return "没完成";
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "没完成";
	}

	// 获取该样本中被接受的样本数量
	public int getAccpetedCount(String samplingPlanID, String productID, String qualChrtID, int lotNo, int sampleNo,
			String taskID) {
		String queryWhere = "SampTaskID='" + taskID + "' and ProductID='" + productID + "' and QualChrtID='"
				+ qualChrtID + "' and SampSchemeID='" + samplingPlanID + "' and LotNo=" + lotNo + " and SampleNo="
				+ sampleNo;
		int accCount = 0;
		String sql = "select count(*) AcceptedCount, 1 as ID from SampInspectedInfo where " + queryWhere
				+ " and InspectionResult=1";
		System.out.println(sql);
		try {
			ResultSet rs = SQLHelper.getResultSet(sql);
			while (rs.next()) {
				accCount = Integer.parseInt(rs.getString("AcceptedCount"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return accCount;
	}

	// 获取该样本中被拒绝的样本数量
	public int getRejectedCount(String sampSchemeID, String productID, String qualChrtID, int lotNo, int sampleNo,
			String taskID) {
		String queryWhere = "SampTaskID='" + taskID + "' and ProductID='" + productID + "' and QualChrtID='"
				+ qualChrtID + "' and SampSchemeID='" + sampSchemeID + "' and LotNo=" + lotNo + " and SampleNo="
				+ sampleNo;
		int rejCount = 0;
		String sql = "select count(*) RejectedCount, 1 as ID from SampInspectedInfo where " + queryWhere
				+ " and InspectionResult=0";
		try {
			ResultSet rs = SQLHelper.getResultSet(sql);
			while (rs.next()) {
				rejCount = Integer.parseInt(rs.getString("RejectedCount"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rejCount;
	}

}
