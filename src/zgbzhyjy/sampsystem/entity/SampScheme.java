package zgbzhyjy.sampsystem.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="SampSchemeInfo")
public class SampScheme implements Serializable{
	
	/**
	 * 抽样计划信息实体类
	 */
	private static final long serialVersionUID = 1L;
	
	private String SSid;
	private Product product;
	private QualChrt qualChrt;
	private String SampSchemeID;
	private int LotSize;
	private int IL;
	private double AQL;
	private String SeverityLevel;
	private int NPlanType;
	private int TPlanType;
	private int RPlanType;
	private int needReduced;
	private int initStringency;
	private int isDel;
	private String Remark;
	
	@Id
	@GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name="SSid")
	public String getSSid() {
		return SSid;
	}
	public void setSSid(String sSid) {
		SSid = sSid;
	}
	
	@Column(name="SampSchemeID")
	public String getSampSchemeID() {
		return SampSchemeID;
	}
	public void setSampSchemeID(String sampSchemeID) {
		SampSchemeID = sampSchemeID;
	}
	
	@Column(name="LotSize")
	public int getLotSize() {
		return LotSize;
	}
	public void setLotSize(int lotSize) {
		LotSize = lotSize;
	}
	
	@ManyToOne(targetEntity=Product.class) //指出关联的表
	@JoinColumn(name="pid",nullable=false)	//使用外键列进行规范
	@Cascade(CascadeType.PERSIST)	
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	
	@ManyToOne(targetEntity=QualChrt.class) //指出关联的表
	@JoinColumn(name="qid",nullable=false)	//使用外键列进行规范
	@Cascade(CascadeType.PERSIST)
	public QualChrt getQualChrt() {
		return qualChrt;
	}
	public void setQualChrt(QualChrt qualChrt) {
		this.qualChrt = qualChrt;
	}
	
	@Column(name="IL")
	public int getIL() {
		return IL;
	}
	public void setIL(int iL) {
		IL = iL;
	}
	
	@Column(name="AQL")
	public double getAQL() {
		return AQL;
	}
	public void setAQL(double aQL) {
		AQL = aQL;
	}
	
	@Column(name="SeverityLevel")
	public String getSeverityLevel() {
		return SeverityLevel;
	}
	public void setSeverityLevel(String severityLevel) {
		SeverityLevel = severityLevel;
	}
	
	@Column(name="NPlanType")
	public int getNPlanType() {
		return NPlanType;
	}
	public void setNPlanType(int nPlanType) {
		NPlanType = nPlanType;
	}
	
	@Column(name="TPlanType")
	public int getTPlanType() {
		return TPlanType;
	}
	public void setTPlanType(int tPlanType) {
		TPlanType = tPlanType;
	}
	
	@Column(name="RPlanType")
	public int getRPlanType() {
		return RPlanType;
	}
	public void setRPlanType(int rPlanType) {
		RPlanType = rPlanType;
	}
	
	@Column(name="NeedReduced")
	public int getNeedReduced() {
		return needReduced;
	}
	public void setNeedReduced(int needReduced) {
		this.needReduced = needReduced;
	}
	
	@Column(name="InitStringency")
	public int getInitStringency() {
		return initStringency;
	}
	public void setInitStringency(int initStringency) {
		this.initStringency = initStringency;
	}
	
	@Column(name="isDel")
	public int getIsDel() {
		return isDel;
	}
	public void setIsDel(int isDel) {
		this.isDel = isDel;
	}
	
	@Column(name="Remark")
	public String getRemark() {
		return Remark;
	}
	public void setRemark(String remark) {
		Remark = remark;
	}
	
	
}
