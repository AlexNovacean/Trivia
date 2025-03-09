package ro.alex.trivia.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ro.alex.trivia.model.Joke;

import java.util.Objects;

@Service
public class JokeService {

    private final RestTemplate restTemplate;
    private final String jokeUrl;

    public JokeService() {
        this.restTemplate = new RestTemplate();
        this.jokeUrl = "https://api.chucknorris.io/jokes/random";
    }

    public String getJoke() {
        return Objects.requireNonNull(restTemplate.getForObject(jokeUrl, Joke.class)).getValue();
    }

}
