package com.mamcose.nlp.twitterapi;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("label/tweet")
public class LabelTweetController {
    private final LabelTweetsRepository repository;
    private final TweetsRepository repositoryTweet;

    @Autowired
    public LabelTweetController(LabelTweetsRepository repository, TweetsRepository repositoryTweet) {
        this.repository = repository;
        this.repositoryTweet = repositoryTweet;
    }

    @PutMapping("/{id}/{label}")
    public LabelTweets createPet(@PathVariable String id, @PathVariable int label) {
        Tweets tweet = repositoryTweet.findBy_id(new ObjectId(id));
        LabelTweets labelTweets = new LabelTweets(tweet.get_id(), tweet.getId(), tweet.getText(), tweet.getUser(), label);
        repository.save(labelTweets);
        return labelTweets;
    }

    @GetMapping("/count")
    public long getCount(){
        return repository.count();
    }

    @GetMapping("/count/{label}")
    public long getCount(@PathVariable int label){
        return repository.countAllByLabelIs(label);
    }

}
