package com.services.ServiceProvider.constant;

import java.util.Map;

public class Constants {

    private Constants() {
        throw new IllegalArgumentException("Could not initialize this class");
    }

    public static final Map<Integer, String> RESPONSE = Map.<Integer, String>ofEntries(

            //codes for successful attempts
            Map.entry(110, "OTP Sent Successfully."),
            Map.entry(111, "OTP verified successfully."),
            Map.entry(112, "Password Verified Successfully."),
            Map.entry(113, "OTP Expired! Please request a new OTP"),
            Map.entry(114, "Password already created"),
            Map.entry(118, "User created successfully"),
            Map.entry(120, "Logout successful"),
            Map.entry(122, "Email is already verified!"),
            Map.entry(125, "Password successfully created"),
            Map.entry(126, "Token generated"),
            Map.entry(127, "Invitation sent successfully."),
            Map.entry(129, "Password Updated successfully"),
            Map.entry(130,"Document uploaded successfully"),
            Map.entry(131,"Password reset link sent to your email"),
            Map.entry(210, "Conference created successfully"),
            Map.entry(211, "Conference updated successfully"),



            //codes for unsuccessful attempts
            Map.entry(410,"User not found!"),
            Map.entry(429,"Wrong Otp entered!"),
            Map.entry(430,"Wrong password entered!"),
            Map.entry(431, "Passwords do not match!"),
            Map.entry(432, "Failed to send otp!"),
            Map.entry(433, "Token not generated!"),
            Map.entry(434, "OTP Expired! Please request a new OTP"),
            Map.entry(435, "Error while sending Invitation."),
            Map.entry(436,"Failed to sent password reset link"),
            Map.entry(437,"Invalid Parameter Passed"),
            Map.entry(438,"User already exists"),
            Map.entry(440,"Token has expired"),
            Map.entry(441,"Unsupported token"),
            Map.entry(442,"Invalid token"),
            Map.entry(443,"Invalid token signature"),
            Map.entry(444,"Token string is empty or null"),
            Map.entry(445,"Could not parse token"),
            Map.entry(446,"Invalid or expired token."),
            Map.entry(447,"Only User can do booking"),
            Map.entry(448,"Failed to assign role to user."),
            Map.entry(449,"Otp is incorrect"),
            Map.entry(450,"Otp is expired!"),
            Map.entry(451,"Maximum request limit reached"),
            Map.entry(452,"User already exists"),
            Map.entry(453,"Provider not found"),
            Map.entry(454,"Logout failed"),
            Map.entry(470, "Invalid input sent while creation of conference"),
            Map.entry(471, "Conference with this title already exists"),
            Map.entry(472, "Conference with this acronym already exists"),
            Map.entry(473, "Early bird deadline must be between registration start date and registration end date"),
            Map.entry(474, "Acronym must begin with initials of title words and may end with digits"),
            Map.entry(475, "Acronym is required"),
            Map.entry(476, "location fields are required and must be valid."),
            Map.entry(477, "Conference not found or access denied")


    );



    public static final long JWT_TOKEN_VALIDITY = 24*60*60*1000;
    public static final long REFRESH_TOKEN_VALIDITY = 7*24*60*60*1000;
    public static final long OTP_MAX_REQUESTS_IN_ONE_DAY = 5;
    public static final long OTP_RESEND_WAIT_MILLIS_AFTER_MAX_REQUESTS =24*60*60*1000 ;
    public static final long OTP_RESEND_WAIT_MILLIS = 15*1000;
    public static final long INVITATION_TOKEN_VALIDITY= 24*60*60*1000;

}