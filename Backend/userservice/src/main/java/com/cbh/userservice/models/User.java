package com.cbh.userservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "user")
public class User {

    @Id
    private String userId;

    private String name;
    private String email;
    private String mobileNumber;
    private String dob;
    private String password;

    private String kycStatus;

    @Field("address")  
    private Address address;

    @Field("kyc")  
    private Kyc kyc;
}
