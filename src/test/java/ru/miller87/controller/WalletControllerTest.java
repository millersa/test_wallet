package ru.miller87.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.miller87.model.dto.WalletRequestDto;
import ru.miller87.service.WalletService;

import java.util.UUID;

import static org.mockito.Mockito.when;

@WebMvcTest(WalletController.class)
public class WalletControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WalletService walletService;

    @Test
    public void testUpdateWallet() throws Exception {
        WalletRequestDto walletRequestDto = new WalletRequestDto();
        mockMvc.perform(MockMvcRequestBuilders.put("/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(walletRequestDto)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testGetWallet() throws Exception {
        UUID walletId = UUID.randomUUID();
        when(walletService.getWallet(walletId)).thenReturn(100L);
        mockMvc.perform(MockMvcRequestBuilders.get("/wallet/{id}", walletId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("100"));
    }

    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
