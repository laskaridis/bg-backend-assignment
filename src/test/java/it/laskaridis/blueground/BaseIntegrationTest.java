package it.laskaridis.blueground;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.laskaridis.blueground.users.model.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static java.lang.String.format;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BaseIntegrationTest {

    protected @Autowired MockMvc mockMvc;
    protected @Autowired UsersRepository users;
    protected @Autowired ObjectMapper objectMapper;

    protected String authenticate(String username, String password) throws Exception {
        String jwt = this.mockMvc.perform(post("/api/v1/auth/login")
                .header("x-auth-username", username)
                .header("x-auth-password", password))
                .andExpect(status().isNoContent())
                .andReturn()
                .getResponse()
                .getHeader("Authorization");
        return jwt;
    }

    protected String getBearerAuthenticationHeaderFor(String user, String pass) throws Exception {
        String jwt = authenticate(user, pass);
        return format("Bearer %s",jwt);
    }

    protected String getAdminBearerAuthenticationHeader() throws Exception {
        return getBearerAuthenticationHeaderFor("admin@localhost", "changeme");
    }
}
