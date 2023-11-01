package wld.accelerate.pipelinec.java.model;

import wld.accelerate.pipelinec.java.entity.Contact;

public class ContactModel {
	private Long id;
	private String forename;
	private String lastname;
	private String email;
	private String tel;
	private String description;
	public static ContactModel fromContact(Contact contact) {
		ContactModel contactModel = new ContactModel();
		contactModel.forename = contact.getForename();
		contactModel.lastname = contact.getLastname();
		contactModel.email = contact.getEmail();
		contactModel.tel = contact.getTel();
		contactModel.description = contact.getDescription();
		return contactModel;
	}
	public static Contact toContact(ContactModel contactModel){
		Contact contact = new Contact();
		contactModel.setForename(contact.getForename());
		contactModel.setLastname(contact.getLastname());
		contactModel.setEmail(contact.getEmail());
		contactModel.setTel(contact.getTel());
		contactModel.setDescription(contact.getDescription());
		return contact;
	}

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