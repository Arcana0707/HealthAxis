package model;

import java.io.Serializable;

public class IdMapping implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String employeeId;
	private String id2023Second;
	private String id2024First;
	private String id2024Second;
	private String id2025First;
	private String id2025Second;
	private String id2026First;
	
	public IdMapping() {
		
	}
	
	
	public String getEmployeeId() {
		return employeeId;
	}
	public String getId2023Second() {
		return id2023Second;
	}
	public String getId2024First() {
		return id2024First;
	}
	public String getId2024Second() {
		return id2024Second;
	}
	public String getId2025First() {
		return id2025First;
	}
	public String getId2025Second() {
		return id2025Second;
	}
	public String getId2026First() {
		return id2026First;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public void setId2023Second(String id2023Second) {
		this.id2023Second = id2023Second;
	}
	public void setId2024First(String id2024First) {
		this.id2024First = id2024First;
	}
	public void setId2024Second(String id2024Second) {
		this.id2024Second = id2024Second;
	}
	public void setId2025First(String id2025First) {
		this.id2025First = id2025First;
	}
	public void setId2025Second(String id2025Second) {
		this.id2025Second = id2025Second;
	}
	public void setId2026First(String id2026First) {
		this.id2026First = id2026First;
	}
	
}
