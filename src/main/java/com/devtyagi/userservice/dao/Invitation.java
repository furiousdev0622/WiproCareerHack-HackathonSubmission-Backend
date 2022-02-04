package com.devtyagi.userservice.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "invitations")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Invitation {

    @Id
    private String invitationId;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    private User user;

    private Boolean used;
}
