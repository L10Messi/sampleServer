package zgbzhyjy.sampsystem.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import zgbzhyjy.sampsystem.dao.QualChrtDao;
import zgbzhyjy.sampsystem.dao.SampSchemeDao;
import zgbzhyjy.sampsystem.entity.Product;
import zgbzhyjy.sampsystem.entity.QualChrt;
import zgbzhyjy.sampsystem.util.PageFinder;
import zgbzhyjy.sampsystem.util.StringUtil;

@Service
@Transactional
public class QualChrtService {
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private QualChrtDao qualChrtDao;
	
	@Autowired
	private SampSchemeDao sampSchemeDao;
	
	
	/**
	 * 编辑质量特性
	 * @author LRover
	 */
	public void saveQualChrt(QualChrt qualChrt){
		if (StringUtil.isNotBlank(qualChrt.getQid())) {
			this.qualChrtDao.update(qualChrt);
		}else{
			String id=CalQualChrtID(qualChrt.getProduct().getPid());		
			qualChrt.setQualChrtID(id);
			this.qualChrtDao.save(qualChrt);
			}
	}
	
	/**
	 * 质量特性ID自动生成
	 * 		根据产品Id计算质量特性ID
	 * @author LRover
	 * @param productid
	 */
	public String CalQualChrtID(String id){		
		Product product=new Product();
		product=this.productService.queryProduct(id);
		
		//质量特性ID实现自动生成
		String hql = "from QualChrt q where 1=1";
		hql += "and q.product = '" +id +"' ";
		List<QualChrt> qualChrt=this.qualChrtDao.getListByHQL(hql);
		long len=qualChrt.size();
		String qualChrtID = String.format("%s%03d", product.getProductID(), len+1);
		return qualChrtID;
	}
	
	/**
	 * 查询质量特性列表
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageFinder<QualChrt> queryQualChrtByPage(QualChrt qualChrt,int page,int rows){
		String hql = "from QualChrt q  where 1=1";
		if(qualChrt.getProduct() != null){
			hql += " and q.product = '"+qualChrt.getProduct().getPid()+"' ";
		}
		hql += " and q.isDel = 0 ";
		hql += " order by q.qualChrtID";
		return this.qualChrtDao.pagedByHQL(hql, page, rows);
	}
		
	/**
	 * 查询质量特性
	 * 		根据质量特性主键 查询质量特性
	 * @param qid
	 * @return
	 */
	public QualChrt getQualChrtById(String qid){
		return this.qualChrtDao.get(qid, QualChrt.class);
	}
	
	/**
	 * 查询质量特性
	 * 		根据质量特性ID 查询质量特性
	 * @param QualChrtID
	 * @return
	 */
	public QualChrt getQualChrtByQualChrtID(String qualChrtID){
		String hql = "from QualChrt q  where 1=1";
		hql += " and q.qualChrtID = '" + qualChrtID+"'" ;
		return this.qualChrtDao.getByHQL(hql);
	}
	
	/**
	 * 删除质量特性（WQ）
	 * @param ids
	 * @throws Exception 
	 */
	@SuppressWarnings("rawtypes")
	public void deleteQualChrt(String ids) throws Exception{
		
		String qids = this.qualChrtDao.getParamFormat(ids);

		//校验有没有抽样计划使用该质量特性（记得补齐）
		String sqlTask = "SELECT 1 FROM SampSchemeInfo WHERE qid IN (" + qids + ") and isDel=0";
		List tasklistcount=this.sampSchemeDao.getListBySQL(sqlTask);
		if (tasklistcount != null && tasklistcount.size() > 0) {
			throw new Exception("该质量特性还存在抽样计划，如想删除此产品，请等抽样计划结束");
		}
		//删除指定质量特性
		String[] qualChrtIds = ids.split(",");
		for (String qid : qualChrtIds) {
			String hql = "update QualChrt q set q.isDel = 1";
			hql += " where  q.qid = '"+qid+"'";
			this.qualChrtDao.updateIsDel(hql);
		}
	}
	
}
