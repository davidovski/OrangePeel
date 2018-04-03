package com.mouldycheerio.discord.web;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import com.mrpowergamerbr.temmiediscordauth.TemmieDiscordAuth;

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

    static boolean TEST = false;


    private HashMap<String, TemmieDiscordAuth> sessions;

    public WebServer(String token) {
        this.token = token;
        client = ClientFactory.createClient(token, true);
        dispatcher = client.getDispatcher();
        dispatcher.registerListener(this);
        sessions = new HashMap<String, TemmieDiscordAuth>();
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
                new ClientHandler(s, client, this);
            } catch (Exception x) {
                System.out.println(x);
            }
        }
    }

    public HashMap<String, TemmieDiscordAuth> getSessions() {
        return sessions;
    }

    public void setSessions(HashMap<String, TemmieDiscordAuth> sessions) {
        this.sessions = sessions;
    }

    public TemmieDiscordAuth getSession(String ip) {
        TemmieDiscordAuth t = sessions.get(ip);

        return t;
    }

}
