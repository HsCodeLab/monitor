package com.hs.monitor.service;

import com.hs.monitor.entity.History;
import com.hs.monitor.mapper.HistoryMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

import static com.hs.monitor.utils.SqlUtil.getSqlSessionFactory;


public class HistoryService {

    public static ObservableList<History> getTableItems(int pageNum, int pageSize) {
        ObservableList<History> list = FXCollections.observableArrayList();

        // 1、获取sqlSessionFactory对象
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        // 2、获取sqlSession对象
        SqlSession session = sqlSessionFactory.openSession(true);
        try {
            HistoryMapper mapper = session.getMapper(HistoryMapper.class);
            List list1 = mapper.getPage(pageNum, pageSize);
            list.setAll(list1);
        } finally {
            session.close();
        }
        return list;
    }

    public static int getTableTotal() {
        int total = 0;

        // 1、获取sqlSessionFactory对象
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        // 2、获取sqlSession对象
        SqlSession session = sqlSessionFactory.openSession(true);
        try {
            HistoryMapper mapper = session.getMapper(HistoryMapper.class);
            total = mapper.selectCount();
        } finally {
            session.close();
        }
        return total;
    }
}
