package com.mouldycheerio.discord.orangepeel.commands;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.json.JSONObject;

import com.mouldycheerio.discord.orangepeel.OrangePeel;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IMessage;

public class EvalCommand extends OrangePeelAdminCommand {
    private HashMap<String, String> vars;

    public EvalCommand() {
        setName("eval");
        setDescription(new CommandDescription("Evaluate", "Evaluate", "eval [expression]"));
        setCommandlvl(1);
        vars = new HashMap<String, String>();
        vars.put("pi", "" + Math.PI);
        addAlias("maths");
    }

    public static double eval(final String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char)ch);
                return x;
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)`
            //        | number | functionName factor | factor `^` factor

            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if      (eat('+')) x += parseTerm(); // addition
                    else if (eat('-')) x -= parseTerm(); // subtraction
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if      (eat('*')) x *= parseFactor(); // multiplication
                    else if (eat('/')) x /= parseFactor(); // division
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor(); // unary plus
                if (eat('-')) return -parseFactor(); // unary minus

                double x;
                int startPos = this.pos;
                if (eat('(')) { // parentheses
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z') { // functions
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = str.substring(startPos, this.pos);
                    x = parseFactor();
                    if (func.equals("sqrt")) x = Math.sqrt(x);
                    else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));
                    else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));
                    else if (func.equals("tan")) x = Math.tan(Math.toRadians(x));
                    else throw new RuntimeException("Unknown function: " + func);
                } else {
                    throw new RuntimeException("Unexpected: " + (char)ch);
                }

                if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

                return x;
            }
        }.parse();
    }

    public void onCommand(OrangePeel orangepeel, IDiscordClient client, IMessage commandMessage, String[] args) {
        vars.put("op.servers", "" + orangepeel.getClient().getGuilds().size());
        vars.put("op.guilds", "" + orangepeel.getClient().getGuilds().size());
        vars.put("op.members", "" + orangepeel.getClient().getUsers().size());


        JSONObject stats = orangepeel.getStatsCounter().getStats();
        Iterator<?> keys = stats.keys();

        while( keys.hasNext() ) {
            String key = (String)keys.next();
            vars.put("op.stat." + key, "" + stats.getInt(key));

        }

        if (args.length >= 2) {
            IMessage m = commandMessage.getChannel().sendMessage("Calculating...");
            try {
            StringBuilder sb = new StringBuilder();
            if (args.length > 1) {
                for (int i = 1; i < args.length; i++) {
                    if (i > 1) {
                        sb.append(" ");
                    }
                    sb.append(args[i]);
                }
            }

            String text = sb.toString();
            text = text.replace(" ", "");
//            ExprEvaluator util = new ExprEvaluator();

            String[] split = text.split("=");
            for (Iterator<Entry<String, String>> iterator = vars.entrySet().iterator(); iterator.hasNext();) {
                Entry<String, String> entry = iterator.next();
                text = text.replace(entry.getKey(), entry.getValue());
                System.out.println(text);

            }
            if (split.length > 1) {
                vars.put(split[0], "" + eval(split[1]));
                m.edit("```" + split[0] + "=" + vars.get(split[0]) + "```");
            } else {
                double eval = eval(text);
                m.edit("```=" + eval + "```");
            }
            }catch (Exception e) {
                orangepeel.logError(e);
                m.edit("```ERROR```");
                e.printStackTrace();
            }

        }
    }
}
