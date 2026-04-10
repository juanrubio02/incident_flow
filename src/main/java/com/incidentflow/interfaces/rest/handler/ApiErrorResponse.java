package com.incidentflow.interfaces.rest.handler;

import java.time.Instant;

public record ApiErrorResponse(
        String message,
        Instant timestamp
) {
}
