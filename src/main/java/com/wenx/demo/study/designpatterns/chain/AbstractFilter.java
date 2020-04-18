package com.wenx.demo.study.designpatterns.chain;

public abstract class AbstractFilter implements Filter {
    @Override
    public void doFilter(Request request, Response response, FilterChain chain) {
        doRequestFilter(request);
        chain.doFilter(request, response);
        doResponseFilter(response);
    }

    abstract void doRequestFilter(Request request);

    abstract void doResponseFilter(Response response);
}
