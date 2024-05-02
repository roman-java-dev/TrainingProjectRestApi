package com.example.trainingprojectrestapi.dto.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CustomerResponseDto {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
}
