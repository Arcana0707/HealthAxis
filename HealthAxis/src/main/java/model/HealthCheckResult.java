package model;

public class HealthCheckResult {
	private double weight;
	private double height;
	private String timeTag;
	private Employee employee;
	
	public HealthCheckResult() {
	}

	
	public void setWeight(double weight) {this.weight = weight;}
	public void setHeight(double height) {this.height = height;}
	public void setTimeTag(String timeTag) {this.timeTag = timeTag;}
	public void setEmployee(Employee employee) {this.employee = employee;}

	
	public double getWeight() {return weight;}
	public double getHeight() {return height;}
	public String getTimeTag() {return timeTag;}
	public Employee getEmployee() {return employee;}
	
}
