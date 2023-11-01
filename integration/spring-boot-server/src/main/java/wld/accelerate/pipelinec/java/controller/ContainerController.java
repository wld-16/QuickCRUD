package wld.accelerate.pipelinec.java.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import wld.accelerate.pipelinec.java.entity.Container;
import wld.accelerate.pipelinec.java.model.ContainerModel;
import wld.accelerate.pipelinec.java.service.ContainerService;
import java.util.List;

@RestController
public class ContainerController {

	@Autowired
	private ContainerService containerService;

	@GetMapping("/container/{id}")
	public ResponseEntity<ContainerModel> getContainer(@PathVariable Integer id) {
		return ResponseEntity.ok(ContainerModel.fromContainer(containerService.findById(id)));
	}
	@GetMapping("/container/")
	public ResponseEntity<List<ContainerModel>> getAllContainer() {
		return ResponseEntity.ok(containerService.findAll().stream().map(ContainerModel::fromContainer).toList());
	}
	@PostMapping("/container/")
	public ResponseEntity<ContainerModel> saveContainer(ContainerModel containerModel) {
		Container container = containerService.createContainer(containerModel);
		return ResponseEntity.ok(ContainerModel.fromContainer(container));
	}
	@PostMapping("/container/{id}")
	public ResponseEntity<ContainerModel> updateContainer(@PathVariable Integer id, ContainerModel containerModel) {
		Container container = containerService.updateContainer(id, containerModel);
		return ResponseEntity.ok(ContainerModel.fromContainer(container));
	}
}