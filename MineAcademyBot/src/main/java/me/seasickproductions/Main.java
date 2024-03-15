package me.seasickproductions;

import me.seasickproductions.listener.NewsBot;
import net.dv8tion.jda.api.JDABuilder;
import okhttp3.*;
import org.apache.commons.cli.*;

import java.io.IOException;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {

        Options options = new Options();

        options.addOption(new Option("h", "help", false, "Displays this help menu"));
        options.addOption(new Option("t", "token", true, "Provide the token during startup"));

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();

        try {
            CommandLine cmd = parser.parse(options, args);

            if (cmd.hasOption("help")) {
                formatter.printHelp("Help Menu", options);
                System.exit(0);
            }

            String token = cmd.hasOption("token") ? cmd.getOptionValue("token") : null;
            if (token == null) {
                System.out.println("ERROR: no token provided, please provide a token using the -t or --token flag.");
                formatter.printHelp("", options);
                System.exit(0);
            }

            MineAcademyBot.selfBot = new MineAcademyBot(token);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("", options);
            System.exit(0);
        }

        try {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("text/plain");
            RequestBody body = RequestBody.create(mediaType, "");
            Request request = new Request.Builder()
                    .url("https://newsapi.org/v2/top-headlines?country=us&apiKey=98764800550341bb8a44c9ee77769619")
                    .get()
                    .build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                System.out.println("Response Body:");
                System.out.println(response.body().string());
            } else {
                System.err.println("Error: " + response.code() + " - " +
                        response.message());
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            JDABuilder.createDefault("MTE5OTc5MTg5MTc4MTI3NTc2MQ.GINqtz.FV6YZz7uIHTsGWedrSbBzmFvPpW4mSqe88_8Yk") // discord bot token
                    .addEventListeners(new NewsBot())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}