package com.hs.monitor.mapper;

import com.hs.monitor.entity.History;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HistoryMapper {
    int insertOne(History history);
    List<History> getAll();

    int selectCount();

    /**
     * @param pageSize 每页显示的条数
     * @param pageNum 当前页的页码
     * @return
     */
    List<History> getPage(@Param("pageNum")int pageNum, @Param("pageSize")int pageSize);
}
