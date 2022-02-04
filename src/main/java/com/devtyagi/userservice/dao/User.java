package com.devtyagi.userservice.dao;

import com.devtyagi.userservice.enums.UserStatus;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid2")
    private String userId;

    private String username;

    private String fullName;

    private String emailAddress;

    @JsonIgnore
    private String password;

    @Enumerated(EnumType.STRING)
    private UserStatus status = UserStatus.PENDING;

    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "roleId")
    private Role role;

    @JsonGetter("role")
    public String getTheRole() {
        return role.getRoleName();
    }
}
