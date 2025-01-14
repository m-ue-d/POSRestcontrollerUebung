package dev.fabianmild.posrestcontrolleruebung.presentation;

import dev.fabianmild.posrestcontrolleruebung.domain.Email;
import dev.fabianmild.posrestcontrolleruebung.domain.Invoice;

import java.time.LocalDateTime;

public record InvoiceDto(LocalDateTime date, Long price, Email email) {
    public InvoiceDto(Invoice invoice) {
        this(invoice.date(), invoice.price(), invoice.email());
    }
}