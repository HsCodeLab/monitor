package com.hs.monitor.factory;

import com.hs.monitor.component.Dialog;
import com.hs.monitor.component.IPage;
import com.hs.monitor.component.Page;
import com.hs.monitor.component.TabPanel;
//import com.hs.component.TabPanel;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class PageFactory {
    private static final Map<String, Function<Map<String,Object>, IPage>> pageMap = new HashMap<>();

    static {
        pageMap.put("page", (map) -> new Page(map));
        pageMap.put("dialog", (map) -> new Dialog(map));
        pageMap.put("tabpane", (map) -> new TabPanel(map));
    }

    public static IPage createPage(Map<String,Object> map) {
        Function<Map<String,Object>, IPage> constructor = pageMap.get(map.get("type"));
        if (constructor != null) {
            return constructor.apply(map);
        }
        // 处理默认情况，如果需要的话
        return null;
    }
}
