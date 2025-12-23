package org.example.springbootdemo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBehaviorStatsDTO {
    private LocalDate date;
    private long totalEvents;
    private long uniqueUsers;
}
