package zgbzhyjy.sampsystem.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name="ClientInfo")
public class Client implements Serializable{
	
	/**
	 * 客户端信息实体类
	 */
	private static final long serialVersionUID = 1L;
	private String cid;
	private String clientID;
	private String clientType;
	private String macAddress;
	private int isDel;
	private String remark;
	
	@Id
	@GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(name="cid")
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	
	@Column(name="ClientID")
	public String getClientID() {
		return clientID;
	}
	public void setClientID(String clientID) {
		this.clientID = clientID;
	}
	
	@Column(name="ClientType")
	public String getClientType() {
		return clientType;
	}
	public void setClientType(String clientType) {
		this.clientType = clientType;
	}
	
	@Column(name="MacAddress")
	public String getMacAddress() {
		return this.macAddress;
	}
	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}
	
	@Column(name="isDel")
	public int getIsDel() {
		return this.isDel;
	}
	public void setIsDel(int isDel) {
		this.isDel = isDel;
	}
	
	@Column(name="Remark")
	public String getRemark() {
		return this.remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

}
