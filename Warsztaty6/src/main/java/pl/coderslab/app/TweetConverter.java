package pl.coderslab.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

import pl.coderslab.entities.Tweet;
import pl.coderslab.repositories.TweetRepository;

public class TweetConverter implements Converter<String, Tweet>{
	
@Autowired TweetRepository tweetRepository;
	
	@Override
	public Tweet convert(String source) {
		Tweet tweet = tweetRepository.findFirstById(Integer.parseInt(source));
		return tweet;
	}
	

}
