package net.uway.journalApp.dto;

import lombok.Data;
import java.util.List;
import java.util.UUID;

@Data
public class UserDetailsDto {
    private long id;
    private String fullName;
    private String email;
    private String mobileNumber;
    private String city;
    private String referrer; // Full name of the referrer
    private String referrerId; // Referral ID of the referrer
    private String referralId; // Referral ID that this user uses to refer others
    private boolean isPaymentComplete;
    private List<ReferralDTO> referralTree;
}
