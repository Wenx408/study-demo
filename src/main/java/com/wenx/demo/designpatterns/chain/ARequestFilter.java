package com.wenx.demo.designpatterns.chain;

/**
 * @author Wenx
 * @date 2020/4/18
 */
public class ARequestFilter extends AbstractRequestFilter {
    @Override
    void doRequestFilter(Request request) {
        request.str += ",AFilter";
    }
}
