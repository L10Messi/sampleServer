package zgbzhyjy.sampsystem.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import zgbzhyjy.sampsystem.dao.SampInspectedDao;
import zgbzhyjy.sampsystem.entity.SampInspected;
import zgbzhyjy.sampsystem.util.DateUtil;
import zgbzhyjy.sampsystem.util.PageFinder;
import zgbzhyjy.sampsystem.util.StringUtil;

@Service
@Transactional
public class SampInspectedService {

	@Autowired
	private SampInspectedDao sampInspectedDao;

	/**
	 * 查询样本检测记录列表
	 * @param sampInspected,page,rows
	 * @return pageFinder
	 */
	@SuppressWarnings("unchecked")
	public PageFinder<SampInspected> querySampInspectedByPage(SampInspected sampInspected, int page, int rows) {
		String hql = "from SampInspected s where 1=1 ";
		if (StringUtils.isNotBlank(sampInspected.getProductID())) {
			hql += " and s.productID like '%" + sampInspected.getProductID() + "%'";
		}
		if (StringUtils.isNotBlank(sampInspected.getQualChrtID())) {
			hql += " and s.qualChrtID like '%" + sampInspected.getQualChrtID() + "%'";
		}
		if (StringUtils.isNotBlank(sampInspected.getClientID())) {
			hql += " and s.clientID like '%" + sampInspected.getClientID() + "%'";
		}
		hql += " order by s.sampSchemeID,s.lotNo,s.inspectionSequence ";
		return this.sampInspectedDao.pagedByHQL(hql, page, rows);
	}
	
	/**
	 * 查询样本检测记录列表
	 * @param SampInspected
	 * @return List
	 */
	public List<SampInspected> querySampInspectedByPage(SampInspected sampInspected) {
		String hql = "from SampInspected s where 1=1 ";
		if (StringUtils.isNotBlank(sampInspected.getProductID())) {
			hql += " and s.productID like '%" + sampInspected.getProductID() + "%'";
		}
		if (StringUtils.isNotBlank(sampInspected.getQualChrtID())) {
			hql += " and s.qualChrtID like '%" + sampInspected.getQualChrtID() + "%'";
		}
		if (StringUtils.isNotBlank(sampInspected.getClientID())) {
			hql += " and s.clientID like '%" + sampInspected.getClientID() + "%'";
		}
		//hql += " and s.lotNo like '%" + sampInspected.getLotNo() + "%'";
		hql += " order by s.sampSchemeID,s.lotNo,s.inspectionSequence ";
		
		return this.sampInspectedDao.getListByHQL(hql);
	}

	/**
	 * 查询样本检测记录列表的商品名
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getProductName() {
		// 通过左连接查询得到productID和productName
		String sql = "SELECT distinct s.ProductID,p.ProductName,p.pid FROM SampInspectedInfo s left join ProductInfo p on p.ProductID=s.ProductID";
		return this.sampInspectedDao.getMapBySQL(sql);
	}

	/**
	 * 查询样本检测记录列表的质量特性名
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getQualChrtName(String ProductID) {
		// 现在查询的还是ID

		// String sql = "Select distinct(QualChrtID) from SampInspectedInfo s ";
		// sql += " where s.ProductID='"+ProductID+"'";
		// ResultSet rs=SQLHelper.getResultSet(sql);
		// return rs;
		// 利用左连接查询QualChrtID和QualChrtName
		String sql = "SELECT distinct s.QualChrtID,q.QualChrtName FROM SampInspectedInfo s left join QualChrtInfo q on q.QualChrtID=s.QualChrtID";
		if (StringUtil.isNotBlank(ProductID)) {
			sql += " where s.productID='"+ProductID+"'";
		}
//		return this.sampInspectedDao.getMapBySQL(sql);
//		return this.sampInspectedDao.getListByHQL(sql);
		return this.sampInspectedDao.getMapBySQL(sql);
	}
	
	
	/**
	 * 查询客户端记录
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getClientID(String QualChrtID) {
		// 通过左连接查询得到productID和productName
		String sql = "SELECT ClientID FROM SampInspectedInfo ";
		if (StringUtil.isNotBlank(QualChrtID)) {
			sql += " where QualChrtID='" +QualChrtID +"'";
		}
		sql +="group by ClientID";
		return this.sampInspectedDao.getMapBySQL(sql);
	}

	/**
	 * 生成样本的流水号,并存储每个客户端要检测的任务
	 */
	public void SetserilID(String sampSchemeID, String productID, String qualChrtID, String sampTaskID, String clientID,
			int LotNo,int sampleSize,String productName,String qualChrtName) {// LotNo是批次；sampleNo是样本号；sampleSize是样本数

		String[] clients = clientID.split(",");
		// 传入客户端id数组和抽样计划类（可以根据抽样计划ID判断如果存在就不添加），还有批次号
		String SerialID = "";

		// 查询符合条件的数量，如果存在的话就不再添加。
		String countExist = "from SampInspected si where 1=1 ";
		countExist += "and si.sampSchemeID = '" + sampSchemeID + "' ";
		countExist += "and si.productID = '" + productID + "' ";
		countExist += "and si.qualChrtID = '" + qualChrtID + "' ";
		countExist += "and si.lotNo = '" + LotNo + "' ";

		System.out.println(countExist);
		long returnCount = sampInspectedDao.countByHql(countExist);

		int rowCount = 0;

		rowCount = (int) returnCount;
		
		if (rowCount == 0) {
			String date=DateUtil.getMachingCurrentTime("yyMMddHHmmss");
			for (int i = 0; i < sampleSize; i = i + clients.length) {
				// 给每个要检测的质量特性生成一个流水号serialID
				for (int j = 0; j < clients.length; j++) {
					SerialID = productID + "|" + qualChrtID + "|" + date + "|"
							+ (String.format("%03d", i + j));

					SampInspected si = new SampInspected();
					si.setSerialID(SerialID);
					si.setProductID(productID);
					
					si.setProductName(productName);
					si.setQualChrtName(qualChrtName);
					
					si.setSampSchemeID(sampSchemeID);
					si.setQualChrtID(qualChrtID);
					si.setSampTaskID(sampTaskID);
					si.setClientID(clients[j]);

					si.setInspectionStatus(0);// 测试中:0；测试结束：1
					si.setInspectionSequence(i + j);
					si.setInspectionResult("0");

					si.setLotNo(LotNo);
					si.setSampleNo(i + j);// 样本号的概念和检测序列是一样的

					si.setInspectionResultDetail("null");
					this.sampInspectedDao.save(si);
					System.out.println("流水号生成完成");
				}
			}
		}
	}

	/**
	 * @author LRover
	 * @since 2016-7-28
	 * 更新 数据和检测结果信息
	 * @param InspectionResultDetail,QualChrtID
	 * */
	public void updateInspectedResult(String inspectionResultDetail,int inspectionResult,String serialID){
		String sqlUpdChrt = "update SampInspectedInfo set InspectionResultDetail='" + inspectionResultDetail
				+ "', InspectionResult = '" + inspectionResult + "',InspectionStatus=1  where serialID='" + serialID
				+ "'";
		this.sampInspectedDao.executeSql(sqlUpdChrt);
	}
	
	
	/**
	 * @author LRover
	 * @since 2016-8-15 
	 * 查询图例 商品+批次，在生成图例的时候使用
	 * @param InspectionResultDetail,QualChrtID
	 * */
	public List<Map<String, Object>> getlegend(SampInspected sampInspected){
		
		String sql = "SELECT distinct s.ProductID,s.ProductName,s.QualChrtID,s.LotNo FROM SampInspectedInfo s where 1=1 ";
		
		if (StringUtils.isNotBlank(sampInspected.getProductID())) {
			sql += " and s.ProductID ='" + sampInspected.getProductID() + "'";
		}
		if (StringUtils.isNotBlank(sampInspected.getQualChrtID())) {
			sql += " and s.QualChrtID ='" + sampInspected.getQualChrtID() + "'";
		}
		if (StringUtils.isNotBlank(sampInspected.getClientID())) {
			sql += " and s.ClientID = '" + sampInspected.getClientID() + "'";
		}
		return this.sampInspectedDao.getMapBySQL(sql);
		
	}
	

}
