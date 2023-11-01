package wld.accelerate.pipelinec.java.model;

import wld.accelerate.pipelinec.java.entity.Opportunity;
import wld.accelerate.pipelinec.java.Probability;
import wld.accelerate.pipelinec.java.Status;

public class OpportunityModel {
	private Long id;
	private String name;
	private Probability probability;
	private Status status;
	public static OpportunityModel fromOpportunity(Opportunity opportunity) {
		OpportunityModel opportunityModel = new OpportunityModel();
		opportunityModel.name = opportunity.getName();
		opportunityModel.probability = opportunity.getProbability();
		opportunityModel.status = opportunity.getStatus();
		return opportunityModel;
	}
	public static Opportunity toOpportunity(OpportunityModel opportunityModel){
		Opportunity opportunity = new Opportunity();
		opportunityModel.setName(opportunity.getName());
		opportunityModel.setProbability(opportunity.getProbability());
		opportunityModel.setStatus(opportunity.getStatus());
		return opportunity;
	}

	public String getName(){
		return name;
	}

	public Probability getProbability(){
		return probability;
	}

	public Status getStatus(){
		return status;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setProbability(Probability probability) {
		this.probability = probability;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

}