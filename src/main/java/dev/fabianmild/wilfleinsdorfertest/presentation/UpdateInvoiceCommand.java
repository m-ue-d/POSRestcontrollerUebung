package dev.fabianmild.wilfleinsdorfertest.presentation;

import dev.fabianmild.wilfleinsdorfertest.domain.APIKey;
import dev.fabianmild.wilfleinsdorfertest.domain.Email;

public record UpdateInvoiceCommand (APIKey key, Long price, Email email) { }
