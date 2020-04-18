package com.wenx.demo.study.designpatterns.chain;

public class CResponseFilter extends AbstractResponseFilter {
    @Override
    void doResponseFilter(Response response) {
        response.str += ",CFilter";
    }
}
