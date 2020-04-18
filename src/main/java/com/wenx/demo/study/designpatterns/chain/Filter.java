package com.wenx.demo.study.designpatterns.chain;

public interface Filter {
    void doFilter(Request request, Response response, FilterChain chain);
}
