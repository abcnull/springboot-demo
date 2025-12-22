package org.example.springbootdemo.service.impl;

import lombok.SneakyThrows;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.example.springbootdemo.service.IHbaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * hbase service
 */
@Service
public class HbaseServiceImpl implements IHbaseService {
    @Autowired
    private Connection hbaseConn;

    /**
     * 插入或更新数据
     *
     * @param tableName    表名
     * @param rowKey       行键
     * @param columnFamily 列族
     * @param columns      列名和值的映射
     */
    @SneakyThrows
    public void putData(String tableName, String rowKey, String columnFamily, Map<String, String> columns) {
        Table table = hbaseConn.getTable(TableName.valueOf(tableName));
        try {
            Put put = new Put(Bytes.toBytes(rowKey));
            for (Map.Entry<String, String> entry : columns.entrySet()) {
                put.addColumn(
                        Bytes.toBytes(columnFamily),
                        Bytes.toBytes(entry.getKey()),
                        Bytes.toBytes(entry.getValue())
                );
            }
            table.put(put);
        } finally {
            table.close();
        }
    }

    /**
     * 获取单行数据
     *
     * @param tableName    表名
     * @param rowKey       行键
     * @param columnFamily 列族
     * @return 列名和值的映射
     */
    @SneakyThrows
    public Map<String, String> getData(String tableName, String rowKey, String columnFamily) {
        Table table = hbaseConn.getTable(TableName.valueOf(tableName));
        try {
            Get get = new Get(Bytes.toBytes(rowKey));
            get.addFamily(Bytes.toBytes(columnFamily));
            Result result = table.get(get);

            Map<String, String> resultMap = new java.util.HashMap<>();
            for (Cell cell : result.listCells()) {
                String qualifier = Bytes.toString(CellUtil.cloneQualifier(cell));
                String value = Bytes.toString(CellUtil.cloneValue(cell));
                resultMap.put(qualifier, value);
            }
            return resultMap;
        } finally {
            table.close();
        }
    }

    /**
     * 扫描表数据
     *
     * @param tableName    表名
     * @param columnFamily 列族
     * @return 结果列表
     */
    @SneakyThrows
    public List<Map<String, String>> scanTable(String tableName, String columnFamily) {
        Table table = hbaseConn.getTable(TableName.valueOf(tableName));
        try {
            Scan scan = new Scan();
            scan.addFamily(Bytes.toBytes(columnFamily));

            ResultScanner scanner = table.getScanner(scan);
            List<Map<String, String>> resultList = new ArrayList<>();

            for (Result result : scanner) {
                String rowKey = Bytes.toString(result.getRow());
                Map<String, String> rowMap = new java.util.HashMap<>();
                rowMap.put("_rowkey", rowKey);

                for (Cell cell : result.listCells()) {
                    String qualifier = Bytes.toString(CellUtil.cloneQualifier(cell));
                    String value = Bytes.toString(CellUtil.cloneValue(cell));
                    rowMap.put(qualifier, value);
                }
                resultList.add(rowMap);
            }
            return resultList;
        } finally {
            table.close();
        }
    }

    /**
     * 删除数据
     *
     * @param tableName 表名
     * @param rowKey    行键
     */
    @SneakyThrows
    public void deleteData(String tableName, String rowKey) {
        Table table = hbaseConn.getTable(TableName.valueOf(tableName));
        try {
            Delete delete = new Delete(Bytes.toBytes(rowKey));
            table.delete(delete);
        } finally {
            table.close();
        }
    }
}
