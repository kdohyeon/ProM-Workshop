package org.processmining.models.anomaly.detection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.processmining.data.overlap.Overlap;
import org.processmining.data.processing.Processing;
import org.processmining.data.relation.Relation;
import org.processmining.data.relation.RelationMatrix;
import org.processmining.data.transition.Transition;
import org.processmining.data.trueX.TrueX;
import org.processmining.data.trueY.TrueY;
import org.processmining.mining.anomaly.detection.AnomalyDetectionMiningParameters;
import org.processmining.models.activity.ActivityModel;
import org.processmining.models.anomaly.profile.AnomalyProfileModel;
import org.processmining.models.relation.RelationModel;

public class ResourceRuleMatchingModel {
	
	private ActivityModel trainingActModel;
	private ActivityModel testActModel;
	private RelationModel trainingRelModel;
	private RelationModel testRelModel;
	private AnomalyProfileModel profileModel;
	private AnomalyDetectionMiningParameters parameters;
	
	private float resourceScore;
	
	public ResourceRuleMatchingModel(ActivityModel trainingActModel, ActivityModel testActModel, 
			RelationModel trainingRelModel, RelationModel testRelModel, 
			AnomalyProfileModel profileModel, AnomalyDetectionMiningParameters parameters) {
		
		/*
		 * Parameters
		 * */
		this.setTrainingActModel(trainingActModel);
		this.setTestActModel(testActModel);
		this.setTrainingRelModel(trainingRelModel);
		this.setTestRelModel(testRelModel);
		this.setProfileModel(profileModel);
		this.setParameters(parameters);
		
		float logToRule = 100;
		float ruleToLog = 100;
		
		resourceScore = 100;
		
		float sigma = parameters.getSigma();
		
		// Cardinality
		RelationMatrix trainingResourceMatrix = trainingRelModel.getRelationResourceMatrix();
		int rRuleSize = trainingResourceMatrix.getRelationMatrixListSize();
		int logSize = testRelModel.getRelationCardinality();
		
		/*
		 * Rule to Log
		 * */
		Map<String, ArrayList<Relation>> normalInLog = new HashMap<String, ArrayList<Relation>>();
		ArrayList<Processing> processingList = new ArrayList<Processing>();
		processingList = profileModel.getProcessingModel().getProcessingList_Res();
		
		ArrayList<Transition> transitionList = new ArrayList<Transition>();
		transitionList = profileModel.getTransitionModel().getTransitionList_Res();
		
		ArrayList<Overlap> overlapList = new ArrayList<Overlap>();
		overlapList = profileModel.getOverlapModel().getOverlapList_Res();
		
		ArrayList<TrueX> trueXList = new ArrayList<TrueX>();
		trueXList = profileModel.getTrueXModel().getTrueXList_Res();
		
		ArrayList<TrueY> trueYList = new ArrayList<TrueY>();
		trueYList = profileModel.getTrueYModel().getTrueYList_Res();
		
		for(int i = 0; i < rRuleSize; i++) { // for each rule
			
			String trainingAntecedentResourceID = trainingResourceMatrix.getAntecedent(i);
			String trainingConsequentResourceID = trainingResourceMatrix.getConsequent(i);
			String trainingRelationType = trainingResourceMatrix.getRelationType(i);
			
			float trainingAntecedentResourceProcessingTimeLowerBoundary = 0;
			float trainingAntecedentResourceProcessingTimeUpperBoundary = 0;
			float trainingConsequentResourceProcessingTimeLowerBoundary = 0;
			float trainingConsequentResourceProcessingTimeUpperBoundary = 0;
			float trainingTransitionTimeLowerBoundary = 0;
			float trainingTransitionTimeUpperBoundary = 0;
			float trainingOverlapTimeLowerBoundary = 0;
			float trainingOverlapTimeUpperBoundary = 0;
			float trainingTrueXTimeLowerBoundary = 0;
			float trainingTrueXTimeUpperBoundary = 0;
			float trainingTrueYTimeLowerBoundary = 0;
			float trainingTrueYTimeUpperBoundary = 0;
			
			// get lower and upper boundary for processing time of antecedent and consequent
			if(parameters.isProcessing()) {
				for(int j = 0; j < processingList.size(); j++) {
					if(processingList.get(j).getResourceID().equals(trainingAntecedentResourceID)) {
						trainingAntecedentResourceProcessingTimeLowerBoundary = processingList.get(j).getAvg() - sigma*processingList.get(j).getStdev();
						trainingAntecedentResourceProcessingTimeUpperBoundary = processingList.get(j).getAvg() + sigma*processingList.get(j).getStdev();	
					}
					
					if(processingList.get(j).getResourceID().equals(trainingConsequentResourceID)) {
						trainingConsequentResourceProcessingTimeLowerBoundary = processingList.get(j).getAvg() - sigma*processingList.get(j).getStdev();
						trainingConsequentResourceProcessingTimeUpperBoundary = processingList.get(j).getAvg() + sigma*processingList.get(j).getStdev();
					}
				}
			}

			// get lower and upper boundary for relation time, specific for its relation type
			if(parameters.isTransition() && 
					trainingRelationType.equals("<")) {
				for(int j = 0; j < transitionList.size(); j++) {
					if(transitionList.get(j).getFromResourceID().equals(trainingAntecedentResourceID) && transitionList.get(j).getToResourceID().equals(trainingConsequentResourceID)) {
						trainingTransitionTimeLowerBoundary = transitionList.get(j).getAvg() - sigma*transitionList.get(j).getStdev();
						trainingTransitionTimeUpperBoundary = transitionList.get(j).getAvg() + sigma*transitionList.get(j).getStdev();
					}
				}
			}
			
			if(parameters.isOverlap() && (
					trainingRelationType.equals("o") 
					|| trainingRelationType.equals("d")
					|| trainingRelationType.equals("s")
					|| trainingRelationType.equals("f")
					|| trainingRelationType.equals("="))) {
				for(int j = 0; j < overlapList.size(); j++) {
					if(overlapList.get(j).getFromResourceID().equals(trainingAntecedentResourceID) && overlapList.get(j).getToResourceID().equals(trainingConsequentResourceID)) {
						trainingOverlapTimeLowerBoundary = overlapList.get(j).getAvg() - sigma*overlapList.get(j).getStdev();
						trainingOverlapTimeUpperBoundary = overlapList.get(j).getAvg() + sigma*overlapList.get(j).getStdev();
					}
				}
			}
			
			if(parameters.isTrueX() && (
					trainingRelationType.equals("o")
					|| trainingRelationType.equals("d")
					|| trainingRelationType.equals("f")
					)) {
				for(int j = 0; j < trueXList.size(); j++) {
					if(trueXList.get(j).getFromResourceID().equals(trainingAntecedentResourceID) && trueXList.get(j).getToResourceID().equals(trainingConsequentResourceID)) {
						trainingTrueXTimeLowerBoundary = trueXList.get(j).getAvg() - sigma*trueXList.get(j).getStdev();
						trainingTrueXTimeUpperBoundary = trueXList.get(j).getAvg() + sigma*trueXList.get(j).getStdev();
					}
				}
			}
			
			if(parameters.isTrueY() &&
					trainingRelationType.equals("o")
					|| trainingRelationType.equals("s")
					|| trainingRelationType.equals("d")
					) {
				for(int j = 0; j < trueYList.size(); j++) {
					if(trueYList.get(j).getFromResourceID().equals(trainingAntecedentResourceID) && trueYList.get(j).getToResourceID().equals(trainingConsequentResourceID)) {
						trainingTrueYTimeLowerBoundary = trueYList.get(j).getAvg() - sigma*trueYList.get(j).getStdev();
						trainingTrueYTimeUpperBoundary = trueYList.get(j).getAvg() + sigma*trueYList.get(j).getStdev();
					}
				}
			}
			
			// check if current rule exists in the log (by case)
			for(int j = 0; j < logSize; j++) {
				
				String caseID = testRelModel.getCaseID(j);				
				String testAntecedentResourceID = testRelModel.getAntecedentResource(j);
				String testConsequentResourceID = testRelModel.getConsequentResource(j);
				String testRelationType = testRelModel.getRelationType(j);
				
				float testAntecedentResourceProcessingTime = testRelModel.getAntecedentActivity(j).getProcessingTime();
				float testConsequentResourceProcessingTime = testRelModel.getConsequentActivity(j).getProcessingTime();
				
				float testTransitionTime = testRelModel.getTransitionTime(j);
				float testOverlapTime = testRelModel.getOverlapTime(j);
				float testTrueXTime = testRelModel.getTrueXTime(j);
				float testTrueYTime = testRelModel.getTrueYTime(j);
				
				boolean processing = false;
				boolean transition = false;
				boolean overlap = false;
				boolean trueX = false;
				boolean trueY = false;
				
				// rule 1
				if(testRelationType.equals("<")) {
					// check the activities
					if(trainingAntecedentResourceID.equals(testAntecedentResourceID) && trainingConsequentResourceID.equals(testConsequentResourceID)) { 
						// check the processing time of antecedent activities
						// if this needs to be compared
						if(parameters.isProcessing()) {
							if(trainingAntecedentResourceProcessingTimeLowerBoundary < testAntecedentResourceProcessingTime && testAntecedentResourceProcessingTime < trainingAntecedentResourceProcessingTimeUpperBoundary) {
								if(trainingConsequentResourceProcessingTimeLowerBoundary < testConsequentResourceProcessingTime && testConsequentResourceProcessingTime < trainingConsequentResourceProcessingTimeUpperBoundary) {
									processing = true;	
								}
							}
						}else { // if not needed
							processing = true;
						}
						
						if(parameters.isTransition()) {
							if(trainingTransitionTimeLowerBoundary < testTransitionTime && testTransitionTime < trainingTransitionTimeUpperBoundary) {
								transition = true;
							}
						}else {
							transition = true;
						}
						
						if(processing && transition) {
							if(normalInLog.containsKey(caseID)) { // if the key already exists
								ArrayList<Relation> temp = normalInLog.get(caseID);
								temp.add(testRelModel.getRelation(j));
								normalInLog.put(caseID, temp);
							}else { // if there exists no key
								ArrayList<Relation> temp = new ArrayList<Relation>();
								temp.add(testRelModel.getRelation(j));
								normalInLog.put(caseID, temp);	
							}
						}					
					}
				}else if(testRelationType.equals("m")) {// rule 2
					// check the activities
					if(trainingAntecedentResourceID.equals(testAntecedentResourceID) && trainingConsequentResourceID.equals(testConsequentResourceID)) { 
						// check the processing time of antecedent activities
						// if this needs to be compared
						if(parameters.isProcessing()) {
							if(trainingAntecedentResourceProcessingTimeLowerBoundary < testAntecedentResourceProcessingTime && testAntecedentResourceProcessingTime < trainingAntecedentResourceProcessingTimeUpperBoundary) {
								if(trainingConsequentResourceProcessingTimeLowerBoundary < testConsequentResourceProcessingTime && testConsequentResourceProcessingTime < trainingConsequentResourceProcessingTimeUpperBoundary) {
									processing = true;	
								}
							}
						}else { // if not needed
							processing = true;
						}
						
						if(processing) {
							if(normalInLog.containsKey(caseID)) { // if the key already exists
								ArrayList<Relation> temp = normalInLog.get(caseID);
								temp.add(testRelModel.getRelation(j));
								normalInLog.put(caseID, temp);
							}else { // if there exists no key
								ArrayList<Relation> temp = new ArrayList<Relation>();
								temp.add(testRelModel.getRelation(j));
								normalInLog.put(caseID, temp);	
							}
						}					
					}
				}else if(testRelationType.equals("o")) {// rule 3
					// check the activities
					if(trainingAntecedentResourceID.equals(testAntecedentResourceID) && trainingConsequentResourceID.equals(testConsequentResourceID)) { 
						// check the processing time of antecedent activities
						// if this needs to be compared
						if(parameters.isOverlap()) {
							if(trainingOverlapTimeLowerBoundary < testOverlapTime && testOverlapTime < trainingOverlapTimeUpperBoundary) {
								overlap = true;
							}
						}else {
							overlap = true;
						}
						
						if(parameters.isTrueX()) {
							if(trainingTrueXTimeLowerBoundary < testTrueXTime && testTrueXTime < trainingTrueXTimeUpperBoundary) {
								trueX = true;
							}
						}else {
							trueX = true;
						}
						
						if(parameters.isTrueY()) {
							if(trainingTrueYTimeLowerBoundary < testTrueYTime && testTrueYTime < trainingTrueYTimeUpperBoundary) {
								trueY = true;
							}
						}else {
							trueY = true;
						}
						
						if(overlap && trueX && trueY) {
							if(normalInLog.containsKey(caseID)) { // if the key already exists
								ArrayList<Relation> temp = normalInLog.get(caseID);
								temp.add(testRelModel.getRelation(j));
								normalInLog.put(caseID, temp);
							}else { // if there exists no key
								ArrayList<Relation> temp = new ArrayList<Relation>();
								temp.add(testRelModel.getRelation(j));
								normalInLog.put(caseID, temp);	
							}
						}					
					}
				}else if(testRelationType.equals("s")) {// rule 4
					// check the activities
					if(trainingAntecedentResourceID.equals(testAntecedentResourceID) && trainingConsequentResourceID.equals(testConsequentResourceID)) { 
						// check the processing time of antecedent activities
						// if this needs to be compared
						if(parameters.isOverlap()) {
							if(trainingOverlapTimeLowerBoundary < testOverlapTime && testOverlapTime < trainingOverlapTimeUpperBoundary) {
								overlap = true;
							}
						}else {
							overlap = true;
						}
						
						if(parameters.isTrueY()) {
							if(trainingTrueYTimeLowerBoundary < testTrueYTime && testTrueYTime < trainingTrueYTimeUpperBoundary) {
								trueY = true;
							}
						}else {
							trueY = true;
						}
						
						if(overlap && trueY) {
							if(normalInLog.containsKey(caseID)) { // if the key already exists
								ArrayList<Relation> temp = normalInLog.get(caseID);
								temp.add(testRelModel.getRelation(j));
								normalInLog.put(caseID, temp);
							}else { // if there exists no key
								ArrayList<Relation> temp = new ArrayList<Relation>();
								temp.add(testRelModel.getRelation(j));
								normalInLog.put(caseID, temp);	
							}
						}					
					}
				}else if(testRelationType.equals("d")) {// rule 5
					// check the activities
					if(trainingAntecedentResourceID.equals(testAntecedentResourceID) && trainingConsequentResourceID.equals(testConsequentResourceID)) { 
						// check the processing time of antecedent activities
						// if this needs to be compared
						if(parameters.isOverlap()) {
							if(trainingOverlapTimeLowerBoundary < testOverlapTime && testOverlapTime < trainingOverlapTimeUpperBoundary) {
								overlap = true;
							}
						}else {
							overlap = true;
						}
						
						if(parameters.isTrueX()) {
							if(trainingTrueXTimeLowerBoundary < testTrueXTime && testTrueXTime < trainingTrueXTimeUpperBoundary) {
								trueX = true;
							}
						}else {
							trueX = true;
						}
						
						if(parameters.isTrueY()) {
							if(trainingTrueYTimeLowerBoundary < testTrueYTime && testTrueYTime < trainingTrueYTimeUpperBoundary) {
								trueY = true;
							}
						}else {
							trueY = true;
						}
						
						if(overlap && trueX && trueY) {
							if(normalInLog.containsKey(caseID)) { // if the key already exists
								ArrayList<Relation> temp = normalInLog.get(caseID);
								temp.add(testRelModel.getRelation(j));
								normalInLog.put(caseID, temp);
							}else { // if there exists no key
								ArrayList<Relation> temp = new ArrayList<Relation>();
								temp.add(testRelModel.getRelation(j));
								normalInLog.put(caseID, temp);	
							}
						}					
					}
				}else if(testRelationType.equals("f")) {// rule 6
					// check the activities
					if(trainingAntecedentResourceID.equals(testAntecedentResourceID) && trainingConsequentResourceID.equals(testConsequentResourceID)) { 
						// check the processing time of antecedent activities
						// if this needs to be compared
						if(parameters.isOverlap()) {
							if(trainingOverlapTimeLowerBoundary < testOverlapTime && testOverlapTime < trainingOverlapTimeUpperBoundary) {
								overlap = true;
							}
						}else {
							overlap = true;
						}
						
						if(parameters.isTrueX()) {
							if(trainingTrueXTimeLowerBoundary < testTrueXTime && testTrueXTime < trainingTrueXTimeUpperBoundary) {
								trueX = true;
							}
						}else {
							trueY = true;
						}
						
						if(overlap && trueX) {
							if(normalInLog.containsKey(caseID)) { // if the key already exists
								ArrayList<Relation> temp = normalInLog.get(caseID);
								temp.add(testRelModel.getRelation(j));
								normalInLog.put(caseID, temp);
							}else { // if there exists no key
								ArrayList<Relation> temp = new ArrayList<Relation>();
								temp.add(testRelModel.getRelation(j));
								normalInLog.put(caseID, temp);	
							}
						}					
					}
				}else if(testRelationType.equals("=")) {// rule 7
					// check the activities
					if(trainingAntecedentResourceID.equals(testAntecedentResourceID) && trainingConsequentResourceID.equals(testConsequentResourceID)) { 
						// check the processing time of antecedent activities
						// if this needs to be compared
						if(parameters.isOverlap()) {
							if(trainingOverlapTimeLowerBoundary < testOverlapTime && testOverlapTime < trainingOverlapTimeUpperBoundary) {
								overlap = true;
							}
						}else {
							overlap = true;
						}
						
						if(overlap) {
							if(normalInLog.containsKey(caseID)) { // if the key already exists
								ArrayList<Relation> temp = normalInLog.get(caseID);
								temp.add(testRelModel.getRelation(j));
								normalInLog.put(caseID, temp);
							}else { // if there exists no key
								ArrayList<Relation> temp = new ArrayList<Relation>();
								temp.add(testRelModel.getRelation(j));
								normalInLog.put(caseID, temp);	
							}
						}					
					}
				}
			}
		}
		
		Iterator<String> keys = normalInLog.keySet().iterator();
		System.out.println("Rule To Log");
		while(keys.hasNext()) {
			String key = keys.next();
			System.out.println(
					"Case ID: " + key + 
					": " + normalInLog.get(key).size() + " / " + rRuleSize + 
					" = " + calculateRuleToLogScore(rRuleSize, normalInLog.get(key).size()));			
		}
		
		/*
		 * Log to Rule
		 * */
		Map<String, ArrayList<Relation>> normalInRules = new HashMap<String, ArrayList<Relation>>();
		
		for(int i = 0; i < logSize; i++) { // for each rule
			String caseID = testRelModel.getCaseID(i);
			String testAntecedentResourceID = testRelModel.getAntecedentResource(i); 					
			String testConsequentResourceID = testRelModel.getConsequentResource(i);
			String testRelationType = testRelModel.getRelationType(i);
			
			float trainingAntecedentResourceProcessingTimeLowerBoundary = 0;
			float trainingAntecedentResourceProcessingTimeUpperBoundary = 0;
			float trainingConsequentResourceProcessingTimeLowerBoundary = 0;
			float trainingConsequentResourceProcessingTimeUpperBoundary = 0;
			float trainingTransitionTimeLowerBoundary = 0;
			float trainingTransitionTimeUpperBoundary = 0;
			float trainingOverlapTimeLowerBoundary = 0;
			float trainingOverlapTimeUpperBoundary = 0;
			float trainingTrueXTimeLowerBoundary = 0;
			float trainingTrueXTimeUpperBoundary = 0;
			float trainingTrueYTimeLowerBoundary = 0;
			float trainingTrueYTimeUpperBoundary = 0;
			
			// get lower and upper boundary for processing time of antecedent and consequent
			if(parameters.isProcessing()) {
				for(int j = 0; j < processingList.size(); j++) {
					if(processingList.get(j).getResourceID().equals(testAntecedentResourceID)) {
						trainingAntecedentResourceProcessingTimeLowerBoundary = processingList.get(j).getAvg() - sigma*processingList.get(j).getStdev();
						trainingAntecedentResourceProcessingTimeUpperBoundary = processingList.get(j).getAvg() + sigma*processingList.get(j).getStdev();	
					}
					
					if(processingList.get(j).getResourceID().equals(testConsequentResourceID)) {
						trainingConsequentResourceProcessingTimeLowerBoundary = processingList.get(j).getAvg() - sigma*processingList.get(j).getStdev();
						trainingConsequentResourceProcessingTimeUpperBoundary = processingList.get(j).getAvg() + sigma*processingList.get(j).getStdev();
					}
				}
			}

			// get lower and upper boundary for relation time, specific for its relation type
			if(parameters.isTransition() && 
					testRelationType.equals("<")) {
				for(int j = 0; j < transitionList.size(); j++) {
					if(transitionList.get(j).getFromResourceID().equals(testAntecedentResourceID) && transitionList.get(j).getToResourceID().equals(testConsequentResourceID)) {
						trainingTransitionTimeLowerBoundary = transitionList.get(j).getAvg() - sigma*transitionList.get(j).getStdev();
						trainingTransitionTimeUpperBoundary = transitionList.get(j).getAvg() + sigma*transitionList.get(j).getStdev();
					}
				}
			}
			
			if(parameters.isOverlap() && (
					testRelationType.equals("o") 
					|| testRelationType.equals("d")
					|| testRelationType.equals("s")
					|| testRelationType.equals("f")
					|| testRelationType.equals("="))) {
				for(int j = 0; j < overlapList.size(); j++) {
					if(overlapList.get(j).getFromResourceID().equals(testAntecedentResourceID) && overlapList.get(j).getToResourceID().equals(testConsequentResourceID)) {
						trainingOverlapTimeLowerBoundary = overlapList.get(j).getAvg() - sigma*overlapList.get(j).getStdev();
						trainingOverlapTimeUpperBoundary = overlapList.get(j).getAvg() + sigma*overlapList.get(j).getStdev();
					}
				}
			}
			
			if(parameters.isTrueX() && (
					testRelationType.equals("o")
					|| testRelationType.equals("d")
					|| testRelationType.equals("f")
					)) {
				for(int j = 0; j < trueXList.size(); j++) {
					if(trueXList.get(j).getFromResourceID().equals(testAntecedentResourceID) && trueXList.get(j).getToResourceID().equals(testConsequentResourceID)) {
						trainingTrueXTimeLowerBoundary = trueXList.get(j).getAvg() - sigma*trueXList.get(j).getStdev();
						trainingTrueXTimeUpperBoundary = trueXList.get(j).getAvg() + sigma*trueXList.get(j).getStdev();
					}
				}
			}
			
			if(parameters.isTrueY() &&
					testRelationType.equals("o")
					|| testRelationType.equals("s")
					|| testRelationType.equals("d")
					) {
				for(int j = 0; j < trueYList.size(); j++) {
					if(trueYList.get(j).getFromResourceID().equals(testAntecedentResourceID) && trueYList.get(j).getToResourceID().equals(testConsequentResourceID)) {
						trainingTrueYTimeLowerBoundary = trueYList.get(j).getAvg() - sigma*trueYList.get(j).getStdev();
						trainingTrueYTimeUpperBoundary = trueYList.get(j).getAvg() + sigma*trueYList.get(j).getStdev();
					}
				}
			}
			
			// check if current log exists in the rule (by case)
			for(int j = 0; j < rRuleSize; j++) {
				
				String trainingAntecedentResourceID = trainingResourceMatrix.getAntecedent(j);
				String trainingConsequentResourceID = trainingResourceMatrix.getConsequent(j);
				String trainingRelationType = trainingResourceMatrix.getRelationType(j);
				
				float testAntecedentResourceProcessingTime = testRelModel.getAntecedentActivity(i).getProcessingTime();
				float testConsequentResourceProcessingTime = testRelModel.getConsequentActivity(i).getProcessingTime();
				
				float testTransitionTime = testRelModel.getTransitionTime(i);
				float testOverlapTime = testRelModel.getOverlapTime(i);
				float testTrueXTime = testRelModel.getTrueXTime(i);
				float testTrueYTime = testRelModel.getTrueYTime(i);
				
				boolean processing = false;
				boolean transition = false;
				boolean overlap = false;
				boolean trueX = false;
				boolean trueY = false;
				
				// rule 1
				if(trainingRelationType.equals("<")) {
					// check the activities
					if(trainingAntecedentResourceID.equals(testAntecedentResourceID) && trainingConsequentResourceID.equals(testConsequentResourceID)) { 
						// check the processing time of antecedent activities
						// if this needs to be compared
						if(parameters.isProcessing()) {
							if(trainingAntecedentResourceProcessingTimeLowerBoundary < testAntecedentResourceProcessingTime && testAntecedentResourceProcessingTime < trainingAntecedentResourceProcessingTimeUpperBoundary) {
								if(trainingConsequentResourceProcessingTimeLowerBoundary < testConsequentResourceProcessingTime && testConsequentResourceProcessingTime < trainingConsequentResourceProcessingTimeUpperBoundary) {
									processing = true;	
								}
							}
						}else { // if not needed
							processing = true;
						}
						
						if(parameters.isTransition()) {
							if(trainingTransitionTimeLowerBoundary < testTransitionTime && testTransitionTime < trainingTransitionTimeUpperBoundary) {
								transition = true;
							}
						}else {
							transition = true;
						}
						
						if(processing && transition) {
							if(normalInRules.containsKey(caseID)) { // if the key already exists
								ArrayList<Relation> temp = normalInRules.get(caseID);
								temp.add(testRelModel.getRelation(j));
								normalInRules.put(caseID, temp);
							}else { // if there exists no key
								ArrayList<Relation> temp = new ArrayList<Relation>();
								temp.add(testRelModel.getRelation(j));
								normalInRules.put(caseID, temp);	
							}
						}					
					}
				}else if(trainingRelationType.equals("m")) {// rule 2
					// check the activities
					if(trainingAntecedentResourceID.equals(testAntecedentResourceID) && trainingConsequentResourceID.equals(testConsequentResourceID)) { 
						// check the processing time of antecedent activities
						// if this needs to be compared
						if(parameters.isProcessing()) {
							if(trainingAntecedentResourceProcessingTimeLowerBoundary < testAntecedentResourceProcessingTime && testAntecedentResourceProcessingTime < trainingAntecedentResourceProcessingTimeUpperBoundary) {
								if(trainingConsequentResourceProcessingTimeLowerBoundary < testConsequentResourceProcessingTime && testConsequentResourceProcessingTime < trainingConsequentResourceProcessingTimeUpperBoundary) {
									processing = true;	
								}
							}
						}else { // if not needed
							processing = true;
						}
						
						if(processing) {
							if(normalInRules.containsKey(caseID)) { // if the key already exists
								ArrayList<Relation> temp = normalInRules.get(caseID);
								temp.add(testRelModel.getRelation(j));
								normalInRules.put(caseID, temp);
							}else { // if there exists no key
								ArrayList<Relation> temp = new ArrayList<Relation>();
								temp.add(testRelModel.getRelation(j));
								normalInRules.put(caseID, temp);	
							}
						}					
					}
				}else if(trainingRelationType.equals("o")) {// rule 3
					// check the activities
					if(trainingAntecedentResourceID.equals(testAntecedentResourceID) && trainingConsequentResourceID.equals(testConsequentResourceID)) { 
						// check the processing time of antecedent activities
						// if this needs to be compared
						if(parameters.isOverlap()) {
							if(trainingOverlapTimeLowerBoundary < testOverlapTime && testOverlapTime < trainingOverlapTimeUpperBoundary) {
								overlap = true;
							}
						}else {
							overlap = true;
						}
						
						if(parameters.isTrueX()) {
							if(trainingTrueXTimeLowerBoundary < testTrueXTime && testTrueXTime < trainingTrueXTimeUpperBoundary) {
								trueX = true;
							}
						}else {
							trueX = true;
						}
						
						if(parameters.isTrueY()) {
							if(trainingTrueYTimeLowerBoundary < testTrueYTime && testTrueYTime < trainingTrueYTimeUpperBoundary) {
								trueY = true;
							}
						}else {
							trueY = true;
						}
						
						if(overlap && trueX && trueY) {
							if(normalInRules.containsKey(caseID)) { // if the key already exists
								ArrayList<Relation> temp = normalInRules.get(caseID);
								temp.add(testRelModel.getRelation(j));
								normalInRules.put(caseID, temp);
							}else { // if there exists no key
								ArrayList<Relation> temp = new ArrayList<Relation>();
								temp.add(testRelModel.getRelation(j));
								normalInRules.put(caseID, temp);	
							}
						}					
					}
				}else if(trainingRelationType.equals("s")) {// rule 4
					// check the activities
					if(trainingAntecedentResourceID.equals(testAntecedentResourceID) && trainingConsequentResourceID.equals(testConsequentResourceID)) { 
						// check the processing time of antecedent activities
						// if this needs to be compared
						if(parameters.isOverlap()) {
							if(trainingOverlapTimeLowerBoundary < testOverlapTime && testOverlapTime < trainingOverlapTimeUpperBoundary) {
								overlap = true;
							}
						}else {
							overlap = true;
						}
						
						if(parameters.isTrueY()) {
							if(trainingTrueYTimeLowerBoundary < testTrueYTime && testTrueYTime < trainingTrueYTimeUpperBoundary) {
								trueY = true;
							}
						}else {
							trueY = true;
						}
						
						if(overlap && trueY) {
							if(normalInRules.containsKey(caseID)) { // if the key already exists
								ArrayList<Relation> temp = normalInRules.get(caseID);
								temp.add(testRelModel.getRelation(j));
								normalInRules.put(caseID, temp);
							}else { // if there exists no key
								ArrayList<Relation> temp = new ArrayList<Relation>();
								temp.add(testRelModel.getRelation(j));
								normalInRules.put(caseID, temp);	
							}
						}					
					}
				}else if(trainingRelationType.equals("d")) {// rule 5
					// check the activities
					if(trainingAntecedentResourceID.equals(testAntecedentResourceID) && trainingConsequentResourceID.equals(testConsequentResourceID)) { 
						// check the processing time of antecedent activities
						// if this needs to be compared
						if(parameters.isOverlap()) {
							if(trainingOverlapTimeLowerBoundary < testOverlapTime && testOverlapTime < trainingOverlapTimeUpperBoundary) {
								overlap = true;
							}
						}else {
							overlap = true;
						}
						
						if(parameters.isTrueX()) {
							if(trainingTrueXTimeLowerBoundary < testTrueXTime && testTrueXTime < trainingTrueXTimeUpperBoundary) {
								trueX = true;
							}
						}else {
							trueX = true;
						}
						
						if(parameters.isTrueY()) {
							if(trainingTrueYTimeLowerBoundary < testTrueYTime && testTrueYTime < trainingTrueYTimeUpperBoundary) {
								trueY = true;
							}
						}else {
							trueY = true;
						}
						
						if(overlap && trueX && trueY) {
							if(normalInRules.containsKey(caseID)) { // if the key already exists
								ArrayList<Relation> temp = normalInRules.get(caseID);
								temp.add(testRelModel.getRelation(j));
								normalInRules.put(caseID, temp);
							}else { // if there exists no key
								ArrayList<Relation> temp = new ArrayList<Relation>();
								temp.add(testRelModel.getRelation(j));
								normalInRules.put(caseID, temp);	
							}
						}					
					}
				}else if(trainingRelationType.equals("f")) {// rule 6
					// check the activities
					if(trainingAntecedentResourceID.equals(testAntecedentResourceID) && trainingConsequentResourceID.equals(testConsequentResourceID)) { 
						// check the processing time of antecedent activities
						// if this needs to be compared
						if(parameters.isOverlap()) {
							if(trainingOverlapTimeLowerBoundary < testOverlapTime && testOverlapTime < trainingOverlapTimeUpperBoundary) {
								overlap = true;
							}
						}else {
							overlap = true;
						}
						
						if(parameters.isTrueX()) {
							if(trainingTrueXTimeLowerBoundary < testTrueXTime && testTrueXTime < trainingTrueXTimeUpperBoundary) {
								trueX = true;
							}
						}else {
							trueY = true;
						}
						
						if(overlap && trueX) {
							if(normalInRules.containsKey(caseID)) { // if the key already exists
								ArrayList<Relation> temp = normalInRules.get(caseID);
								temp.add(testRelModel.getRelation(j));
								normalInRules.put(caseID, temp);
							}else { // if there exists no key
								ArrayList<Relation> temp = new ArrayList<Relation>();
								temp.add(testRelModel.getRelation(j));
								normalInRules.put(caseID, temp);	
							}
						}					
					}
				}else if(trainingRelationType.equals("=")) {// rule 7
					// check the activities
					if(trainingAntecedentResourceID.equals(testAntecedentResourceID) && trainingConsequentResourceID.equals(testConsequentResourceID)) { 
						// check the processing time of antecedent activities
						// if this needs to be compared
						if(parameters.isOverlap()) {
							if(trainingOverlapTimeLowerBoundary < testOverlapTime && testOverlapTime < trainingOverlapTimeUpperBoundary) {
								overlap = true;
							}
						}else {
							overlap = true;
						}
						
						if(overlap) {
							if(normalInRules.containsKey(caseID)) { // if the key already exists
								ArrayList<Relation> temp = normalInRules.get(caseID);
								temp.add(testRelModel.getRelation(j));
								normalInRules.put(caseID, temp);
							}else { // if there exists no key
								ArrayList<Relation> temp = new ArrayList<Relation>();
								temp.add(testRelModel.getRelation(j));
								normalInRules.put(caseID, temp);	
							}
						}					
					}
				}
			}
		}
		
		keys = normalInRules.keySet().iterator();
		System.out.println("Log To Rule");
		while(keys.hasNext()) {
			String key = keys.next();
			int caseSize = testRelModel.getCaseSize(key);
			System.out.println(
					"Case ID: " + key + 
					": " + normalInRules.get(key).size() + " / " + caseSize + 
					" = " + calculateLogToRuleScore(caseSize, normalInRules.get(key).size()));			
		}		
		
		/*
		 * Control-flow Anomaly Score
		 * */
		calculateResourceScore(logToRule, ruleToLog);
	}
	
	private float calculateRuleToLogScore(int rules, int matched) {
		float result = 0;
		
		result = (float)((matched * 1.0) / rules);
		
		return result;
	}
	
	private float calculateLogToRuleScore(int log, int matched) {
		float result = 0;
		
		result = (float)((matched * 1.0) / log);
		
		return result;
	}
	
	private void calculateResourceScore(float logToRule, float ruleToLog) {
		float ratio = parameters.getLogRuleRatio();
		resourceScore = ratio*logToRule + (1-ratio)*ruleToLog;
	}
	
	/*
	 * Getters and Setters
	 * */
	
	public float getResourceScore() {
		return resourceScore;
	}

	public ActivityModel getTrainingActModel() {
		return trainingActModel;
	}

	public void setTrainingActModel(ActivityModel trainingActModel) {
		this.trainingActModel = trainingActModel;
	}

	public ActivityModel getTestActModel() {
		return testActModel;
	}

	public void setTestActModel(ActivityModel testActModel) {
		this.testActModel = testActModel;
	}

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

	public AnomalyProfileModel getProfileModel() {
		return profileModel;
	}

	public void setProfileModel(AnomalyProfileModel profileModel) {
		this.profileModel = profileModel;
	}

	public AnomalyDetectionMiningParameters getParameters() {
		return parameters;
	}

	public void setParameters(AnomalyDetectionMiningParameters parameters) {
		this.parameters = parameters;
	}

	public void setResourceScore(float resourceScore) {
		this.resourceScore = resourceScore;
	}
	
	
}
