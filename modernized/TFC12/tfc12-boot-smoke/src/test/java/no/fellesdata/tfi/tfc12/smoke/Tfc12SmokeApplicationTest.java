package no.fellesdata.tfi.tfc12.smoke;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class Tfc12SmokeApplicationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void contextLoads() {
    }

    @Test
    void pingEndpoint() throws Exception {
        mockMvc.perform(get("/tfc12/smoke/ping"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.system").value("TFC12"));
    }

    @Test
    void createChargeSmokeRunsHklStack() throws Exception {
        mockMvc.perform(post("/tfc12/smoke/create-charge"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.returnKind").value("OK"));
    }
}
