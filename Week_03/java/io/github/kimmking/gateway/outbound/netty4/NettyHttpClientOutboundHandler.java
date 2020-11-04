package io.github.kimmking.gateway.outbound.netty4;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.EmptyByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;

import java.net.URI;
import java.nio.charset.Charset;

public class NettyHttpClientOutboundHandler  extends ChannelInboundHandlerAdapter {

    private String backendUrl;

    private ChannelHandlerContext ctx;

    public NettyHttpClientOutboundHandler(String backendUrl) {
        this.backendUrl = backendUrl.endsWith("/")?backendUrl.substring(0,backendUrl.length()-1):backendUrl;
    }

    public void handle(final FullHttpRequest fullRequest,final ChannelHandlerContext ctx) throws Exception {
        this.ctx = ctx;
        final String url = this.backendUrl + fullRequest.uri();

        URI uri = new URI(url);
        String host = uri.getHost();
        int port = uri.getPort();

        NettyHttpClientOutboundHandler  nettyHttpClientOutboundHandler = this;
        //Configure the client
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {

            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    // 客户端接收到的是httpResponse响应，所以要使用HttpResponseDecoder进行解码
                    ch.pipeline().addLast(new HttpResponseDecoder());
                    // 客户端发送的是httprequest，所以要使用HttpRequestEncoder进行编码
                    ch.pipeline().addLast(new HttpRequestEncoder());

                    ch.pipeline().addLast(nettyHttpClientOutboundHandler);
                }
            });

            ChannelFuture f = b.connect(host,port).sync();
            f.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if(channelFuture.isSuccess()) {
                        Channel channel = channelFuture.channel();
                        channel.writeAndFlush(fullRequest);
                    }
                }
            });
            f.channel().closeFuture().sync();
        } finally {
            //shut down executor threads to exit
            workerGroup.shutdownGracefully();
        }

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof HttpContent) {
            LastHttpContent httpContent = (LastHttpContent) msg;
            ByteBuf byteData = httpContent.content();
            String sendMsg = "";
            if (!(byteData instanceof EmptyByteBuf)) {
                //接收msg消息
                byte[] msgByte = new byte[byteData.readableBytes()];
                byteData.readBytes(msgByte);
                sendMsg = new String(msgByte, Charset.forName("UTF-8"));
            }
            System.out.println(sendMsg);
            FullHttpResponse response = new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1,
                    HttpResponseStatus.OK,
                    Unpooled.wrappedBuffer(sendMsg.getBytes(Charset.forName("UTF-8")))
            );
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain;charset=UTF-8");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
            this.ctx.write(response);
            this.ctx.flush();
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.close();
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
       cause.printStackTrace();
       ctx.close();
    }
}