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

    /**
     * Once the invitation has been used to activate one's account,
     * it can not be used to update the password again.
     * This method is used to mark it used.
     */
    public void setInviteUsed(Invitation invitation) {
        invitation.setUsed(true);
        invitationRepository.save(invitation);
    }

    /**
     * This method is used to create a unique invitation code for the user
     * and trigger the sendEmailToUser function.
     * @param user
     */
    public void sendInvitationToUser(User user) {
        val invite = Invitation.builder()
                .invitationId(UUID.randomUUID().toString())
                .user(user)
                .used(false)
                .build();

        invitationRepository.save(invite);
        emailService.sendEmailToUser(user.getEmailAddress(), user.getFullName(), invite.getInvitationId());
    }

    /**
     * This method fetched the Invitation by ID from the Database.
     * @param id ID/InviteCode for the invitation.
     * @return Invitation Object.
     */
    public Invitation getInvitationById(String id) {
        return invitationRepository.findById(id)
                .orElseThrow(InvalidInvitationException::new);
    }

    /**
     * Since the Invite Table has a foreign key constraint on user_id,
     * the invite has to be deleted before deleting the user's account.
     * @param userId
     */
    public void deleteInvite(String userId) {
        invitationRepository.deleteByUser_UserId(userId);
    }
}
