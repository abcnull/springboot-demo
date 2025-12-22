package org.example.springbootdemo.service;

import java.util.List;
import java.util.Map;

public interface IHbaseService {
    void putData(String tableName, String rowKey, String columnFamily, Map<String, String> columns);
    Map<String, String> getData(String tableName, String rowKey, String columnFamily);
    List<Map<String, String>> scanTable(String tableName, String columnFamily);
    void deleteData(String tableName, String rowKey);
}
