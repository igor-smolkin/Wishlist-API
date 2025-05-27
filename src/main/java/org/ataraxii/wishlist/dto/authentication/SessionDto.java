package org.ataraxii.wishlist.dto.authentication;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class SessionDto {
    private UUID sessionId;
    private Instant createdAt;
    private Instant expiredAt;
}
