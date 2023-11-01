package wld.accelerate.pipelinec.java.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import wld.accelerate.pipelinec.java.entity.ContainerCrane;
import wld.accelerate.pipelinec.java.model.ContainerCraneModel;
import wld.accelerate.pipelinec.java.service.ContainerCraneService;
import java.util.List;

@RestController
public class ContainerCraneController {

	@Autowired
	private ContainerCraneService containerCraneService;

	@GetMapping("/containerCrane/{id}")
	public ResponseEntity<ContainerCraneModel> getContainerCrane(@PathVariable Integer id) {
		return ResponseEntity.ok(ContainerCraneModel.fromContainerCrane(containerCraneService.findById(id)));
	}
	@GetMapping("/containerCrane/")
	public ResponseEntity<List<ContainerCraneModel>> getAllContainerCrane() {
		return ResponseEntity.ok(containerCraneService.findAll().stream().map(ContainerCraneModel::fromContainerCrane).toList());
	}
	@PostMapping("/containerCrane/")
	public ResponseEntity<ContainerCraneModel> saveContainerCrane(ContainerCraneModel containerCraneModel) {
		ContainerCrane containerCrane = containerCraneService.createContainerCrane(containerCraneModel);
		return ResponseEntity.ok(ContainerCraneModel.fromContainerCrane(containerCrane));
	}
	@PostMapping("/containerCrane/{id}")
	public ResponseEntity<ContainerCraneModel> updateContainerCrane(@PathVariable Integer id, ContainerCraneModel containerCraneModel) {
		ContainerCrane containerCrane = containerCraneService.updateContainerCrane(id, containerCraneModel);
		return ResponseEntity.ok(ContainerCraneModel.fromContainerCrane(containerCrane));
	}
}