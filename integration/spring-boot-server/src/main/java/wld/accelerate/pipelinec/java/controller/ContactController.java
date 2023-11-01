package wld.accelerate.pipelinec.java.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import wld.accelerate.pipelinec.java.entity.Contact;
import wld.accelerate.pipelinec.java.model.ContactModel;
import wld.accelerate.pipelinec.java.service.ContactService;
import java.util.List;

@RestController
public class ContactController {

	@Autowired
	private ContactService contactService;

	@GetMapping("/contact/{id}")
	public ResponseEntity<ContactModel> getContact(@PathVariable Integer id) {
		return ResponseEntity.ok(ContactModel.fromContact(contactService.findById(id)));
	}
	@GetMapping("/contact/")
	public ResponseEntity<List<ContactModel>> getAllContact() {
		return ResponseEntity.ok(contactService.findAll().stream().map(ContactModel::fromContact).toList());
	}
	@PostMapping("/contact/")
	public ResponseEntity<ContactModel> saveContact(ContactModel contactModel) {
		Contact contact = contactService.createContact(contactModel);
		return ResponseEntity.ok(ContactModel.fromContact(contact));
	}
	@PostMapping("/contact/{id}")
	public ResponseEntity<ContactModel> updateContact(@PathVariable Integer id, ContactModel contactModel) {
		Contact contact = contactService.updateContact(id, contactModel);
		return ResponseEntity.ok(ContactModel.fromContact(contact));
	}
}