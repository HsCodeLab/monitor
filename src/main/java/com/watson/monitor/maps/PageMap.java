package com.watson.monitor.maps;

import java.util.HashMap;

/**
 * 页面相关的Map
 */
public class PageMap {
    /**
     * 存放历史数据类似数据的map
     *
     * map.put("pagination",pagination)
     * map.put("tableView",tableView)
     * map.put("page_num",comboBox)
     *
     * map.put("chart_type",comboBox)
     * map.put("stringProperty",stringProperty)
     */
    public static HashMap<String,HashMap<String,Object>> map_history = new HashMap<>();//保存一个pagination,一个

//    //表格Map
//    // 两个Map的key保持一致，在创建的时候分别添加到两个Map中，取的时候因为key一致，可以根据pagination获取对应TableView,
//    //点击不同页面的收可以显示对应的表格数据
//    public static HashMap<String,Object> map_table = new HashMap<>();

    /**
     * 周期的map
     */
    public static HashMap<String,Object> map_cycle = new HashMap<>();

    /**
     * 存放页面数据的map
     */
    public static HashMap<String,Object> map_data_page = new HashMap<>();
}
