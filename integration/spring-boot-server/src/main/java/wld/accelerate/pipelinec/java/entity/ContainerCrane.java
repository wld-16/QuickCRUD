package wld.accelerate.pipelinec.java.entity;


import java.io.Serializable;
import jakarta.persistence.*;
@Entity
public class ContainerCrane implements Serializable {
	@Id
	@SequenceGenerator(name="containerCrane_id_seq", sequenceName="containerCrane_id_seq", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="containerCrane_id_seq")
	private Long id = null;

	@Column(nullable = false)
	String name = "";


	@Column(nullable = false)
	Integer hoistPower = null;

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