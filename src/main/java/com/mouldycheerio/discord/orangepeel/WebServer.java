package com.mouldycheerio.discord.orangepeel;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.mouldycheerio.discord.web.ServerConfig;
import com.mrpowergamerbr.temmiediscordauth.TemmieDiscordAuth;
import com.mrpowergamerbr.temmiediscordauth.responses.CurrentUserResponse;
import com.mrpowergamerbr.temmiediscordauth.responses.OAuthTokenResponse;
import com.mrpowergamerbr.temmiediscordauth.utils.TemmieGuild;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IExtendedInvite;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IInvite;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;

@SuppressWarnings("restriction")
public class WebServer {
    private OrangePeel orangePeel;
    private IDiscordClient client;

    private HashMap<String, TemmieDiscordAuth> sessions;

    public WebServer(OrangePeel orangePeel) {
        this.orangePeel = orangePeel;
        client = orangePeel.getClient();
        sessions = new HashMap<String, TemmieDiscordAuth>();

    }

    public void start(final int port) throws IOException {

        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", new MyHandler(this));
        server.setExecutor(null); // creates a default executor
        server.start();
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

    public void handleRequest() {

    }

    public OrangePeel getOrangePeel() {
        return orangePeel;
    }

    public void setOrangePeel(OrangePeel orangePeel) {
        this.orangePeel = orangePeel;
    }

    public IDiscordClient getClient() {
        return client;
    }

    public void setClient(IDiscordClient client) {
        this.client = client;
    }

    static class MyHandler implements HttpHandler {
        private WebServer server;
        private IDiscordClient client;

        public MyHandler(WebServer server) {
            this.server = server;
            client = server.getClient();

        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            URI requestURI = exchange.getRequestURI();

            String request = requestURI.getPath();
            JSONObject response = new JSONObject();
            String auth = "";

            int responseCode = 200;

            if (exchange.getRequestHeaders().getFirst("Authorization") != null) {
                auth = exchange.getRequestHeaders().getFirst("Authorization");
            }
            if (request.equals("/test")) {
                response.put("test", "hello");

            } else if (request.equals("/test")) {
                response.put("test", "hello");
            } else if (request.equals("/invite")) {
                response.put("invite", "https://discordapp.com/oauth2/authorize?client_id=306115875784622080&permissions=470084679&scope=bot");
            } else if (request.equals("/server")) {
                IGuild g = client.getGuildByID(306508548651745282L);
                String invite = "";
                for (IExtendedInvite i : g.getExtendedInvites()) {
                    if (i.getInviter().equals(client.getOurUser())) {
                            invite = i.getCode();
                            break;
                    }

                }
                if (invite.equals("")) {
                    IInvite createInvite = g.getChannelByID(404732599597137923L).createInvite(0, 0, false, false);
                    invite = createInvite.getCode();
                }
                response.put("id", g.getStringID());
                response.put("size", g.getUsers().size());
                response.put("invite", "https://discord.gg/" + invite);
            } else if (request.equals("/stats")) {
                response.put("users", client.getUsers().size());
                response.put("guilds", client.getGuilds().size());
                response.put("channels", client.getChannels().size());
                response.put("commands", server.getOrangePeel().getEventListener().getCommandController().getCommands().size());
            } else if (request.startsWith("/auth")) {
                String queries = exchange.getRequestURI().toString().split("\\?")[1];
                String code = "";

                String redirect = "";
                for (String query : queries.split("&")) {
                    String k = query.split("=")[0];
                    String v = query.split("=")[1];
                    if (k.equals("code")) {
                        code = v;
                    }
                    if (k.equals("redirect")) {
                        redirect = v;
                    }

                }
                System.out.println("code: " + code);

                TemmieDiscordAuth temmie = new TemmieDiscordAuth(code, "http://mc.mouldycheerio.com:8213/auth", "370240387455123457", "jaLok-rslhZr9SStroMqHCpollEi70_q");

                OAuthTokenResponse e = temmie.doTokenExchange();

                String randomToken = PeelingUtils.getRandomToken();
                server.getSessions().put(randomToken, temmie);
                response.put("token", randomToken);
                response.put("expires", System.currentTimeMillis() + e.getExpiresIn());
                response.put("message", "Authorization successful!");

            } else if (request.equals("/guilds")) {
                JSONArray array = new JSONArray();

                for (IGuild iGuild : client.getGuilds()) {
                    JSONObject guild = new JSONObject();
                    guild.put("id", iGuild.getStringID());
                    guild.put("name", iGuild.getName());
                    guild.put("ownerName", iGuild.getOwner().getName() + "#" + iGuild.getOwner().getDiscriminator());
                    guild.put("ownerId", iGuild.getOwner().getStringID());
                    guild.put("memberCount", iGuild.getUsers().size());
                    int humans = 0;
                    for (IUser u : iGuild.getUsers()) {
                        if (!u.isBot()) {
                            humans++;
                        }
                    }
                    guild.put("humanCount", humans);
                    guild.put("iconUrl", iGuild.getIconURL());

                    array.put(guild);
                }

                response.put("guilds", array);
            }  else if (request.equals("/userinfo")) {
                if (!auth.equals("") && server.getSession(auth) != null) {
                    TemmieDiscordAuth session = server.getSession(auth);
                    CurrentUserResponse cui = session.getCurrentUserIdentification();
                    response.put("id", cui.getId());
                    response.put("name", cui.getUsername());
                    response.put("discriminator", cui.getDiscriminator());
                    response.put("has2fa", cui.isTwoFactorAuthenticationEnabled());
                    response.put("email", cui.getEmail());
                    response.put("verified", cui.isVerified());
                }
            } else if (request.equals("/sharedGuilds")) {
                if (!auth.equals("") && server.getSession(auth) != null) {
                    TemmieDiscordAuth session = server.getSession(auth);
                    IUser user = client.getUserByID(Long.parseLong(session.getCurrentUserIdentification().getId()));

                    JSONArray array = new JSONArray();

                    for (TemmieGuild temmieGuild : session.getUserGuilds()) {

                        if (client.getGuildByID(Long.parseLong(temmieGuild.getId())) != null) {
                            IGuild iGuild = client.getGuildByID(Long.parseLong(temmieGuild.getId()));
                            JSONObject guild = new JSONObject();
                            guild.put("id", iGuild.getStringID());
                            guild.put("name", iGuild.getName());
                            guild.put("ownerName", iGuild.getOwner().getName() + "#" + iGuild.getOwner().getDiscriminator());
                            guild.put("ownerId", iGuild.getOwner().getStringID());
                            guild.put("memberCount", iGuild.getUsers().size());
                            int humans = 0;
                            for (IUser u : iGuild.getUsers()) {
                                if (!u.isBot()) {
                                    humans++;
                                }
                            }
                            guild.put("humanCount", humans);
                            guild.put("iconUrl", iGuild.getIconURL());


                            if (user.getPermissionsForGuild(iGuild).contains(Permissions.ADMINISTRATOR) || iGuild.getOwner().equals(iGuild)) {
                                guild.put("admin", true);
                            } else {
                                guild.put("admin", false);

                            }
                            array.put(guild);
                        }
                    }

                    response.put("guilds", array);
                } else {
                    responseCode = 401;
                    response.put("message", "please provide an Authorization header!");

                }
            } else if (request.startsWith("/config")) {
                if (!auth.equals("") && server.getSession(auth) != null) {
                    TemmieDiscordAuth session = server.getSession(auth);
                    IUser user = client.getUserByID(Long.parseLong(session.getCurrentUserIdentification().getId()));
                    String guildID = request.split("/")[2];
                    String action = request.split("/")[3];

                    ServerConfig config = server.getOrangePeel().getConfig(Long.parseLong(guildID));

                    if (config != null) {
                        if (user.getPermissionsForGuild(config.getGuild()).contains(Permissions.ADMINISTRATOR) || config.getGuild().getOwner().equals(user)) {
                            if (action.equals("post")) {
                                JSONTokener parser = new JSONTokener(exchange.getRequestBody().toString());

                                JSONObject obj = (JSONObject) parser.nextValue();

                                if (obj.has("autorole")) {
                                    JSONObject autorole = obj.getJSONObject("autorole");
                                    if (autorole.getBoolean("has")) {
                                        config.setAutoRoleID(autorole.getString("role"));
                                    }
                                }

                                if (obj.has("greeter")) {
                                    JSONObject greeter = obj.getJSONObject("greeter");
                                    if (greeter.getBoolean("has")) {
                                        config.setGreetChannelID(greeter.getString("channel"));
                                    }
                                    config.setLeaveMessage(greeter.getString("leave"));
                                    config.setGreetMessage(greeter.getString("join"));
                                }

                                if (obj.has("muted")) {
                                    JSONObject muted = obj.getJSONObject("muted");
                                    if (muted.getBoolean("has")) {
                                        config.setMutedRoleID(muted.getString("role"));
                                    }
                                }

                                if (obj.has("magic")) {
                                    config.getMagicChannels().clear();
                                    JSONArray magic = obj.getJSONArray("magic");
                                    for (int i = 0; i < magic.length(); i++) {
                                        JSONObject mc = magic.getJSONObject(i);
                                        config.getMagicChannels().add(new MagicChannel(client, mc.getString("channel"), mc.getString("role")));
                                    }
                                }
                            } else if (action.equals("get")) {
                                JSONObject autorole = new JSONObject();
                                if (config.hasAutoRole()) {
                                    autorole.put("has", true);
                                    autorole.put("role", config.getAutoRole().getStringID());
                                } else {
                                    autorole.put("has", false);
                                    autorole.put("role", "undefined");
                                }
                                response.put("autorole", autorole);

                                JSONObject greeter = new JSONObject();
                                if (config.hasGreetChannel()) {
                                    greeter.put("has", true);
                                    greeter.put("channel", config.getGreetChannel().getStringID());
                                } else {
                                    greeter.put("has", false);
                                    greeter.put("channel", "");
                                }
                                greeter.put("join", config.getGreetMessage());
                                greeter.put("leave", config.getLeaveMessage());
                                response.put("greet", greeter);

                                JSONObject muted = new JSONObject();
                                if (config.hasMutedRole()) {
                                    muted.put("has", true);
                                    muted.put("role", config.getMutedRole().getStringID());
                                } else {
                                    muted.put("has", false);
                                    muted.put("role", "");
                                }
                                response.put("muted", muted);

                                JSONArray magic = new JSONArray();
                                for (MagicChannel magicChannel : config.getMagicChannels()) {
                                    JSONObject mc = new JSONObject();
                                    mc.put("role", magicChannel.getRole().getStringID());
                                    mc.put("channel", magicChannel.getVoiceChannel().getStringID());
                                }
                                response.put("magic", magic);
                            } else {
                            }
                        }
                    } else {
                        responseCode = 400;
                        response.put("message", "Invalid Server ID");
                    }

                }
            }

            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.sendResponseHeaders(responseCode, response.toString().getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.toString().getBytes());
            os.close();

        }

    }
}