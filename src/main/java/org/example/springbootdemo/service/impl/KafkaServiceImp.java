package org.example.springbootdemo.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.springbootdemo.service.IKafkaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.List;

/**
 * kafka service
 * <p>
 * 「生产者」 生产 消息，消息发送到 kafka 集群 =>
 * kafka 集群将消息放在某一个 partition 分区中，依据 key 判定放在哪个分区，发送到消费者组 =>
 * 该消息只能被消费者组中的一个消费者消费，但是不同的消费组都可以消费该消息
 * <p>
 * 如果有 A，B 两个消费组，其中 A 消费组有 C1，C2 两个消费者，那么当 kafka 集群配置好消费组（包括其中消费者）时，那么 kafka 就已经定好哪几个分区固定发给消费组 A 中的 C1，哪几个分区未来始终固定发给消费组 B 中的 C2
 * <p>
 * 关键词：主题，生产者，消费组，消费者，分区，key，偏移量
 * <p>
 * 关键场景：
 * 1.生产者生产消息发往 kafka 集群 topic，发送指定 topic 和 msg
 * 2.kafka 集群依据 key 将消息放入依据 key 计算出来的分区，key 可由消费者发送
 * 3.kafka 默认策略，一旦 kafka 集群配置好消费组和消费者，消费组中具体哪个消费者消费具体哪一个分区就已经确定好了
 * 4.同一个 topic 的消息，不同消费组都可以消费，但是同一个消费组中只有一个消费者可以消费，消费指定 topic 和消费组
 * 5.消费者消费默认是自动提交偏移量，消费 5s 后自动告诉 kafka 消息被消费了，下次无需再发送了
 * 6.消费者手动提交偏移量，需要 application.yml 配置支持手动提交偏移量，且配置 Configuration 中指定 kafka 的 bean，然后编写方法和注解，注解指定这个 kafka 提交方式的 bean，代码中通过 ack.acknowledge(); 手动提交偏移量
 */
@Slf4j
@Service
public class KafkaServiceImp implements IKafkaService {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    /* ========== 生产者 ========== */

    /**
     * 生产者
     * 常规发送消息
     *
     * @param topic 主题
     * @param msg   消息
     */
    @Override
    public void sendMsg(String topic, String msg) {
        // 给 topic 主题发送消息
        // 和 sendDefault() 区别，一个需要指定主题，一个用默认主题
        kafkaTemplate.send(topic, msg);
    }

    /**
     * 生产者
     * 发送带有 key 的消息
     * kafka 使用 key 的哈希值来决定消息发往 topic 的哪个 partition，相同的 key 发到的分区也相同
     * 即：相同 key 的消息能保证在该分区是顺序发出的
     * key 通常表示业务主键，如用户 id，订单 id 等，比如 user_12345
     *
     * @param topic 主题
     * @param key   key
     * @param msg   消息
     */
    @Override
    public void sendMsgWithKey(String topic, String key, String msg) {
        // 给 topic 主题发送消息
        kafkaTemplate.send(topic, key, msg);
    }

    /**
     * 生产者
     * 发送消息并且获取结果
     *
     * @param topic 主题
     * @param msg   消息
     */
    @Override
    public void sendMsgAsync(String topic, String msg) {
        // 发送消息，并且获取 future
        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, msg);

        future.addCallback(
                result -> System.out.println("Message sent successfully"),
                ex -> System.out.println("Failed to send message: " + ex.getMessage())
        );
    }

    /**
     * 生产者
     * 批量发送消息
     *
     * @param topic   主题
     * @param msgList 批量消息
     */
    @Override
    public void sendBatchMsg(String topic, List<String> msgList) {
        for (String msg : msgList) {
            kafkaTemplate.send(topic, msg);
        }
    }

    /* ========== 消费者 ========== */

    /**
     * 消费者
     * 通过注解来监听消息过来
     * 如果有多个服务都配置这个消费者组 groupId = "my-group"，那么消息过来只会被该消费者组中的其中 1 个消费者正常消费
     *
     * @param msg 消息
     */
    @KafkaListener(topics = "my-topic", groupId = "my-group")
    @Override
    public void listenMsg(String msg) {
        System.out.println("接收消息: msg");
    }

    /**
     * 消费者
     * 监听多个主题
     *
     * @param msg 消息
     */
    @KafkaListener(topics = {"my-topic", "my-topic2"}, groupId = "my-group")
    @Override
    public void listenMutiTopics(String msg) {
        // 消息处理
    }

    /**
     * 消费者
     * 获取完整消息元数据
     *
     * @param msg       消息
     * @param topic     主题
     * @param partition 分区
     * @param offset    偏移
     */
    @KafkaListener(topics = "my-topic", groupId = "my-group")
    @Override
    public void listenWithMetadata(String msg,
                                   @Header(KafkaHeaders.RECEIVED_TOPIC) String topic, // 专有参数写法
                                   @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                                   @Header(KafkaHeaders.OFFSET) long offset,
                                   @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key
    ) {

        System.out.println("Received message: " + msg);
        System.out.println("Topic: " + topic + ", Partition: " + partition + ", Offset: " + offset + ", MsgKey: " + key);
    }

    /**
     * 消费者
     * 手动提交偏移量（确保消息处理成功后再提交）
     * <p>
     * kafka 默认是自动提交偏移量（消息在分区中的编号），来告知 kafka 这个消息消费完了，默认 5s
     * 如果消息处理失败，但偏移量过 5s 后自动提交了，这 5s 中程序处理时候并没有存储数据库，那么这条消息就永远丢失了！然后由于提交了偏移量，kafka 认为消息已处理，下次就不会再发这条消息了
     * spring:
     * kafka:
     * consumer:
     * enable-auto-commit: true
     * <p>
     * 如果需要手动提交偏移量你需要：
     * 1.application.yml 中配置手动提交偏移量（这部分已经注释了）
     * 2.config/KafkaConfig.java 中定义 Bean：kafkaListenerContainerFactory（这部分已经注释了）
     * 3.listenWithManualAck() 方法指定 containerFactory，并且手动提交 ack.acknowledge();
     *
     * @param msg 消息
     * @param ack 消费完确认
     */
    @KafkaListener(topics = "my-topic", groupId = "my-group",
            containerFactory = "kafkaListenerContainerFactory") // 指定这个 Bean，来源 config 中生成的 bean，表示 kafka 的提交模式
    @Override
    public void listenWithManualAck(String msg, Acknowledgment ack) {
        try {
            // 处理 msg 业务逻辑
            System.out.println("这里处理业务逻辑, msg: " + msg);

            // 手动提交偏移量
            ack.acknowledge();
        } catch (Exception e) {
            log.error("listenWithManualAck, 出现异常，偏移量没有提交，下次会重试，msg: {}", msg, e);
        }
    }
}
