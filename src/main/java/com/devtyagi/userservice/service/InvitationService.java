package com.devtyagi.userservice.service;

import com.devtyagi.userservice.dao.Invitation;
import com.devtyagi.userservice.dao.User;
import com.devtyagi.userservice.exception.InvalidCredentialsException;
import com.devtyagi.userservice.exception.InvalidInvitationException;
import com.devtyagi.userservice.repository.InvitationRepository;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InvitationService {

    private final InvitationRepository invitationRepository;

    private final EmailService emailService;

    public void setInviteUsed(Invitation invitation) {
        invitation.setUsed(true);
        invitationRepository.save(invitation);
    }

    public void sendInvitationToUser(User user) {
        val invite = Invitation.builder()
                .invitationId(UUID.randomUUID().toString())
                .user(user)
                .used(false)
                .build();

        invitationRepository.save(invite);
        emailService.sendEmailToUser(user.getEmailAddress(), invite.getInvitationId());
    }

    public Invitation getInvitationById(String id) {
        return invitationRepository.findById(id)
                .orElseThrow(InvalidInvitationException::new);
    }

    public void deleteInvite(String userId) {
        invitationRepository.deleteByUser_UserId(userId);
    }
}
