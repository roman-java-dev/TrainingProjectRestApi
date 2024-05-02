package com.example.trainingprojectrestapi.controller;

import com.example.trainingprojectrestapi.dto.request.OrderRequestDto;
import com.example.trainingprojectrestapi.service.OrderService;
import com.example.trainingprojectrestapi.util.FileOperationUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

import static com.example.trainingprojectrestapi.util.ConstantsUtil.REPORT_FILE_NAME;

/**
 * Controller class for handling order-related operations.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<Object> add(@RequestBody @Valid OrderRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.addOrder(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> get(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getOrder(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable Long id,
                                                   @RequestBody @Valid OrderRequestDto dto) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.updateOrder(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/_list")
    public ResponseEntity<Object> findByCriteria(@RequestBody Map<String, Object> request,
                                                 @RequestParam(required = false, defaultValue = "10") short size,
                                                 @RequestParam(required = false, defaultValue = "1") short page) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getOrdersByCriteria(request, size, page));
    }

    @PostMapping("/_report")
    public void getReportFile(@RequestBody Map<String, Object> request,
                                                HttpServletResponse response) throws IOException {

        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setHeader("Content-Disposition", "attachment; filename=" + REPORT_FILE_NAME);

        FileOperationUtil.exportToCSV(orderService.getOrdersByCriteria(request), response.getOutputStream());
        response.flushBuffer();
    }

    @PostMapping("/upload")
    public ResponseEntity<Object> uploadOrders(@ModelAttribute MultipartFile file) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.processOrders(file));
    }
}
