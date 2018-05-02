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
	
	public Relation(Activity antecedent, Activity consequent) throws ParseException {
		this.antecedent = antecedent;
		this.consequent = consequent;
		
		relationType = identifyRelationType();
	}
	
	private String identifyRelationType() throws ParseException {
		
		String date_format = "yyyy-MM-dd";
		DateFormat format = new SimpleDateFormat(date_format);
		Date ante_start_date = format.parse(antecedent.getStartTimestamp());
		Date cons_start_date = format.parse(consequent.getStartTimestamp());
		Date ante_complete_date = format.parse(antecedent.getCompleteTimestamp());
		Date cons_complete_date = format.parse(consequent.getCompleteTimestamp());
		
		String relType = ""; 
		RelationType relTypeDef = new RelationType();
		
		// relation 1
		if(cons_start_date.after(ante_complete_date)) {
			//System.out.println("Relation 1");
			relType = relTypeDef.getRELATIONTYPE1();
			
		}else if(cons_start_date.equals(ante_complete_date)) {
			//System.out.println("Relation 2");
			relType = relTypeDef.getRELATIONTYPE2();
			
		}else if(cons_start_date.before(ante_complete_date) && ante_start_date.before(cons_start_date) && ante_complete_date.before(cons_complete_date)) {
			//System.out.println("Relation 3");
			relType = relTypeDef.getRELATIONTYPE3();
			
		}else if(
				(ante_start_date.equals(cons_start_date) && ante_complete_date.before(cons_complete_date))
				|| (ante_start_date.equals(cons_start_date) && ante_complete_date.after(cons_complete_date))) {
			relType = relTypeDef.getRELATIONTYPE4();
			
		}else if(ante_start_date.before(cons_start_date) && ante_complete_date.after(cons_complete_date)){
			relType = relTypeDef.getRELATIONTYPE5();
			
		}else if(ante_start_date.before(cons_start_date) && ante_complete_date.equals(cons_complete_date)){
			relType = relTypeDef.getRELATIONTYPE6();
			
		}else if(ante_start_date.equals(cons_start_date) && ante_complete_date.equals(cons_complete_date)){
			relType = relTypeDef.getRELATIONTYPE7();
		}else {
			relType = "error";
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

}
