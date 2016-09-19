package zgbzhyjy.sampsystem.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import zgbzhyjy.sampsystem.dao.SampTaskDao;
import zgbzhyjy.sampsystem.entity.SampTask;
import zgbzhyjy.sampsystem.util.DateUtil;
import zgbzhyjy.sampsystem.util.PageFinder;
import zgbzhyjy.sampsystem.util.StringUtil;

@Service
@Transactional
public class SampTaskService {

	@Autowired
	private SampTaskDao sampTaskDao;

	@Autowired
	private ReturnSampPlanService returnSampPlanService;

	/**
	 * 保存抽样任务
	 */
	public void saveSampTask(SampTask sampTask) {
		if (StringUtil.isNotBlank(sampTask.getSTid())) {
			this.sampTaskDao.update(sampTask);
		} else {
			sampTask.setSampTaskStatus(0);
			sampTask.setIsDel(0);
			// 抽样任务ID实现自动生成,以日期作为ID
			Date date = new Date();
			String createDate = DateUtil.dateToString(date, "yyyyMMdd");
			long len = madeSampTaskID(createDate);
			String ID = String.format("%s%03d", createDate, len + 1);
			sampTask.setSampTaskID(ID);

			sampTask.setCreateDate(date);
			this.sampTaskDao.save(sampTask);
		}
	}

	/**
	 * 查询抽样任务列表
	 * 
	 */
	@SuppressWarnings("unchecked")
	public PageFinder<SampTask> querySampTaskByPage(SampTask sampTask, int page, int rows) {
		String hql = "from SampTask s where 1=1 ";
		hql += " and s.isDel=0";
		// hql += " and s.sampTaskStatus !=4 order by CreateDate";
		hql += " order by CreateDate";
		return this.sampTaskDao.pagedByHQL(hql, page, rows);
	}

	/**
	 * 查询同一时间端内抽样任务的数量(WQ)
	 * 
	 */
	public Long madeSampTaskID(String createDate) {
		String hql = "from SampTask s where 1=1 ";
		hql += "and date_format(s.createDate,'%Y%m%d') = '" + createDate + "' ";
		return this.sampTaskDao.countByHql(hql);
	}

	/**
	 * 删除抽样任务
	 * 
	 * @param ids
	 * @throws Exception
	 */
	public void deleteSampTask(String ids) throws Exception {
		// 删除指定抽样任务（注；删除只是让 isDel属性值变为1，所以相当于是进行更新）
		String[] idlist = ids.split(",");
		for (String id : idlist) {
			String hql = "update SampTask s set s.sampTaskStatus =5";
			hql += " where  s.STid = '" + id + "'";
			this.sampTaskDao.updateIsDel(hql);

		}
	}

	/**
	 * 抽样任务实体类查询
	 * 
	 * @param ids
	 * @throws Exception
	 */
	public SampTask querySampTask(String id) {
		return this.sampTaskDao.get(id, SampTask.class);
	}

	/**
	 * 抽样任务实体类查询
	 * 
	 * @param sampTaskID
	 * @throws Exception
	 */
	public SampTask queryBySamTaskID(String sampTaskID) {
		String hql = "from SampTask q  where 1=1";
		hql += " and q.sampTaskID = '" + sampTaskID+"'" ;
		return this.sampTaskDao.getByHQL(hql);
	}

	/**
	 * 开始抽样任务
	 * 
	 * @param ids
	 * @throws Exception
	 */
	public void SampTaskBegin(String ids) throws Exception {
		SampTask sampTask = new SampTask();
		// 根据id获得抽样任务相关信息；
		sampTask = this.sampTaskDao.get(ids, SampTask.class);
		// 查表计算抽样方案 7.28
		this.returnSampPlanService.handleReturnSamplan(sampTask);
	}

}
