package com.wenx.demo.study.designpatterns.chain;

public abstract class AbstractResponseFilter extends AbstractFilter {
    public void doRequestFilter(Request request) {

    }

    abstract void doResponseFilter(Response response);
}
