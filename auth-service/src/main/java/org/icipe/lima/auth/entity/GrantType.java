package org.icipe.lima.auth.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "grant_type")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class GrantType extends AbstractEntity {
  @Column(name = "grant_type")
  private String grantType;

  @ManyToMany
  @JoinTable(
      name = "client_grant_type",
      joinColumns = @JoinColumn(name = "grant_type_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "client_id", referencedColumnName = "id"))
  private List<Client> clients;

  public static Consumer<Set<AuthorizationGrantType>> from(List<GrantType> grantTypes) {
    return c ->
        c.addAll(
            grantTypes.stream()
                .map(grantType -> new AuthorizationGrantType(grantType.getGrantType()))
                .collect(Collectors.toSet()));
  }
}
