package me.seasickproductions.listener;

import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

public class NewsBot extends ListenerAdapter {


    private static final String NEWS_API_KEY = "98764800550341bb8a44c9ee77769619"; // news API KEY
    private static final String NEWS_API_URL = "https://newsapi.org/v2/top-headlines?category=technology&apiKey=" + NEWS_API_KEY;

    @Override
    public void onSlashCommand(SlashCommandInteractionEvent event) {
        if (event.getName().equals("technews")) {
            event.deferReply().queue();

            try {
                String news = fetchTechNews();
                MessageChannel channel = event.getChannel();
                channel.sendMessage(news).queue();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String fetchTechNews() throws IOException {
        StringBuilder response = new StringBuilder();
        URL url = new URL(NEWS_API_URL); // news Api URL
        try (Scanner scanner = new Scanner(url.openStream())){
            while (scanner.hasNextLine()) {
                response.append(scanner.nextLine());
            }
        }

        JSONObject jsonObject = new JSONObject(response.toString());
        JSONArray articles = jsonObject.getJSONArray("articles");
        StringBuilder news = new StringBuilder("Tech News:\n");
        for (int i = 0; i < Math.min(5, articles.length()); i++) {
            JSONObject article = articles.getJSONObject(i);
            String title = article.getString("title");
            String description = article.getString("description");
            String articleUrl = article.getString("url");
            news.append("**").append(title).append("**\n");
            news.append(description).append("\n").append(articleUrl).append("\n\n");

        }
        return news.toString();
    }
}
