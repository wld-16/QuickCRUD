package wld.accelerate.pipelinec.java.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wld.accelerate.pipelinec.java.entity.Container;
import wld.accelerate.pipelinec.java.model.ContainerModel;
import wld.accelerate.pipelinec.java.repository.ContainerRepository;
import java.util.List;
@Service
public class ContainerService {
	@Autowired
	public ContainerRepository containerRepository;


	public Container findById(Integer id) {
		return containerRepository.findById(id).orElseThrow();
	}
	public List<Container> findAll() {
		return containerRepository.findAll();
	}
	public Container createContainer(ContainerModel containerModel){
		Container container = ContainerModel.toContainer(containerModel);
		return containerRepository.save(container);
	}
	public Container updateContainer(Integer id, ContainerModel containerModel){
		Container container = findById(id);
		container.setName(container.getName());
		container.setLoadCapacity(container.getLoadCapacity());
		return containerRepository.save(container);
	}
}