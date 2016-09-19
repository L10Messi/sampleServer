package zgbzhyjy.sampsystem.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="ReturnedSamplingPlan")
public class ReturnSampPlan implements Serializable{

	/**
	 * 张青楠返回值的实体类 对应QualChrtSamplingPlan
	 * 根据相关参数 进行查询 匹配的抽样方案
	 * 抽样方案的实体类
	 */
	private static final long serialVersionUID = 1L;
	
	private String RSPid;							//	匹配的抽样方案 标示码
	
	private SampTask sampTask;						//	抽样任务实体类
	private int LotNo;								//	批号
//	private int SampleNo;							//	样本号
	private int sampleSize;								//样本量
	
	private int NeedReduced;						//	是否需要放宽检验，0：否；1：是
	private int SSMethod;							//	转移得分计算方法
	private int ACJ;								//	正常检验方案中的AQL加严一级接收数
	private int NormalPlanType;						//	正常检验方案类型
	private String NormalCharacterCode;				//	正常检验方案字码
	private String NormalSampleSize;				//	正常检验方案样本量数组
	private String NormalAc;						//	正常检验方案接收数数组
	private String NormalRe;						//	正常检验方案拒收数数组
	
	private int TightenedPlanType;					//	加严检验方案类型
	private String TightenedCharacterCode;			//	加严检验方案字码
	private String TightenedSampleSize;				//	加严检验方案样本量数组，以逗号作为分隔符
	private String TightenedAc;						//	加严检验翻案接收数数组
	private String TightenedRe;						//	检验检验方案拒收数数组
	
	private int ReducedPlanType;					//	放宽检验方案类型
	private String ReducedCharacterCode;			//	放宽检验方案字码
	private String ReducedSampleSize;				//	放宽检验方案样本量数组
	private String ReducedAc;						//	放宽检验方案接收数数组
	private String ReducedRe;						//	放宽检验方案拒收数数组
	
	/**
	 * @since 2016.7.28
	 * */
	private int InspectedCount;
	private int AcceptCount;
	private int RefuseCount;
	private int SwitchScore;
	private int InspectionResult;
	
	@Id
	@GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(name="RSPid")
	public String getRSPid() {
		return RSPid;
	}
	public void setRSPid(String rSPid) {
		RSPid = rSPid;
	}
	
	@ManyToOne(targetEntity=SampTask.class)
	@JoinColumn(name="STid")
	public SampTask getSampTask() {
		return sampTask;
	}
	public void setSampTask(SampTask sampTask) {
		this.sampTask = sampTask;
	}
	
	@Column(name="LotNo")
	public int getLotNo() {
		return LotNo;
	}
	public void setLotNo(int lotNo) {
		LotNo = lotNo;
	}
//	
//	@Column(name="SampleNo")
//	public int getSampleNo() {
//		return SampleNo;
//	}
//	public void setSampleNo(int sampleNo) {
//		SampleNo = sampleNo;
//	}
	@Column(name="NeedReduced")
	public int getNeedReduced() {
		return NeedReduced;
	}
	public void setNeedReduced(int needReduced) {
		NeedReduced = needReduced;
	}
	
	@Column(name="SSMethod")
	public int getSSMethod() {
		return SSMethod;
	}
	public void setSSMethod(int sSMethod) {
		SSMethod = sSMethod;
	}
	
	@Column(name="ACJ")
	public int getACJ() {
		return ACJ;
	}
	public void setACJ(int aCJ) {
		ACJ = aCJ;
	}
	
	@Column(name="NormalPlanType")
	public int getNormalPlanType() {
		return NormalPlanType;
	}
	public void setNormalPlanType(int normalPlanType) {
		NormalPlanType = normalPlanType;
	}
	
	@Column(name="NormalCharacterCode")
	public String getNormalCharacterCode() {
		return NormalCharacterCode;
	}
	public void setNormalCharacterCode(String normalCharacterCode) {
		NormalCharacterCode = normalCharacterCode;
	}
	
	@Column(name="NormalSampleSize")
	public String getNormalSampleSize() {
		return NormalSampleSize;
	}
	public void setNormalSampleSize(String normalSampleSize) {
		NormalSampleSize = normalSampleSize;
	}
	
	@Column(name="NormalAc")
	public String getNormalAc() {
		return NormalAc;
	}
	public void setNormalAc(String normalAc) {
		NormalAc = normalAc;
	}
	
	@Column(name="NormalRe")
	public String getNormalRe() {
		return NormalRe;
	}
	public void setNormalRe(String normalRe) {
		NormalRe = normalRe;
	}
	
	@Column(name="TightenedPlanType")
	public int getTightenedPlanType() {
		return TightenedPlanType;
	}
	public void setTightenedPlanType(int tightenedPlanType) {
		TightenedPlanType = tightenedPlanType;
	}
	
	@Column(name="TightenedCharacterCode")
	public String getTightenedCharacterCode() {
		return TightenedCharacterCode;
	}
	public void setTightenedCharacterCode(String tightenedCharacterCode) {
		TightenedCharacterCode = tightenedCharacterCode;
	}
	
	@Column(name="TightenedSampleSize")
	public String getTightenedSampleSize() {
		return TightenedSampleSize;
	}
	public void setTightenedSampleSize(String tightenedSampleSize) {
		TightenedSampleSize = tightenedSampleSize;
	}
	
	@Column(name="TightenedAc")
	public String getTightenedAc() {
		return TightenedAc;
	}
	public void setTightenedAc(String tightenedAc) {
		TightenedAc = tightenedAc;
	}
	@Column(name="TightenedRe")
	public String getTightenedRe() {
		return TightenedRe;
	}
	public void setTightenedRe(String tightenedRe) {
		TightenedRe = tightenedRe;
	}
	@Column(name="ReducedPlanType")
	public int getReducedPlanType() {
		return ReducedPlanType;
	}
	public void setReducedPlanType(int reducedPlanType) {
		ReducedPlanType = reducedPlanType;
	}
	
	@Column(name="ReducedCharacterCode")
	public String getReducedCharacterCode() {
		return ReducedCharacterCode;
	}
	public void setReducedCharacterCode(String reducedCharacterCode) {
		ReducedCharacterCode = reducedCharacterCode;
	}
	
	@Column(name="ReducedSampleSize")
	public String getReducedSampleSize() {
		return ReducedSampleSize;
	}
	public void setReducedSampleSize(String reducedSampleSize) {
		ReducedSampleSize = reducedSampleSize;
	}
	
	@Column(name="ReducedAc")
	public String getReducedAc() {
		return ReducedAc;
	}
	public void setReducedAc(String reducedAc) {
		ReducedAc = reducedAc;
	}
	
	@Column(name="ReducedRe")
	public String getReducedRe() {
		return ReducedRe;
	}
	public void setReducedRe(String reducedRe) {
		ReducedRe = reducedRe;
	}
	
	/**
	 * @author LRover
	 * @since 7.28
	 * */
	@Column(name="sampleSize")
	public int getSampleSize() {
		return sampleSize;
	}
	public void setSampleSize(int sampleSize) {
		this.sampleSize = sampleSize;
	}
	@Column(name="SwitchScore")
	public int getSwitchScore() {
		return SwitchScore;
	}
	public void setSwitchScore(int switchScore) {
		SwitchScore = switchScore;
	}
	@Column(name="InspectionResult")
	public int getInspectionResult() {
		return InspectionResult;
	}
	public void setInspectionResult(int inspectionResult) {
		InspectionResult = inspectionResult;
	}
	@Column(name="InspectedCount")
	public int getInspectedCount() {
		return InspectedCount;
	}
	public void setInspectedCount(int inspectedCount) {
		InspectedCount = inspectedCount;
	}
	@Column(name="AcceptCount")
	public int getAcceptCount() {
		return AcceptCount;
	}
	public void setAcceptCount(int acceptCount) {
		AcceptCount = acceptCount;
	}
	@Column(name="RefuseCount")
	public int getRefuseCount() {
		return RefuseCount;
	}
	public void setRefuseCount(int refuseCount) {
		RefuseCount = refuseCount;
	}
	
	
}
