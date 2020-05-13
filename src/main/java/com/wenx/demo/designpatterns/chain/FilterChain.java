package com.wenx.demo.designpatterns.chain;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Wenx
 * @date 2020/4/18
 */
public class FilterChain {
    private List<Filter> list = new ArrayList<>();
    private int pos;
    private int n;

    public FilterChain add(Filter filter) {
        list.add(filter);
        n++;
        return this;
    }

    public void doFilter(Request request, Response response) {
        if (pos < n) {
            Filter filter = list.get(pos++);
            filter.doFilter(request, response, this);
        }
    }
}
