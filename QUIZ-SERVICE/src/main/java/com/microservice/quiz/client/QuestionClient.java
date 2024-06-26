package com.microservice.quiz.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.microservice.quiz.entity.Question;


//@FeignClient(url = "http://localhost:8091",value = "Question-Client")
@FeignClient("QUESTION-SERVICE")
//@FeignClient(name ="QUESTION-SERVICE")
public interface QuestionClient {
	
	@GetMapping("/question/getRandomQuestions")
	public List<Long> findRandomQuestions(@RequestParam("category") String category,
			@RequestParam("difficultyLevel") String difficultyLevel, @RequestParam("noOfQuestions") int noOfQuestions);
	
	@PostMapping("/question/getquestion")
	public List<Question> getQuestionByID(@RequestBody List<Long> questionIds);

}
