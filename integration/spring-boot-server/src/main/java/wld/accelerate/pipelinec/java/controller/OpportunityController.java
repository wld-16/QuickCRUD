package wld.accelerate.pipelinec.java.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import wld.accelerate.pipelinec.java.entity.Opportunity;
import wld.accelerate.pipelinec.java.model.OpportunityModel;
import wld.accelerate.pipelinec.java.service.OpportunityService;
import java.util.List;

@RestController
public class OpportunityController {

	@Autowired
	private OpportunityService opportunityService;

	@GetMapping("/opportunity/{id}")
	public ResponseEntity<OpportunityModel> getOpportunity(@PathVariable Integer id) {
		return ResponseEntity.ok(OpportunityModel.fromOpportunity(opportunityService.findById(id)));
	}
	@GetMapping("/opportunity/")
	public ResponseEntity<List<OpportunityModel>> getAllOpportunity() {
		return ResponseEntity.ok(opportunityService.findAll().stream().map(OpportunityModel::fromOpportunity).toList());
	}
	@PostMapping("/opportunity/")
	public ResponseEntity<OpportunityModel> saveOpportunity(OpportunityModel opportunityModel) {
		Opportunity opportunity = opportunityService.createOpportunity(opportunityModel);
		return ResponseEntity.ok(OpportunityModel.fromOpportunity(opportunity));
	}
	@PostMapping("/opportunity/{id}")
	public ResponseEntity<OpportunityModel> updateOpportunity(@PathVariable Integer id, OpportunityModel opportunityModel) {
		Opportunity opportunity = opportunityService.updateOpportunity(id, opportunityModel);
		return ResponseEntity.ok(OpportunityModel.fromOpportunity(opportunity));
	}
}