package com.example.trainingprojectrestapi.model;

import lombok.Builder;
import lombok.Getter;

/**
 * A data class representing invalid input data.
 */
@Builder
@Getter
public class InvalidInputData {
    private String errorMessage;
    private String incorrectField;
    private Object fieldValue;
}
