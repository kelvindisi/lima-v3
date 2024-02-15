package org.icipe.lima.auth.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "redirect_uri")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class RedirectUri extends AbstractEntity {
  private String uri;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(
      name = "client_id",
      referencedColumnName = "id",
      foreignKey = @ForeignKey(name = "FK_REDIRECT_CLIENT_ID"))
  private Client client;

  public static List<RedirectUri> from(Client c, Set<String> redirectUris) {
    return redirectUris.stream().map(s -> RedirectUri.builder().client(c).uri(s).build()).toList();
  }
}
