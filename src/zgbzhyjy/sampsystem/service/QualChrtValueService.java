package zgbzhyjy.sampsystem.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import zgbzhyjy.sampsystem.dao.QualChrtValueDao;
import zgbzhyjy.sampsystem.dao.SampSchemeDao;
import zgbzhyjy.sampsystem.entity.QualChrtValue;
import zgbzhyjy.sampsystem.util.PageFinder;
import zgbzhyjy.sampsystem.util.StringUtil;

@Service
@Transactional
public class QualChrtValueService {

	@Autowired
	private QualChrtValueDao qualChrtValueDao;

	@Autowired
	private SampSchemeDao sampSchemeDao;

	/**
	 * 编辑质量特性
	 * 
	 * @param QualChrtValue
	 */
	public void saveQualChrtValue(QualChrtValue qualChrtValue) {
		if (StringUtil.isNotBlank(qualChrtValue.getQVid())) {
			this.qualChrtValueDao.update(qualChrtValue);
		} else {
			this.qualChrtValueDao.save(qualChrtValue);
		}
	}

	/**
	 * 查询质量特性列表
	 * 
	 * @param page
	 * @param rows
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageFinder<QualChrtValue> queryQualChrtValueByPage(QualChrtValue qualChrtValue, int page, int rows) {
		String hql = "from QualChrtValue q where 1=1 ";
		hql += " and q.isDel = 0";
		return this.qualChrtValueDao.pagedByHQL(hql, page, rows);
	}

	/**
	 * 删除质量特性限定值
	 * @param ids
	 * @throws Exception 
	 */
	public void deleteQualChrtValue(String ids) throws Exception {
		String qids = this.qualChrtValueDao.getParamFormat(ids);
		

		// 校验有没有抽样计划使用该质量特性（记得补齐）
		String sqlTask = "SELECT 1 FROM SampSchemeInfo WHERE qid IN (" + qids + ") and isDel=0";
		List<?> tasklistcount=this.sampSchemeDao.getListBySQL(sqlTask);
		if (tasklistcount != null && tasklistcount.size() > 0) {
			throw new Exception("该产品还存在抽样任务，如想删除此产品，请等抽样任务结束");
		}
		// 删除指定质量特性
		String[] pids = ids.split(",");
		for (String id : pids) {
			String hql = "update QualChrtValue q set q.isDel = 1";
			hql += " where  q.QVid = '" + id + "'";
			this.qualChrtValueDao.updateIsDel(hql);
		}
	}
	
	/**
	 * 根据QualChrt得到QualChrtValue
	 * @author LRover
	 * @since 2016.7.28
	 * @param qid
	 * @return QualChrtValue
	 * */
	public QualChrtValue getQualChrtValueByqid(String  qid){
		String hql = "from QualChrtValue qv  where 1=1";
		hql += " and qv.qualChrt = '" + qid+"'" ;
		return this.qualChrtValueDao.getByHQL(hql);
	}
}


