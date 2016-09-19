package zgbzhyjy.sampsystem.service;

import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;

import org.apache.axis.client.Call;
import org.apache.axis.encoding.XMLType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import zgbzhyjy.sampsystem.dao.InvokeWSDao;
import zgbzhyjy.sampsystem.entity.WebService;
import zgbzhyjy.sampsystem.util.PageFinder;

@Service
@Transactional
public class InvokeWSService {
	//调用WebService的业务层控制
	//WebServices实现类，可以实现调取张青楠的WebService代码
	
	@Autowired
	private InvokeWSDao invokeWSDao;
	
	/**
	 * 更新WebService信息
	 * @param WebService
	 */
	public void updateInvokeWS(WebService webService){
			this.invokeWSDao.update(webService);
	}
	
	/**
	 * 查询WebService列表
	 * @param WebService
	 * @param page
	 * @param rows
	 */
	@SuppressWarnings("unchecked")
	public PageFinder<WebService> queryInvokeWSByPage(WebService webService,int page,int rows){
		String hql = "from WebService ws where 1=1 ";
		return this.invokeWSDao.pagedByHQL(hql, page, rows);
	}
	
	/**
	 * WS的ID，获取WebService
	 * @param webServiceID
	 * @return WebService 
	 */
	public WebService getWS(String webServiceID){
		return this.invokeWSDao.get(webServiceID, WebService.class);
	}
	
	/**
	 * 调用webService
	 * @param String url, String method, Object[] params,String[] paramsNames
	 * @return String
	 * */
	public String invokeWS(String url, String method, Object[] params,String[] paramsNames){
		try{
			org.apache.axis.client.Service service =new org.apache.axis.client.Service();
	        Call call = (Call) service.createCall(); 
	        call.setTargetEndpointAddress(url); 
	        call.setUseSOAPAction(true);   
	        call.setReturnType(XMLType.XSD_STRING);  
	        call.setOperationName(new QName("http://tempuri.org/", method)); 
	        for(String paramName : paramsNames){
	        	call.addParameter(new QName("http://tempuri.org/",paramName), XMLType.XSD_STRING, ParameterMode.IN);
	        }
	        call.setSOAPActionURI("http://tempuri.org/"+method);  
	       
	        String retVal = (String) call.invoke(params);  
			System.out.println(retVal);
			
			//测试
//			String retVal="1|2|0|1|H|32,32|0,3|3,4|1|H|32,32|0,1|2,2|1|H|13,13|0,1|1,2";
	        return retVal;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return "";
	}
	
//	与上面一样
//	//调用webService转移得分处理程序的
//		public String invokeWSScore(String url, String method, Object[] params,String[] paramsName){
//			try{
//				org.apache.axis.client.Service service =new org.apache.axis.client.Service();
//		        Call call = (Call) service.createCall();  
//		        call.setTargetEndpointAddress(url);  
//		        call.setUseSOAPAction(true);   
//		        call.setReturnType(XMLType.XSD_STRING);  
//		        call.setOperationName(new QName("http://tempuri.org/", method)); 
//		        for(String paramName : paramsName){
//		        	call.addParameter(new QName("http://tempuri.org/",paramName), XMLType.XSD_STRING, ParameterMode.IN);
//		        }
//		       
//		        call.setSOAPActionURI("http://tempuri.org/"+method);  
//		        String retVal = (String) call.invoke(params);  
//		        return retVal;
//			}catch(Exception ex){
//				ex.printStackTrace();
//			}
//			return "";
//		}
//	
}
