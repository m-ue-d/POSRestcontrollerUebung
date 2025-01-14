package dev.fabianmild.wilfleinsdorfertest.persistence;

import dev.fabianmild.wilfleinsdorfertest.domain.APIKey;
import dev.fabianmild.wilfleinsdorfertest.domain.Invoice;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public interface InvoiceService {

    Invoice createInvoice(Long price, LocalDateTime date);
    Invoice getInvoice(APIKey key);
    Boolean deleteInvoice();
}
