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
@Table(name="QualChrtInfo")
public class QualChrt implements Serializable{
	/**
	 * 质量特性信息实体类
	 */
	private static final long serialVersionUID = 1L;
	
	private String Qid;
	private String QualChrtID;
	private String QualChrtName;
	private Product Product;
	private int InspectionType;
	private String InspectionDescription;	
	private int isDel;


	@Id
	@GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(name="Qid")
	public String getQid() {
		return Qid;
	}
	public void setQid(String qid) {
		Qid = qid;
	}
	
	@Column(name="QualChrtID")
	public String getQualChrtID() {
		return QualChrtID;
	}
	public void setQualChrtID(String qualChrtID) {
		this.QualChrtID = qualChrtID;
	}


	@Column(name="QualChrtName")
	public String getQualChrtName() {
		return QualChrtName;
	}
	public void setQualChrtName(String qualChrtName) {
		this.QualChrtName = qualChrtName;
	}
	
	//利用hibernate里面的关联规则
	//产品表与质量特性表为1:N规则，使用无连接表的N－1连接
	//
	@ManyToOne(targetEntity=Product.class) //指出关联的表
	@JoinColumn(name="pid",nullable=false)	//使用外键列进行规范
	@Cascade(CascadeType.PERSIST)
	public Product getProduct() {
		return Product;
	}
	public void setProduct(Product product) {
		Product = product;
	}
	
	
	@Column(name="InspectionType")
	public int getInspectionType() {
		return InspectionType;
	}
	public void setInspectionType(int inspectionType) {
		this.InspectionType = inspectionType;
	}


	@Column(name="InspectionDescription")
	public String getInspectionDescription() {
		return InspectionDescription;
	}
	public void setInspectionDescription(String inspectionDescription) {
		this.InspectionDescription = inspectionDescription;
	}


	@Column(name="isDel")
	public int getIsDel() {
		return isDel;
	}
	public void setIsDel(int isDel) {
		this.isDel = isDel;
	}
}
