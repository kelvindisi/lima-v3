package org.icipe.lima.auth.repository.client;

import org.icipe.lima.auth.entity.client.RedirectUri;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RedirectUriRepository extends JpaRepository<RedirectUri, String> {}
