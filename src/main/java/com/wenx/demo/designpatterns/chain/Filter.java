package com.wenx.demo.designpatterns.chain;

/**
 * @author Wenx
 * @date 2020/4/18
 */
public interface Filter {
    void doFilter(Request request, Response response, FilterChain chain);
}
