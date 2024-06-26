package com.microservice.quiz.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.microservice.quiz.client.QuestionClient;
import com.microservice.quiz.dao.QuizDAO;
import com.microservice.quiz.entity.Question;
import com.microservice.quiz.entity.QuestionWrapper;
import com.microservice.quiz.entity.Quiz;
import com.microservice.quiz.entity.QuizResponse;

@Service
public class QuizServiceImpl implements QuizService {

	@Autowired
	private QuizDAO quizDAO;
	
	@Autowired
	private QuestionClient questionClient;


	@Override
	public Quiz createNewQuiz(String title, String difficultyLevel, String category, int noOfQue) {
		List<Long> questions= this.questionClient.findRandomQuestions(category,difficultyLevel,noOfQue);// = //this.questionDAO.findRandomQuestions(category, difficultyLevel, noOfQue);
		Quiz quiz = new Quiz();
		quiz.setQuizTitle(title);
		quiz.setQuestionsID(questions);
		quiz = this.quizDAO.save(quiz);
		return quiz;
	}

	@Override
	public List<QuestionWrapper> getQuizByID(long quizId) {
		Quiz quiz = this.quizDAO.findById(quizId).get();
		List<Long> questionIDs = quiz.getQuestionsID();
		List <Question> questionFromQuestionService= this.questionClient.getQuestionByID(questionIDs);
		
		
		List<QuestionWrapper> que = new ArrayList<>();
		for (Question question : questionFromQuestionService) {
			que.add(new QuestionWrapper(question.getQuestionId(), question.getQuestionTitle(), question.getOption1(),
					question.getOption2(), question.getOption3(), question.getOption4()));

		}
		System.out.println(quiz);

		return que;
	}

	@Override
	public int calcResult(List<QuizResponse> quizResponse) {
		int result = 0;
		List<Long> quizResponseQuestionIDList = new ArrayList<>();
		for (QuizResponse res : quizResponse) {
			quizResponseQuestionIDList.add(res.getQuestionID());
		}

		List<Question> questionFromDB = this.questionClient.getQuestionByID(quizResponseQuestionIDList);
		
		

		for (QuizResponse res : quizResponse) {
			for (Question que : questionFromDB) {
				if (res.getQuestionID() == que.getQuestionId()) {
					if (res.getResponse().equals(que.getCorrectAnswer())) {
						result++;
					}
				}

			}

		}

		return result;
	}
}
