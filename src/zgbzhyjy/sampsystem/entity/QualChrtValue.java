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
@Table(name="QualChrtValueInfo")
public class QualChrtValue implements Serializable{

	/**
	 * 质量特性信息值的类
	 */
	private static final long serialVersionUID = 1L;

	
	private String QVid;
	private QualChrt qualChrt;
	private String QualChrtValue;
	private String QualChrtValSymbol;
	private String QualChrtValUnit;
	private int isDel;
	
	
	@Id
	@GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(name="QVid")
	public String getQVid() {
		return QVid;
	}
	public void setQVid(String qVid) {
		QVid = qVid;
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
	
	@Column(name="QualChrtValue")
	public String getQualChrtValue() {
		return QualChrtValue;
	}
	public void setQualChrtValue(String qualChrtValue) {
		QualChrtValue = qualChrtValue;
	}
	
	@Column(name="QualChrtValSymbol")
	public String getQualChrtValSymbol() {
		return QualChrtValSymbol;
	}
	public void setQualChrtValSymbol(String qualChrtValSymbol) {
		QualChrtValSymbol = qualChrtValSymbol;
	}
	
	@Column(name="QualChrtValUnit")
	public String getQualChrtValUnit() {
		return QualChrtValUnit;
	}
	public void setQualChrtValUnit(String qualChrtValUnit) {
		QualChrtValUnit = qualChrtValUnit;
	}
	
	
	@Column(name="isDel")
	public int getIsDel() {
		return isDel;
	}
	public void setIsDel(int isDel) {
		this.isDel = isDel;
	}
	
	
}
