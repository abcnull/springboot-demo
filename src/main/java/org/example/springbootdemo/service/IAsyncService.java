package org.example.springbootdemo.service;

import java.util.concurrent.CompletableFuture;

/**
 * 异步处理 IService
 */
public interface IAsyncService {
    void basicAsync();

    void customAsync();

    void customMutiAsync();

    CompletableFuture<String> asyncRespGet();

    void asyncPitFalls();
}
