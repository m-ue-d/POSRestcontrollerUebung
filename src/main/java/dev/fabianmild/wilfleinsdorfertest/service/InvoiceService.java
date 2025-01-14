package dev.fabianmild.wilfleinsdorfertest.service;

import dev.fabianmild.wilfleinsdorfertest.domain.APIKey;
import dev.fabianmild.wilfleinsdorfertest.domain.Email;
import dev.fabianmild.wilfleinsdorfertest.domain.Invoice;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public interface InvoiceService {

    Invoice createInvoice(Long price, Email email);
    Optional<Invoice> getInvoice(APIKey key);
    Boolean deleteInvoice(APIKey key);
}
