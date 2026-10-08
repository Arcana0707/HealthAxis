package model;

public class Employee {
	private String id;
	private String name;
	private String dept;
	private String initialPassword;
	private IdMapping idMapping;
	
	public Employee() {
	}
	
	
	public void setId(String id) {this.id = id;}
	public void setName(String name) {this.name = name;}
	public void setDept(String dept) {this.dept = dept;}
	public void setInitialPassword(String initialPassword) {this.initialPassword = initialPassword;}
	
	
	public String getId() {return id;}
	public String getName() {return name;}
	public String getDept() {return dept;}
	public String getInitialPassword() {return initialPassword;}
	
	public IdMapping getIdMapping() {return idMapping;}
	public void setIdMapping(IdMapping idMapping) {this.idMapping = idMapping ;}
}
