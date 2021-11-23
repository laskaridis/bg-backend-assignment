package it.laskaridis.blueground.units;

import it.laskaridis.blueground.BaseIntegrationTest;
import it.laskaridis.blueground.units.model.Unit;
import it.laskaridis.blueground.units.model.UnitsRepository;
import it.laskaridis.blueground.units.view.CreateUnitView;
import it.laskaridis.blueground.units.view.ShowUnitView;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;

import static it.laskaridis.blueground.units.Fixtures.*;
import static it.laskaridis.blueground.units.controller.ResourceUriFactories.newResourceUriForUnit;
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
public class UnitsControllerTests extends BaseIntegrationTest {

    private @Autowired UnitsRepository units;

    @Transactional
    public void shouldCreateUnits() throws Exception {
        CreateUnitView form = newValidCreateUnitForm();
        this.mockMvc.perform(post("/api/v1/units")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .header("Authorization", getAdminBearerAuthenticationHeader())
                .content(objectMapper.writeValueAsString(form)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value(form.getTitle()))
                .andExpect(jsonPath("$.region").value(form.getRegion()))
                .andExpect(jsonPath("$.priceAmount").value(form.getPriceAmount()))
                .andExpect(jsonPath("$.priceCurrency").value(form.getPriceCurrency()))
                .andExpect(jsonPath("$.imageFile").exists())
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.lastUpdatedAt").exists())
                .andExpect(jsonPath("$.createdBy").exists())
                .andExpect(jsonPath("$.lastUpdatedBy").exists());
    }

    @Test
    public void shouldNotCreateUnitWithNoTitle() throws Exception {
        CreateUnitView form = newCreateUnitFormWithNoTitle();
        this.mockMvc.perform(post("/api/v1/units")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .header("Authorization", getAdminBearerAuthenticationHeader())
                .content(objectMapper.writeValueAsString(form)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void shouldNotCreateUnitWithNoCancellationPolicy() throws Exception {
        CreateUnitView form = newCreateUnitFormWithNoCancellationPolicy();
        this.mockMvc.perform(post("/api/v1/units")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .header("Authorization", getAdminBearerAuthenticationHeader())
                .content(objectMapper.writeValueAsString(form)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void shouldNotCreateUnitWithNoRegion() throws Exception {
        CreateUnitView form = newCreateUnitFormWithNoRegion();
        this.mockMvc.perform(post("/api/v1/units")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .header("Authorization", getAdminBearerAuthenticationHeader())
                .content(objectMapper.writeValueAsString(form)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void shouldNotCreateUnitWithNoPriceAmount() throws Exception {
        CreateUnitView form = newCreateUnitFormWithNoPriceAmount();
        this.mockMvc.perform(post("/api/v1/units")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .header("Authorization", getAdminBearerAuthenticationHeader())
                .content(objectMapper.writeValueAsString(form)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void shouldNotCreateUnitWithNoPriceCurrency() throws Exception {
        CreateUnitView form = newCreateUnitFormWithNoPriceCurrency();
        this.mockMvc.perform(post("/api/v1/units")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .header("Authorization", getAdminBearerAuthenticationHeader())
                .content(objectMapper.writeValueAsString(form)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void shouldRetrieveUnitWhenPresent() throws Exception {
        String authHeader = getAdminBearerAuthenticationHeader();
        CreateUnitView form = newValidCreateUnitForm();
        Unit unit = this.units.save(form.toModel());
        String response = this.mockMvc.perform(get(newResourceUriForUnit(unit))
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .header("Authorization", authHeader)
                .content(objectMapper.writeValueAsString(form)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value(form.getTitle()))
                .andExpect(jsonPath("$.region").value(form.getRegion()))
                .andExpect(jsonPath("$.priceAmount").value(form.getPriceAmount()))
                .andExpect(jsonPath("$.priceCurrency").value(form.getPriceCurrency()))
                .andExpect(jsonPath("$.imageFile").isEmpty())
                .andReturn()
                .getResponse()
                .getContentAsString();
        ShowUnitView view = this.objectMapper.createParser(response).readValueAs(ShowUnitView.class);
        assertThat(this.units.findByUuid(view.getId())).isPresent();
    }

    @Test
    public void shouldNotRetrieveUnitWhenNotFound() throws Exception {
        String authHeader = getAdminBearerAuthenticationHeader();
        Unit unit = new Unit();
        unit.setUuid("invalid");
        this.mockMvc.perform(get(newResourceUriForUnit(unit))
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .header("Authorization", authHeader))
                .andExpect(status().isNotFound());
    }
}
