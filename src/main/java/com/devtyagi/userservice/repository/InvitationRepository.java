package com.devtyagi.userservice.repository;

import com.devtyagi.userservice.dao.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

public interface InvitationRepository extends JpaRepository<Invitation, String> {

    @Transactional
    void deleteByUser_UserId(String userId);

}
