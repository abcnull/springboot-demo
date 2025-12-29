package org.example.springbootdemo.service.impl;

import org.example.springbootdemo.dto.UserBehaviorStatsDTO;
import org.example.springbootdemo.service.IClickHouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * clickhouse service
 */
@Service
public class ClickHouseServiceImpl implements IClickHouseService {
    // ⬇️ 因为没有 clickhouse 服务端，如果 clickhouse 服务端 ok 可以开放这里
//    @Autowired
//    private JdbcTemplate clickHouseJdbcTemplate;

    /**
     * 查询用户行为统计 - 最常见的业务场景
     * <p>
     * clickhouse 查出来的数据转成 dto 数据 UserBehaviorStatsDTO
     * ClickHouse中时间类型要特别注意，Java的LocalDateTime对应ClickHouse的DateTime
     * 分页查询：避免使用OFFSET，改用基于游标的分页
     */
    public List<UserBehaviorStatsDTO> getUserBehaviorStats(LocalDate startDate, LocalDate endDate, String eventType) {
        // ⬇️ 因为没有 clickhouse 服务端，如果 clickhouse 服务端 ok 可以开放这里
//        String sql = "SELECT " +
//                "toDate(event_time) AS date, " +
//                "count() AS total_events, " +
//                "uniq(user_id) AS unique_users " +
//                "FROM user_events " +
//                "WHERE event_time BETWEEN ? AND ? " +
//                "AND event_type = ? " +
//                "GROUP BY date " +
//                "ORDER BY date";
//
//        return clickHouseJdbcTemplate.query(
//                sql,
//                new Object[]{
//                        startDate.atStartOfDay(), // 填充 sql 的 event_time 的开始时间
//                        endDate.atTime(23, 59, 59), // 填充 sql 的 event_time 的结束时间
//                        eventType // 填充 sql 的 event_type
//                },
//                (rs, rowNum) -> {
//                    UserBehaviorStatsDTO stats = new UserBehaviorStatsDTO();
//                    stats.setDate(rs.getDate("date").toLocalDate()); // 时间映射
//                    stats.setTotalEvents(rs.getLong("total_events")); // 事件数映射
//                    stats.setUniqueUsers(rs.getLong("unique_users")); // 用户数映射
//                    return stats;
//                }
//        );

        return null;
    }
}
