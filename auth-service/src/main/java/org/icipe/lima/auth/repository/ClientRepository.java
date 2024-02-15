package org.icipe.lima.auth.repository;

import java.util.Optional;
import org.icipe.lima.auth.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClientRepository extends JpaRepository<Client, String> {
  Optional<Client> findClientByClientId(@Param("clientId") String clientId);
  int countByClientId(String clientId);
}
