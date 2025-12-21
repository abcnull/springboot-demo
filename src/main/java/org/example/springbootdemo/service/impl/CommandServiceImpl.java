package org.example.springbootdemo.service.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.springbootdemo.service.ICommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 发送 CMD 命令的 service
 * <p>
 * 使用 WebSocket 发送 CMD 命令
 */
@Service
@Slf4j
public class CommandServiceImpl implements ICommandService {
    // 引入 spring-boot-starter-websocket 依赖后，
    // 加上 类中有了 @EnableWebSocketMessageBroker 注解后可以直接注入使用，关键就是这个 @EnableWebSocketMessageBroker 注解实现了相应 Bean 的生成
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // 1. 直接发送给特定用户
    // 也可以先将数据存储到 redis 中，成功就删除 redis 数据，异常就不删除，等客户端重连接时候，去查询下 redis 中数据 for 循环回发给客户端，成功就剔除 redis 中数据，这样来保障数据发给客户端无丢失
    public <T> void sendToUser(String userId, String cmdType, T payload) {
        CmdMessage<T> message = new CmdMessage<>(cmdType, payload);

        // 发送给特定用户
        messagingTemplate.convertAndSendToUser(userId, "/queue/cmd", message);
    }

    // 2. 广播给房间内所有用户
    public <T> void broadcastToRoom(String roomId, String cmdType, T payload) {
        CmdMessage<T> message = new CmdMessage<>(cmdType, payload);

        // 广播给房间内所有用户
        messagingTemplate.convertAndSend("/topic/room/" + roomId + "/cmd", message);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CmdMessage<T> {
        private String cmdType;     // 命令类型：GIFT_ANIMATION, ROOM_NOTICE等
        private T payload;          // 命令载荷
    }
}
