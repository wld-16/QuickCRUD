package wld.accelerate.pipelinec.java.model;

import wld.accelerate.pipelinec.java.entity.Ship;

public class ShipModel {
	private Long id;
	private String name;
	private Integer loadCapacity;
	public static ShipModel fromShip(Ship ship) {
		ShipModel shipModel = new ShipModel();
		shipModel.name = ship.getName();
		shipModel.loadCapacity = ship.getLoadCapacity();
		return shipModel;
	}
	public static Ship toShip(ShipModel shipModel){
		Ship ship = new Ship();
		shipModel.setName(ship.getName());
		shipModel.setLoadCapacity(ship.getLoadCapacity());
		return ship;
	}

	public String getName(){
		return name;
	}

	public Integer getLoadCapacity(){
		return loadCapacity;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setLoadCapacity(Integer loadCapacity) {
		this.loadCapacity = loadCapacity;
	}

}