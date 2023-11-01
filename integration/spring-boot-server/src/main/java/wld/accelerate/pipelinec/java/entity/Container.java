package wld.accelerate.pipelinec.java.entity;


import java.io.Serializable;
import jakarta.persistence.*;
@Entity
public class Container implements Serializable {
	@Id
	@SequenceGenerator(name="container_id_seq", sequenceName="container_id_seq", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="container_id_seq")
	private Long id = null;

	@Column(nullable = false)
	String name = "";


	@Column(nullable = false)
	Integer loadCapacity = null;

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