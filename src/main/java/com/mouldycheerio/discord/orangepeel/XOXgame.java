package com.mouldycheerio.discord.orangepeel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IReaction;

public class XOXgame {
    private IMessage message;
    private int nextnumbertoadd;

    private boolean userTurn = true;
    private IDiscordClient client;

    private int turns = 0;
    private boolean ended = false;
    private OrangePeel op;

    public XOXgame(IMessage m, OrangePeel op) {
        this.op = op;
        this.client = op.getClient();
        this.setMessage(m);
        setNextnumbertoadd(1);
        Logger.info("XOX game created!");
        // TODO Auto-generated constructor stub
    }

    public int getNextnumbertoadd() {
        return nextnumbertoadd;
    }

    public void setNextnumbertoadd(int nextnumbertoadd) {
        this.nextnumbertoadd = nextnumbertoadd;
    }

    public XOXsquare hasWinner() {
        int n = 3;
        XOXsquare[][] g = createArrays();
        XOXsquare s;
        for (int c = 0; c < 2; c++) {
            if (c == 0) {
                s = XOXsquare.O;
            } else {
                s = XOXsquare.X;
            }
            for (int x = 0; x < g.length; x++) {
                XOXsquare[] column = g[x];
                for (int y = 0; y < column.length; y++) {
                    XOXsquare xoXsquare = column[y];
                    for (int i = 0; i < n; i++) {
                        if (g[x][i] != s)
                            break;
                        if (i == n - 1) {
                            return s;
                        }
                    }
                    for (int i = 0; i < n; i++) {
                        if (g[i][y] != s)
                            break;
                        if (i == n - 1) {
                            return s;
                        }
                    }
                    if (x == y) {
                        for (int i = 0; i < n; i++) {
                            if (g[i][i] != s)
                                break;
                            if (i == n - 1) {
                                return s;
                            }
                        }
                    }

                    if (x + y == n - 1) {
                        for (int i = 0; i < n; i++) {
                            if (g[i][(n - 1) - i] != s)
                                break;
                            if (i == n - 1) {
                                return s;
                            }
                        }
                    }
                }
            }

        }

        return XOXsquare.EMPTY;
    }

    public String getNextMove() {
        int x = -1;
        int y = -1;
        XOXsquare[][] g = createArrays();
        List<IReaction> mine = new ArrayList<IReaction>();
        for (IReaction iReaction : message.getReactions()) {
            if (iReaction.getUsers().contains(client.getOurUser()) && iReaction.getUsers().size() == 1) {
                mine.add(iReaction);
            }
        }
        Random r = new Random();
        if (mine.size() == 0) {
            message.edit(":x:*An unknown error has occured*:x:");
            ended = true;
            return "";
        }
        IReaction myturn = mine.get(r.nextInt(mine.size()));
        String emo = myturn.getUnicodeEmoji().getAliases().get(0);
        boolean stopped = false;
        int n = 2;


        if (g[0][0] == XOXsquare.O) {
            if (g[1][0] == XOXsquare.EMPTY) {
                x = 1;
                y = 0;
            } else if (g[0][1] == XOXsquare.EMPTY) {
                x = 0;
                y = 1;
            } else if (g[1][1] == XOXsquare.EMPTY) {
                x = 1;
                y = 1;
            }
        } else if (g[1][0] == XOXsquare.O) {
            if (g[0][0] == XOXsquare.EMPTY) {
                x = 0;
                y = 0;
            } else if (g[1][1] == XOXsquare.EMPTY) {
                x = 1;
                y = 1;
            } else if (g[2][1] == XOXsquare.EMPTY) {
                x = 2;
                y = 1;
            }
        } else if (g[2][0] == XOXsquare.O) {
            if (g[2][0] == XOXsquare.EMPTY) {
                x = 2;
                y = 0;
            } else if (g[0][1] == XOXsquare.EMPTY) {
                x = 0;
                y = 1;
            } else if (g[1][1] == XOXsquare.EMPTY) {
                x = 1;
                y = 1;
            }
        } else if (g[0][1] == XOXsquare.O) {
            if (g[1][1] == XOXsquare.EMPTY) {
                x = 1;
                y = 1;
            } else if (g[0][0] == XOXsquare.EMPTY) {
                x = 0;
                y = 0;
            } else if (g[0][2] == XOXsquare.EMPTY) {
                x = 0;
                y = 2;
            }
        } else if (g[1][1] == XOXsquare.O) {
            if (g[0][0] == XOXsquare.EMPTY) {
                x = 0;
                y = 0;
            } else if (g[0][1] == XOXsquare.EMPTY) {
                x = 0;
                y = 1;
            } else if (g[0][2] == XOXsquare.EMPTY) {
                x = 0;
                y = 2;
            } else if (g[1][0] == XOXsquare.EMPTY) {
                x = 1;
                y = 0;
            } else if (g[1][2] == XOXsquare.EMPTY) {
                x = 1;
                y = 2;
            } else if (g[2][0] == XOXsquare.EMPTY) {
                x = 2;
                y = 0;
            } else if (g[2][1] == XOXsquare.EMPTY) {
                x = 2;
                y = 1;
            } else if (g[2][2] == XOXsquare.EMPTY) {
                x = 2;
                y = 2;
            }
        } else if (g[1][2] == XOXsquare.O) {
            if (g[1][1] == XOXsquare.EMPTY) {
                x = 1;
                y = 1;
            } else if (g[2][2] == XOXsquare.EMPTY) {
                x = 2;
                y = 2;
            } else if (g[2][0] == XOXsquare.EMPTY) {
                x = 2;
                y = 0;
            }
        } else if (g[2][0] == XOXsquare.O) {
            if (g[1][1] == XOXsquare.EMPTY) {
                x = 1;
                y = 1;
            } else if (g[0][1] == XOXsquare.EMPTY) {
                x = 0;
                y = 1;
            } else if (g[1][2] == XOXsquare.EMPTY) {
                x = 1;
                y = 2;
            }
        } else if (g[2][1] == XOXsquare.O) {
            if (g[1][1] == XOXsquare.EMPTY) {
                x = 1;
                y = 1;
            } else if (g[2][2] == XOXsquare.EMPTY) {
                x = 2;
                y = 2;
            } else if (g[0][2] == XOXsquare.EMPTY) {
                x = 0;
                y = 2;
            }
        } else if (g[2][2] == XOXsquare.O) {
            if (g[1][1] == XOXsquare.EMPTY) {
                x = 1;
                y = 1;
            } else if (g[1][2] == XOXsquare.EMPTY) {
                x = 1;
                y = 2;
            } else if (g[2][1] == XOXsquare.EMPTY) {
                x = 2;
                y = 1;
            }
        }
        Logger.warn("(x/y) " + x + "/" + y);
        if (x == 0 && y == 0) {
            emo = "one";
        } else if (x == 1 && y == 0) {
            emo = "two";
        } else if (x == 2 && y == 0) {
            emo = "three";
        } else if (x == 0 && y == 1) {
            emo = "four";
        } else if (x == 1 && y == 1) {
            emo = "five";
        } else if (x == 2 && y == 1) {
            emo = "six";
        } else if (x == 0 && y == 2) {
            emo = "seven";
        } else if (x == 1 && y == 2) {
            emo = "eight";
        } else if (x == 2 && y == 2) {
            emo = "nine";
        }
        return emo;
    }

    private XOXsquare[][] createArrays() {
        int y = 0;
        XOXsquare[][] game = new XOXsquare[3][3];
        for (String string : message.getContent().split("\n")) {
            string = string.replace("::", " ").replace(":", "");

            String[] split = string.split(" ");
            int x = 0;
            for (String square : split) {
                XOXsquare type;
                if (square.equals("o2")) {
                    type = XOXsquare.O;
                } else if (square.equals("negative_squared_cross_mark")) {
                    type = XOXsquare.X;
                } else {
                    type = XOXsquare.EMPTY;
                }
                game[x][y] = type;
                x++;
            }
            y++;

        }
        return game;
    }

    public void addedNumber() {
        nextnumbertoadd++;
    }

    public IMessage getMessage() {
        return message;
    }

    public void setMessage(IMessage message) {
        this.message = message;
    }

    public boolean isUserTurn() {
        return userTurn;
    }

    public void setUserTurn(boolean userTurn) {
        this.userTurn = userTurn;
    }

    public void toggleUserTurn() {
        userTurn = !userTurn;
    }

    public int getTurns() {
        return turns;
    }
    public boolean ended() {
        return ended;
    }

    public void nextTurn() {
        this.turns++;
        if (hasWinner() == XOXsquare.O) {
            message.edit("YAY! :o2: wins!!!");
            op.getStatsCounter().incrementStat("xox-wins");

            ended = true;
        } else if (hasWinner() == XOXsquare.X) {
            message.edit("YAY! :negative_squared_cross_mark: wins!!!");
            op.getStatsCounter().incrementStat("xox-losses");

            ended = true;
        } else if (turns >= 9) {
            message.edit("DRAW!!!");
            ended = true;
        }
    }

}
