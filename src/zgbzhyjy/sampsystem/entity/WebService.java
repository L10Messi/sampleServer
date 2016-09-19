package zgbzhyjy.sampsystem.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * WebService 实体信息类
 */
@Entity
@Table(name="WebServiceInfo")
public class WebService implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String WebServiceID;
	private String WebServiceURL;
	
	@Id
	@Column(name="WebServiceID")
	public String getWebServiceID() {
		return WebServiceID;
	}
	public void setWebServiceID(String webServiceID) {
		WebServiceID = webServiceID;
	}
	
	@Column(name="WebServiceURL")
	public String getWebServiceURL() {
		return WebServiceURL;
	}
	public void setWebServiceURL(String webServiceURL) {
		WebServiceURL = webServiceURL;
	}
	
	
	

}
