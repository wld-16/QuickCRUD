package wld.accelerate.pipelinec.java.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wld.accelerate.pipelinec.java.entity.Ship;
import wld.accelerate.pipelinec.java.model.ShipModel;
import wld.accelerate.pipelinec.java.repository.ShipRepository;
import java.util.List;
@Service
public class ShipService {
	@Autowired
	public ShipRepository shipRepository;


	public Ship findById(Integer id) {
		return shipRepository.findById(id).orElseThrow();
	}
	public List<Ship> findAll() {
		return shipRepository.findAll();
	}
	public Ship createShip(ShipModel shipModel){
		Ship ship = ShipModel.toShip(shipModel);
		return shipRepository.save(ship);
	}
	public Ship updateShip(Integer id, ShipModel shipModel){
		Ship ship = findById(id);
		ship.setName(ship.getName());
		ship.setLoadCapacity(ship.getLoadCapacity());
		return shipRepository.save(ship);
	}
}