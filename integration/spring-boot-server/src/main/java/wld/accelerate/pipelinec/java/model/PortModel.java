package wld.accelerate.pipelinec.java.model;

import wld.accelerate.pipelinec.java.entity.Port;

public class PortModel {
	private Long id;
	private String name;
	private Integer numberOfContainerCranes;
	public static PortModel fromPort(Port port) {
		PortModel portModel = new PortModel();
		portModel.name = port.getName();
		portModel.numberOfContainerCranes = port.getNumberOfContainerCranes();
		return portModel;
	}
	public static Port toPort(PortModel portModel){
		Port port = new Port();
		portModel.setName(port.getName());
		portModel.setNumberOfContainerCranes(port.getNumberOfContainerCranes());
		return port;
	}

	public String getName(){
		return name;
	}

	public Integer getNumberOfContainerCranes(){
		return numberOfContainerCranes;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNumberOfContainerCranes(Integer numberOfContainerCranes) {
		this.numberOfContainerCranes = numberOfContainerCranes;
	}

}