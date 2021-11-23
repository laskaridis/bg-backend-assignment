package it.laskaridis.blueground.bookings;

import it.laskaridis.blueground.BaseIntegrationTest;
import it.laskaridis.blueground.bookings.model.BookingsRepository;
import it.laskaridis.blueground.bookings.view.CreateBookingParams;
import it.laskaridis.blueground.units.model.Unit;
import it.laskaridis.blueground.units.model.UnitsRepository;
import it.laskaridis.blueground.units.view.CreateUnitView;
import it.laskaridis.blueground.users.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;

import static it.laskaridis.blueground.bookings.controller.ResourceUriFactories.createResourceUriForUnitBookings;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class BookingsController extends BaseIntegrationTest {

    private @Autowired BookingsRepository bookings;
    private @Autowired UnitsRepository units;

    // fixtures:
    private Unit unit;
    private User user;

    @BeforeEach
    public void setUp() {
        this.unit = this.units.save(CreateUnitView.builder()
                .title("my unit")
                .region("Europe")
                .cancellationPolicy("none")
                .priceAmount(BigDecimal.valueOf(10))
                .priceCurrency("EUR")
                .build()
                .toModel()
        );
        this.user = this.users.findByEmail("admin@localhost").get();
    }

    @Test
    public void shouldCreateBooking() throws Exception {
        LocalDate bookedFrom = LocalDate.now().plusYears(1);
        LocalDate bookedUntil = bookedFrom.plusMonths(1);
        CreateBookingParams form = CreateBookingParams.builder()
                .bookedFrom(LocalDate.now().plusYears(1))
                .bookedUntil(bookedUntil)
                .build();

        String jwtToken = getAdminBearerAuthenticationHeader();
        String newBookingUri = this.mockMvc.perform(post(createResourceUriForUnitBookings(this.unit))
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .header("Authorization", jwtToken)
                .content(objectMapper.writeValueAsString(form.toModel(unit, user))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.bookingId").exists())
                .andExpect(jsonPath("$.unitId").value(this.unit.getUuid()))
                .andExpect(jsonPath("$.bookedFrom").exists())
                .andExpect(jsonPath("$.bookedUntil").exists())
                .andExpect(jsonPath("$.reminderScheduledAt").exists())
                .andExpect(jsonPath("$.reminderSentAt").isEmpty())
                .andReturn()
                .getResponse()
                .getHeader("Location");

        this.mockMvc.perform(get(newBookingUri)
                .header("Authorization", jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookingId").exists())
                .andExpect(jsonPath("$.unitId").value(this.unit.getUuid()))
                .andExpect(jsonPath("$.bookedFrom").exists())
                .andExpect(jsonPath("$.bookedUntil").exists())
                .andExpect(jsonPath("$.reminderScheduledAt").exists())
                .andExpect(jsonPath("$.reminderSentAt").isEmpty());
    }

    @Test
    public void shouldNotCreateBookingWithNoStartDate() throws Exception {
        LocalDate bookedFrom = LocalDate.now().plusYears(1);
        LocalDate bookedUntil = bookedFrom.plusMonths(1);
        CreateBookingParams form = CreateBookingParams.builder()
                .bookedUntil(bookedUntil)
                .build();

        String jwtToken = getAdminBearerAuthenticationHeader();
        this.mockMvc.perform(post(createResourceUriForUnitBookings(this.unit))
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .header("Authorization", jwtToken)
                .content(objectMapper.writeValueAsString(form.toModel(unit, user))))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void shouldNotCreateBookingWithNoEndDate() throws Exception {
        LocalDate bookedFrom = LocalDate.now().plusYears(1);
        CreateBookingParams form = CreateBookingParams.builder()
                .bookedFrom(bookedFrom)
                .build();

        String jwtToken = getAdminBearerAuthenticationHeader();
        this.mockMvc.perform(post(createResourceUriForUnitBookings(this.unit))
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .header("Authorization", jwtToken)
                .content(objectMapper.writeValueAsString(form.toModel(unit, user))))
                .andExpect(status().isUnprocessableEntity());
    }
}
