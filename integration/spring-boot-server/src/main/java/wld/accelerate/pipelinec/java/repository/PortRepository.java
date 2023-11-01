package wld.accelerate.pipelinec.java.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wld.accelerate.pipelinec.java.entity.Port;


@Repository
public interface PortRepository extends JpaRepository<Port, Integer> {
}