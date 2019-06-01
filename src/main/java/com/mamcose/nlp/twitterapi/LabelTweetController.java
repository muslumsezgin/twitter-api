package com.mamcose.nlp.twitterapi;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("label/tweet")
public class LabelTweetController {
    private final LabelTweetsRepository repository;

    @Autowired
    public LabelTweetController(LabelTweetsRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/")
    public LabelTweets createPet(@Valid @RequestBody LabelTweets tweet) {
        repository.save(tweet);
        return tweet;
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
