package net.uway.journalApp.dto;

import lombok.Data;

import java.util.List;

@Data
public class ReferralDTO {
    private String fullName;
    private String email;
    private String paymentStatus;
    private List<ReferralDTO> referrals;
}
