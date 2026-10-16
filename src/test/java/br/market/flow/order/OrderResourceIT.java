package br.market.flow.order;

import br.market.flow.order.domain.OrderStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OrderResourceIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void createGetAndCancelOrder() throws Exception {
        String payload = """
                {
                  "userId": "user-1",
                  "address": "Rua A, 100",
                  "paymentMethod": "CREDIT_CARD",
                  "items": [
                    {
                      "productId": "sku-1",
                      "productPrice": 10.50,
                      "productQuantity": 2
                    }
                  ]
                }
                """;

        MvcResult created = mockMvc.perform(post("/api/v1/orders")
                        .header("X-Idempotency-Key", "key-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderId").isNotEmpty())
                .andReturn();

        String orderId = objectMapper.readTree(created.getResponse().getContentAsString()).get("orderId").asText();

        mockMvc.perform(get("/api/v1/orders/{orderId}", orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("user-1"))
                .andExpect(jsonPath("$.address").value("Rua A, 100"))
                .andExpect(jsonPath("$.paymentMethod").value("CREDIT_CARD"))
                .andExpect(jsonPath("$.status").value(OrderStatus.CREATED.name()))
                .andExpect(jsonPath("$.items[0].productId").value("sku-1"))
                .andExpect(jsonPath("$.items[0].productQuantity").value(2));

        mockMvc.perform(patch("/api/v1/orders/{orderId}/cancel", orderId))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.status").value(OrderStatus.CANCELLED.name()));

        mockMvc.perform(patch("/api/v1/orders/{orderId}/cancel", orderId))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void getUnknownOrderReturnsNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/orders/{orderId}", "missing"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createOrderRejectsInvalidPayload() throws Exception {
        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("userId", ""))))
                .andExpect(status().isBadRequest());
    }
}
