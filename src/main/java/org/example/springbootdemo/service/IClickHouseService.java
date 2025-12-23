package org.example.springbootdemo.service;

import org.example.springbootdemo.dto.UserBehaviorStatsDTO;

import java.time.LocalDate;
import java.util.List;

public interface IClickHouseService {

    List<UserBehaviorStatsDTO> getUserBehaviorStats(LocalDate startDate, LocalDate endDate, String eventType);
}
