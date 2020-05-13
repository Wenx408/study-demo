package com.wenx.demo.designpatterns.chain;

/**
 * @author Wenx
 * @date 2020/4/18
 */
public abstract class AbstractRequestFilter extends AbstractFilter {
    @Override
    abstract void doRequestFilter(Request request);

    @Override
    public void doResponseFilter(Response response) {

    }
}
