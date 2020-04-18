package com.wenx.demo.study.designpatterns.chain;

import java.util.ArrayList;
import java.util.List;

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
