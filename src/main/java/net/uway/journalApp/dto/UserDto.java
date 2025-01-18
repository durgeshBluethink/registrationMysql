package net.uway.journalApp.dto;



import lombok.Data;

@Data
public class UserDto {
    private String fullName;
    private String email;
    private String mobileNumber;
    private String city;
    private String password;
    private String referrerId; // Field to store referrer's Id
 }

