package it.laskaridis.blueground.units;

import it.laskaridis.blueground.units.model.RelationalUnitSearchService;
import it.laskaridis.blueground.units.model.Unit;
import it.laskaridis.blueground.units.model.UnitsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@SpringJUnitConfig
public class RelationalUnitSearchServiceTests {

    private RelationalUnitSearchService subject;

    private @MockBean UnitsRepository units;

    @BeforeEach
    public void setUp() {
        this.subject = new RelationalUnitSearchService(units);
    }

    public Unit createUnit() {
        Unit unit = new Unit();
        unit.setUuid(UUID.randomUUID().toString());
        return unit;
    }

    @Test
    public void search_shouldSearchByQueryTerm() {
        // Given
        Optional<String> term = Optional.of("my query term");
        Pageable pageable = mock(Pageable.class);
        Page<Unit> expectedResult = mock(Page.class);

        // Then
        doReturn(expectedResult).when(this.units)
                .findAllByTitleContainingOrRegionContainingIgnoreCase(
                        term.get(), term.get(), pageable);

        // When
        Page<Unit> result = this.subject.search(term, pageable);
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    public void search_shouldSearchByNoQueryTerm() {
        // Given
        Pageable pageable = mock(Pageable.class);
        Page<Unit> expectedResult = mock(Page.class);

        // Then
        doReturn(expectedResult).when(this.units).findAll(pageable);

        // When
        Page<Unit> result = this.subject.search(pageable);
        assertThat(result).isEqualTo(expectedResult);
    }
}
