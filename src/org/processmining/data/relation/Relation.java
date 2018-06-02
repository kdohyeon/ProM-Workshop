package org.processmining.data.relation;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.processmining.data.activity.Activity;

public class Relation {
	
	private Activity antecedent;
	private Activity consequent;
	private String relationType;
	private String relation;
	
	private float transitionTime;
	private float overlapTime;
	private float trueXTime;
	private float trueYTime;
	
	public Relation(Activity antecedent, Activity consequent) throws ParseException {
		this.antecedent = antecedent;
		this.consequent = consequent;
		
		transitionTime = 0;
		overlapTime = 0;
		trueXTime = 0;
		trueYTime = 0;
		
		relationType = identifyRelationType();
		
		relation = antecedent.getActivityID() + "_" + relationType + "_" + consequent.getActivityID();
	}
	
	
	private String identifyRelationType() throws ParseException {
		
		String date_format = "yyyy-MM-dd HH:mm:ss";
		DateFormat format = new SimpleDateFormat(date_format);
		
		antecedent.setStartTimestamp(antecedent.getStartTimestamp().replace("T", " "));
		//antecedent.setStartTimestamp(antecedent.getStartTimestamp().replace("12", "13"));
		
		antecedent.setCompleteTimestamp(antecedent.getCompleteTimestamp().replace("T", " "));
		//antecedent.setCompleteTimestamp(antecedent.getCompleteTimestamp().replace("12", "13"));
		
		consequent.setStartTimestamp(consequent.getStartTimestamp().replace("T", " "));
		//consequent.setStartTimestamp(consequent.getStartTimestamp().replace("12", "13"));
		
		consequent.setCompleteTimestamp(consequent.getCompleteTimestamp().replace("T", " "));
		//consequent.setCompleteTimestamp(consequent.getCompleteTimestamp().replace("12", "13"));
		
		
		Date ante_start_date = format.parse(antecedent.getStartTimestamp());
		Date cons_start_date = format.parse(consequent.getStartTimestamp());
		Date ante_complete_date = format.parse(antecedent.getCompleteTimestamp());
		Date cons_complete_date = format.parse(consequent.getCompleteTimestamp());
		
		
		
		String relType = ""; 
		RelationType relTypeDef = new RelationType();
		
		//NumberFormat formatter = new DecimalFormat("#0.00");
		//long conversionValue = 24 * 60 * 60 * 1000; // day
		float conversionValue = 60 * 60 * 1000; // hour
		//long diff = (complete_date.getTime() - start_date.getTime()) / (24 * 60 * 60 * 1000);
		
		//processingTime = String.valueOf(formatter.format(diff));
		
		// relation 1
		if(cons_start_date.after(ante_complete_date)) {
			//System.out.println("Relation 1");
			relType = relTypeDef.getRELATIONTYPE1();
			
			float trans = (cons_start_date.getTime() - ante_complete_date.getTime()) / (conversionValue);
			transitionTime = trans;
			
		}else if(cons_start_date.equals(ante_complete_date)) {
			//System.out.println("Relation 2");
			relType = relTypeDef.getRELATIONTYPE2();
			
		}else if(cons_start_date.before(ante_complete_date) && ante_start_date.before(cons_start_date) && ante_complete_date.before(cons_complete_date)) {
			//System.out.println("Relation 3");
			relType = relTypeDef.getRELATIONTYPE3();
			
			float overlap = (ante_complete_date.getTime() - cons_start_date.getTime()) / (conversionValue);
			overlapTime = overlap;
			
			float trueX = (cons_start_date.getTime() - ante_start_date.getTime()) / (conversionValue);
			trueXTime = trueX;
			
			float trueY = (cons_complete_date.getTime() - ante_complete_date.getTime()) / (conversionValue);
			trueYTime = trueY;
			
		}else if(
				(ante_start_date.equals(cons_start_date) && ante_complete_date.before(cons_complete_date))
				|| (ante_start_date.equals(cons_start_date) && ante_complete_date.after(cons_complete_date))) {
			// relation 4
			relType = relTypeDef.getRELATIONTYPE4();
			
			float trueX = 0;
			float trueY = 0;
			float overlap = 0;
			
			if(ante_complete_date.before(cons_complete_date)) {
				trueY = consequent.getProcessingTime() - antecedent.getProcessingTime();
				overlap = (antecedent.getProcessingTime());
			}else {
				trueX = antecedent.getProcessingTime() - consequent.getProcessingTime();
				overlap = (consequent.getProcessingTime());
			}
			
			trueXTime = trueX;
			trueYTime = trueY;
			overlapTime = overlap;
			
		}else if(ante_start_date.before(cons_start_date) && ante_complete_date.after(cons_complete_date)){
			// relation 5
			relType = relTypeDef.getRELATIONTYPE5();
			
			float overlap = consequent.getProcessingTime();
			float trueX = (cons_start_date.getTime() - ante_start_date.getTime()) / (conversionValue);
			float trueY = (ante_complete_date.getTime() - cons_complete_date.getTime()) / (conversionValue);
			
			overlapTime = overlap;
			trueXTime = trueX;
			trueYTime = trueY;
			
		}else if(ante_start_date.before(cons_start_date) && ante_complete_date.equals(cons_complete_date)){
			// relation 6
			relType = relTypeDef.getRELATIONTYPE6();
			
			float overlap = (consequent.getProcessingTime());
			float trueX = antecedent.getProcessingTime() - consequent.getProcessingTime();
			//long trueY = (long)(consequent.getProcessingTime());
			
			trueXTime = trueX;
			overlapTime = overlap;
			//trueYTime = trueY;
			
		}else if(ante_start_date.equals(cons_start_date) && ante_complete_date.equals(cons_complete_date)){
			// relation 7
			relType = relTypeDef.getRELATIONTYPE7();
			float overlap = (consequent.getProcessingTime());
			overlapTime = overlap;
		}else {
			relType = "error";
			
			System.out.println("########");
			System.out.println("error");
			System.out.println(antecedent.getCaseID());
			System.out.println(antecedent.getCaseID() + ": " + antecedent.getActivityID() + " (" + antecedent.getStartTimestamp() + ", " + antecedent.getCompleteTimestamp() + ")");
			System.out.println(consequent.getCaseID() + ": " + consequent.getActivityID() + " (" + consequent.getStartTimestamp() + ", " + consequent.getCompleteTimestamp() + ")");
			System.out.println("########");
		}
		
		return relType;		
	}

	public Activity getAntecedent() {
		return antecedent;
	}

	public void setAntecedent(Activity antecedent) {
		this.antecedent = antecedent;
	}

	public Activity getConsequent() {
		return consequent;
	}

	public void setConsequent(Activity consequent) {
		this.consequent = consequent;
	}

	public String getRelationType() {
		return relationType;
	}

	public void setRelationType(String relationType) {
		this.relationType = relationType;
	}

	public float getTransitionTime() {
		return transitionTime;
	}

	public void setTransitionTime(float transitionTime) {
		this.transitionTime = transitionTime;
	}

	public float getOverlapTime() {
		return overlapTime;
	}

	public void setOverlapTime(float overlapTime) {
		this.overlapTime = overlapTime;
	}

	public float getTrueXTime() {
		return trueXTime;
	}

	public void setTrueXTime(float trueXTime) {
		this.trueXTime = trueXTime;
	}

	public float getTrueYTime() {
		return trueYTime;
	}

	public void setTrueYTime(float trueYTime) {
		this.trueYTime = trueYTime;
	}

	public String getActivity() {
		return antecedent.getActivityID() + "_" + relationType + "_" + consequent.getActivityID();
	}
	
	public String getResource() {
		return antecedent.getResourceID() + "_" + relationType + "_" + consequent.getResourceID();
	}


	public String getRelation() {
		return relation;
	}


	public void setRelation(String relation) {
		this.relation = relation;
	}
}
