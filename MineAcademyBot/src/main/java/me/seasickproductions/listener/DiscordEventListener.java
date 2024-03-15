package me.seasickproductions.listener;

import me.seasickproductions.MineAcademyBot;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DiscordEventListener extends ListenerAdapter {
    public MineAcademyBot bot;
    public DiscordEventListener(MineAcademyBot bot) {
        this.bot = bot;
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        registerCommands(bot.getShardManager());
    }

    private void registerCommands(ShardManager jda) {
        Guild g = jda.getGuildById("1198407787194097704");
        if (g != null){
            CommandListUpdateAction commands = g.updateCommands();
            commands.addCommands(Commands.slash("hello",  "the bot say hello to you in an ephemeral message!")).queue();
            commands.addCommands(Commands.slash("ping", "The bot says pong")).queue();
        }
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("hello")) {
            event.reply("Hello " + event.getUser().getAsMention() + "!")
                    .setEphemeral(true)
                    .queue();
        }

        else if (event.getInteraction().equals("ping")) {
            try {
                URL url = new URL("https://api.weather.gov/gridpoints/OKX/34,37/forecast");

                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                con.setRequestMethod("GET");
                int responseCode = con.getResponseCode();
                System.out.println("Response Code: " + responseCode);
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                System.out.println("Response: " + response.toString());
                event.reply(response.toString())
                                .setEphemeral(true)
                                        .queue();
                con.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }

                //    .url("https://newsapi.org/v2/top-headlines?country=us&apiKey=98764800550341bb8a44c9ee77769619")

//            event.reply("pong " + event.getUser().getAsMention() + "!")
//                    .setEphemeral(true)
//                    .queue();
        }
    }
}
