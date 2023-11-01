package wld.accelerate.pipelinec.java.model;

import wld.accelerate.pipelinec.java.entity.Container;

public class ContainerModel {
	private Long id;
	private String name;
	private Integer loadCapacity;
	public static ContainerModel fromContainer(Container container) {
		ContainerModel containerModel = new ContainerModel();
		containerModel.name = container.getName();
		containerModel.loadCapacity = container.getLoadCapacity();
		return containerModel;
	}
	public static Container toContainer(ContainerModel containerModel){
		Container container = new Container();
		containerModel.setName(container.getName());
		containerModel.setLoadCapacity(container.getLoadCapacity());
		return container;
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