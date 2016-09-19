package zgbzhyjy.sampsystem.service;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import zgbzhyjy.sampsystem.dao.ClientDao;
import zgbzhyjy.sampsystem.dao.SampTaskDao;
import zgbzhyjy.sampsystem.entity.Client;
import zgbzhyjy.sampsystem.util.PageFinder;
import zgbzhyjy.sampsystem.util.StringUtil;

@Service
@Transactional
public class ClientService {
	
	@Autowired
	private ClientDao clientDao;
	
	@Autowired
	private SampTaskDao sampTaskDao;
	
	
	/**
	 * 新增或更新客户端
	 * @param Client
	 */
	public void saveClient(Client client){
		if (StringUtil.isNotBlank(client.getCid())) {
			this.clientDao.update(client);
		}else{
			this.clientDao.save(client);
		}
	}
	
	
	/**
	 * 查询客户端列表
	 */
	@SuppressWarnings("unchecked")
	public PageFinder<Client> queryClientByPage(Client client,int page,int rows){
		String hql = "from Client c where 1=1 ";
		hql += " and c.isDel = 0";
		if(StringUtils.isNotBlank(client.getClientID())){
			hql += " and c.clientID like '%"+client.getClientID()+"%'";
		}
		hql += " order by c.clientID";
		return this.clientDao.pagedByHQL(hql, page, rows);
	}
	
	
	/**
	 * 删除客户端
	 * @param ids
	 * @throws Exception 
	 */
	public void deleteClient(String ids) throws Exception{
		String clientIds=this.clientDao.getParamFormat(ids);
		
		//校验有没有 检测任务 使用该客户端
		String sqlTask = "SELECT 1 FROM SampTaskInfo WHERE cid IN (" + clientIds + ") and SampTaskStatus=0 ";
		List<?> tasklistcount=sampTaskDao.getListBySQL(sqlTask);
		if (tasklistcount != null && tasklistcount.size() > 0) {
			throw new Exception("该客户端还存在抽样任务，如想删除此客户端，请等抽样任务结束");
		}
				
		//删除指定客户端（注；删除只是让 isDel属性值变为1，所以相当于是进行更新）
		String[] idlist = ids.split(",");
		for (String id : idlist) {
			String hql = "update Client c set c.isDel = 1";
			hql += " where  c.id = '"+id+"'";
			this.clientDao.updateIsDel(hql);
		}
	}
	
	/**
	 * 通过id查找实体类
	 * @author LRover
	 * @param id
	 * */
	public Client queryClient(String id){
		return this.clientDao.get(id, Client.class);
	}
}
