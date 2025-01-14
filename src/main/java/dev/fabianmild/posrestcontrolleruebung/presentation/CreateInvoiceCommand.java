package dev.fabianmild.posrestcontrolleruebung.presentation;

import dev.fabianmild.posrestcontrolleruebung.domain.Email;

public record CreateInvoiceCommand(Long price, Email email) { }
