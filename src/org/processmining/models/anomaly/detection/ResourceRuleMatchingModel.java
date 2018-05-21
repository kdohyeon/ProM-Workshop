package org.processmining.models.anomaly.detection;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.processmining.data.anomaly.ResourceScore;
import org.processmining.data.overlap.Overlap;
import org.processmining.data.processing.Processing;
import org.processmining.data.relation.RelationMatrix;
import org.processmining.data.transition.Transition;
import org.processmining.data.trueX.TrueX;
import org.processmining.data.trueY.TrueY;
import org.processmining.data.xes.PathDefinition;
import org.processmining.models.activity.ActivityModel;
import org.processmining.models.anomaly.profile.AnomalyProfileModel;
import org.processmining.models.anomaly.score.ResourceScoreModel;
import org.processmining.models.relation.RelationModel;
import org.processmining.plugins.anomaly.detection.AnomalyDetectionMiningParameters;

public class ResourceRuleMatchingModel extends AbstractRuleMatchingModel{
	
	private ActivityModel trainingActModel;
	private ActivityModel testActModel;
	private RelationModel trainingRelModel;
	private RelationModel testRelModel;
	private AnomalyProfileModel profileModel;
	private AnomalyDetectionMiningParameters parameters;
	
	private Map<String, ResourceScore> resourceScore;
	
	public ResourceRuleMatchingModel(ActivityModel trainingActModel, ActivityModel testActModel, 
			RelationModel trainingRelModel, RelationModel testRelModel, 
			AnomalyProfileModel profileModel, AnomalyDetectionMiningParameters parameters) throws IOException {
		
		/*
		 * Parameters
		 * */
		this.setTrainingActModel(trainingActModel);
		this.setTestActModel(testActModel);
		this.setTrainingRelModel(trainingRelModel);
		this.setTestRelModel(testRelModel);
		this.setProfileModel(profileModel);
		this.setParameters(parameters);
		
		PathDefinition path = new PathDefinition();
		File file = new File(path.getCurrPathResource());
		FileWriter writer = new FileWriter(file);
		
		resourceScore = new HashMap<String, ResourceScore>(); // key: caseID, value: resource anomaly score
		
		ResourceScoreModel rsm = new ResourceScoreModel();
		
		float sigma = parameters.getSigma();
		
		// Cardinality
		RelationMatrix trainingResourceMatrix = trainingRelModel.getRelationResourceMatrix();
		int rRuleSize = trainingResourceMatrix.getRelationMatrixListSize();
		
		/*
		 * Rule to Log
		 * */
		
		Map<String, Set<String>> normalSet = new HashMap<String, Set<String>>();
		Map<String, ArrayList<String>> normalArrayList = new HashMap<String, ArrayList<String>>();
		Map<String, ArrayList<String>> reasonMap = new HashMap<String, ArrayList<String>>();
		ArrayList<String> caseIDList = new ArrayList<String>();
		caseIDList = testRelModel.getCaseIDList();
		
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
		
		System.out.println("");
		System.out.println("$$$$$ Resource Perspective Rule Matching");
		
		/*
		 * Add Rules to the normalSet
		 * */
		for(int i = 0; i < caseIDList.size(); i++) {
			String caseID = caseIDList.get(i);
			Set<String> temp = new HashSet<String>();
			
			for(int j = 0; j < rRuleSize; j++) {
				temp.add(trainingResourceMatrix.getPair(j));
			}	
			
			normalSet.put(caseID, temp);
		}
		
		/*
		 * Add Log Relations to the normalArrayList
		 * */
		for(int i = 0; i < testRelModel.getRelationCardinality(); i++) {
			String caseID = testRelModel.getCaseID(i);
			
			if(normalArrayList.containsKey(caseID)) {
				ArrayList<String> temp = normalArrayList.get(caseID);
				temp.add(testRelModel.getRelation(i).getResource());
				normalArrayList.put(caseID, temp);
			}else {
				ArrayList<String> temp = new ArrayList<String>();
				temp.add(testRelModel.getRelation(i).getResource());
				normalArrayList.put(caseID, temp);
			}
		}
		
		/*
		 * Resource Rule Matching Model
		 * */
		for(int i = 0; i < rRuleSize; i++) { // for each rule
			
			String trainingAntecedentResourceID = trainingResourceMatrix.getAntecedent(i);
			String trainingConsequentResourceID = trainingResourceMatrix.getConsequent(i);
			String trainingRelationType = trainingResourceMatrix.getRelationType(i);
			
			int ruleIdx = i+1;
			
			System.out.println("$$$$ Rule # " + ruleIdx + " (" + trainingAntecedentResourceID + " " + trainingRelationType + " " + trainingConsequentResourceID + ")");
			writer.append("$$$$ Rule # " + ruleIdx + " (" + trainingAntecedentResourceID + " " + trainingRelationType + " " + trainingConsequentResourceID + ")");
			
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
			if(parameters.isTransition()){
				for(int j = 0; j < transitionList.size(); j++) {
					if(transitionList.get(j).getFromResourceID().equals(trainingAntecedentResourceID) 
							&& transitionList.get(j).getToResourceID().equals(trainingConsequentResourceID)
							&& transitionList.get(j).getRelation().equals(trainingRelationType)) {
						trainingTransitionTimeLowerBoundary = transitionList.get(j).getAvg() - sigma*transitionList.get(j).getStdev();
						trainingTransitionTimeUpperBoundary = transitionList.get(j).getAvg() + sigma*transitionList.get(j).getStdev();
					}
				}
			}
			
			if(parameters.isOverlap()) {
				for(int j = 0; j < overlapList.size(); j++) {
					if(overlapList.get(j).getFromResourceID().equals(trainingAntecedentResourceID) 
							&& overlapList.get(j).getToResourceID().equals(trainingConsequentResourceID)
							&& overlapList.get(j).getRelation().equals(trainingRelationType)) {
						trainingOverlapTimeLowerBoundary = overlapList.get(j).getAvg() - sigma*overlapList.get(j).getStdev();
						trainingOverlapTimeUpperBoundary = overlapList.get(j).getAvg() + sigma*overlapList.get(j).getStdev();
					}
				}
			}
			
			if(parameters.isTrueX()) {
				for(int j = 0; j < trueXList.size(); j++) {
					if(trueXList.get(j).getFromResourceID().equals(trainingAntecedentResourceID) 
							&& trueXList.get(j).getToResourceID().equals(trainingConsequentResourceID)
							&& trueXList.get(j).getRelation().equals(trainingRelationType)) {
						trainingTrueXTimeLowerBoundary = trueXList.get(j).getAvg() - sigma*trueXList.get(j).getStdev();
						trainingTrueXTimeUpperBoundary = trueXList.get(j).getAvg() + sigma*trueXList.get(j).getStdev();
					}
				}
			}
			
			if(parameters.isTrueY()) {
				for(int j = 0; j < trueYList.size(); j++) {
					if(trueYList.get(j).getFromResourceID().equals(trainingAntecedentResourceID) 
							&& trueYList.get(j).getToResourceID().equals(trainingConsequentResourceID)
							&& trueYList.get(j).getRelation().equals(trainingRelationType)) {
						trainingTrueYTimeLowerBoundary = trueYList.get(j).getAvg() - sigma*trueYList.get(j).getStdev();
						trainingTrueYTimeUpperBoundary = trueYList.get(j).getAvg() + sigma*trueYList.get(j).getStdev();
					}
				}
			}
				
			// for each unique case ID
			for(int k = 0; k < caseIDList.size(); k++) {

				// get testRelModel for that specific case ID
				String caseID = caseIDList.get(k);
				RelationModel testRelModelByCase = new RelationModel(this.testActModel, caseID);
				int logSizeByCase = testRelModelByCase.getCaseSize(caseID);
				
				boolean doesRuleExist = false;
				String reason = "";
				
				// check if current rule exists in the log (by case)
				for(int j = 0; j < logSizeByCase; j++) {
					
					String testAntecedentResourceID = testRelModelByCase.getAntecedentResource(j);
					String testConsequentResourceID = testRelModelByCase.getConsequentResource(j);
					String testRelationType = testRelModelByCase.getRelationType(j);
					
					float testAntecedentResourceProcessingTime = testRelModelByCase.getAntecedentActivity(j).getProcessingTime();
					float testConsequentResourceProcessingTime = testRelModelByCase.getConsequentActivity(j).getProcessingTime();
					
					float testTransitionTime = testRelModelByCase.getTransitionTime(j);
					float testOverlapTime = testRelModelByCase.getOverlapTime(j);
					float testTrueXTime = testRelModelByCase.getTrueXTime(j);
					float testTrueYTime = testRelModelByCase.getTrueYTime(j);
					
					boolean processing = false;
					boolean processing_ante = false;
					boolean processing_cons = false;
					boolean transition = false;
					boolean overlap = false;
					boolean trueX = false;
					boolean trueY = false;
					
					if(trainingRelationType.equals(testRelationType) 
							&& trainingAntecedentResourceID.equals(testAntecedentResourceID) 
							&& trainingConsequentResourceID.equals(testConsequentResourceID)) {
						// rule 1
						if(trainingRelationType.equals("<")) {
							// if this needs to be compared
							if(parameters.isProcessing()) {
								// check the processing time of antecedent activities
								if(trainingAntecedentResourceProcessingTimeLowerBoundary <= testAntecedentResourceProcessingTime && testAntecedentResourceProcessingTime <= trainingAntecedentResourceProcessingTimeUpperBoundary) {
									if(trainingConsequentResourceProcessingTimeLowerBoundary <= testConsequentResourceProcessingTime && testConsequentResourceProcessingTime <= trainingConsequentResourceProcessingTimeUpperBoundary) {
										processing = true;
										processing_ante = true;
										processing_cons = true;
									}else {
										processing_cons = false;
									}
								}else {
									processing_ante = false;
								}
							}else { // if not needed
								processing = true;
								processing_ante = true;
								processing_cons = true;
							}
							
							// if this needs to be compared
							if(parameters.isTransition()) {
								if(trainingTransitionTimeLowerBoundary <= testTransitionTime && testTransitionTime <= trainingTransitionTimeUpperBoundary) {
									transition = true;
									
								}
							}else {
								transition = true;
							}
							
							if(processing && transition) {
								doesRuleExist = true;
								reason = reason + "successful";
								Iterator<String> iter2 = normalArrayList.get(caseID).iterator();
								while(iter2.hasNext()) {
									String temp = iter2.next();
									
									if(temp.equals(testRelModelByCase.getRelation(j).getResource())) {
										normalArrayList.get(caseID).remove(temp);
										break;
									}
								}
								
								Iterator<String> iter = normalSet.get(caseID).iterator();
								while(iter.hasNext()) {
									String temp = iter.next();
									
									if(temp.equals(testRelModelByCase.getRelation(j).getResource())) {
										//System.out.println("Found!");
										//System.out.println(normalSet.get(caseID).remove(temp));
										normalSet.get(caseID).remove(temp);
										break;
									}
								}
							}
							
							if(!processing && !processing_ante) {
								reason 
								= reason + "processing time not matched. Ante: "
								+ testAntecedentResourceProcessingTime
								+ " -> [" + trainingAntecedentResourceProcessingTimeLowerBoundary 
								+ " ~ "  + trainingAntecedentResourceProcessingTimeUpperBoundary 
								+ "], ";
							}
							
							if(!processing && !processing_cons) {
								reason 
								= reason + "processing time not matched. Conseq: "
								+ testConsequentResourceProcessingTime
								+ " -> [" + trainingConsequentResourceProcessingTimeLowerBoundary
								+ " ~ " + trainingConsequentResourceProcessingTimeUpperBoundary 
								+ "], ";
							}
							
							if(!transition) {
								reason 
								= reason 
								+ "transition time not matched. Tran. Time: " 
								+ testTransitionTime 
								+ " -> [" 
								+ trainingTransitionTimeLowerBoundary 
								+ " ~ " 
								+ trainingTransitionTimeUpperBoundary 
								+ "]";
							}
							
						}else if(trainingRelationType.equals("m")) {// rule 2
							// if this needs to be compared
							if(parameters.isProcessing()) {
								if(trainingAntecedentResourceProcessingTimeLowerBoundary <= testAntecedentResourceProcessingTime && testAntecedentResourceProcessingTime <= trainingAntecedentResourceProcessingTimeUpperBoundary) {
									if(trainingConsequentResourceProcessingTimeLowerBoundary <= testConsequentResourceProcessingTime && testConsequentResourceProcessingTime <= trainingConsequentResourceProcessingTimeUpperBoundary) {
										processing = true;
										processing_ante = true;
										processing_cons = true;
									}else {
										processing_cons = false;
									}
								}else {
									processing_ante = false;
								}
							}else { // if not needed
								processing = true;
								processing_ante = true;
								processing_cons = true;
							}
							
							if(processing) {
								doesRuleExist = true;
								reason = reason + "successful";
								Iterator<String> iter2 = normalArrayList.get(caseID).iterator();
								while(iter2.hasNext()) {
									String temp = iter2.next();
									
									if(temp.equals(testRelModelByCase.getRelation(j).getResource())) {
										normalArrayList.get(caseID).remove(temp);
										break;
									}
								}
								
								Iterator<String> iter = normalSet.get(caseID).iterator();
								while(iter.hasNext()) {
									String temp = iter.next();
									
									if(temp.equals(testRelModelByCase.getRelation(j).getResource())) {
										//System.out.println("Found!");
										//System.out.println(normalSet.get(caseID).remove(temp));
										normalSet.get(caseID).remove(temp);
										break;
									}
								}
							}
							
							if(!processing && !processing_ante) {
								reason 
								= reason + "processing time not matched. Ante: "
								+ testAntecedentResourceProcessingTime
								+ " -> [" + trainingAntecedentResourceProcessingTimeLowerBoundary 
								+ " ~ "  + trainingAntecedentResourceProcessingTimeUpperBoundary 
								+ "], ";
							}
							
							if(!processing && !processing_cons) {
								reason 
								= reason + "processing time not matched. Conseq: "
								+ testConsequentResourceProcessingTime
								+ " -> [" + trainingConsequentResourceProcessingTimeLowerBoundary
								+ " ~ " + trainingConsequentResourceProcessingTimeUpperBoundary 
								+ "], ";
							}
							
						}else if(trainingRelationType.equals("o")) {// rule 3
							 
							// check the processing time of antecedent activities
							// if this needs to be compared
							if(parameters.isOverlap()) {
								if(trainingOverlapTimeLowerBoundary <= testOverlapTime && testOverlapTime <= trainingOverlapTimeUpperBoundary) {
									overlap = true;
								}
							}else {
								overlap = true;
							}
							
							if(parameters.isTrueX()) {
								if(trainingTrueXTimeLowerBoundary <= testTrueXTime && testTrueXTime <= trainingTrueXTimeUpperBoundary) {
									trueX = true;
								}
							}else {
								trueX = true;
							}
							
							if(parameters.isTrueY()) {
								if(trainingTrueYTimeLowerBoundary <= testTrueYTime && testTrueYTime <= trainingTrueYTimeUpperBoundary) {
									trueY = true;
								}
							}else {
								trueY = true;
							}
							
							if(overlap && trueX && trueY) {
								doesRuleExist = true;
								reason = reason + "successful";
								Iterator<String> iter2 = normalArrayList.get(caseID).iterator();
								while(iter2.hasNext()) {
									String temp = iter2.next();
									
									if(temp.equals(testRelModelByCase.getRelation(j).getResource())) {
										normalArrayList.get(caseID).remove(temp);
										break;
									}
								}
								
								Iterator<String> iter = normalSet.get(caseID).iterator();
								while(iter.hasNext()) {
									String temp = iter.next();
									
									if(temp.equals(testRelModelByCase.getRelation(j).getResource())) {
										//System.out.println("Found!");
										//System.out.println(normalSet.get(caseID).remove(temp));
										normalSet.get(caseID).remove(temp);
										break;
									}
								}
							}
							
							if(!overlap) {
								reason 
								= reason 
								+ "Overlap time not matched. Overlap Time: " 
								+ testOverlapTime 
								+ " -> [" 
								+ trainingOverlapTimeLowerBoundary 
								+ " ~ " 
								+ trainingOverlapTimeUpperBoundary 
								+ "]";
							}
							
							if(!trueX) {
								reason 
								= reason 
								+ "TrueX time not matched. TrueX Time: " 
								+ testTrueXTime 
								+ " -> [" 
								+ trainingTrueXTimeLowerBoundary 
								+ " ~ " 
								+ trainingTrueXTimeUpperBoundary 
								+ "]";
							}
							
							if(!trueY) {
								reason 
								= reason 
								+ "TrueY time not matched. TrueX Time: " 
								+ testTrueYTime 
								+ " -> [" 
								+ trainingTrueYTimeLowerBoundary 
								+ " ~ " 
								+ trainingTrueYTimeUpperBoundary 
								+ "]";
							}
							
						}else if(testRelationType.equals("s")) {// rule 4
							 
							// check the processing time of antecedent activities
							// if this needs to be compared
							if(parameters.isOverlap()) {
								if(trainingOverlapTimeLowerBoundary <= testOverlapTime && testOverlapTime <= trainingOverlapTimeUpperBoundary) {
									overlap = true;
								}
							}else {
								overlap = true;
							}
							
							if(parameters.isTrueY()) {
								if(trainingTrueYTimeLowerBoundary <= testTrueYTime && testTrueYTime <= trainingTrueYTimeUpperBoundary) {
									trueY = true;
								}
							}else {
								trueY = true;
							}
							
							if(overlap && trueY) {
								doesRuleExist = true;
								reason = reason + "successful";
								Iterator<String> iter2 = normalArrayList.get(caseID).iterator();
								while(iter2.hasNext()) {
									String temp = iter2.next();
									
									if(temp.equals(testRelModelByCase.getRelation(j).getResource())) {
										normalArrayList.get(caseID).remove(temp);
										break;
									}
								}
								
								Iterator<String> iter = normalSet.get(caseID).iterator();
								while(iter.hasNext()) {
									String temp = iter.next();
									
									if(temp.equals(testRelModelByCase.getRelation(j).getResource())) {
										//System.out.println("Found!");
										//System.out.println(normalSet.get(caseID).remove(temp));
										normalSet.get(caseID).remove(temp);
										break;
									}
								}
							}	
							
							if(!overlap) {
								reason 
								= reason 
								+ "Overlap time not matched. Overlap Time: " 
								+ testOverlapTime 
								+ " -> [" 
								+ trainingOverlapTimeLowerBoundary 
								+ " ~ " 
								+ trainingOverlapTimeUpperBoundary 
								+ "]";
							}
							
							if(!trueY) {
								reason 
								= reason 
								+ "TrueY time not matched. TrueX Time: " 
								+ testTrueYTime 
								+ " -> [" 
								+ trainingTrueYTimeLowerBoundary 
								+ " ~ " 
								+ trainingTrueYTimeUpperBoundary 
								+ "]";
							}
							
						}else if(trainingRelationType.equals("d")) {// rule 5
							 
							// check the processing time of antecedent activities
							// if this needs to be compared
							if(parameters.isOverlap()) {
								if(trainingOverlapTimeLowerBoundary <= testOverlapTime && testOverlapTime <= trainingOverlapTimeUpperBoundary) {
									overlap = true;
								}
							}else {
								overlap = true;
							}
							
							if(parameters.isTrueX()) {
								if(trainingTrueXTimeLowerBoundary <= testTrueXTime && testTrueXTime <= trainingTrueXTimeUpperBoundary) {
									trueX = true;
								}
							}else {
								trueX = true;
							}
							
							if(parameters.isTrueY()) {
								if(trainingTrueYTimeLowerBoundary <= testTrueYTime && testTrueYTime <= trainingTrueYTimeUpperBoundary) {
									trueY = true;
								}
							}else {
								trueY = true;
							}
							
							if(overlap && trueX && trueY) {
								doesRuleExist = true;
								reason = reason + "successful";
								Iterator<String> iter2 = normalArrayList.get(caseID).iterator();
								while(iter2.hasNext()) {
									String temp = iter2.next();
									
									if(temp.equals(testRelModelByCase.getRelation(j).getResource())) {
										normalArrayList.get(caseID).remove(temp);
										break;
									}
								}
								
								Iterator<String> iter = normalSet.get(caseID).iterator();
								while(iter.hasNext()) {
									String temp = iter.next();
									
									if(temp.equals(testRelModelByCase.getRelation(j).getResource())) {
										//System.out.println("Found!");
										//System.out.println(normalSet.get(caseID).remove(temp));
										normalSet.get(caseID).remove(temp);
										break;
									}
								}
							}		
							
							if(!overlap) {
								reason 
								= reason 
								+ "Overlap time not matched. Overlap Time: " 
								+ testOverlapTime 
								+ " -> [" 
								+ trainingOverlapTimeLowerBoundary 
								+ " ~ " 
								+ trainingOverlapTimeUpperBoundary 
								+ "]";
							}
							
							if(!trueX) {
								reason 
								= reason 
								+ "TrueX time not matched. TrueX Time: " 
								+ testTrueXTime 
								+ " -> [" 
								+ trainingTrueXTimeLowerBoundary 
								+ " ~ " 
								+ trainingTrueXTimeUpperBoundary 
								+ "]";
							}
							
							if(!trueY) {
								reason 
								= reason 
								+ "TrueY time not matched. TrueX Time: " 
								+ testTrueYTime 
								+ " -> [" 
								+ trainingTrueYTimeLowerBoundary 
								+ " ~ " 
								+ trainingTrueYTimeUpperBoundary 
								+ "]";
							}
							
						}else if(trainingRelationType.equals("f")) {// rule 6
							 
							// check the processing time of antecedent activities
							// if this needs to be compared
							if(parameters.isOverlap()) {
								if(trainingOverlapTimeLowerBoundary <= testOverlapTime && testOverlapTime <= trainingOverlapTimeUpperBoundary) {
									overlap = true;
								}
							}else {
								overlap = true;
							}
							
							if(parameters.isTrueX()) {
								if(trainingTrueXTimeLowerBoundary <= testTrueXTime && testTrueXTime <= trainingTrueXTimeUpperBoundary) {
									trueX = true;
								}
							}else {
								trueY = true;
							}
							
							if(overlap && trueX) {
								doesRuleExist = true;
								reason = reason + "successful";
								Iterator<String> iter2 = normalArrayList.get(caseID).iterator();
								while(iter2.hasNext()) {
									String temp = iter2.next();
									
									if(temp.equals(testRelModelByCase.getRelation(j).getResource())) {
										normalArrayList.get(caseID).remove(temp);
										break;
									}
								}
								
								Iterator<String> iter = normalSet.get(caseID).iterator();
								while(iter.hasNext()) {
									String temp = iter.next();
									
									if(temp.equals(testRelModelByCase.getRelation(j).getResource())) {
										//System.out.println("Found!");
										//System.out.println(normalSet.get(caseID).remove(temp));
										normalSet.get(caseID).remove(temp);
										break;
									}
								}
							}		
							
							if(!overlap) {
								reason 
								= reason 
								+ "Overlap time not matched. Overlap Time: " 
								+ testOverlapTime 
								+ " -> [" 
								+ trainingOverlapTimeLowerBoundary 
								+ " ~ " 
								+ trainingOverlapTimeUpperBoundary 
								+ "]";
							}
							
							if(!trueX) {
								reason 
								= reason 
								+ "TrueX time not matched. TrueX Time: " 
								+ testTrueXTime 
								+ " -> [" 
								+ trainingTrueXTimeLowerBoundary 
								+ " ~ " 
								+ trainingTrueXTimeUpperBoundary 
								+ "]";
							}
						}else if(trainingRelationType.equals("=")) {// rule 7
							 
							// check the processing time of antecedent activities
							// if this needs to be compared
							if(parameters.isOverlap()) {
								if(trainingOverlapTimeLowerBoundary <= testOverlapTime && testOverlapTime <= trainingOverlapTimeUpperBoundary) {
									overlap = true;
								}
							}else {
								overlap = true;
							}
							
							if(overlap) {
								doesRuleExist = true;
								reason = reason + "successful";
								Iterator<String> iter2 = normalArrayList.get(caseID).iterator();
								while(iter2.hasNext()) {
									String temp = iter2.next();
									
									if(temp.equals(testRelModelByCase.getRelation(j).getResource())) {
										normalArrayList.get(caseID).remove(temp);
										break;
									}
								}
								
								Iterator<String> iter = normalSet.get(caseID).iterator();
								while(iter.hasNext()) {
									String temp = iter.next();
									
									if(temp.equals(testRelModelByCase.getRelation(j).getResource())) {
										//System.out.println("Found!");
										//System.out.println(normalSet.get(caseID).remove(temp));
										normalSet.get(caseID).remove(temp);
										break;
									}
								}
							}					
							
							if(!overlap) {
								reason 
								= reason 
								+ "Overlap time not matched. Overlap Time: " 
								+ testOverlapTime 
								+ " -> [" 
								+ trainingOverlapTimeLowerBoundary 
								+ " ~ " 
								+ trainingOverlapTimeUpperBoundary 
								+ "]";
							}
						}
					}
				}
				
				if(reason.equals("")) {
					reason = "rule does not match";
				}
				
				String temp = trainingAntecedentResourceID+"_"+trainingRelationType+"_"+trainingConsequentResourceID;
				System.out.println("$$$ " + caseID + ": " + temp + " " + reason);
				writer.append("$$$ " + caseID + ": " + temp + " " + reason + "\n");
				
				if(reasonMap.containsKey(caseID)) {
					ArrayList<String> tempArrayList = reasonMap.get(caseID);
					tempArrayList.add(temp + " : " + reason);
					reasonMap.put(caseID, tempArrayList);
				}else {
					ArrayList<String> tempArrayList = new ArrayList<String>();
					tempArrayList.add(temp + " : " + reason);
					reasonMap.put(caseID, tempArrayList);
				}
			}
		}
		
		for(int k = 0; k < caseIDList.size(); k++) {
			String key = caseIDList.get(k);
			int denom, numer;
			float ruleToLog;
			
			denom = rRuleSize;
			numer = normalSet.get(key).size();
			if(denom != 0) {
				ruleToLog = divide(numer, denom);
			}else {
				ruleToLog = 0;
			}
			
			System.out.println("Rule-To-Log - Case ID: " + key + ": " + numer + " / " + denom + " = " + ruleToLog);
			writer.append("Rule-To-Log - Case ID: " + key + ": " + numer + " / " + denom + " = " + ruleToLog + "\n");
			rsm.addRuleToLogElem(key, ruleToLog);
			
			int caseSize = testRelModel.getCaseSize(key);
			denom = caseSize;
			numer = normalArrayList.get(key).size();
			float logToRule;

			if(denom != 0) {
				logToRule = divide(numer, denom);
			}else {
				logToRule = 0;
			}
			
			System.out.println("Log-To-Rule - Case ID: " + key + ": " + numer + " / " + denom + " = " + logToRule);
			writer.append("Log-To-Rule - Case ID: " + key + ": " + numer + " / " + denom + " = " + logToRule + "\n");
			rsm.addLogToRuleElem(key, logToRule);
			
			System.out.println("");
		}
		
		/*
		Iterator<String> normalInLogkeys = normalInLog.keySet().iterator();
		
		while(normalInLogkeys.hasNext()) {
			String key = normalInLogkeys.next();
			
			int denom, numer;
			
			denom = rRuleSize;
			numer = normalSet.get(key).size();
			
			System.out.println(
					"Rule-To-Log - Case ID: " + key + 
					": " + numer + " / " + denom + 
					" = " + divide(numer, denom));
			
			rsm.addRuleToLogElem(key, divide(numer, denom));
			
			int caseSize = testRelModel.getCaseSize(key);
			denom = caseSize;
			numer = caseSize - normalInLog.get(key).size();
			//numer = caseSize - normalSet.get(key).size();
			System.out.println(
					"Log-To-Rule - Case ID: " + key + 
					": " + numer + " / " + denom + 
					" = " + divide(numer, denom));
			
			rsm.addLogToRuleElem(key, divide(numer, denom));
			
			System.out.println("");
		}
		*/
		
		/*
		 * Resource Anomaly Score
		 * */
		rsm.calculateResourceScore(parameters);
		this.setResourceScore(rsm.getResourceScoreMap());
	}
	
	/*
	 * Getters and Setters
	 * */
	
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
	
	public Map<String, ResourceScore> getResourceScore() {
		return resourceScore;
	}
	
	public void setResourceScore(Map<String, ResourceScore> map) {
		this.resourceScore = map;
	}

	public AnomalyDetectionMiningParameters getParameters() {
		return parameters;
	}

	public void setParameters(AnomalyDetectionMiningParameters parameters) {
		this.parameters = parameters;
	}
}
