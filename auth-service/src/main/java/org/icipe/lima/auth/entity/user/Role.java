package org.icipe.lima.auth.entity.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
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
@Table(name = "role")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Role extends AbstractEntity {
    private String roleName;
}
