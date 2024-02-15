package org.icipe.lima.auth.repository;

import org.icipe.lima.auth.entity.Scope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ScopeRepository extends JpaRepository<Scope, String> {
  @Query("""
          SELECT s FROM Scope s WHERE s.scopeName=:scope
          """)
  Optional<Scope> findScopeByScopeName(@Param("scope") String scopeName);
}
