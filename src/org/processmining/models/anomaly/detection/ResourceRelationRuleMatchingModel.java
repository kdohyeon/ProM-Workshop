package org.processmining.models.anomaly.detection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.processmining.data.relation.RelationMatrix;
import org.processmining.models.relation.RelationModel;
import org.processmining.plugins.anomaly.detection.AnomalyDetectionMiningParameters;

public class ResourceRelationRuleMatchingModel extends AbstractRuleMatchingModel{
	private RelationModel trainingRelModel;
	private RelationModel testRelModel;
	private AnomalyDetectionMiningParameters parameters;
	private Map<String, Float> resultMap;
	
	public ResourceRelationRuleMatchingModel(RelationModel trainingRelModel, RelationModel testRelModel, AnomalyDetectionMiningParameters parameters) {
		/*
		 * Parameters
		 * */
		this.setTrainingRelModel(trainingRelModel);
		this.setTestRelModel(testRelModel);
		this.setParameters(parameters);
		resultMap = new HashMap<String, Float>();
		
		/*
		 * Activity
		 * */
		// get the size of r act rel rule size
		RelationMatrix rRelationMatrix = new RelationMatrix();
		rRelationMatrix = trainingRelModel.getRelationResourceMatrix();
		int rRelRuleSize = rRelationMatrix.getRelationMatrixListSize();
				
		// get the case id list
		ArrayList<String> caseIDList = new ArrayList<String>();
		caseIDList = testRelModel.getCaseIDList();
		
		// get the unique relation list of profile
		ArrayList<String> trainingRelationSet = new ArrayList<String>();
		for(int i = 0; i < rRelRuleSize; i++) {
			//trainingRelationSet.add(trainingRelModel.getRelation(i).getResource());
			trainingRelationSet.add(rRelationMatrix.getPair(i));
		}
		
		// get the unique relation list of test
		ArrayList<String> testRelationSet = new ArrayList<String>();
		for(int i = 0; i < testRelModel.getRelationCardinality(); i++) {
			testRelationSet.add(testRelModel.getRelation(i).getResource());
		}
		
		// for each case
		for(int i = 0; i < caseIDList.size(); i++) {
			String thisCase = caseIDList.get(i);
			
			ArrayList<String> temp = new ArrayList<String>();
			for(int j = 0; j < testRelModel.getRelationCardinality(); j++) {
				if(thisCase.equals(testRelModel.getCaseID(j))) {
					temp.add(testRelModel.getRelation(j).getResource());
				}
			}
			
			// this.computeRuleToLog
			float a = this.computeLogToRule(trainingRelationSet, temp);
			
			// this.computeLogToRule
			float b = this.computeRuleToLog(trainingRelationSet, temp);		
			
			// print
			//System.out.println("Case: " + thisCase + " -- " + a + ", " + b);
			
			// calculate
			float weight1, weight2;
			weight1 = (float) 0.5; // equal weight
			weight2 = (float) 0.5; // equal weight
			
			float score = weight1*a + weight2*b;
			resultMap.put(thisCase, score);
		}
	}
	
	

	/*
	 * Getters and Setters
	 * */
	
	public RelationModel getTrainingRelModel() {
		return trainingRelModel;
	}

	public void setTrainingRelModel(RelationModel trainingRelModel) {
		this.trainingRelModel = trainingRelModel;
	}

	public RelationModel getTestRelModel() {
		return testRelModel;
	}

	public void setTestRelModel(RelationModel testRelModel) {
		this.testRelModel = testRelModel;
	}

	public AnomalyDetectionMiningParameters getParameters() {
		return parameters;
	}

	public void setParameters(AnomalyDetectionMiningParameters parameters) {
		this.parameters = parameters;
	}



	public Map<String, Float> getResultMap() {
		return resultMap;
	}



	public void setResultMap(Map<String, Float> resultMap) {
		this.resultMap = resultMap;
	}
}
