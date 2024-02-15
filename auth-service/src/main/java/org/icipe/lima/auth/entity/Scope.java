package org.icipe.lima.auth.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "scopes")
public class Scope extends AbstractEntity {
  @Column(name = "name")
  private String scopeName;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "client_scopes",
      joinColumns = @JoinColumn(name = "scope_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "client_id", referencedColumnName = "id"))
  private List<Client> clients = new ArrayList<>();

  public static Consumer<Set<String>> from(List<Scope> scopes) {
    return c -> c.addAll(scopes.stream().map(Scope::getScopeName).collect(Collectors.toSet()));
  }
}
