package com.lc.product.center.domain.bo;

/**
 * <pre>
 *     封装键值对
 * <pre/>
 * @author : Lu Cheng
 * @date : 6/2/26 14:07
 * @version : 1.0
 */
public record QueryFilter(String code, String desc) implements Comparable<QueryFilter> {

    @Override
    public boolean equals(Object obj) {
        return obj instanceof QueryFilter && ((QueryFilter) obj).code().equals(this.code());
    }

    @Override
    public int compareTo(QueryFilter o) {
        return o.desc().compareTo(desc());
    }
}
