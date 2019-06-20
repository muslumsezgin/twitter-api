package com.mamcose.nlp.twitterapi;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import zemberek.morphology.TurkishMorphology;
import zemberek.morphology.analysis.SingleAnalysis;
import zemberek.morphology.analysis.WordAnalysis;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/lemma")
public class LemmaController {

    private final TurkishMorphology morphology;

    @Autowired
    public LemmaController() {
        this.morphology = TurkishMorphology.createWithDefaults();
    }


    public String textCleaner(String line) {
        String s =  line.replaceAll("\\B\\@([\\w\\-]+)", "")
                .replaceAll("http.*", "")
                .replaceAll("www.*?\\s", "")
                .replaceAll("[!#$%^&*?|+'_=\\[\\],.;?\"0-9/;():-]", "")
                .replace("RT", "")
                .replaceAll("quot", "")
                .replaceAll("amp", "");

        return Arrays.stream(s.split("\\s"))
                .filter(i -> !"".equals(i.trim()))
                .map(w -> {
                    String result = w;
                    WordAnalysis analyze = morphology.analyze(w);
                    List<SingleAnalysis> analysisResults = analyze.getAnalysisResults();
                    if (analysisResults.size() != 0) {
                        List<String> lemmas = analyze.getAnalysisResults().get(analysisResults.size() - 1).getLemmas();
                        result = lemmas.get(lemmas.size() - 1);
                    }
                    return result;
                })
                .collect(Collectors.joining(" "));

    }

    @PostMapping("/")
    public String getOneTweet(@RequestBody Lemma text) {
        return textCleaner(text.getText());
    }


}
