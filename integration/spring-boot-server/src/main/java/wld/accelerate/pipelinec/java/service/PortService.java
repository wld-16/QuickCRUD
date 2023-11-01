package wld.accelerate.pipelinec.java.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wld.accelerate.pipelinec.java.entity.Port;
import wld.accelerate.pipelinec.java.model.PortModel;
import wld.accelerate.pipelinec.java.repository.PortRepository;
import java.util.List;
@Service
public class PortService {
	@Autowired
	public PortRepository portRepository;


	public Port findById(Integer id) {
		return portRepository.findById(id).orElseThrow();
	}
	public List<Port> findAll() {
		return portRepository.findAll();
	}
	public Port createPort(PortModel portModel){
		Port port = PortModel.toPort(portModel);
		return portRepository.save(port);
	}
	public Port updatePort(Integer id, PortModel portModel){
		Port port = findById(id);
		port.setName(port.getName());
		port.setNumberOfContainerCranes(port.getNumberOfContainerCranes());
		return portRepository.save(port);
	}
}