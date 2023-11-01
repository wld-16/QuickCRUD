package wld.accelerate.pipelinec.java.entity;


import java.io.Serializable;
import jakarta.persistence.*;
@Entity
public class Article implements Serializable {
	@Id
	@SequenceGenerator(name="article_id_seq", sequenceName="article_id_seq", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="article_id_seq")
	private Long id = null;

	@Column(nullable = false)
	String name = "";


	@Column(nullable = false)
	String number = "";

	public String getName(){
		return name;
	}


	public String getNumber(){
		return number;
	}

	public void setName(String name) {
		this.name = name;
	}


	public void setNumber(String number) {
		this.number = number;
	}

}