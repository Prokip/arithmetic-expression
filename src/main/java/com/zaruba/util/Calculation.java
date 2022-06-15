package com.zaruba.util;

import com.zaruba.exception.ExpressionFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Stack;

import static com.zaruba.util.Constants.*;

public class Calculation {

    private final Logger logger = LoggerFactory.getLogger(Calculation.class);

    public Double getResult(String[] expression) {
        Stack<String> operators = new Stack<>();
        Stack<Double> values = new Stack<>();
        separateNumbersAndOperators(expression, operators, values);
        while (!operators.empty()) {
            calculation(operators, values);
        }
        return values.pop();
    }

    private void separateNumbersAndOperators(String[] expression, Stack<String> operators, Stack<Double> values) {
        for (String item : expression) {
            if (!EMPTY_STRING.equals(item)) {
                if (!OPENED_PARENTHESIS.equals(item)) {
                    if (PLUS.equals(item) || MULTIPLY.equals(item) || DIVIDE.equals(item) || MINUS.equals(item)) {
                        operators.push(item);
                    } else if (CLOSED_PARENTHESIS.equals(item)) {
                        calculation(operators, values);
                    } else {
                        values.push(Double.parseDouble(item));
                    }
                }
            }
        }
    }

    private void calculation(Stack<String> operators, Stack<Double> values) {
        String operation = operators.pop();
        if (PLUS.equals(operation)) {
            values.push(values.pop() + values.pop());
        } else if (MULTIPLY.equals(operation)) {
            values.push(values.pop() * values.pop());
        } else if (DIVIDE.equals(operation)) {
            dividing(values);
        } else if (MINUS.equals(operation)) {
            Double k = values.pop();
            values.push(values.pop() - k);
        }
        if (operators.empty() && !values.empty()) {
            if (values.size() != 1) {
                operators.push(PLUS);
            }
        }
    }

    private void dividing(Stack<Double> values) {
        Double b = values.pop();
        if (b == 0) {
            logger.error("Zero cannot be divided -> infinity");
            throw new ExpressionFormatException("Not valid expression");
        }
        Double a = values.pop();
        if (a == 0) {
            logger.error("Cannot divide by zero");
            throw new ExpressionFormatException("Not valid expression");
        }
        values.push(a / b);
    }


}



