package dev.fabianmild.wilfleinsdorfertest.domain;

import java.time.LocalDateTime;

public record Invoice(APIKey key, LocalDateTime date, Long price, Email email) {
}
