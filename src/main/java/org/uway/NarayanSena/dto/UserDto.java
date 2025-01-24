package org.uway.NarayanSena.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String fullName;
    private String email;
    private String mobileNumber;
    private String city;
    private String password;
    private String referrerId;   // Optional field to accept referral information
}