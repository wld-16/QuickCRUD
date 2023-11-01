package wld.accelerate.pipelinec.java.entity;


import java.io.Serializable;
import jakarta.persistence.*;
@Entity
public class Port implements Serializable {
	@Id
	@SequenceGenerator(name="port_id_seq", sequenceName="port_id_seq", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="port_id_seq")
	private Long id = null;

	@Column(nullable = false)
	String name = "";


	@Column(nullable = false)
	Integer numberOfContainerCranes = null;

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