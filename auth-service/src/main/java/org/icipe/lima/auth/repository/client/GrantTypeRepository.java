package org.icipe.lima.auth.repository.client;

import org.icipe.lima.auth.entity.client.GrantType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface GrantTypeRepository extends JpaRepository<GrantType, String> {
  @Query("""
              SELECT g FROM GrantType g WHERE g.grantType=:grantType
          """)
  Optional<GrantType> findGrantTypeByGrantType(@Param("grantType") String grantType);
}
