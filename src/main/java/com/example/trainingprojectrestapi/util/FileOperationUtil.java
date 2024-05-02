package com.example.trainingprojectrestapi.util;

import com.example.trainingprojectrestapi.dto.request.OrderRequestDto;
import com.example.trainingprojectrestapi.dto.response.OrderResponseDto;
import com.example.trainingprojectrestapi.exception.FileOperationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.opencsv.CSVWriter;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

import static com.example.trainingprojectrestapi.util.ConstantsUtil.JSON_FILE_EXTENSION;
import static java.util.Objects.nonNull;

/**
 * Utility class for file-related operations.
 */
public class FileOperationUtil {

    /**
     * Exports a list of orders to a CSV file.
     *
     * @param orders       The list of orders to export.
     * @param outputStream The output stream to write the CSV data.
     * @throws IOException If an I/O error occurs while writing the CSV data.
     */
    public static void exportToCSV(List<OrderResponseDto> orders, OutputStream outputStream) throws IOException {
        try (CSVWriter writer = new CSVWriter(new OutputStreamWriter(outputStream))) {
            writer.writeNext(new String[]{"Customer ID", "Order Date", "Description", "Total Price"});

            for (OrderResponseDto order : orders) {
                String[] data = {
                    String.valueOf(order.getCustomerId()),
                    order.getOrderDate().toString(),
                    order.getDescription(),
                    order.getTotalPrice().toString()
                };
                writer.writeNext(data);
            }
        }
    }

    /**
     * Checks if the provided file is in JSON format.
     *
     * @param file The multipart file to check.
     * @return {@code true} if the file is in JSON format, {@code false} otherwise.
     */
    public static boolean isJsonFile(MultipartFile file) {
        String fileName = nonNull(file) ? file.getOriginalFilename() : null;
        return nonNull(fileName) && fileName.contains(JSON_FILE_EXTENSION);
    }

    /**
     * Parses data from a JSON file into a list of order request DTOs.
     *
     * @param file The multipart file containing the JSON data.
     * @return A list of order request DTOs parsed from the JSON file.
     * @throws FileOperationException If an error occurs while parsing the JSON data.
     */
    public static List<OrderRequestDto> parseJsonFileData(MultipartFile file) {
        try {
            ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
            return objectMapper.readValue(file.getInputStream(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, OrderRequestDto.class));
        } catch (IOException ioException) {
            throw new FileOperationException(ioException.getMessage());
        }
    }
}
