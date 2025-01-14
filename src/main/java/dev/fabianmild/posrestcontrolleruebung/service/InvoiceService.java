package dev.fabianmild.posrestcontrolleruebung.service;

import dev.fabianmild.posrestcontrolleruebung.domain.APIKey;
import dev.fabianmild.posrestcontrolleruebung.domain.Email;
import dev.fabianmild.posrestcontrolleruebung.domain.Invoice;
import dev.fabianmild.posrestcontrolleruebung.presentation.InvoiceException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface InvoiceService {

    Invoice createInvoice(Long price, Email email) throws InvoiceException;
    Optional<Invoice> updateInvoice(APIKey key, Long price, Email email) throws InvoiceException;
    Optional<Invoice> getInvoice(APIKey key) throws InvoiceException;
    Boolean deleteInvoice(APIKey key) throws InvoiceException;
}
