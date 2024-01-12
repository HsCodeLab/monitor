package com.watson.monitor.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.*;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static com.watson.monitor.utils.JsonUtil.*;

@Slf4j
public class LanUtil {
    public static StringProperty LAN = new SimpleStringProperty();
    public static Map<String, Object> map_lan;
    private static String lan_dir = "/language/";

    static {
        map_lan = getMap(lan_dir + "lan");

        LAN.set((String) map_lan.get("default"));
    }

    public static String getText(String keyName) {
        if (keyName == null) {
            return null;
        }
        String path = lan_dir + LAN.get();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode node = null;
        try {
            node = objectMapper.readTree(new File(path_prefix + path + SUFFIX));
        } catch (Exception e) {
//            throw new RuntimeException(e);
            String file = LAN.get() + SUFFIX;
            log.info("找不到对应文件" + file + ",请确保" + lan_dir + "目录下有" + file + "文件");
            return keyName;
        }

        String[] parts = keyName.split("\\.");

        for (String part : parts) {
            if (node.has(part)) {
                node = node.get(part);
            } else {
                // 路径无效或节点不存在

                log.info("找不到" + keyName + "对应的文本");
                return keyName;
            }
        }

        if (node.isTextual()) {
            return node.asText();
        } else {
            // 节点不是文本节点
            log.info(keyName + "对应的不是文本节点");
            return keyName;
        }
    }

    /**
     * 添加国际化语言监听
     *
     * @param keyName 类似 home.file para.setting等
     * @param control 控件
     */
    public static void addLanguageListen(String keyName, Object control) {
        if (control instanceof Labeled) {
            Labeled labeled = (Labeled) control;
            LAN.addListener((observable, oldValue, newValue) -> labeled.setText(getText(keyName)));
        } else if (control instanceof MenuItem) {
            MenuItem menuItem = (MenuItem) control;
            LAN.addListener((observable, oldValue, newValue) -> menuItem.setText(getText(keyName)));
        } else if (control instanceof TreeItem<?>) {
            TreeItem<String> treeItem = (TreeItem<String>) control;
            LAN.addListener((observable, oldValue, newValue) -> treeItem.setValue(getText(keyName)));
        } else if (control instanceof Tab) {
            Tab tab = (Tab) control;
            LAN.addListener((observable, oldValue, newValue) -> tab.setText(getText(keyName)));
        } else if (control instanceof Dialog<?>) {
            Dialog<?> dialog = (Dialog<?>) control;
            LAN.addListener((observable, oldValue, newValue) -> dialog.setTitle(getText(keyName)));
        }
    }

}
