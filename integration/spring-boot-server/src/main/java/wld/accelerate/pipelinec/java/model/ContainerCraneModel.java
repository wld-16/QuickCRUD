package wld.accelerate.pipelinec.java.model;

import wld.accelerate.pipelinec.java.entity.ContainerCrane;

public class ContainerCraneModel {
	private Long id;
	private String name;
	private Integer hoistPower;
	public static ContainerCraneModel fromContainerCrane(ContainerCrane containerCrane) {
		ContainerCraneModel containerCraneModel = new ContainerCraneModel();
		containerCraneModel.name = containerCrane.getName();
		containerCraneModel.hoistPower = containerCrane.getHoistPower();
		return containerCraneModel;
	}
	public static ContainerCrane toContainerCrane(ContainerCraneModel containerCraneModel){
		ContainerCrane containerCrane = new ContainerCrane();
		containerCraneModel.setName(containerCrane.getName());
		containerCraneModel.setHoistPower(containerCrane.getHoistPower());
		return containerCrane;
	}

	public String getName(){
		return name;
	}

	public Integer getHoistPower(){
		return hoistPower;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setHoistPower(Integer hoistPower) {
		this.hoistPower = hoistPower;
	}

}