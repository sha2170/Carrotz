package com.carrotzmarket.api.domain.transaction.controller;

import com.carrotzmarket.api.domain.transaction.dto.PurchaseRequest;
import com.carrotzmarket.api.domain.transaction.service.ProductTransactionService;
import com.carrotzmarket.db.transaction.ProductTransactionEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductTransactionController {

    private final ProductTransactionService service;

    @PostMapping("/transaction")
    public ProductTransactionEntity createTransaction(@RequestBody PurchaseRequest request) {
        return service.createTransaction(request);
    }
}
