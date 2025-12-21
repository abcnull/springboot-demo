package org.example.springbootdemo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 点击事件表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClickData {
    private String userId;
    private String productId;
    private String eventType;
    private Long eventTime;
    private String source;
}
