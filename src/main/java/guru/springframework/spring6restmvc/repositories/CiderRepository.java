package guru.springframework.spring6restmvc.repositories;

import guru.springframework.spring6restmvc.entities.Cider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CiderRepository extends JpaRepository<Cider, UUID> {
}
