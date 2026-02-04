package com.fulfilment.application.monolith.fulfilment.domain.usecases;

import com.fulfilment.application.monolith.fulfilment.adapters.database.FulfilmentRepository;
import com.fulfilment.application.monolith.fulfilment.domain.models.Fulfilment;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetFulfilmentUseCaseTest {

    @Mock
    private FulfilmentRepository fulfilmentRepository;

    @InjectMocks
    private GetFulfilmentUseCase getFulfilmentUseCase;

    @Test
    @DisplayName("GIVEN fulfilments exist WHEN getting all THEN return list of fulfilments")
    void getAllFulfilmentsSuccessfully() {
        Fulfilment fulfilment = new Fulfilment();
        List<Fulfilment> expectedList = Collections.singletonList(fulfilment);
        
        PanacheQuery query = mock(PanacheQuery.class);
        when(fulfilmentRepository.findAll()).thenReturn(query);
        when(query.list()).thenReturn(expectedList);

        List<Fulfilment> result = getFulfilmentUseCase.getAll();

        assertEquals(expectedList, result);
    }
}
