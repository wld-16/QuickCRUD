package wld.accelerate.pipelinec.java.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wld.accelerate.pipelinec.java.entity.ContainerCrane;
import wld.accelerate.pipelinec.java.model.ContainerCraneModel;
import wld.accelerate.pipelinec.java.repository.ContainerCraneRepository;
import java.util.List;
@Service
public class ContainerCraneService {
	@Autowired
	public ContainerCraneRepository containerCraneRepository;


	public ContainerCrane findById(Integer id) {
		return containerCraneRepository.findById(id).orElseThrow();
	}
	public List<ContainerCrane> findAll() {
		return containerCraneRepository.findAll();
	}
	public ContainerCrane createContainerCrane(ContainerCraneModel containerCraneModel){
		ContainerCrane containerCrane = ContainerCraneModel.toContainerCrane(containerCraneModel);
		return containerCraneRepository.save(containerCrane);
	}
	public ContainerCrane updateContainerCrane(Integer id, ContainerCraneModel containerCraneModel){
		ContainerCrane containerCrane = findById(id);
		containerCrane.setName(containerCrane.getName());
		containerCrane.setHoistPower(containerCrane.getHoistPower());
		return containerCraneRepository.save(containerCrane);
	}
}