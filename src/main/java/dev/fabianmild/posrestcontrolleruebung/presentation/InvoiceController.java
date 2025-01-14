package dev.fabianmild.posrestcontrolleruebung.presentation;

import dev.fabianmild.posrestcontrolleruebung.domain.APIKey;
import dev.fabianmild.posrestcontrolleruebung.domain.Invoice;
import dev.fabianmild.posrestcontrolleruebung.service.InvoiceService;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static dev.fabianmild.posrestcontrolleruebung.presentation.InvoiceController.BASE_URL;
import static dev.fabianmild.posrestcontrolleruebung.presentation.RestAPIRouteSupport.API;
import static dev.fabianmild.posrestcontrolleruebung.presentation.RestAPIRouteSupport._SLASH;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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
    public ResponseEntity<EntityModel<InvoiceDto>> createInvoice(@RequestBody CreateInvoiceCommand command) {
        Invoice invoice = invoiceService.createInvoice(command.price(), command.email());

        //URI location = URI.create("%s%s%s".formatted(BASE_URL, _SLASH, invoice.key().value()));

        //return ResponseEntity.created(location).body(new InvoiceDto(invoice));

        // HATEOAS
        EntityModel<InvoiceDto> invoiceResource = EntityModel.of(new InvoiceDto(invoice),
                linkTo(methodOn(InvoiceController.class).getInvoice(invoice.key())).withSelfRel());
                //linkTo(methodOn(InvoiceController.class).getAllInvoices()).withRel("invoices"));  // könnte man z.B. auch machen

        URI location = linkTo(methodOn(InvoiceController.class).getInvoice(invoice.key())).toUri();

        return ResponseEntity.created(location).body(invoiceResource);
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
    public ResponseEntity<ProblemDetail> handleException(InvoiceException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        System.out.printf("An %s error occurred: %s", e.getClass().getSimpleName(), e.getMessage());

        return ResponseEntity.internalServerError().body(problemDetail);
    }
}
