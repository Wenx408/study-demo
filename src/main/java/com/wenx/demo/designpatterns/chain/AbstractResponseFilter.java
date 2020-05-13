package com.wenx.demo.designpatterns.chain;

/**
 * @author Wenx
 * @date 2020/4/18
 */
public abstract class AbstractResponseFilter extends AbstractFilter {
    @Override
    public void doRequestFilter(Request request) {

    }

    @Override
    abstract void doResponseFilter(Response response);
}
