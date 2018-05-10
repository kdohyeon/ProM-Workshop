package org.processmining.models.anomaly.detection;

import java.text.ParseException;

import org.deckfour.xes.model.XLog;
import org.processmining.framework.util.HTMLToString;
import org.processmining.models.anomaly.profile.AnomalyProfileModel;

public class AnomalyDetectionModel implements HTMLToString{
	
	private XLog log;
	private AnomalyProfileModel profileModel;
	
	public AnomalyDetectionModel(XLog log, AnomalyProfileModel profileModel) throws ParseException {
		this.log = log;
		this.profileModel = profileModel;
	}
	
	public String toHTMLString(boolean includeHTMLTags) {
		StringBuffer buffer = new StringBuffer();
		if (includeHTMLTags) {
			buffer.append("<html>");
		}
		
		buffer.append("<p> Anomaly Detection Plugin </p>");
		
		buffer.append("<p> profile model : " + profileModel.getActModel().getActivityID(0)+ "</p>");
		
		if (includeHTMLTags) {
			buffer.append("</html>");
		}
		return buffer.toString();
	}
	
}
