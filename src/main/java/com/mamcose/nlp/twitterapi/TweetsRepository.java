package com.mamcose.nlp.twitterapi;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TweetsRepository extends MongoRepository<Tweets, String> {
    Tweets findBy_id(ObjectId _id);
    Tweets findById(long id);
    Tweets findFirstBy_idIsNotIn(List ids);
}
