package dev.fabianmild.wilfleinsdorfertest.presentation;

import dev.fabianmild.wilfleinsdorfertest.domain.Email;

import java.time.LocalDateTime;

public record CreateInvoiceCommand(Long price, Email email) { }
