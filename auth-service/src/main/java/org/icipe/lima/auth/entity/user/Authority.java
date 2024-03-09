package org.icipe.lima.auth.entity.user;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.icipe.lima.auth.entity.AbstractEntity;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Entity
@Table(name = "authority")
public class Authority extends AbstractEntity {
  @Column(name = "authority")
  private String authority;

  @ManyToMany(cascade = CascadeType.PERSIST)
  @JoinTable(
      name = "user_authority",
      joinColumns = @JoinColumn(name = "authority_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
  private List<AppUser> users;

  @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
  @JoinTable(
          name = "authority_roles",
          joinColumns = @JoinColumn(
                  name = "authority_id", referencedColumnName = "id"
          ),
          inverseJoinColumns = @JoinColumn(
                  name = "role_id", referencedColumnName = "id"
          )
  )
  private List<Role> roles;
}
