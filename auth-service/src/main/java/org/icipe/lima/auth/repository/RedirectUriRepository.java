package org.icipe.lima.auth.repository;

import org.icipe.lima.auth.entity.RedirectUri;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RedirectUriRepository extends JpaRepository<RedirectUri, String> {}
