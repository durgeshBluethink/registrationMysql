package org.uway.NarayanSena.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    private List<ReferralDTO> referralTree;
}