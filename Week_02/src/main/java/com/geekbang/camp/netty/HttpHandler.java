package com.geekbang.camp.netty;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.netty.handler.codec.http.HttpHeaderValues.KEEP_ALIVE;
import static org.apache.http.HttpHeaders.CONNECTION;

public class HttpHandler extends ChannelInboundHandlerAdapter {
    private static Logger logger = LoggerFactory.getLogger(HttpHandler.class);

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
      try {
          FullHttpRequest fullHttpRequest = (FullHttpRequest) msg;
          String uri = fullHttpRequest.uri();
          if(uri.contains("test")) {
              handlerTest(fullHttpRequest,ctx);
          }
      } finally {
          ReferenceCountUtil.release(msg);
      }
    }

    private void handlerTest(FullHttpRequest fullHttpRequest, ChannelHandlerContext ctx) {
        FullHttpResponse response = null;
        try {
            String value = "hello,zendwang";
            response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer(value.getBytes("UTF-8")));
            response.headers().set("Content-Type","application/json");
            response.headers().setInt("Content-Length",response.content().readableBytes());
        }catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.NO_CONTENT);
        } finally {
          if(fullHttpRequest != null) {
              if(!HttpUtil.isKeepAlive(fullHttpRequest)) {
                  ctx.write(response).addListener(ChannelFutureListener.CLOSE);
              } else {
                  response.headers().set(CONNECTION,KEEP_ALIVE);
                  ctx.write(response);
              }
          }
        }
    }


}
