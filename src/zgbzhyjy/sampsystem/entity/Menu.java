package zgbzhyjy.sampsystem.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="T_SYS_MENU")
public class Menu implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public String id;
	public String menuCode;
	public String menuName;
	public String url;
	public String des;
	public String pid;
	
	@Id
	@GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(name="ID")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	@Column(name="MENU_CODE")
	public String getMenuCode() {
		return menuCode;
	}
	public void setMenuCode(String menuCode) {
		if("".equals(menuCode)){
			this.menuCode = null;
		}else{
			this.menuCode = menuCode;
		}
	}
	
	@Column(name="MENU_NAME")	
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		if(!"".equals(menuName)){
			this.menuName = menuName;
		}
	}
	
	@Column(name="URL")	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		if(!"".equals(url)){
			this.url = url;
		}
		
	}
	
	@Column(name="DES")
	public String getDes() {
		return des;
	}
	public void setDes(String des) {
		if(!"".equals(des)){
			this.des = des;
		}
	}
	
	@Column(name="PID")	
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		if(!"".equals(pid)){
			this.pid = pid;
		}
	}
}
