package com.hs.monitor.utils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hs.monitor.component.IPage;
import com.hs.monitor.factory.PageFactory;
import com.hs.monitor.protocol.ProtocolUtil;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * JSON工具类,JSON文件读写
 */
public class JsonUtil {
    //javaFX控件前缀包名
    public static final String CTRL_PREFIX = "javafx.scene.control.";
    public static final String CTRL_PREFIX1 = "org.controlsfx.control.";

    /**
     * 控件类型集合,在具体的json中需要指定type(控件类型)
     * 所有的控件类型都要在此预定义
     */
//    public static Map<String, String> map_types;

    /**
     * 菜单栏map对象,控制菜单栏显示哪些项
     */
    public static Map<String, Object> map_menubar;

    /**
     * 工具栏map对象,控制工具栏显示哪些项
     */
    public static Map<String, Object> map_toolbar;

    /**
     * 菜单栏对应的页面集合
     * 点击相应菜单项跳转到相应页面或者弹出弹窗
     */
    public static Map<String, IPage> map_control_menubar = new LinkedHashMap<>();

    /**
     * 工具栏对应的页面集合
     * 点击相应项跳转到相应页面或者弹出弹窗
     */
    public static Map<String, IPage> map_control_toolbar = new LinkedHashMap<>();

    private static String path_prefix_pro = System.getProperty("user.dir") + "/app";//生产环境路径
    private static String path_prefix_dev = System.getProperty("user.dir")+"/build/resources/main";//开发环境路径
//
    public static String path_prefix = path_prefix_dev;

    //json文件后缀名
    public static final String SUFFIX = ".json";

    static {
        map_menubar = getMap("/page_conf/menubar");

        map_toolbar = JsonUtil.getMap("/page_conf/toolbar");

//        readJSON(map_menubar, (key, map) -> {
//            if (StringUtils.isNotEmpty(map.get("path"))) {
//                IPage page = PageFactory.createPage(map);
//                // 将页面对象存储在map中
//                if (page != null) {
//                    map_control_menubar.put(key, page);
//                }
//            }
//        });

        map_menubar.forEach((key, value) -> {
            LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) value;
            LinkedHashMap<String, Object> map_items = (LinkedHashMap<String, Object>) map.get("items");
            map_items.forEach((key_item, value_item) -> {
                HashMap<String, Object> map_item = (HashMap<String, Object>) value_item;
                if (StringUtils.isNotEmpty((CharSequence) map_item.get("path"))) {
                    IPage page = PageFactory.createPage(map_item);
                    // 将页面对象存储在map中
                    if (page != null) {
                        map_control_menubar.put(key_item, page);
                    }
                }
            });
        });

        map_toolbar.forEach((key, value) -> {
            Map<String, Object> map = (Map<String, Object>) value;
            if (StringUtils.isNotEmpty((CharSequence) map.get("group_name"))) {
                ProtocolUtil.group_list.add((String) map.get("group_name"));
            }
            if (StringUtils.isNotEmpty((CharSequence) map.get("cmd_read"))) {
                ProtocolUtil.cmd_list.add((String) map.get("cmd_read"));
                ProtocolUtil.group_cmd.put((String) map.get("group_name"), (String) map.get("cmd_read"));
            }
            IPage page = PageFactory.createPage(map);

            // 将页面对象存储在map中
            if (page != null) {
                map_control_toolbar.put(key, page);
            }
        });

        ProtocolUtil.group_name = ProtocolUtil.group_list.get(0);
        ProtocolUtil.cmd_read = ProtocolUtil.cmd_list.get(0);
    }

    /**
     * 读取JSON对象
     * 获取的value是Object类型,也就是嵌套的JSON对象,也可以获取不嵌套的JSON对象
     *
     * @param path
     * @return
     */
    public static Map<String, Object> getMap(String path) {
        if (!isJsonValid(path)) {
            return null;
        }
        Map<String, Object> map = null;
        try {
            File file = new File(path_prefix + path + SUFFIX);
            ObjectMapper objectMapper = new ObjectMapper();
            map = objectMapper.readValue(file, LinkedHashMap.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 校验JSON文件格式
     */
    public static boolean isJsonValid(String path) {
        boolean result = false;
        ObjectMapper objectMapper = new ObjectMapper();
//        String path2 = "";
        try {
            // 解析JSON文件
//            String jsonString = readJSONFile(path);
//            String path1 = System.getProperty("user.dir");
//            String path2 = path1 + "/app" + path + SUFFIX;
////            String path1 = JsonUtil.class.getResource(path + SUFFIX).getPath();
//            // Check if the URL has "file:" prefix
////            if (path1.startsWith("file:/")) {
////                // Remove the "file:" prefix
////                path1 = path1.substring("file:/".length());
////            }
//////            if (form.startsWith("jrt:")){
//////                form = form.substring("jrt:".length());
//////            }
//            System.out.println("form=" + path2);
            objectMapper.readTree(new File(path_prefix + path + SUFFIX));
//            System.out.println("JSON格式正确，没有发现错误。");
            result = true;
        } catch (JsonParseException e) {
//            e.printStackTrace();
//            System.err.println("JSON格式错误：" + e.getOriginalMessage());
//            System.err.println("错误位置：行 " + e.getLocation().getLineNr() + "，列 " + e.getLocation().getColumnNr());
            // 可以根据需要输出更多错误信息，如具体出错的JSON片段
            result = false;

            String str1 = "说明,行和列号不一定能准确标识真正的错误,但是基本就是上一行或者下一行有错,详情参考json规范\n";
            String str2 = "文件路径:" + JsonUtil.class.getResource(path + SUFFIX).getPath() + "\n";
            String str3 = "错误位置：行 " + e.getLocation().getLineNr() + "，列 " + e.getLocation().getColumnNr() + "\n";
            String str4 = e.getOriginalMessage();


            // 创建一个带滚动条的TextArea来显示完整的错误信息
            TextArea textArea = new TextArea(str1 + str2 + str3 + str4);
            textArea.setEditable(false);
            textArea.setWrapText(true);

            // 将TextArea放入GridPane中
            GridPane gridPane = new GridPane();
            gridPane.add(textArea, 0, 0);

            // 显示警告弹窗，将GridPane作为内容显示
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("提示");
            alert.setHeaderText("JSON格式错误");
            alert.getDialogPane().setContent(gridPane);
            alert.showAndWait();
        } catch (IOException e) {
            System.err.println("读取JSON文件失败：" + e.getMessage());
            result = false;
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("提示");
            alert.setHeaderText(null);
            alert.setContentText("读取JSON文件失败：" + e.getMessage());
            alert.showAndWait();
        }
        return result;
    }

    /**
     * 所有的JSON对象解析都用此递归函数来解析
     */
    public static void readJSON(Map<String, ?> map, Callback callback) {
        map.forEach((key, value) -> {
            if (value instanceof LinkedHashMap<?, ?>) {
                LinkedHashMap<String, ?> innerMap = (LinkedHashMap<String, ?>) value;
                // 在这里你可以根据 innerMap 的内容来判断具体的类型
                // 例如，你可以检查 innerMap 是否只包含 String 类型的值
                boolean isStringMap = innerMap.values().stream().allMatch(v -> v instanceof String);
                if (isStringMap) {
                    // 处理 LinkedHashMap<String, String> 类型的情况
                    callback.call(key, (LinkedHashMap<String, String>) innerMap);
                } else {
                    // 处理 LinkedHashMap<String, Object> 类型的情况
                    readJSON(innerMap, callback);
                }
            }
        });
    }

    @FunctionalInterface
    public interface Callback {
        void call(String key, LinkedHashMap<String, String> map);
    }
}
