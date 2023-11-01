package wld.accelerate.pipelinec.java.entity;


import java.io.Serializable;
import jakarta.persistence.*;
@Entity
public class Activity implements Serializable {
	@Id
	@SequenceGenerator(name="activity_id_seq", sequenceName="activity_id_seq", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="activity_id_seq")
	private Long id = null;

	@Column(nullable = false)
	String name = "";

	public String getName(){
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}