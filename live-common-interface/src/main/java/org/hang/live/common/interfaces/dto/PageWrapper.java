package org.hang.live.common.interfaces.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Used for query a page of PO.
 * @Author hang
 * @Date: Created in 22:54 2024/8/27
 * @Description
 */
public class PageWrapper<T> implements Serializable {

    private List<T> list;
    private boolean hasNext;

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    @Override
    public String toString() {
        return "PageWrapper{" +
                "list=" + list +
                ", hasNext=" + hasNext +
                '}';
    }
}
