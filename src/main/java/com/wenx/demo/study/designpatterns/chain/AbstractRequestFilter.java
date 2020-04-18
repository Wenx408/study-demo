package com.wenx.demo.study.designpatterns.chain;

public abstract class AbstractRequestFilter extends AbstractFilter {
    abstract void doRequestFilter(Request request);

    public void doResponseFilter(Response response) {

    }
}
