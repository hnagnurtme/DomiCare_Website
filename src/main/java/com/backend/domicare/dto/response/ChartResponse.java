package com.backend.domicare.dto.response;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChartResponse {
    Map<String, Long> totalRevenue;
    Float growthRate;
}
