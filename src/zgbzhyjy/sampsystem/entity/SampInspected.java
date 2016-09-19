package zgbzhyjy.sampsystem.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="SampInspectedInfo")
public class SampInspected implements Serializable{

	/**
	 * 样本检测记录实体信息表 对应原来数据库中的 qualchrtinspected
	 * 
	 * 2016.7.28备注：待测试完成之后 删除
	 */
	private static final long serialVersionUID = 1L;
	
	private String SIid;				//主键标示码
	private String SerialID;			//	流水号
	private String SampSchemeID;		//	抽样计划编号
	private String ProductID;			//	产品ID
	private String ProductName;
	private String QualChrtName;
	private String QualChrtID;			//	质量特性ID
	private String SampTaskID;			//	抽样任务编号
	private String ClientID;			//	检测的客户端编号
	private int LotNo; 					//	批号
	private int SampleNo;				//	样本号
	private int InspectionStatus;	//	检测状态
	private int InspectionSequence;	//	检测序列
	private String InspectionResult;	//	检测结果
	private String InspectionResultDetail;			//	检测结果详情
	
	@Id
	@GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name="SIid")
	public String getSIid() {
		return SIid;
	}
	public void setSIid(String sIid) {
		SIid = sIid;
	}
	
	@Column(name="SerialID")
	public String getSerialID() {
		return SerialID;
	}
	public void setSerialID(String serialID) {
		SerialID = serialID;
	}
	
	@Column(name="SampSchemeID")
	public String getSampSchemeID() {
		return SampSchemeID;
	}
	public void setSampSchemeID(String sampSchemeID) {
		SampSchemeID = sampSchemeID;
	}
	
	@Column(name="ProductID")
	public String getProductID() {
		return ProductID;
	}
	public void setProductID(String productID) {
		ProductID = productID;
	}
	
	@Column(name="QualChrtID")
	public String getQualChrtID() {
		return QualChrtID;
	}
	public void setQualChrtID(String qualChrtID) {
		QualChrtID = qualChrtID;
	}
	
	@Column(name="ProductName")
	public String getProductName() {
		return ProductName;
	}
	public void setProductName(String productName) {
		ProductName = productName;
	}
	
	@Column(name="QualChrtName")
	public String getQualChrtName() {
		return QualChrtName;
	}
	public void setQualChrtName(String qualChrtName) {
		QualChrtName = qualChrtName;
	}
	
	@Column(name="SampTaskID")
	public String getSampTaskID() {
		return SampTaskID;
	}
	public void setSampTaskID(String sampTaskID) {
		SampTaskID = sampTaskID;
	}
	
	@Column(name="ClientID")
	public String getClientID() {
		return ClientID;
	}
	public void setClientID(String clientID) {
		ClientID = clientID;
	}
	
	@Column(name="LotNo")
	public int getLotNo() {
		return LotNo;
	}
	public void setLotNo(int lotNo) {
		LotNo = lotNo;
	}
	
	@Column(name="SampleNo")
	public int getSampleNo() {
		return SampleNo;
	}
	public void setSampleNo(int sampleNo) {
		SampleNo = sampleNo;
	}
	
	@Column(name="InspectionStatus")
	public int getInspectionStatus() {
		return InspectionStatus;
	}
	public void setInspectionStatus(int inspectionStatus) {
		InspectionStatus = inspectionStatus;
	}
	
	@Column(name="InspectionSequence")
	public int getInspectionSequence() {
		return InspectionSequence;
	}
	public void setInspectionSequence(int inspectionSequence) {
		InspectionSequence = inspectionSequence;
	}
	
	@Column(name="InspectionResult")
	public String getInspectionResult() {
		return InspectionResult;
	}
	public void setInspectionResult(String inspectionResult) {
		InspectionResult = inspectionResult;
	}
	
	@Column(name="InspectionResultDetail")
	public String getInspectionResultDetail() {
		return InspectionResultDetail;
	}
	public void setInspectionResultDetail(String inspectionResultDetail) {
		InspectionResultDetail = inspectionResultDetail;
	}
	
	

}
