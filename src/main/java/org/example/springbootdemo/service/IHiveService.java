package org.example.springbootdemo.service;

import com.alibaba.fastjson.JSONObject;
import org.example.springbootdemo.model.ClickData;

public interface IHiveService {
    void saveData(JSONObject clickData);

    ClickData queryData(long timestamp);
}
