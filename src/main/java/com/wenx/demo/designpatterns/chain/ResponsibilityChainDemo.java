package com.wenx.demo.designpatterns.chain;

/**
 * @author Wenx
 * @date 2020/4/18
 */
public class ResponsibilityChainDemo {
    public static void main(String[] args) {
        Request request = new Request();
        request.str = "请求参数";
        Response response = new Response();
        response.str = "相应结果";

        FilterChain chain = new FilterChain();
        chain.add(new ARequestFilter())
                .add(new BRequestFilter())
                .add(new CResponseFilter())
                .add(new DResponseFilter());
        chain.doFilter(request, response);

        System.out.println(request.str);
        System.out.println(response.str);
    }
}
