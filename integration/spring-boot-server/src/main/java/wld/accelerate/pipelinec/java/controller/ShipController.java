package wld.accelerate.pipelinec.java.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import wld.accelerate.pipelinec.java.entity.Ship;
import wld.accelerate.pipelinec.java.model.ShipModel;
import wld.accelerate.pipelinec.java.service.ShipService;
import java.util.List;

@RestController
public class ShipController {

	@Autowired
	private ShipService shipService;

	@GetMapping("/ship/{id}")
	public ResponseEntity<ShipModel> getShip(@PathVariable Integer id) {
		return ResponseEntity.ok(ShipModel.fromShip(shipService.findById(id)));
	}
	@GetMapping("/ship/")
	public ResponseEntity<List<ShipModel>> getAllShip() {
		return ResponseEntity.ok(shipService.findAll().stream().map(ShipModel::fromShip).toList());
	}
	@PostMapping("/ship/")
	public ResponseEntity<ShipModel> saveShip(ShipModel shipModel) {
		Ship ship = shipService.createShip(shipModel);
		return ResponseEntity.ok(ShipModel.fromShip(ship));
	}
	@PostMapping("/ship/{id}")
	public ResponseEntity<ShipModel> updateShip(@PathVariable Integer id, ShipModel shipModel) {
		Ship ship = shipService.updateShip(id, shipModel);
		return ResponseEntity.ok(ShipModel.fromShip(ship));
	}
}