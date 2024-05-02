package com.example.trainingprojectrestapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import static com.example.trainingprojectrestapi.util.ConstantsUtil.EMAIL_PATTERN;
import static com.example.trainingprojectrestapi.util.ConstantsUtil.PHONE_NUMBER_PATTERN;

@Getter
@Setter
@Builder
public class CustomerRequestDto {
    @NotBlank(message = "{validation.firstName.required}")
    private String firstName;

    @NotBlank(message = "{validation.lastName.required}")
    private String lastName;

    @Pattern(regexp = PHONE_NUMBER_PATTERN, message = "{validation.phoneNumber.invalid}")
    private String phoneNumber;

    @NotBlank(message = "{validation.email.required}")
    @Pattern(regexp = EMAIL_PATTERN, message = "{validation.email.invalid}")
    private String email;
}
