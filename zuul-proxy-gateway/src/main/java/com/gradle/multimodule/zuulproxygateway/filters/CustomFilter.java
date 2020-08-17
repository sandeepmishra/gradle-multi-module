package com.gradle.multimodule.zuulproxygateway.filters;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

@Slf4j
public class CustomFilter extends ZuulFilter {


    /**
     * fileter type can be pre, post, route, error
     * @return
     */
    @Override
    public String filterType() {
        return "pre";
    }

    /**
     * ordering based on numbers in ascending if more than one filter
     * @return
     */
    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest servletRequest = requestContext.getRequest();
        log.info(String.format("%s request to %s ",servletRequest.getMethod(), requestContext.getRequest().getRequestURI()));
        return null;
    }
}
