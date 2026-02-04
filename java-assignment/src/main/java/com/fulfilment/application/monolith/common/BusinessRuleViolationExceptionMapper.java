package com.fulfilment.application.monolith.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fulfilment.application.monolith.common.exception.BusinessRuleViolationException;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_CONFLICT;

@Provider
public class BusinessRuleViolationExceptionMapper implements ExceptionMapper<BusinessRuleViolationException> {

    private static final Logger LOGGER = Logger.getLogger(BusinessRuleViolationExceptionMapper.class);

    @Inject
    ObjectMapper objectMapper;

    @Override
    public Response toResponse(BusinessRuleViolationException exception) {
        LOGGER.error("Business rule violation", exception);

        int code = HTTP_BAD_REQUEST;
        if (exception.getMessage() != null && exception.getMessage().contains("already exists")) {
            code = HTTP_CONFLICT;
        }

        ObjectNode exceptionJson = objectMapper.createObjectNode();
        exceptionJson.put("exceptionType", exception.getClass().getName());
        exceptionJson.put("code", code);

        if (exception.getMessage() != null) {
            exceptionJson.put("error", exception.getMessage());
        }

        return Response.status(code).entity(exceptionJson).build();
    }
}