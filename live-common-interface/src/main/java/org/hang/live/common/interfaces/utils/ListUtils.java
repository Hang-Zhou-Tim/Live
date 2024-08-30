package org.hang.live.common.interfaces.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/27
 * @Description Split a list into sublists of specific number.
 */
public class ListUtils {

    /**
     * Split a list into sub-lists of specific number.
     */
    public static <T> List<List<T>> splistList(List<T> list, int subNum) {
        List<List<T>> resultList = new ArrayList<>();
        int priIndex = 0;
        int lastIndex = 0;
        int insertTime = list.size() / subNum;
        List<T> subList;
        for (int i = 0; i <= insertTime; i++) {
            priIndex = subNum * i;
            lastIndex = priIndex + subNum;
            if (i != insertTime) {
                subList = list.subList(priIndex, lastIndex);
            } else {
                subList = list.subList(priIndex, list.size());
            }
            if (subList.size() > 0) {
                resultList.add(subList);
            }
        }
        return resultList;
    }
}