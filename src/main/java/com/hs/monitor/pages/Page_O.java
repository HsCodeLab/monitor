package com.hs.monitor.pages;

import com.hs.monitor.utils.JsonUtil;
import javafx.geometry.Insets;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static com.hs.monitor.utils.JsonUtil.CTRL_PREFIX;

public class Page_O extends GridPane {
    public Page_O(String name) {
        render(name);
    }

    public void render(String name) {
        this.setPadding(new Insets(10));
        this.setHgap(10);
        this.setVgap(10);


        Map<String, Object> map = JsonUtil.getMap(name);
        AtomicInteger i = new AtomicInteger();
        map.forEach((key, value) -> {
            //vo bl_in,vol_out等对象
            LinkedHashMap<String, String> linkedHashMap = (LinkedHashMap<String, String>) value;

            // 文本
            this.add(new Label(linkedHashMap.get("name")), 0, i.get());
            this.add(createComponent(linkedHashMap.get("type")), 1, i.get());
            this.add(new Label(linkedHashMap.get("unit")), 2, i.get());
            i.getAndIncrement();
        });
    }

    public static Control createComponent(String className) {
        try {
            Class<?> componentClass = Class.forName(CTRL_PREFIX + className);
            return (Control) componentClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
