package com.mouldycheerio.discord.orangepeel.games;

import java.util.Random;

import com.mouldycheerio.discord.orangepeel.Logger;
import com.mouldycheerio.discord.orangepeel.OrangePeel;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IMessage;

public class XOXgame {
    private IMessage message;
    private int nextnumbertoadd;

    private boolean userTurn = true;
    private IDiscordClient client;

    private int turns = 0;
    private boolean ended = false;
    private OrangePeel op;
    private XOXsquare[][] arrays;

    public XOXgame(IMessage m, OrangePeel op) {
        this.setOp(op);
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
        System.out.println("checking for winner");

        int n = 3;

        XOXsquare s;
        for (int c = 0; c < 2; c++) {
            if (c == 0) {
                s = XOXsquare.O;
            } else {
                s = XOXsquare.X;
            }
            for (int x = 0; x < arrays.length; x++) {
                XOXsquare[] column = arrays[x];
                for (int y = 0; y < column.length; y++) {

                    XOXsquare xoXsquare = column[y];

                    for (int i = 0; i < n; i++) {
                        if (arrays[x][i] != s)
                            break;
                        if (i == n - 1) {
                            return s;
                        }
                    }
                    for (int i = 0; i < n; i++) {
                        if (arrays[i][y] != s)
                            break;
                        if (i == n - 1) {
                            return s;
                        }
                    }
                    if (x == y) {
                        for (int i = 0; i < n; i++) {
                            if (arrays[i][i] != s)
                                break;
                            if (i == n - 1) {
                                return s;
                            }
                        }
                    }

                    if (x + y == n - 1) {
                        for (int i = 0; i < n; i++) {
                            if (arrays[i][(n - 1) - i] != s)
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

        System.out.println("getting reactions ");


        Random r = new Random();
        String emo = "";
        x = r.nextInt(2);
        y = r.nextInt(2);
        boolean stopped = false;
        int n = 2;

        System.out.println("thinking ");

        if (arrays[0][0] == XOXsquare.O) {
            if (arrays[1][0] == XOXsquare.EMPTY) {
                x = 1;
                y = 0;
            } else if (arrays[0][1] == XOXsquare.EMPTY) {
                x = 0;
                y = 1;
            } else if (arrays[1][1] == XOXsquare.EMPTY) {
                x = 1;
                y = 1;
            }
        } else if (arrays[1][0] == XOXsquare.O) {
            if (arrays[0][0] == XOXsquare.EMPTY) {
                x = 0;
                y = 0;
            } else if (arrays[1][1] == XOXsquare.EMPTY) {
                x = 1;
                y = 1;
            } else if (arrays[2][1] == XOXsquare.EMPTY) {
                x = 2;
                y = 1;
            }
        } else if (arrays[2][0] == XOXsquare.O) {
            if (arrays[2][0] == XOXsquare.EMPTY) {
                x = 2;
                y = 0;
            } else if (arrays[0][1] == XOXsquare.EMPTY) {
                x = 0;
                y = 1;
            } else if (arrays[1][1] == XOXsquare.EMPTY) {
                x = 1;
                y = 1;
            }
        } else if (arrays[0][1] == XOXsquare.O) {
            if (arrays[1][1] == XOXsquare.EMPTY) {
                x = 1;
                y = 1;
            } else if (arrays[0][0] == XOXsquare.EMPTY) {
                x = 0;
                y = 0;
            } else if (arrays[0][2] == XOXsquare.EMPTY) {
                x = 0;
                y = 2;
            }
        } else if (arrays[1][1] == XOXsquare.O) {
            if (arrays[0][0] == XOXsquare.EMPTY) {
                x = 0;
                y = 0;
            } else if (arrays[0][1] == XOXsquare.EMPTY) {
                x = 0;
                y = 1;
            } else if (arrays[0][2] == XOXsquare.EMPTY) {
                x = 0;
                y = 2;
            } else if (arrays[1][0] == XOXsquare.EMPTY) {
                x = 1;
                y = 0;
            } else if (arrays[1][2] == XOXsquare.EMPTY) {
                x = 1;
                y = 2;
            } else if (arrays[2][0] == XOXsquare.EMPTY) {
                x = 2;
                y = 0;
            } else if (arrays[2][1] == XOXsquare.EMPTY) {
                x = 2;
                y = 1;
            } else if (arrays[2][2] == XOXsquare.EMPTY) {
                x = 2;
                y = 2;
            }
        } else if (arrays[1][2] == XOXsquare.O) {
            if (arrays[1][1] == XOXsquare.EMPTY) {
                x = 1;
                y = 1;
            } else if (arrays[2][2] == XOXsquare.EMPTY) {
                x = 2;
                y = 2;
            } else if (arrays[2][0] == XOXsquare.EMPTY) {
                x = 2;
                y = 0;
            }
        } else if (arrays[2][0] == XOXsquare.O) {
            if (arrays[1][1] == XOXsquare.EMPTY) {
                x = 1;
                y = 1;
            } else if (arrays[0][1] == XOXsquare.EMPTY) {
                x = 0;
                y = 1;
            } else if (arrays[1][2] == XOXsquare.EMPTY) {
                x = 1;
                y = 2;
            }
        } else if (arrays[2][1] == XOXsquare.O) {
            if (arrays[1][1] == XOXsquare.EMPTY) {
                x = 1;
                y = 1;
            } else if (arrays[2][2] == XOXsquare.EMPTY) {
                x = 2;
                y = 2;
            } else if (arrays[0][2] == XOXsquare.EMPTY) {
                x = 0;
                y = 2;
            }
        } else if (arrays[2][2] == XOXsquare.O) {
            if (arrays[1][1] == XOXsquare.EMPTY) {
                x = 1;
                y = 1;
            } else if (arrays[1][2] == XOXsquare.EMPTY) {
                x = 1;
                y = 2;
            } else if (arrays[2][1] == XOXsquare.EMPTY) {
                x = 2;
                y = 1;
            }
        }
//        Logger.warn("(x/y) " + x + "/" + y);

//      <:xox1:392070187610013696L>
//      <:xox2:392070187601625118L>
//      <:xox3:392070187601625088L>
//      <:xox4:392070187563876352L>
//      <:xox5:392070187794432000L>
//      <:xox6:392070187362418701L>
//      <:xox7:392070187559682048L>
//      <:xox8:392070187589042186L>
//      <:xox9:392070187584585728L>
        if (x == 0 && y == 0) {
            emo = "<:xox1:392070187610013696>";
        } else if (x == 1 && y == 0) {
            emo = "<:xox2:392070187601625118>";
        } else if (x == 2 && y == 0) {
            emo = "<:xox3:392070187601625088>";
        } else if (x == 0 && y == 1) {
            emo = "<:xox4:392070187563876352>";
        } else if (x == 1 && y == 1) {
            emo = "<:xox5:392070187794432000>";
        } else if (x == 2 && y == 1) {
            emo = "<:xox6:392070187362418701>";
        } else if (x == 0 && y == 2) {
            emo = "<:xox7:392070187559682048>";
        } else if (x == 1 && y == 2) {
            emo = "<:xox8:392070187589042186>";
        } else if (x == 2 && y == 2) {
            emo = "<:xox9:392070187584585728>";
        } else {
            emo = "<:xox1:39207018761001369L>";
        }
        return emo;
    }

    public XOXsquare[][] createArrays(String content) {
        System.out.println("making arrays");

        int y = 0;
        XOXsquare[][] game = new XOXsquare[3][3];
        for (String string : content.split("\n")) {

            String[] split = string.split(" ");
            int x = 0;
            for (String square : split) {
                XOXsquare type;
                if (square.equals(":o2:")) {
                    type = XOXsquare.O;
                } else if (square.equals(":negative_squared_cross_mark:")) {
                    type = XOXsquare.X;
                } else {
                    type = XOXsquare.EMPTY;
                }
                game[x][y] = type;
                x++;
            }
            y++;

        }
//        System.out.println("\n");
//        for (XOXsquare[] x : game) {
//            for (XOXsquare i : x) {
//                System.out.print(i);
//            }
//            System.out.print("\n");
//
//        }
//        System.out.println("\n");


        arrays = game;
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
        XOXsquare winner = hasWinner();
        if (winner == XOXsquare.O) {
            message.edit("YAY! :o2: wins!!!");
            getOp().getStatsCounter().incrementStat("xox-wins");

            ended = true;
        } else if (winner == XOXsquare.X) {
            message.edit("YAY! :negative_squared_cross_mark: wins!!!");
            getOp().getStatsCounter().incrementStat("xox-losses");

            ended = true;
        } else if (turns >= 9) {
            message.edit("DRAW!!!");
            ended = true;
        }
    }

    public OrangePeel getOp() {
        return op;
    }

    public void setOp(OrangePeel op) {
        this.op = op;
    }

}
