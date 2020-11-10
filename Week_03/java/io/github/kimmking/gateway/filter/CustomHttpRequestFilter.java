package io.github.kimmking.gateway.filter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultHttpRequest;
import io.netty.handler.codec.http.FullHttpRequest;

/**
 * 新增自定义 http request header
 */
public class CustomHttpRequestFilter implements HttpRequestFilter {

    private static final String  CUSTOM_HEADER= "nio";

    public void filter(FullHttpRequest fullRequest, ChannelHandlerContext ctx) {
        DefaultHttpRequest request = (DefaultHttpRequest) fullRequest;
        request.headers().add(CUSTOM_HEADER,"wangzhenxian");
    }
}
