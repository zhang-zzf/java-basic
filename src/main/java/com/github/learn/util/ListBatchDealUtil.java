package com.github.learn.util;

import java.util.List;
import java.util.function.Function;

/**
 * @author zhang.zzf
 * @date 2020-04-21
 */
public class ListBatchDealUtil {

    /**
     * 对 List 分批处理
     *
     * @param list 数据
     * @param batchSize 每个批次处理的数据量大小
     * @param function 数据处理逻辑
     * @return 成功处理的数据量
     */
    public static <T> int deal(List<T> list, int batchSize, Function<? super List<T>, Integer> function) {
        int dealtCount = 0;
        int totalSize = list.size();
        int batchCount = totalSize / batchSize + 1;
        for (int i = 0; i < batchCount; i++) {
            int start = i * batchSize;
            int end = (i + 1) * batchSize;
            end = end > totalSize ? totalSize : end;
            if (start == end) {
                continue;
            }
            // do something with the subList
            dealtCount += function.apply(list.subList(start, end));
        }
        return dealtCount;
    }
}
