package com.zaruba.service;

import com.zaruba.entity.Expression;

import java.util.List;

public interface ExpressionService {

    void save(Expression expression);

    void update(Expression expression);

    List<Expression> findAll();

    Expression findResultById(Long id);

    List<Expression> findEqualResult(Double condition);

    List<Expression> findLessResults(Double condition);

    List<Expression> findGreaterResults(Double condition);

}
