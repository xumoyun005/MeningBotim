package com.company.entity;

import com.company.enums.CustomerStatus;
import com.company.enums.Language;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
    private String userId;
    private String firstName;
    private String lastName;
    private String username;
    private String phoneNumber;
    private Language language = Language.UZ;
    private CustomerStatus status;
}
