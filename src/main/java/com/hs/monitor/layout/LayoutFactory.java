package com.hs.monitor.layout;

import com.hs.monitor.wrappers.LayoutWrapper;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * 页面内部各个部分的布局工厂
 */
public class LayoutFactory {
    private static final Map<String, BiFunction<LayoutWrapper, Map<String, String>, Layout>> layoutMap = new HashMap<>();

    static {
        layoutMap.put("grid", LayoutGrid::new);
        layoutMap.put("hbox", LayoutHBox::new);
        layoutMap.put("vbox", LayoutVBox::new);
        layoutMap.put("table", LayoutTable::new);
        layoutMap.put("chart", LayoutChart::new);
    }

    //    mapItem.get("layout"), mapItem.get("path")
    public static Layout createLayout(LayoutWrapper wrapper, Map<String, String> map) {
        BiFunction<LayoutWrapper, Map<String, String>, Layout> function = layoutMap.get(map.get("layout"));
        if (function != null) {
            return function.apply(wrapper, map);
        }
        // 处理默认情况，如果需要的话
        return null;
    }
}
