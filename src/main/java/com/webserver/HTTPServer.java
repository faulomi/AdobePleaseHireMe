/*
* Copyright 2012 The Netty Project
*
* The Netty Project licenses this file to you under the Apache License,
* version 2.0 (the "License"); you may not use this file except in compliance
* with the License. You may obtain a copy of the License at:
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
* WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
* License for the specific language governing permissions and limitations
* under the License.
*/
package com.webserver;

import com.webserver.conf.HTTPConfiguration;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslProvider;
import io.netty.handler.ssl.util.SelfSignedCertificate;

public class HTTPServer {




    private HTTPConfiguration configuration;


    public HTTPServer(HTTPConfiguration configuration){
        this.configuration = configuration;

    }


    public void start() throws Exception{

        // Configure SSL.
        final SslContext sslCtx;
        if (configuration.useSSL()) {
            SelfSignedCertificate ssc = new SelfSignedCertificate();
            sslCtx = SslContext.newServerContext(SslProvider.JDK, ssc.certificate(), ssc.privateKey());
        } else {
            sslCtx = null;
        }

        final EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        final EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();

            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new HttpStaticFileServerInitializer(sslCtx));

            Channel ch = b.bind(configuration.getPort()).sync().channel();

            System.err.println("Open your web browser and navigate to " +
                    (configuration.useSSL()? "https" : "http") + "://127.0.0.1:" + configuration.getPort() + '/');

            ch.closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }


    }



    public static void main(String[] args) throws Exception {

        HTTPConfiguration configuration = new HTTPConfiguration.Builder().build();
        final HTTPServer httpServer = new HTTPServer(configuration);
        httpServer.start();

    }
}