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

import org.processmining.data.anomaly.ControlFlowScore;
import org.processmining.data.overlap.Overlap;
import org.processmining.data.processing.Processing;
import org.processmining.data.relation.RelationMatrix;
import org.processmining.data.transition.Transition;
import org.processmining.data.trueX.TrueX;
import org.processmining.data.trueY.TrueY;
import org.processmining.data.xes.PathDefinition;
import org.processmining.models.activity.ActivityModel;
import org.processmining.models.anomaly.profile.AnomalyProfileModel;
import org.processmining.models.anomaly.score.ControlFlowScoreModel;
import org.processmining.models.relation.RelationModel;
import org.processmining.plugins.anomaly.detection.AnomalyDetectionMiningParameters;

public class ControlFlowRuleMatchingModel extends AbstractRuleMatchingModel{
	
	private ActivityModel trainingActModel;
	private ActivityModel testActModel;
	private RelationModel trainingRelModel;
	private RelationModel testRelModel;
	private AnomalyProfileModel profileModel;
	private AnomalyDetectionMiningParameters parameters;
	
	private Map<String, ControlFlowScore> controlFlowScore;
	
	public ControlFlowRuleMatchingModel(
			ActivityModel trainingActModel, ActivityModel testActModel, 
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
		File file = new File(path.getCurrPathControlFlow());
		FileWriter writer = new FileWriter(file);
		
		controlFlowScore = new HashMap<String, ControlFlowScore>(); // key: caseID, value: control-flow anomaly score
		
		ControlFlowScoreModel cfsm = new ControlFlowScoreModel();
		
		float sigma = parameters.getSigma();
		
		// Cardinality
		RelationMatrix trainingActivityMatrix = trainingRelModel.getRelationActivityMatrix();
		int cfRuleSize = trainingActivityMatrix.getRelationMatrixListSize();
		
		/*
		 * Rule to Log
		 * */
		
		Map<String, Set<String>> normalSet = new HashMap<String, Set<String>>();
		Map<String, ArrayList<String>> normalArrayList = new HashMap<String, ArrayList<String>>();
		Map<String, ArrayList<String>> reasonMap = new HashMap<String, ArrayList<String>>();
		
		ArrayList<String> caseIDList = new ArrayList<String>();
		caseIDList = testRelModel.getCaseIDList();
		
		ArrayList<Processing> processingList = new ArrayList<Processing>();
		processingList = profileModel.getProcessingModel().getProcessingList_Act();
		
		ArrayList<Transition> transitionList = new ArrayList<Transition>();
		transitionList = profileModel.getTransitionModel().getTransitionList_Act();
		
		ArrayList<Overlap> overlapList = new ArrayList<Overlap>();
		overlapList = profileModel.getOverlapModel().getOverlapList_Act();
		
		ArrayList<TrueX> trueXList = new ArrayList<TrueX>();
		trueXList = profileModel.getTrueXModel().getTrueXList_Act();
		
		ArrayList<TrueY> trueYList = new ArrayList<TrueY>();
		trueYList = profileModel.getTrueYModel().getTrueYList_Act();
		
		System.out.println("$$$$$ Control-Flow Perspective Rule Matching");
		writer.append("$$$$$ Control-Flow Perspective Rule Matching" + "\n");
		
		/*
		 * Add Rules to the normalSet
		 * */
		for(int i = 0; i < caseIDList.size(); i++) {
			String caseID = caseIDList.get(i);
			Set<String> temp = new HashSet<String>();
			
			for(int j = 0; j < cfRuleSize; j++) {
				temp.add(trainingActivityMatrix.getPair(j));
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
				temp.add(testRelModel.getRelation(i).getActivity());
				normalArrayList.put(caseID, temp);
			}else {
				ArrayList<String> temp = new ArrayList<String>();
				temp.add(testRelModel.getRelation(i).getActivity());
				normalArrayList.put(caseID, temp);
			}
		}
		
		/*
		 * Control-Flow Rule Matching Model
		 * */
		for(int i = 0; i < cfRuleSize; i++) { // for each rule
			
			String trainingAntecedentActivityID = trainingActivityMatrix.getAntecedent(i);
			String trainingConsequentActivityID = trainingActivityMatrix.getConsequent(i);
			String trainingRelationType = trainingActivityMatrix.getRelationType(i);
			
			int ruleIdx = i+1;
			
			System.out.println(
					"$$$$ Rule # " + ruleIdx + " (" + trainingAntecedentActivityID + " " + trainingRelationType + " " + trainingConsequentActivityID + ")");
			writer.append("$$$$ Rule # " + ruleIdx + " (" + trainingAntecedentActivityID + " " + trainingRelationType + " " + trainingConsequentActivityID + ")" + "\n");
			
			float trainingAntecedentActivityProcessingTimeLowerBoundary = 0;
			float trainingAntecedentActivityProcessingTimeUpperBoundary = 0;
			float trainingConsequentActivityProcessingTimeLowerBoundary = 0;
			float trainingConsequentActivityProcessingTimeUpperBoundary = 0;
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
					if(processingList.get(j).getActivityID().equals(trainingAntecedentActivityID)) {
						trainingAntecedentActivityProcessingTimeLowerBoundary = processingList.get(j).getAvg() - sigma*processingList.get(j).getStdev();
						trainingAntecedentActivityProcessingTimeUpperBoundary = processingList.get(j).getAvg() + sigma*processingList.get(j).getStdev();	
					}
					
					if(processingList.get(j).getActivityID().equals(trainingConsequentActivityID)) {
						trainingConsequentActivityProcessingTimeLowerBoundary = processingList.get(j).getAvg() - sigma*processingList.get(j).getStdev();
						trainingConsequentActivityProcessingTimeUpperBoundary = processingList.get(j).getAvg() + sigma*processingList.get(j).getStdev();
					}
				}
			}

			// get lower and upper boundary for relation time, specific for its relation type
			if(parameters.isTransition()){
				for(int j = 0; j < transitionList.size(); j++) {
					if(transitionList.get(j).getFromActivityID().equals(trainingAntecedentActivityID) 
							&& transitionList.get(j).getToActivityID().equals(trainingConsequentActivityID)
							&& transitionList.get(j).getRelation().equals(trainingRelationType)) {
						trainingTransitionTimeLowerBoundary = transitionList.get(j).getAvg() - sigma*transitionList.get(j).getStdev();
						trainingTransitionTimeUpperBoundary = transitionList.get(j).getAvg() + sigma*transitionList.get(j).getStdev();
					}
				}
			}
			
			if(parameters.isOverlap()) {
				for(int j = 0; j < overlapList.size(); j++) {
					if(overlapList.get(j).getFromActivityID().equals(trainingAntecedentActivityID) 
							&& overlapList.get(j).getToActivityID().equals(trainingConsequentActivityID)
							&& overlapList.get(j).getRelation().equals(trainingRelationType)) {
						trainingOverlapTimeLowerBoundary = overlapList.get(j).getAvg() - sigma*overlapList.get(j).getStdev();
						trainingOverlapTimeUpperBoundary = overlapList.get(j).getAvg() + sigma*overlapList.get(j).getStdev();
					}
				}
			}
			
			if(parameters.isTrueX()) {
				for(int j = 0; j < trueXList.size(); j++) {
					if(trueXList.get(j).getFromActivityID().equals(trainingAntecedentActivityID) 
							&& trueXList.get(j).getToActivityID().equals(trainingConsequentActivityID)
							&& trueXList.get(j).getRelation().equals(trainingRelationType)) {
						trainingTrueXTimeLowerBoundary = trueXList.get(j).getAvg() - sigma*trueXList.get(j).getStdev();
						trainingTrueXTimeUpperBoundary = trueXList.get(j).getAvg() + sigma*trueXList.get(j).getStdev();
					}
				}
			}
			
			if(parameters.isTrueY()) {
				for(int j = 0; j < trueYList.size(); j++) {
					if(trueYList.get(j).getFromActivityID().equals(trainingAntecedentActivityID) 
							&& trueYList.get(j).getToActivityID().equals(trainingConsequentActivityID)
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
					
					String testAntecedentActivityID = testRelModelByCase.getAntecedentActivityID(j);
					String testConsequentActivityID = testRelModelByCase.getConsequentActivityID(j);
					String testRelationType = testRelModelByCase.getRelationType(j);
					
					float testAntecedentActivityProcessingTime = testRelModelByCase.getAntecedentActivity(j).getProcessingTime();
					float testConsequentActivityProcessingTime = testRelModelByCase.getConsequentActivity(j).getProcessingTime();
					
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
							&& trainingAntecedentActivityID.equals(testAntecedentActivityID) 
							&& trainingConsequentActivityID.equals(testConsequentActivityID)) {
						// rule 1
						if(trainingRelationType.equals("<")) {
							// if this needs to be compared
							if(parameters.isProcessing()) {
								// check the processing time of antecedent activities
								if(trainingAntecedentActivityProcessingTimeLowerBoundary <= testAntecedentActivityProcessingTime && testAntecedentActivityProcessingTime <= trainingAntecedentActivityProcessingTimeUpperBoundary) {
									processing_ante = true;
									if(trainingConsequentActivityProcessingTimeLowerBoundary <= testConsequentActivityProcessingTime && testConsequentActivityProcessingTime <= trainingConsequentActivityProcessingTimeUpperBoundary) {
										processing = true;
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
									
									if(temp.equals(testRelModelByCase.getRelation(j).getActivity())) {
										normalArrayList.get(caseID).remove(temp);
										break;
									}
								}
								
								Iterator<String> iter = normalSet.get(caseID).iterator();
								while(iter.hasNext()) {
									String temp = iter.next();
									
									//System.out.println(temp + " ### " + testRelModelByCase.getRelation(j).getActivity());
									
									if(temp.equals(testRelModelByCase.getRelation(j).getActivity())) {
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
								+ testAntecedentActivityProcessingTime
								+ " -> [" + trainingAntecedentActivityProcessingTimeLowerBoundary 
								+ " ~ "  + trainingAntecedentActivityProcessingTimeUpperBoundary 
								+ "], ";
							}
							
							if(!processing && !processing_cons) {
								reason 
								= reason + "processing time not matched. Conseq: "
								+ testConsequentActivityProcessingTime
								+ " -> [" + trainingConsequentActivityProcessingTimeLowerBoundary
								+ " ~ " + trainingConsequentActivityProcessingTimeUpperBoundary 
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
								if(trainingAntecedentActivityProcessingTimeLowerBoundary <= testAntecedentActivityProcessingTime && testAntecedentActivityProcessingTime <= trainingAntecedentActivityProcessingTimeUpperBoundary) {
									processing_ante = true;
									if(trainingConsequentActivityProcessingTimeLowerBoundary <= testConsequentActivityProcessingTime && testConsequentActivityProcessingTime <= trainingConsequentActivityProcessingTimeUpperBoundary) {
										processing = true;
										processing_cons = true;
									}else {
										processing_cons = false;
									}
								}else {
									processing_ante = false;
								}
							}else { // if not needed
								processing = true;
							}
							
							if(processing) {
								doesRuleExist = true;
								reason = reason + "successful";

								Iterator<String> iter2 = normalArrayList.get(caseID).iterator();
								while(iter2.hasNext()) {
									String temp = iter2.next();
									
									if(temp.equals(testRelModelByCase.getRelation(j).getActivity())) {
										normalArrayList.get(caseID).remove(temp);
										break;
									}
								}								
								
								Iterator<String> iter = normalSet.get(caseID).iterator();
								while(iter.hasNext()) {
									String temp = iter.next();
									
									//System.out.println(temp + " ### " + testRelModelByCase.getRelation(j).getActivity());
									
									if(temp.equals(testRelModelByCase.getRelation(j).getActivity())) {
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
								+ testAntecedentActivityProcessingTime
								+ " -> [" + trainingAntecedentActivityProcessingTimeLowerBoundary 
								+ " ~ "  + trainingAntecedentActivityProcessingTimeUpperBoundary 
								+ "], ";
							}
							
							if(!processing && !processing_cons) {
								reason 
								= reason + "processing time not matched. Conseq: "
								+ testConsequentActivityProcessingTime
								+ " -> [" + trainingConsequentActivityProcessingTimeLowerBoundary
								+ " ~ " + trainingConsequentActivityProcessingTimeUpperBoundary 
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
									
									if(temp.equals(testRelModelByCase.getRelation(j).getActivity())) {
										normalArrayList.get(caseID).remove(temp);
										break;
									}
								}
								
								Iterator<String> iter = normalSet.get(caseID).iterator();
								while(iter.hasNext()) {
									String temp = iter.next();
									
									//System.out.println(temp + " ### " + testRelModelByCase.getRelation(j).getActivity());
									
									if(temp.equals(testRelModelByCase.getRelation(j).getActivity())) {
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
									
									if(temp.equals(testRelModelByCase.getRelation(j).getActivity())) {
										normalArrayList.get(caseID).remove(temp);
										break;
									}
								}
								
								Iterator<String> iter = normalSet.get(caseID).iterator();
								while(iter.hasNext()) {
									String temp = iter.next();
									
									//System.out.println(temp + " ### " + testRelModelByCase.getRelation(j).getActivity());
									
									if(temp.equals(testRelModelByCase.getRelation(j).getActivity())) {
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
									
									if(temp.equals(testRelModelByCase.getRelation(j).getActivity())) {
										normalArrayList.get(caseID).remove(temp);
										break;
									}
								}
								
								Iterator<String> iter = normalSet.get(caseID).iterator();
								while(iter.hasNext()) {
									String temp = iter.next();
									
									//System.out.println(temp + " ### " + testRelModelByCase.getRelation(j).getActivity());
									
									if(temp.equals(testRelModelByCase.getRelation(j).getActivity())) {
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
									
									if(temp.equals(testRelModelByCase.getRelation(j).getActivity())) {
										normalArrayList.get(caseID).remove(temp);
										break;
									}
								}
								
								Iterator<String> iter = normalSet.get(caseID).iterator();
								while(iter.hasNext()) {
									String temp = iter.next();
									
									//System.out.println(temp + " ### " + testRelModelByCase.getRelation(j).getActivity());
									
									if(temp.equals(testRelModelByCase.getRelation(j).getActivity())) {
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
									
									if(temp.equals(testRelModelByCase.getRelation(j).getActivity())) {
										normalArrayList.get(caseID).remove(temp);
										break;
									}
								}
								
								Iterator<String> iter = normalSet.get(caseID).iterator();
								while(iter.hasNext()) {
									String temp = iter.next();
									
									//System.out.println(temp + " ### " + testRelModelByCase.getRelation(j).getActivity());
									
									if(temp.equals(testRelModelByCase.getRelation(j).getActivity())) {
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
				
				String temp = trainingAntecedentActivityID+"_"+trainingRelationType+"_"+trainingConsequentActivityID;
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
			
			denom = cfRuleSize;
			numer = normalSet.get(key).size();
			
			if(denom != 0) {
				ruleToLog = divide(numer, denom);
			}else {
				ruleToLog = 0;
			}
			
			System.out.println("Rule-To-Log - Case ID: " + key + ": " + numer + " / " + denom + " = " + ruleToLog);
			writer.append("Rule-To-Log - Case ID: " + key + ": " + numer + " / " + denom + " = " + ruleToLog + "\n");
			cfsm.addRuleToLogElem(key, ruleToLog);
			
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
			cfsm.addLogToRuleElem(key, logToRule);
			
			System.out.println("");
		}
				
		/*
		 * Control-flow Anomaly Score
		 * */
		cfsm.calculateControlFlowScore(parameters);
		this.setControlFlowScore(cfsm.getControlFlowScoreMap());
		
		writer.close();
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
	
	public Map<String, ControlFlowScore> getControlFlowScore() {
		return controlFlowScore;
	}
	
	public void setControlFlowScore(Map<String, ControlFlowScore> map) {
		this.controlFlowScore = map;
	}

	public AnomalyDetectionMiningParameters getParameters() {
		return parameters;
	}

	public void setParameters(AnomalyDetectionMiningParameters parameters) {
		this.parameters = parameters;
	}
}
