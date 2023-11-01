package wld.accelerate.pipelinec.java.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import wld.accelerate.pipelinec.java.entity.Port;
import wld.accelerate.pipelinec.java.model.PortModel;
import wld.accelerate.pipelinec.java.service.PortService;
import java.util.List;

@RestController
public class PortController {

	@Autowired
	private PortService portService;

	@GetMapping("/port/{id}")
	public ResponseEntity<PortModel> getPort(@PathVariable Integer id) {
		return ResponseEntity.ok(PortModel.fromPort(portService.findById(id)));
	}
	@GetMapping("/port/")
	public ResponseEntity<List<PortModel>> getAllPort() {
		return ResponseEntity.ok(portService.findAll().stream().map(PortModel::fromPort).toList());
	}
	@PostMapping("/port/")
	public ResponseEntity<PortModel> savePort(PortModel portModel) {
		Port port = portService.createPort(portModel);
		return ResponseEntity.ok(PortModel.fromPort(port));
	}
	@PostMapping("/port/{id}")
	public ResponseEntity<PortModel> updatePort(@PathVariable Integer id, PortModel portModel) {
		Port port = portService.updatePort(id, portModel);
		return ResponseEntity.ok(PortModel.fromPort(port));
	}
}