package com.hs.monitor.layout;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * 页面级别的布局工厂
 */
public class LayoutFactoryPage {
    private static final Map<String, Supplier<Pane>> layoutMap = new HashMap<>();

    static {
        layoutMap.put("grid", () -> new GridPane());
        layoutMap.put("hbox", () -> new HBox());
        layoutMap.put("vbox", () -> new VBox());
    }

    public static Pane createLayout(String layout) {
        Supplier<Pane> constructor = layoutMap.get(layout);
        if (constructor != null) {
            return constructor.get();
        }
        // 处理默认情况，如果需要的话
        return null;
    }
}
