package org.example.springbootdemo.service;

import org.springframework.kafka.support.Acknowledgment;

import java.util.List;

public interface IKafkaService {
    void sendMsg(String topic, String msg);
    void sendMsgWithKey(String topic, String key, String msg);
    void sendMsgAsync(String topic, String msg);
    void sendBatchMsg(String topic, List<String> msgList);

    void listenMsg(String msg);
    void listenMutiTopics(String msg);
    void listenWithMetadata(String msg, String topic, int partition, long offset, String key);
    void listenWithManualAck(String msg, Acknowledgment ack);
}
