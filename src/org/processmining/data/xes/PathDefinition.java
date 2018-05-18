package org.processmining.data.xes;

public class PathDefinition {
	
	private String currPathControlFlow = "C:\\Users\\dhkim-pc\\Desktop\\evaluation\\fold1\\result_control-flow.csv";
	private String currPathResource = "C:\\Users\\dhkim-pc\\Desktop\\evaluation\\fold1\\result_resource.csv";
	private String currPathTestRelation = "C:\\Users\\dhkim-pc\\Desktop\\evaluation\\fold1\\result_test-relation.csv";
	
	public PathDefinition() {
		
	}

	public String getCurrPathControlFlow() {
		return currPathControlFlow;
	}

	public void setCurrPathControlFlow(String currPathControlFlow) {
		this.currPathControlFlow = currPathControlFlow;
	}

	public String getCurrPathResource() {
		return currPathResource;
	}

	public void setCurrPathResource(String currPathResource) {
		this.currPathResource = currPathResource;
	}

	public String getCurrPathTestRelation() {
		return currPathTestRelation;
	}

	public void setCurrPathTestRelation(String currPathTestRelation) {
		this.currPathTestRelation = currPathTestRelation;
	}
	
	
}

