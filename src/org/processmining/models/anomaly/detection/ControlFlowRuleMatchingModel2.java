package org.processmining.models.anomaly.detection;

import java.util.ArrayList;

import org.processmining.data.overlap.Overlap;
import org.processmining.data.processing.Processing;
import org.processmining.data.transition.Transition;
import org.processmining.data.trueX.TrueX;
import org.processmining.data.trueY.TrueY;
import org.processmining.models.anomaly.profile.AnomalyProfileModel;
import org.processmining.models.relation.RelationModel;
import org.processmining.plugins.anomaly.detection.AnomalyDetectionMiningParameters;

public class ControlFlowRuleMatchingModel2 {
	
	public ControlFlowRuleMatchingModel2(
			AnomalyProfileModel profileModel, 
			RelationModel testRelModel, 
			AnomalyDetectionMiningParameters parameters) {
		
		// get the rules
		RelationModel rules = profileModel.getRelModel();
		// rules.getAntecedentActivity(0).getProcessingTime();
		
		// get the profile list (i.e., processing, transition, overlap, trueX, trueY)
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
		
		// for each rule, get the profile
		
		// compare the rules and the log
		
			// if the rules and the log match,
		
				// put it in the matched list
		
			// else if it does not match, put it in the unmatched list
		
				// check whether it does not match on the log side
		
					// then put it in the log unmatched list
		
				// else if it does not match on the rule side
		
					// then put it in the rule unmatched list
		
	}
}
