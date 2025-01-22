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
    private String referrer;
    private String referrerId;
    private String referralId;
    private boolean paymentComplete;
    private String paymentStatus;  // New field for payment status message
    private String paymentLink;           // New field for payment link
    private List<ReferralDTO> referralTree;


}

