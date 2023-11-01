package wld.accelerate.pipelinec.java.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wld.accelerate.pipelinec.java.entity.Contact;
import wld.accelerate.pipelinec.java.model.ContactModel;
import wld.accelerate.pipelinec.java.repository.ContactRepository;
import java.util.List;
@Service
public class ContactService {
	@Autowired
	public ContactRepository contactRepository;


	public Contact findById(Integer id) {
		return contactRepository.findById(id).orElseThrow();
	}
	public List<Contact> findAll() {
		return contactRepository.findAll();
	}
	public Contact createContact(ContactModel contactModel){
		Contact contact = ContactModel.toContact(contactModel);
		return contactRepository.save(contact);
	}
	public Contact updateContact(Integer id, ContactModel contactModel){
		Contact contact = findById(id);
		contact.setForename(contact.getForename());
		contact.setLastname(contact.getLastname());
		contact.setEmail(contact.getEmail());
		contact.setTel(contact.getTel());
		contact.setDescription(contact.getDescription());
		return contactRepository.save(contact);
	}
}