package com.hs.monitor.factory;

import com.hs.monitor.component.Dialog;
import com.hs.monitor.component.Page;
import com.hs.monitor.component.TabPanel;
import javafx.scene.layout.BorderPane;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import static com.hs.monitor.App.root;

public class PageHandlerFactory {
    private static final Map<Class<?>, BiConsumer<Object, BorderPane>> handlerMap = new HashMap<>();

    static {
        handlerMap.put(Page.class, (selectedPage, root) -> root.setCenter((Page) selectedPage));
        handlerMap.put(Dialog.class, (selectedPage, root) -> ((Dialog) selectedPage).show());
        handlerMap.put(TabPanel.class, (selectedPage, root) -> root.setCenter((TabPanel) selectedPage));
    }

    public static void handlePage(Object selectedPage) {
        Class<?> pageClass = selectedPage.getClass();
        BiConsumer<Object, BorderPane> handler = handlerMap.get(pageClass);
        if (handler != null) {
            handler.accept(selectedPage, root);
        }
    }
}