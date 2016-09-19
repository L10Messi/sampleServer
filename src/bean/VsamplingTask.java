package bean;

import java.io.Serializable;

public class VsamplingTask implements Serializable{
	
	private String productID;
	private String qualChrtID;
	private String serialID;
	private String samplingPlanID; //抽样计划ID
	private String inspectedBy;    //客户端ID
	private String lotNo;			//批次
	private String sampleNo;		//样本号
	private String TaskID;			//抽样任务ID
	private String productName;
	private String qualChrtName;
	private String qualChrtInsptionDescription;
	
	public String getProductID() {
		return productID;
	}
	public void setProductID(String productID) {
		this.productID = productID;
	}
	public String getQualChrtID() {
		return qualChrtID;
	}
	public void setQualChrtID(String qualChrtID) {
		this.qualChrtID = qualChrtID;
	}
	public String getSerialID() {
		return serialID;
	}
	public void setSerialID(String serialID) {
		this.serialID = serialID;
	}
	public String getSamplingPlanID() {
		return samplingPlanID;
	}
	public void setSamplingPlanID(String samplingPlanID) {
		this.samplingPlanID = samplingPlanID;
	}
	public String getInspectedBy() {
		return inspectedBy;
	}
	public void setInspectedBy(String inspectedBy) {
		this.inspectedBy = inspectedBy;
	}
	public String getLotNo() {
		return lotNo;
	}
	public void setLotNo(String lotNo) {
		this.lotNo = lotNo;
	}
	public String getSampleNo() {
		return sampleNo;
	}
	public void setSampleNo(String sampleNo) {
		this.sampleNo = sampleNo;
	}
	public String getTaskID() {
		return TaskID;
	}
	public void setTaskID(String taskID) {
		TaskID = taskID;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getQualChrtName() {
		return qualChrtName;
	}
	public void setQualChrtName(String qualChrtName) {
		this.qualChrtName = qualChrtName;
	}
	public String getQualChrtInsptionDescription() {
		return qualChrtInsptionDescription;
	}
	public void setQualChrtInsptionDescription(String qualChrtInsptionDescription) {
		this.qualChrtInsptionDescription = qualChrtInsptionDescription;
	}

	

}
