package com.wenx.demo.study.designpatterns.chain;

public class ARequestFilter extends AbstractRequestFilter {
    @Override
    void doRequestFilter(Request request) {
        request.str += ",AFilter";
    }
}
