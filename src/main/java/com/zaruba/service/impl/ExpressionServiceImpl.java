package com.zaruba.service.impl;

import com.zaruba.entity.Expression;
import com.zaruba.repository.ExpressionRepository;
import com.zaruba.service.ExpressionService;

import java.util.List;

public class ExpressionServiceImpl implements ExpressionService {

    private final ExpressionRepository expressionRepository;

    public ExpressionServiceImpl(ExpressionRepository expressionRepository) {
        this.expressionRepository = expressionRepository;
    }

    public void save(Expression expression) {
        expressionRepository.save(expression);
    }

    public void update(Expression expression) {
        expressionRepository.update(expression);
    }

    public List<Expression> findAll() {
        return expressionRepository.findAll();
    }

    public Expression findResultById(Long id) {
        return expressionRepository.findResultById(id);
    }

    public List<Expression> findEqualResult(Double condition) {
        return expressionRepository.findEqualResult(condition);
    }

    public List<Expression> findLessResults(Double condition) {
        return expressionRepository.findLessResults(condition);
    }

    public List<Expression> findGreaterResults(Double condition) {
        return expressionRepository.findGreaterResults(condition);
    }

}