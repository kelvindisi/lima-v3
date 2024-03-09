package org.icipe.lima.auth.repository.user;

import org.icipe.lima.auth.entity.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, String> {}
