package zgbzhyjy.sampsystem.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="SampTaskInfo")
public class SampTask implements Serializable{

	/**
	 * 抽样任务信息实体类
	 */
	private static final long serialVersionUID = 1L;

	private String STid;
	private Client client;
	private SampScheme sampScheme;
	private String SampTaskID;
	private int SampTaskStatus;
	private int isDel;
	private String Remark;
	private Date CreateDate;
	/**
	 * @since 7.28
	 * */
	private int lotNo; //批次
	
	@Id
	@GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name="STid")
	public String getSTid() {
		return STid;
	}
	public void setSTid(String sTid) {
		STid = sTid;
	}
	
	@Column(name="SampTaskID")
	public String getSampTaskID() {
		return SampTaskID;
	}
	public void setSampTaskID(String sampTaskID) {
		SampTaskID = sampTaskID;
	}
	
//	@OneToMany(targetEntity=Client.class)
//	@JoinColumn(name="cid")
	@ManyToOne(targetEntity=Client.class) //指出关联的表
	@JoinColumn(name="cid",nullable=false)	//使用外键列进行规范
	public Client getClient() {
		return client;
	}
	public void setClient(Client client) {
		this.client = client;
	}
	
//	@OneToOne(targetEntity=SampScheme.class)
//	@JoinColumn(name="SSid")
	@ManyToOne(targetEntity=SampScheme.class) //指出关联的表
	@JoinColumn(name="SSid",nullable=false)	//使用外键列进行规范
	public SampScheme getSampScheme() {
		return sampScheme;
	}
	public void setSampScheme(SampScheme sampScheme) {
		this.sampScheme = sampScheme;
	}
	
	@Column(name="SampTaskStatus")
	public int getSampTaskStatus() {
		return SampTaskStatus;
	}
	public void setSampTaskStatus(int sampTaskStatus) {
		SampTaskStatus = sampTaskStatus;
	}
	
	@Column(name="isDel")
	public int getIsDel() {
		return isDel;
	}
	public void setIsDel(int isDel) {
		this.isDel = isDel;
	}
	
	@Column(name="CreateDate")
	public Date getCreateDate() {
		return CreateDate;
	}
	public void setCreateDate(Date createDate) {
		CreateDate = createDate;
	}
	
	@Column(name="Remark")
	public String getRemark() {
		return Remark;
	}
	public void setRemark(String remark) {
		Remark = remark;
	}
	
	@Column(name="lotNo")
	public int getLotNo() {
		return lotNo;
	}
	public void setLotNo(int lotNo) {
		this.lotNo = lotNo;
	}
	
	
	
}
