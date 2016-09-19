package bean;

import java.io.Serializable;
import java.util.Vector;

public class TaskList implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String runningNumber;
	private String productName;
	private Vector qualityProperty;
	
	public String getRunningNumber() {
		return runningNumber;
	}
	public void setRunningNumber(String runningNumber) {
		this.runningNumber = runningNumber;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public Vector getQualityProperty() {
		return qualityProperty;
	}
	public void setQualityProperty(Vector qualityProperty) {
		this.qualityProperty = qualityProperty;
	}
	
	
}
