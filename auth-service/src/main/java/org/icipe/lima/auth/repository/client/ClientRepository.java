package org.icipe.lima.auth.repository.client;

import java.util.Optional;
import org.icipe.lima.auth.entity.client.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface ClientRepository extends JpaRepository<Client, String> {
  Optional<Client> findClientByClientId(@Param("clientId") String clientId);
  int countByClientId(String clientId);
}
