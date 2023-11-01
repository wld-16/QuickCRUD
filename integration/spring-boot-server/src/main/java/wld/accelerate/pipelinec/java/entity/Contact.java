package wld.accelerate.pipelinec.java.entity;


import java.io.Serializable;
import jakarta.persistence.*;
@Entity
public class Contact implements Serializable {
	@Id
	@SequenceGenerator(name="contact_id_seq", sequenceName="contact_id_seq", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="contact_id_seq")
	private Long id = null;

	@Column(nullable = false)
	String forename = "";


	@Column(nullable = false)
	String lastname = "";


	@Column(nullable = false)
	String email = "";


	@Column(nullable = false)
	String tel = "";


	@Column(nullable = false)
	String description = "";

	public String getForename(){
		return forename;
	}


	public String getLastname(){
		return lastname;
	}


	public String getEmail(){
		return email;
	}


	public String getTel(){
		return tel;
	}


	public String getDescription(){
		return description;
	}

	public void setForename(String forename) {
		this.forename = forename;
	}


	public void setLastname(String lastname) {
		this.lastname = lastname;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public void setTel(String tel) {
		this.tel = tel;
	}


	public void setDescription(String description) {
		this.description = description;
	}

}