package dev.fabianmild.wilfleinsdorfertest.service;

import dev.fabianmild.wilfleinsdorfertest.domain.APIKey;
import dev.fabianmild.wilfleinsdorfertest.domain.Email;
import dev.fabianmild.wilfleinsdorfertest.domain.Invoice;
import dev.fabianmild.wilfleinsdorfertest.presentation.InvoiceException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface InvoiceService {

    Invoice createInvoice(Long price, Email email) throws InvoiceException;
    Optional<Invoice> updateInvoice(APIKey key, Long price, Email email) throws InvoiceException;
    Optional<Invoice> getInvoice(APIKey key) throws InvoiceException;
    Boolean deleteInvoice(APIKey key) throws InvoiceException;
}
