package com.mamcose.nlp.twitterapi;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface LabelTweetsRepository extends MongoRepository<LabelTweets, String> {
    LabelTweets findBy_id(ObjectId _id);
    long countAllByLabelIs(int label);

}
