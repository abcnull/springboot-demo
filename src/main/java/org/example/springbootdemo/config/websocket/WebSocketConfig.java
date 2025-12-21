package org.example.springbootdemo.config.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * 监听客户端 websocket 连接 config
 * <p>
 * 1.客户端先连接WebSocket端点（/ws）
 * 2.客户端订阅特定主题（如/topic/room/123/updates）
 * 3.服务端通过messagingTemplate.convertAndSend()向该主题发送消息
 * 4.所有订阅该主题的客户端自动收到消息
 * <p>
 * 注解 @EnableWebSocketMessageBroker 的作用？
 * 1.创建基础设施 Bean（包括 SimpMessagingTemplate）
 * 2.查找 WebSocketMessageBrokerConfigurer 实现
 * 3.调用配置方法完成自定义设置
 * 4.于是可以让 @Autowired SimpMessagingTemplate 成功注入
 * <p>
 * WebSocketMessageBrokerConfigurer 接口作用？
 * 1.这是一个回调接口，提供自定义配置的钩子
 * 2.不实现它也能注入 SimpMessagingTemplate，但无法自定义端点和消息代理
 * 3.Spring 会查找所有实现这个接口的 Bean，并调用它们的方法
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    // 灵活配置 WebSocket 端点，客户度通过什么 URL 连接
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 配置 WebSocket 端点，客户端通过这个 URL 连接
        registry.addEndpoint("/ws")  // 1. 客户端连接的地址
                .setAllowedOriginPatterns("*")  // 允许所有跨域
                .withSockJS();  // 启用SockJS降级
    }

    // 配置发送消息的前缀
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 2. 配置服务端可以推送消息的前缀
        registry.enableSimpleBroker("/topic", "/queue");  // 广播和点对点前缀

        // 3. 配置客户端发送消息的前缀
        registry.setApplicationDestinationPrefixes("/app");

        // 4. 配置点对点消息前缀
        registry.setUserDestinationPrefix("/user");
    }
}