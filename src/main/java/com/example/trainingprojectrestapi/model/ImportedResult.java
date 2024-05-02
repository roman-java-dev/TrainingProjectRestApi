package com.example.trainingprojectrestapi.model;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * A data class representing the result of importing data.
 */
@Builder
@Getter
public class ImportedResult {
    private int successfulImports;
    private int failedImports;
    private List<InvalidInputData> invalidInputData;
}
