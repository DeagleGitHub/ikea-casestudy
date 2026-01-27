package com.fulfilment.application.monolith.fulfilment.domain.usecases;

import com.fulfilment.application.monolith.fulfilment.adapters.database.FulfilmentRepository;
import com.fulfilment.application.monolith.fulfilment.domain.models.Fulfilment;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class GetFulfilmentUseCase {

    @Inject
    FulfilmentRepository fulfilmentRepository;

    public List<Fulfilment> getAll() {
        return fulfilmentRepository.findAll().list();
    }
}
