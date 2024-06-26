package com.hs.monitor.component;

import javafx.scene.layout.Pane;

import java.util.Map;

/**
 * 页面类型,读取JSON配置文件的时候要判断当前页面是要渲染成页面还是弹窗
 */
public class Page extends Pane implements IPage {

    public Page(Map<String, Object> map) {

        PageUtil.handlePage(map, this);
    }
}

