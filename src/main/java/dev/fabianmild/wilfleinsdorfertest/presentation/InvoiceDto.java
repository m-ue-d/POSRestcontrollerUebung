package dev.fabianmild.wilfleinsdorfertest.presentation;

import dev.fabianmild.wilfleinsdorfertest.domain.Email;
import dev.fabianmild.wilfleinsdorfertest.domain.Invoice;

import java.time.LocalDateTime;

public record InvoiceDto(LocalDateTime date, Long price, Email email) {
    public InvoiceDto(Invoice invoice) {
        this(invoice.date(), invoice.price(), invoice.email());
    }
}