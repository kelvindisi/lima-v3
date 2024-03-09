package org.icipe.lima.auth.repository.user;

import org.icipe.lima.auth.entity.user.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, String> {}
