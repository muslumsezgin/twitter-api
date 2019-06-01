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
    public Tweets getOneTweet() {
        List<ObjectId> allIds = repositoryLabel.findAll().stream().map(LabelTweets::get_id).collect(Collectors.toList());
        return repository.findFirstBy_idIsNotIn(allIds);
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
    public void deletePet(@PathVariable ObjectId id) {
        repository.delete(repository.findBy_id(id));
    }

    @GetMapping("/hashtag/{query}")
    public boolean getTweetsByQuery(@PathVariable("query") String id) {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("QSAAh9LYwNpRa1c9oIia5TJP0")
                .setOAuthConsumerSecret("8NmuthsKXxWNTUsIy00rGwWM403VYbZ1Rhc9m0co9c72cZkLJy")
                .setOAuthAccessToken("275664951-jjAfr6nzdqNYxqQpN1VSerfitWdFV5gWEmINflb8")
                .setOAuthAccessTokenSecret("ItOpDWg0bvM5RcVhVnvUJtiAx4AATykUMP0lOSxfUxWsR");
        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();
        Query query = new Query("#"+id);
        query.setCount(3);
        query.setLang("tr");
        try {
            QueryResult result = twitter.search(query);
            List<Status> tweets = result.getTweets();
            return saveT(tweets);
        } catch (TwitterException e) {
//            e.printStackTrace();
            System.out.println("HATA:::  #" + id + "  araması olmadı");
            return false;
        }
    }

    private boolean saveT(List<Status> tweets) {
        tweets.forEach(t -> {
            Tweets tweet = new Tweets(ObjectId.get(),t.getId(), t.getText(), t.getUser().getId());
            repository.save(tweet);
        });
        return true;
    }

    @GetMapping("/user/{query}/{size}")
    public boolean getTweetsByUser(@PathVariable("query") String id, @PathVariable int size) {
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
            return saveT(statuses);
        } catch (TwitterException e) {
//            e.printStackTrace();
            System.out.println("HATA:::  " + id  + "  araması olmadı");
            return false;
        }
    }
}
