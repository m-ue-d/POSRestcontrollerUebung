package dev.fabianmild.posrestcontrolleruebung.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.fabianmild.posrestcontrolleruebung.domain.APIKey;
import dev.fabianmild.posrestcontrolleruebung.domain.Email;
import dev.fabianmild.posrestcontrolleruebung.domain.Invoice;
import dev.fabianmild.posrestcontrolleruebung.service.InvoiceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Optional;

import static dev.fabianmild.posrestcontrolleruebung.presentation.InvoiceController.BASE_URL;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InvoiceController.class)
public class InvoiceControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockitoBean
    InvoiceService invoiceService;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    void createInvoiceTest() throws Exception{
        CreateInvoiceCommand cmd = new CreateInvoiceCommand(100L, new Email("ichbinheselig@oarm.spendegeldaunchristian"));
        var now = LocalDateTime.now();

        //geht auch any(), any(), any() statt cmd.price(), cmd.email()
        when(invoiceService.createInvoice(cmd.price(), cmd.email())).thenReturn(
                new Invoice(new APIKey("1234"), now, cmd.price(), cmd.email())
        );

        var request = post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cmd));

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$.date").value(now.toString()))
                .andExpect(jsonPath("$.price").value(cmd.price()))
                .andExpect(jsonPath("$.email.address").value(cmd.email().address()))
                .andDo(print());
    }

    @Test
    void testCreateInvoiceException() throws Exception {
        CreateInvoiceCommand cmd = new CreateInvoiceCommand(100L, new Email("nice@gmail.com"));

        when(invoiceService.createInvoice(cmd.price(), cmd.email())).thenThrow(new InvoiceException("Error"));

        var request = post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cmd));

        mockMvc.perform(request)
                .andExpect(status().isInternalServerError());
    }

    @Test
    void getInvoiceTest() throws Exception{
        var now = LocalDateTime.now().withNano(0);
        Invoice invoice = new Invoice(new APIKey("1234"), now, 100L, new Email("ichbinheselig@oarm.spendegeldaunchristian"));

        when(invoiceService.getInvoice(invoice.key())).thenReturn(
                Optional.of(invoice)
        );

        var request = get(BASE_URL).param("apiKey", invoice.key().value())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.date").value(now.toString()))
                .andExpect(jsonPath("$.price").value(100L))
                .andExpect(jsonPath("$.email.address").value(invoice.email().address()));
    }

    @Test
    void getInvoiceNotFoundTest() throws Exception {
        var key = new APIKey("1234");

        when(invoiceService.getInvoice(key)).thenReturn(
                Optional.empty()
        );

        var request = get(BASE_URL).param("apiKey", key.value())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteInvoice() throws Exception {
        var key = new APIKey("1234");

        when(invoiceService.deleteInvoice(key)).thenReturn(
                true
        );

        var request = delete(BASE_URL).param("apiKey", key.value())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteInvoiceNotFound() throws Exception {
        var key = new APIKey("1234");

        when(invoiceService.deleteInvoice(key)).thenReturn(
                false
        );

        var request = delete(BASE_URL).param("apiKey", key.value())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateInvoice() throws Exception {
        UpdateInvoiceCommand cmd = new UpdateInvoiceCommand(new APIKey("1234"), 100L, new Email("ichbinheselig@oarm.spendegeldaunchristian"));

        when(invoiceService.updateInvoice(cmd.key(), cmd.price(), cmd.email())).thenReturn(
                Optional.of(new Invoice(cmd.key(), LocalDateTime.now(), cmd.price(), cmd.email()))
        );

        var request = put(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cmd));

        mockMvc.perform(request)
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateInvoiceNotFound() throws Exception {
        UpdateInvoiceCommand cmd = new UpdateInvoiceCommand(new APIKey("1234"), 100L, new Email("ichbinheselig@oarm.spendegeldaunchristian"));

        when(invoiceService.updateInvoice(cmd.key(), cmd.price(), cmd.email())).thenReturn(
                Optional.empty()
        );

        var request = put(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cmd));

        mockMvc.perform(request)
                .andExpect(status().isNotFound());
    }
}
