package com.mamcose.nlp.twitterapi;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.web.bind.annotation.*;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tweets")
public class TweetController {
    private final TweetsRepository repository;
    private final LabelTweetsRepository repositoryLabel;

    @Autowired
    public TweetController(TweetsRepository repository, LabelTweetsRepository repositoryLabel) {
        this.repository = repository;
        this.repositoryLabel = repositoryLabel;
    }

    @GetMapping("/")
    public ResponseTweet getOneTweet() {
        List<ObjectId> allIds = repositoryLabel.findAll().stream().map(LabelTweets::get_id).collect(Collectors.toList());
        Tweets tweets = repository.findFirstBy_idIsNotIn(allIds);
        return new ResponseTweet(tweets.get_id().toHexString(), tweets.getText());
    }

    @GetMapping("/count")
    public long getCount() {
        return repository.count();
    }

//    @PostMapping("/")
//    public Tweets createTweet(@Valid @RequestBody Tweets tweet) {
//        repository.save(tweet);
//        return tweet;
//    }


    @DeleteMapping("/{id}")
    public String deleteT(@PathVariable String id) {
        System.out.println(id);
        repository.delete(repository.findBy_id(new ObjectId(id)));
        return id + " silindi";
    }


    @GetMapping("/hashtag/{query}/{size}")
    public String getTweetsByQuery(@PathVariable("query") String id, @PathVariable int size) {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("QSAAh9LYwNpRa1c9oIia5TJP0")
                .setOAuthConsumerSecret("8NmuthsKXxWNTUsIy00rGwWM403VYbZ1Rhc9m0co9c72cZkLJy")
                .setOAuthAccessToken("275664951-jjAfr6nzdqNYxqQpN1VSerfitWdFV5gWEmINflb8")
                .setOAuthAccessTokenSecret("ItOpDWg0bvM5RcVhVnvUJtiAx4AATykUMP0lOSxfUxWsR");
        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();
        Query query = new Query("#" + id);
        query.setCount(size);
        query.setLang("tr");
        try {
            QueryResult result = twitter.search(query);
            List<Status> tweets = result.getTweets();
            System.out.println(tweets.size());
            int i = saveT(tweets);
            return "Toplam " + tweets.size() + "\nEklenen: " + i;
        } catch (TwitterException e) {
//            e.printStackTrace();
            System.out.println("HATA:::  #" + id + "  araması olmadı");
            return "Tweet eklenemedi";
        }
    }

    private int saveT(List<Status> tweets) {
        int i = 0;
        for (Status t : tweets) {
            Tweets byId = repository.findById(t.getId());
            if (Objects.isNull(byId)) {
                Tweets tweet = new Tweets(ObjectId.get(), t.getId(), t.getText(), t.getUser().getId());
                repository.save(tweet);
                i++;
            }
        }
        return i;
    }

    @GetMapping("/user/{query}/{size}")
    public String getTweetsByUser(@PathVariable("query") String id, @PathVariable int size) {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("QSAAh9LYwNpRa1c9oIia5TJP0")
                .setOAuthConsumerSecret("8NmuthsKXxWNTUsIy00rGwWM403VYbZ1Rhc9m0co9c72cZkLJy")
                .setOAuthAccessToken("275664951-jjAfr6nzdqNYxqQpN1VSerfitWdFV5gWEmINflb8")
                .setOAuthAccessTokenSecret("ItOpDWg0bvM5RcVhVnvUJtiAx4AATykUMP0lOSxfUxWsR");
        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();

        try {
            Paging paging = new Paging(1, size);
            ResponseList<Status> statuses = twitter.getUserTimeline(id, paging);
            int i = saveT(statuses);
            return "Toplam " + statuses.size() + "\nEklenen: " + i;
        } catch (TwitterException e) {
//            e.printStackTrace();
            System.out.println("HATA:::  " + id + "  araması olmadı");
            return "Tweet eklenemedi";
        }
    }
}
