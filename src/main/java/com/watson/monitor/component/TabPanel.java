package com.watson.monitor.component;

import com.watson.monitor.layout.LayoutFactoryPage;
import com.watson.monitor.maps.PageMap;
import com.watson.monitor.wrappers.PageWrapper;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static com.watson.monitor.component.PageUtil.paneHandlers;
import static com.watson.monitor.utils.LanUtil.addLanguageListen;
import static com.watson.monitor.utils.LanUtil.getText;

/**
 * 弹窗类型,当JSON中配置type为dialog的时候,就渲染为dialog,配置为page的时候就渲染为page.
 * 读取JSON步骤:
 * 1.读取/pages/pages.json文件,获取有哪些页面.假设有home,about.
 * 2.根据/pages/pages.json里面的value获取/pages/about.json.
 * 3.读取/pages/about.json里面的path,此路径为数据对象的JSON路径.
 * 4.读取/data/about.json文件,里面封装了具体的数据,渲染显示
 */
public class TabPanel extends Pane implements IPage {

    public TabPanel(Map<String, Object> map) {

        TabPane tabPane = new TabPane();

        tabPane.prefWidthProperty().bind(this.widthProperty());
        tabPane.prefHeightProperty().bind(this.heightProperty());

        AtomicInteger i = new AtomicInteger();
        LinkedHashMap<String, Object> map_items = (LinkedHashMap<String, Object>) map.get("items");

        String group_name = (String) map.get("group_name");
        String handler_class = (String) map.get("handler_class");
        HashMap<String, Object> map_obj = new HashMap<>();

        PageMap.map_history.put(group_name, map_obj);

        map_items.forEach((key1, value1) -> {
            LinkedHashMap<String, Object> map_item1 = (LinkedHashMap<String, Object>) value1;
            Tab tab = new Tab();
            tab.setText(getText((String) map_item1.get("tab")));

            addLanguageListen((String) map_item1.get("tab"), tab);

            tabPane.getTabs().add(tab);
            Pane pane = LayoutFactoryPage.createLayout((String) map_item1.get("layout"));


            pane.prefWidthProperty().bind(tabPane.widthProperty());
            pane.prefHeightProperty().bind(tabPane.heightProperty());

            Consumer<PageWrapper> consumer = paneHandlers.get(pane.getClass());
            if (consumer != null) {
                PageWrapper wrapper = PageWrapper.builder().groupName(group_name).handlerClass(handler_class).map((Map<String, Object>) map_item1.get("items1")).container(pane).build();
                consumer.accept(wrapper);
            } else {
                // Handle unknown pane type, or provide a default behavior
            }
            tab.setContent(pane);
            i.getAndIncrement();
        });
        this.getChildren().add(tabPane);
    }


    private void exportToExcel() {

    }

    private void exportToImage() {
        // Display a file chooser dialog for saving
//        FileChooser fileChooser = new FileChooser();
//        fileChooser.setTitle("Save Chart as Image");
//        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("PNG files", "*.png"), new FileChooser.ExtensionFilter("JPEG files", "*.jpeg", "*.jpg"));
//
//        File selectedFile = fileChooser.showSaveDialog(chart.getScene().getWindow());
//
//        if (selectedFile != null) {
//            // Save the chart as the selected image format
//            WritableImage image = chart.snapshot(new SnapshotParameters(), null);
//
//            BufferedImage bufferedImage = new BufferedImage((int) image.getWidth(), (int) image.getHeight(), BufferedImage.TYPE_INT_ARGB);
//            for (int x = 0; x < image.getWidth(); x++) {
//                for (int y = 0; y < image.getHeight(); y++) {
//                    bufferedImage.setRGB(x, y, image.getPixelReader().getArgb(x, y));
//                }
//            }
//
//            try {
//                ImageIO.write(bufferedImage, getFileExtension(selectedFile), selectedFile);
//                System.out.println("Chart saved as image.");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
    }

    private String getFileExtension(File file) {
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1).toLowerCase();
        }
        return ""; // Default to empty extension
    }
}
