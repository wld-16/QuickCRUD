package wld.accelerate.pipelinec.java.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wld.accelerate.pipelinec.java.entity.Opportunity;
import wld.accelerate.pipelinec.java.model.OpportunityModel;
import wld.accelerate.pipelinec.java.repository.OpportunityRepository;
import java.util.List;
@Service
public class OpportunityService {
	@Autowired
	public OpportunityRepository opportunityRepository;


	public Opportunity findById(Integer id) {
		return opportunityRepository.findById(id).orElseThrow();
	}
	public List<Opportunity> findAll() {
		return opportunityRepository.findAll();
	}
	public Opportunity createOpportunity(OpportunityModel opportunityModel){
		Opportunity opportunity = OpportunityModel.toOpportunity(opportunityModel);
		return opportunityRepository.save(opportunity);
	}
	public Opportunity updateOpportunity(Integer id, OpportunityModel opportunityModel){
		Opportunity opportunity = findById(id);
		opportunity.setName(opportunity.getName());
		opportunity.setProbability(opportunity.getProbability());
		opportunity.setStatus(opportunity.getStatus());
		return opportunityRepository.save(opportunity);
	}
}