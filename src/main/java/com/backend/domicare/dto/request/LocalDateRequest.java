package com.backend.domicare.dto.request;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssX")
public class LocalDateRequest {
    @NotNull(message = "startDate không được để trống")
    LocalDate startDate;
    @NotNull(message = "endDate không được để trống")
    LocalDate endDate;
}
