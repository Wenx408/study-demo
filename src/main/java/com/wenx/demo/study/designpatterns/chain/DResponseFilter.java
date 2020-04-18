package com.wenx.demo.study.designpatterns.chain;

public class DResponseFilter extends AbstractResponseFilter {
    @Override
    void doResponseFilter(Response response) {
        response.str += ",DFilter";
    }
}
