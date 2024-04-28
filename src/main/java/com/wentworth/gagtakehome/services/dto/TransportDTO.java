package com.wentworth.gagtakehome.services.dto;

import java.util.UUID;

public record TransportDTO(UUID uuid, String id, String name, String likes, String transport, Double avgSpeed,
                           Double topSpeed) {

    public static TransportDTO build(String line) {
        String[] fields = line.split("\\|");
        return new TransportDTO(
                UUID.fromString(fields[0]),
                fields[1],
                fields[2],
                fields[3],
                fields[4],
                Double.parseDouble(fields[5]),
                Double.parseDouble(fields[6]));
    }
}
