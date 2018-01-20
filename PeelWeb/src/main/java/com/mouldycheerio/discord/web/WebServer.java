package com.mouldycheerio.discord.web;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventDispatcher;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.obj.IUser;

public class WebServer {
    private ServerSocket serverSocket;
    private IDiscordClient client;
    public String token;
    private EventDispatcher dispatcher;

    public WebServer(String token) {
        this.token = token;
        client = ClientFactory.createClient(token, true);
        dispatcher = client.getDispatcher();
        dispatcher.registerListener(this);
    }

    @EventSubscriber
    public void onReadyEvent(ReadyEvent event) {
        IUser u = event.getClient().getOurUser();
        System.out.println("Name: " + u.getName() + "#" + u.getDiscriminator());
    }


    public void run(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        while (true) {
            try {
                Socket s = serverSocket.accept();
                new ClientHandler(s, client);
            } catch (Exception x) {
                System.out.println(x);
            }
        }
    }



}
