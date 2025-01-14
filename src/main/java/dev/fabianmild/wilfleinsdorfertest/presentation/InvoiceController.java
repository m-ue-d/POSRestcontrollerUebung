package dev.fabianmild.wilfleinsdorfertest.presentation;

import dev.fabianmild.wilfleinsdorfertest.domain.APIKey;
import dev.fabianmild.wilfleinsdorfertest.domain.Invoice;
import dev.fabianmild.wilfleinsdorfertest.service.InvoiceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static dev.fabianmild.wilfleinsdorfertest.presentation.InvoiceController.BASE_URL;
import static dev.fabianmild.wilfleinsdorfertest.presentation.RestAPIRouteSupport.API;
import static dev.fabianmild.wilfleinsdorfertest.presentation.RestAPIRouteSupport._SLASH;

@RestController
@RequestMapping(BASE_URL)
public class InvoiceController {
    protected static final String BASE_URL = API + _SLASH + "invoices";

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping
    public ResponseEntity<InvoiceDto> getInvoice(@RequestParam APIKey apiKey) {
        return invoiceService.getInvoice(apiKey)
                .map(InvoiceDto::new)
                .map(invoice -> ResponseEntity.ok().body(invoice))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<InvoiceDto> createInvoice(@RequestBody CreateInvoiceCommand command) {
        Invoice invoice = invoiceService.createInvoice(command.price(), command.email());

        URI location = URI.create("%s%s%s".formatted(BASE_URL, _SLASH, invoice.key().value()));

        return ResponseEntity.created(location).body(new InvoiceDto(invoice));
    }

    @PutMapping
    public ResponseEntity<InvoiceDto> updateInvoice(@RequestBody UpdateInvoiceCommand command) {
        return invoiceService.updateInvoice(command.key(), command.price(), command.email())
                .map(InvoiceDto::new)
                .map(invoice -> ResponseEntity.ok().body(invoice))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteInvoice(@RequestParam APIKey apiKey) {
        return invoiceService.deleteInvoice(apiKey)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @ExceptionHandler(InvoiceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void handleException(InvoiceException e) {
        System.out.printf("An %s error occurred: %s", e.getClass().getSimpleName(), e.getMessage());
    }
}
