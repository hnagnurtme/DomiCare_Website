package com.backend.domicare.service;

import java.util.Map;

import com.backend.domicare.dto.DashboardSummaryDTO;
import com.backend.domicare.dto.request.LocalDateRequest;

public interface DashBoardService {
    Map<String, DashboardSummaryDTO> getDashboardSummary(LocalDateRequest localDateRequest);
}
