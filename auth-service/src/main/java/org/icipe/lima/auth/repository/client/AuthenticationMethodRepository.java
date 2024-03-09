package org.icipe.lima.auth.repository.client;

import java.util.Optional;
import org.icipe.lima.auth.entity.client.AuthenticationMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AuthenticationMethodRepository extends JpaRepository<AuthenticationMethod, String> {

  @Query("""
          SELECT s FROM AuthenticationMethod s WHERE s.method=:method
          """)
  Optional<AuthenticationMethod> findAuthenticationMethodByMethod(@Param("method") String method);
}
