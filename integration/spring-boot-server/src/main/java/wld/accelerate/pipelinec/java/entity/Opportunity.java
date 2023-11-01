package wld.accelerate.pipelinec.java.entity;


import wld.accelerate.pipelinec.java.Probability;
import wld.accelerate.pipelinec.java.Status;
import java.io.Serializable;
import jakarta.persistence.*;
@Entity
public class Opportunity implements Serializable {
	@Id
	@SequenceGenerator(name="opportunity_id_seq", sequenceName="opportunity_id_seq", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="opportunity_id_seq")
	private Long id = null;

	@Column(nullable = false)
	String name = "";


	@Column(nullable = false)
	Probability probability = null;


	@Column(nullable = false)
	Status status = null;

	public String getName(){
		return name;
	}


	public Probability getProbability(){
		return probability;
	}


	public Status getStatus(){
		return status;
	}

	public void setName(String name) {
		this.name = name;
	}


	public void setProbability(Probability probability) {
		this.probability = probability;
	}


	public void setStatus(Status status) {
		this.status = status;
	}

}