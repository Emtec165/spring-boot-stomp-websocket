package com.example.websockets;

import io.netty.channel.EventLoopGroup;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.Reactor2StompCodec;
import org.springframework.messaging.simp.stomp.StompDecoder;
import org.springframework.messaging.simp.stomp.StompEncoder;
import org.springframework.messaging.tcp.reactor.Reactor2TcpClient;
import reactor.Environment;
import reactor.core.config.ReactorConfiguration;
import reactor.io.net.NetStreams;
import reactor.io.net.Spec;
import reactor.io.net.config.SslOptions;
import reactor.io.net.impl.netty.NettyClientSocketOptions;

import java.util.Properties;

import static java.util.Collections.emptyList;

public class StompTcpFactory implements NetStreams.TcpClientFactory<Message<byte[]>, Message<byte[]>> {

    private final Environment environment;
    private final EventLoopGroup eventLoopGroup;
    private final String host;
    private final int port;
    private final boolean ssl;

    StompTcpFactory(String host, int port, boolean ssl) {
        this.host = host;
        this.port = port;
        this.ssl = ssl;
        this.environment = new Environment(() -> new ReactorConfiguration(emptyList(), "sync", new Properties()));
        this.eventLoopGroup = Reactor2TcpClient.initEventLoopGroup();
    }

    @Override
    public Spec.TcpClientSpec<Message<byte[]>, Message<byte[]>> apply(Spec.TcpClientSpec<Message<byte[]>, Message<byte[]>> tcpClientSpec) {
        return tcpClientSpec
                .env(environment)
                .options(new NettyClientSocketOptions().eventLoopGroup(eventLoopGroup))
                .codec(new Reactor2StompCodec(new StompEncoder(), new StompDecoder()))
                .ssl(ssl ? new SslOptions() : null)
                .connect(host, port);
    }

}
