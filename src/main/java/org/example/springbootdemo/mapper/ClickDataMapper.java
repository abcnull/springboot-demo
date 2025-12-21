package org.example.springbootdemo.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.springbootdemo.model.ClickData;

/**
 * clickData mapper
 */
@Mapper
public interface ClickDataMapper {
    @Select("select * from click_data where event_time = #{timestamp}")
    ClickData queryData(long timestamp);

    @Insert("insert into click_data (event_time, user_id, product_id) " +
            "values (#{eventTime}, #{userId}, #{productId})")
    void insertData(ClickData clickData);
}
