package it.laskaridis.blueground.users;

import it.laskaridis.blueground.BaseIntegrationTest;
import it.laskaridis.blueground.users.controller.UserEmailAlreadyExistException;
import it.laskaridis.blueground.users.model.User;
import it.laskaridis.blueground.users.view.CreateUserView;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.ResultActions;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;
import java.util.Optional;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class UserControllerTest extends BaseIntegrationTest {

    @Test
    public void shouldCreateValidUsers() throws Exception {
        CreateUserView form = Fixtures.newCreateValidUserForm();
        this.mockMvc.perform(post("/api/v1/users")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .header("Authorization", getAdminBearerAuthenticationHeader())
                .content(objectMapper.writeValueAsString(form)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.email").value(form.getEmail()))
                .andExpect(jsonPath("$.role").value(form.getRole()))
                .andExpect(jsonPath("$.timezone").value(form.getTimezone()))
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.lastUpdatedAt").exists())
                .andExpect(jsonPath("$.createdBy").exists())
                .andExpect(jsonPath("$.lastUpdatedBy").exists());

        Optional<User> user = this.users.findByEmail(form.getEmail());
        assertThat(user).isPresent();
    }

    @Test
    public void shouldNotCreateUserWithNoEmail() throws Exception {
        String jwt = authenticate("admin@localhost", "changeme");
        CreateUserView form = Fixtures.newCreateUserFormWithNoEmail();
        ResultActions resultActions = this.mockMvc.perform(post("/api/v1/users")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .header("Authorization", format("Bearer %s", jwt))
                .content(objectMapper.writeValueAsString(form)))
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(ConstraintViolationException.class));
    }

    @Test
    public void shouldNotCreateUserWithAlreadyExistingEmail() throws Exception {
        String jwt = authenticate("admin@localhost", "changeme");
        CreateUserView form = Fixtures.newCreateUserFormWithAlreadyExistingEmail();
        this.mockMvc.perform(post("/api/v1/users")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .header("Authorization", format("Bearer %s", jwt))
                .content(objectMapper.writeValueAsString(form)))
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(UserEmailAlreadyExistException.class));
    }

    @Test
    public void shouldNotCreateUserWithNoPassword() throws Exception {
        String jwt = authenticate("admin@localhost", "changeme");
        CreateUserView form = Fixtures.newCreateUserFormWithNoPassword();
        this.mockMvc.perform(post("/api/v1/users")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .header("Authorization", format("Bearer %s", jwt))
                .content(objectMapper.writeValueAsString(form)))
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(ConstraintViolationException.class));
    }

    @Test
    public void shouldNotCreateUserWithNoRole() throws Exception {
        String jwt = authenticate("admin@localhost", "changeme");
        CreateUserView form = Fixtures.newCreateUserFormWithNoRole();
        this.mockMvc.perform(post("/api/v1/users")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .header("Authorization", format("Bearer %s", jwt))
                .content(objectMapper.writeValueAsString(form)))
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(ConstraintViolationException.class));
    }

    @Test
    public void shouldRetrieveAllUsers() throws Exception {
        String jwt = authenticate("admin@localhost", "changeme");
        this.mockMvc.perform(get("/api/v1/users")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .header("Authorization", format("Bearer %s", jwt)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id").exists())
                .andExpect(jsonPath("$[*].email").exists())
                .andExpect(jsonPath("$[*].role").exists())
                .andExpect(jsonPath("$[*].timezone").exists())
                .andExpect(jsonPath("$[*].createdAt").exists())
                .andExpect(jsonPath("$[*].lastUpdatedAt").exists())
                .andExpect(jsonPath("$[*].createdBy").exists())
                .andExpect(jsonPath("$[*].lastUpdatedBy").exists());
    }
}
