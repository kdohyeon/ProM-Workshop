package org.processmining.data.trueY;

import java.util.ArrayList;

import org.processmining.data.relation.Relation;

public class TrueY {
	private String fromActivityID;
	private String toActivityID;
	private String fromResourceID;
	private String toResourceID;
	private String relation;
	
	private ArrayList<Relation> relationList;
	
	private float avg;
	private float stdev;
	private float min;
	private float max;
	private float median;
	private int freq;
	
	public TrueY(String fromActivityID, String toActivityID, String fromResourceID, String toResourceID, String relation) {
		this.fromActivityID = fromActivityID;
		this.toActivityID = toActivityID;
		this.fromResourceID = fromResourceID;
		this.toResourceID = toResourceID;
		this.relation = relation;
		
		this.relationList = new ArrayList<Relation>();
	}
	
	public String getActivityPair() {
		return fromActivityID + "_" + relation + "_" + toActivityID;
	}
	
	public void addRelation(Relation rel) {
		relationList.add(rel);
	}
	
	public int getRelationListCardinality() {
		return relationList.size();
	}
	
	public String getFromActivityID() {
		return fromActivityID;
	}
	
	public String getToActivityID() {
		return toActivityID;
	}
	
	public String getFromResourceID() {
		return fromResourceID;
	}
	
	public String getToResourceID() {
		return toResourceID;
	}
	
	public String getRelation() {
		return relation;
	}
	
	public int calculateFreq() {
		freq = relationList.size();
		return freq;
	}
	
	public float calculateSum() {
		float result = 0;
		for(int i = 0; i < relationList.size(); i++) {
			result = result + relationList.get(i).getTrueYTime();
		}
		
		return result;
	}
	
	public float calculateAverage() {
		float result = 0;
		result = calculateSum();
		
		avg = result / relationList.size();
		return avg;
	}
	
	public float calculateStdev() {
		float result = 0;
		float avg = calculateAverage();
		
		for(int i = 0; i < relationList.size(); i++){
			result = (float) (result + Math.pow((relationList.get(i).getTrueYTime() - avg), 2));
		}
		
		result = (float)Math.sqrt(result / relationList.size());
		
		stdev = result;
		
		return result;
	}
	
	public float calculateMin() {
		float result = 0;
		
		result = relationList.get(0).getTrueYTime();
		for(int i = 1; i < relationList.size(); i++)
		{
			if(result > relationList.get(i).getTrueYTime())
			{
				result = relationList.get(i).getTrueYTime();
			}
		}
		
		min = result;
		return min;
	}
	
	public float calculateaMax() {
		float result = 0;
		
		result = relationList.get(0).getTrueYTime();
		for(int i = 1; i < relationList.size(); i++)
		{
			if(result < relationList.get(i).getTrueYTime())
			{
				result = relationList.get(i).getTrueYTime();
			}
		}
		
		max = result;
		return max;
	}

	public float getAvg() {
		return avg;
	}

	public void setAvg(float avg) {
		this.avg = avg;
	}

	public float getStdev() {
		return stdev;
	}

	public void setStdev(float stdev) {
		this.stdev = stdev;
	}

	public float getMin() {
		return min;
	}

	public void setMin(float min) {
		this.min = min;
	}

	public float getMax() {
		return max;
	}

	public void setMax(float max) {
		this.max = max;
	}

	public float getMedian() {
		return median;
	}

	public void setMedian(float median) {
		this.median = median;
	}

	public int getFreq() {
		return freq;
	}

	public void setFreq(int freq) {
		this.freq = freq;
	}
}
