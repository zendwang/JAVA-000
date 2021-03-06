### 第一题
参考老师的
HttpOutboundHandler.java
NamedThreadFactory.java
异步httpclient + 线程池
整个过程中FullHttpRequest对象、ChannelHandlerContext对象都来自于代理服务中。
### 第二题
参见NettyHttpClientOutboundHandler.java
自我感觉这块掌握的不好，这种形式比较耗费系统资源
```java
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
```
#### 第三题
自定义增强过滤器类CustomHttpRequestFilter.java
```java
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

```
在HttpInboundHandler.java中channelRead方法加上
```java
   ......
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            FullHttpRequest fullRequest = (FullHttpRequest) msg;

            HttpRequestFilter filter = new CustomHttpRequestFilter();
            filter.filter(fullRequest,ctx);
            
            handler.handle(fullRequest, ctx);
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }
   ....
```
#### 第四题
后端随机路由类HttpEndpointRandomRouter.java
```java
/**
 *随机路由
 *
 */
public class HttpEndpointRandomRouter implements HttpEndpointRouter {
    
    public String route(List<String> endpoints) {
        if(null == endpoints || 0 == endpoints.size() ) {
            throw new IllegalArgumentException("endpoints must be not null or empty");
        }
        Random random = new Random();
        int index = random.nextInt(endpoints.size());
        return endpoints.get(index);
    }
    
}
```