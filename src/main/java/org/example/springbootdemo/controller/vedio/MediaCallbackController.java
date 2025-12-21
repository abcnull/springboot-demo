package org.example.springbootdemo.controller.vedio;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.example.springbootdemo.service.ICommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

/**
 * 【通用流程简单解释：】
 * 客户端 (SEI) -> 多媒体服 (回调业务 HTTP 接口) -> 业务 HTTP 接口承接 (CMD 命令) -> 客户端
 * <p>
 * 【SEI 流程解释：】
 * 客户端 >------------- SEI 信息 --------------> 多媒体服务
 * ************************************************ V
 * ************************************************ ｜
 * ************************************************ ｜
 * ********************************* 处理后的数据，回调业务 http 接口来发送数据
 * ************************************************ ｜
 * ************************************************ ｜
 * ************************************************ V
 * ******************************* 业务服务端的回调 HTTP 接口（当前 controller）
 * <p>
 * 【CMD 流程解释：】
 * 多媒体服务 <-- http 连接 --> 业务服务端的回调 HTTP 接口（当前 controller）--------
 * ************************************************************************* V
 * ************************************************************************* ｜
 * 客户端 <--- http 连接 ---> 常规客户度调用 http 请求到业务服务端 controller ***** ｜
 * ************************************************************* V ********* ｜
 * ************************************************************* ｜ ******** ｜
 * ************************************************************* ｜ ******** ｜
 * ********************************************************* 执行业务逻辑及发送 CMD 命令
 * ************************************************************* ｜ ******** ｜
 * ************************************************************* ｜ ******** ｜
 * ************************************************************* V ********* V
 * 客户端 <--- websocket 已经保持互相连接 ---> 业务服务端 socket 连接处 SimpMessagingTemplate
 * <p>
 * 服务端在处理和客户端视频交互的场景时候，
 * 一般客户端发送 SEI 数据给到多媒体服务，
 * 多媒体服务会对 SEI 数据进行处理后，调用业务服务端（这里）的 http 接口，将关键数据传过来
 * 业务服务端可以对数据做处理后执行一些业务操作，也可以执行 CMD 命令
 * 当然 CMD 命令也可以客户端 http 请求业务服务端时候，业务服务端在执行也可以
 * 这里就是多媒体服务回调的业务 http 接口
 */
@RestController
@RequestMapping("/media")
@Slf4j
public class MediaCallbackController {
    @Autowired
    private ICommandService commandService;

    /**
     * SRS服务器回调接口 - 处理SEI数据
     */
    @PostMapping("/sei/callback")
    public String handleSeiCallback(@RequestBody SeiCallbackRequest request) {
        log.info("收到SEI回调数据: {}", request);

        // 1. 验证签名，防止未授权调用
        if (!validateSignature(request)) {
            return "签名验证失败";
        }

        // 2. 根据SEI类型处理不同业务
        switch (request.getSeiType()) {
            case "GIFT":
                System.out.println("收到 GIFT SEI 数据，执行相应业务处理逻辑");
                // 发送 CMD 命令给客户端（WebSocket）
                commandService.broadcastToRoom("123", "GIFT_ANIMATION", request.getSeiPayload());
            case "CHAT":
                System.out.println("收到 CHAT SEI 数据，执行相应业务处理逻辑");
                // 发送 CMD 命令给客户端（WebSocket）
                commandService.sendToUser("123456789", "CHAT_MESSAGE", request.getSeiPayload());
            case "INTERACTION":
                System.out.println("收到 INTERACTION SEI 数据，执行相应业务处理逻辑");
            default:
                return "不支持的SEI类型";
        }
    }

    /**
     * 验证SRS回调签名
     */
    private boolean validateSignature(SeiCallbackRequest request) {
        // 实际 SECRET_KEY 来自于
        String SECRET_KEY = "123456";

        String signature = DigestUtils.md5DigestAsHex(
                (request.getApp() + request.getStream() + request.getSeiPayload() + SECRET_KEY).getBytes()
        );
        return signature.equals(request.getSignature());
    }

    // SEI回调请求结构
    @Data
    public static class SeiCallbackRequest {
        private String action;      // 固定为"on_sei"
        private String vhost;       // 虚拟主机
        private String app;         // 应用名
        private String stream;      // 流名
        private String clientId;    // 客户端ID
        private String seiType;     // SEI类型
        private String seiPayload;  // SEI负载数据
        private String timestamp;   // 时间戳
        private String signature;   // 签名，用于验证回调来源
    }
}
