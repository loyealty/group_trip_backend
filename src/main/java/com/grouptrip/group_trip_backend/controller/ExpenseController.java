package com.grouptrip.group_trip_backend.controller;

import com.grouptrip.group_trip_backend.entity.Expense;
import com.grouptrip.group_trip_backend.repository.ExpenseRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@CrossOrigin(origins = "*")
public class ExpenseController {

    private final ExpenseRepository expenseRepository;

    public ExpenseController(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    @PostMapping
    public Expense createExpense(@RequestBody Expense expense) {
        return expenseRepository.save(expense);
    }

    @GetMapping
    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }

    @GetMapping("/trip-room/{tripRoomId}")
    public List<Expense> getExpensesByTripRoomId(@PathVariable Long tripRoomId) {
        return expenseRepository.findByTripRoomId(tripRoomId);
    }

    @PutMapping("/{id}")
    public Expense updateExpense(@PathVariable Long id, @RequestBody Expense request) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("정산 내역을 찾을 수 없습니다."));

        expense.setCategory(request.getCategory());
        expense.setTitle(request.getTitle());
        expense.setPayer(request.getPayer());
        expense.setAmount(request.getAmount());

        return expenseRepository.save(expense);
    }

    @DeleteMapping("/{id}")
    public void deleteExpense(@PathVariable Long id) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("정산 내역을 찾을 수 없습니다."));

        expenseRepository.delete(expense);
    }
}