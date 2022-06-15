package com.zaruba.util;

import com.zaruba.entity.Expression;
import com.zaruba.service.ExpressionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.regex.Pattern;

import static com.zaruba.util.Constants.*;

public class Logic {

    private final ExpressionService expressionService;
    private final Logger logger = LoggerFactory.getLogger(Logic.class);

    public Logic(ExpressionService expressionService) {
        this.expressionService = expressionService;
    }


    public Expression createExpression(String string) {
        logger.info("Creating expression " + string);
        validate(string);
        Expression expression = new Expression(string, findResult(string));
        countNumbers(string);
        expressionService.save(expression);
        logger.info("Created " + expression.toString());
        return expression;
    }

    public List<Expression> findAllExpression() {
        return expressionService.findAll();
    }

    public Double updateExpression(String string, Long id) {
        logger.info("Updating expression : " + string + "id : " + id);
        validate(string);
        Expression expression = expressionService.findResultById(id);
        expression.setValue(string);
        expression.setResult(findResult(string));
        countNumbers(string);
        expressionService.update(expression);
        logger.info("Updated expression : " + expression.toString());
        return expression.getResult();
    }

    private Double findResult(String expression) {
        return new Calculation().getResult(getAllValues(expression));
    }

    private void validate(String expression) {
        new Validation().isNullOrEmpty(expression);
        new Validation().validateParenthesis(expression);
        new Validation().validateOperators(getAllValues(expression));
    }

    private String[] getAllValues(String expression) {
        logger.info("Building array of strings from " + expression);
        return new Validation().getAllValues(expression,
                Pattern.compile(REGEXP_NUMBER + OR + REGEXP_ALL_OPERATOR).matcher(expression));
    }

    private void countNumbers(String expression) {
        long count = Pattern.compile(REGEXP_NUMBER).matcher(expression).results().count();
        logger.info("Count of numbers in expression : " + count);
        System.out.println("Count of numbers in expression : " + count);
    }


}
