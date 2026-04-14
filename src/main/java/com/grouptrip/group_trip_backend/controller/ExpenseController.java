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
}