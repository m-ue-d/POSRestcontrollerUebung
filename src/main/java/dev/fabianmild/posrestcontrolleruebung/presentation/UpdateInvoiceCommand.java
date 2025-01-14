package dev.fabianmild.posrestcontrolleruebung.presentation;

import dev.fabianmild.posrestcontrolleruebung.domain.APIKey;
import dev.fabianmild.posrestcontrolleruebung.domain.Email;

public record UpdateInvoiceCommand (APIKey key, Long price, Email email) { }
