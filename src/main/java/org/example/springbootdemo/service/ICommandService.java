package org.example.springbootdemo.service;

public interface ICommandService {
    <T> void sendToUser(String userId, String cmdType, T payload);

    <T> void broadcastToRoom(String roomId, String cmdType, T payload);


}
