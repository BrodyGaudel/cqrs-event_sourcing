package com.brodygaudel.ebank.query.model;

public record SearchCustomersQuery(String keyword, int page, int size) {
}
