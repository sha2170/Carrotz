package com.carrotzmarket.api.domain.transaction.controller;

import com.carrotzmarket.api.domain.transaction.dto.PurchaseRequest;
import com.carrotzmarket.api.domain.transaction.dto.TransactionHistoryDto;
import com.carrotzmarket.api.domain.transaction.dto.TransactionStatusUpdateRequest;
import com.carrotzmarket.api.domain.transaction.service.ProductTransactionService;
import com.carrotzmarket.db.transaction.ProductTransactionEntity;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Literal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    /**
     * TODO : 쿼리 파라미터 UserId를 받기 보다 세션을 이용하여 UserId를 받아올 수 있도록 수정
     */
    @GetMapping("/transaction/history/purchase/{userId}")
    public List<TransactionHistoryDto> getPurchaseHistory(@PathVariable Long userId) {
        return service.findAllPurchaseHistory(userId);
    }

    /**
     * TODO : 쿼리 파라미터 UserId를 받기 보다 세션을 이용하여 UserId를 받아올 수 있도록 수정
     */
    @GetMapping("/transaction/history/sales/{userId}")
    public List<TransactionHistoryDto> getSalesHistory(@PathVariable Long userId) {
        return service.findAllSalesHistory(userId);
    }

    @PutMapping("/transaction")
    public ProductTransactionEntity updateTransaction(@RequestBody TransactionStatusUpdateRequest request) {
        return service.updateTransaction(request);
    }

}
