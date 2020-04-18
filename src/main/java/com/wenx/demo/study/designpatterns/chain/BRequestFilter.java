package com.wenx.demo.study.designpatterns.chain;

public class BRequestFilter extends AbstractRequestFilter {
    @Override
    void doRequestFilter(Request request) {
        request.str += ",BFilter";
    }
}
