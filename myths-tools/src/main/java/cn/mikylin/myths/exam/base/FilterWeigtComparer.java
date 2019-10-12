package cn.mikylin.myths.exam.base;

import java.util.Comparator;

/**
 * filter 权重比较器
 * @author mikylin
 * @date 20191012
 */
public final class FilterWeigtComparer implements Comparator<BaseFilter> {

    @Override
    public int compare(BaseFilter o1, BaseFilter o2) {
        return o1.weight() >= o2.weight() ? 1 : -1;
    }
}
