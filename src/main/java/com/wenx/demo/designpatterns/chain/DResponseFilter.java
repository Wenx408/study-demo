package com.wenx.demo.designpatterns.chain;

/**
 * @author Wenx
 * @date 2020/4/18
 */
public class DResponseFilter extends AbstractResponseFilter {
    @Override
    void doResponseFilter(Response response) {
        response.str += ",DFilter";
    }
}
