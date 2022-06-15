package com.zaruba.util;

import com.zaruba.exception.ExpressionFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.zaruba.util.Constants.*;

public class Validation {

    private final Logger logger = LoggerFactory.getLogger(Validation.class);

    public void validateParenthesis(String expression) {
        logger.info("Validate parenthesis");
        String[] allParenthesis = getAllValues(expression, Pattern.compile(REGEXP_PARENTHESIS).matcher(expression));
        Stack<String> stack = new Stack<>();
        checkingAllPosition(allParenthesis, stack);
        if (!stack.empty()) {
            logger.error("Not valid parenthesis" + expression);
            throw new ExpressionFormatException("Not valid expression with parenthesis");
        }
    }

    public String[] getAllValues(String expression, Matcher matcher) {
        String[] values = new String[expression.length()];
        int i = 0;
        while (matcher.find()) {
            values[i] = matcher.group().trim();
            i++;
        }
        return Arrays.stream(values)
                .filter(Objects::nonNull)
                .toArray(String[]::new);
    }

    public void validateOperators(String[] strArray) {
        logger.info("Validate operators");
        List<String> operators = Arrays.asList(MINUS_MULTIPLY, MINUS_DIVIDE, MINUS_PLUS);
        for (int i = 1; i < strArray.length - 1; i++) {
            if (isTwoOperators(strArray, i) || operators.contains(strArray[i])) {
                logger.error("Not valid operators " + Arrays.asList(strArray).toString());
                throw new ExpressionFormatException("Not valid expression with operators");
            }
        }
    }

    private boolean isTwoOperators(String[] strArray, int i) {
        return strArray[i].matches(REGEXP_OPERATOR) && strArray[i - 1].matches(REGEXP_OPERATOR);
    }

    private void checkingAllPosition(String[] allParenthesis, Stack<String> stack) {
        for (String parenthesis : allParenthesis) {
            if (OPENED_PARENTHESIS.equals(parenthesis)) {
                stack.push(parenthesis);
            } else {
                if (stack.empty()) {
                    logger.error("Closed parenthesis goes before opened");
                    throw new ExpressionFormatException("Not valid expression with parenthesis");
                } else {
                    String top = stack.peek();
                    if (CLOSED_PARENTHESIS.equals(parenthesis) && OPENED_PARENTHESIS.equals(top)) {
                        stack.pop();
                    } else {
                        logger.error("Closed parenthesis doesnt much opened");
                        throw new ExpressionFormatException("Not valid expression with parenthesis");
                    }
                }
            }
        }
    }

    public void isNullOrEmpty(String string) {
        if(string == null || string.trim().isEmpty()){
            throw new ExpressionFormatException("Expression cant be empty");
        }
    }


}



