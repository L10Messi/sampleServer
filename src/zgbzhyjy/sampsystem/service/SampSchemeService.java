package zgbzhyjy.sampsystem.service;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import zgbzhyjy.sampsystem.dao.SampSchemeDao;
import zgbzhyjy.sampsystem.entity.QualChrt;
import zgbzhyjy.sampsystem.entity.SampScheme;
import zgbzhyjy.sampsystem.util.PageFinder;
import zgbzhyjy.sampsystem.util.StringUtil;

@Service
@Transactional
public class SampSchemeService {
	
	@Autowired
	private SampSchemeDao sampSchemeDao;
	
	/**
	 * 保存抽样计划(WQ)
	 */
	public void saveSampScheme(SampScheme sampScheme){
		if (StringUtil.isNotBlank(sampScheme.getSSid())) {
			this.sampSchemeDao.update(sampScheme);
		}else{
			sampScheme.setIsDel(0);
			//抽样计划ID实现自动生成
			String id = CalSampSchemeID(sampScheme.getQualChrt());
			sampScheme.setSampSchemeID(id);
			this.sampSchemeDao.save(sampScheme);
		}
	}
	
	/**
	 * 查询抽样计划列表(WQ)
	 */
	@SuppressWarnings("unchecked")
	public PageFinder<SampScheme> querySampSchemeByPage(SampScheme sampScheme,int page,int rows){
		String hql = "from SampScheme s where 1=1 ";
		hql += " and s.isDel = 0 order by s.sampSchemeID ";
		return this.sampSchemeDao.pagedByHQL(hql, page, rows);
	}
	
	/**
	 * 删除抽样计划(WQ)
	 * @param ids
	 * @throws Exception 
	 */
	@SuppressWarnings("rawtypes")
	public void deleteSampScheme(String ids) throws Exception{
		
		String sampIds = this.sampSchemeDao.getParamFormat(ids);
		
		//校验有没有抽样任务使用该质量特性（记得补齐）
		String sqlTask = "SELECT 1 FROM SampTaskInfo WHERE SampSchemeID IN (" + sampIds + ") and SampTaskStatus = 0";
		List tasklistcount=this.sampSchemeDao.getListBySQL(sqlTask);
		if (tasklistcount != null && tasklistcount.size() > 0) {
			throw new Exception("该产品还存在抽样任务，如想删除此产品，请等抽样任务结束");
		}

		//删除指定抽样计划（注；删除只是让 isDel属性值变为1，所以相当于是进行更新）
		String[] sids = ids.split(",");
		for (String id : sids) {
			String hql = "update SampScheme s set s.isDel = 1";
			hql += " where  s.SSid = '"+id+"'";
			this.sampSchemeDao.updateIsDel(hql);
			
		}
	}
	
	/**
	 * 抽样计划ID自动生成(WQ)
	 * 		根据质量特性计算抽样计划ID
	 */
	public String CalSampSchemeID(QualChrt qualChrt){		
		String hql = "from SampScheme s where 1=1 ";
		hql += "and s.qualChrt = '"+qualChrt.getQid()+"' ";
		List<SampScheme> sampList=this.sampSchemeDao.getListByHQL(hql);
		long len=sampList.size();
		String id = String.format("%s%03d", qualChrt.getQualChrtID(),len+1);
		return id;
	}
	
//	/**
//	 *  根据抽样任务获得抽样计划列表(待定)
//	 * 看一下实际需要
//	 */
//	public List<SampScheme> getSampSchemeList(SampTask sampTask){
//		
//		String hql = "from SampScheme sa where 1=1";
//		hql += " and sa.sampSchemeID = '"+sampTask.getSampScheme().getSampSchemeID()+"'";
//		List<SampScheme> sampSchemeList=sampSchemeDao.getListByHQL(hql);
//		return sampSchemeList;
//		
//	}
	
	/**
	 *  根据id获得抽样计划实体类
	 */
	public SampScheme querySampScheme(String id){
		return this.sampSchemeDao.get(id, SampScheme.class);
	}
}
