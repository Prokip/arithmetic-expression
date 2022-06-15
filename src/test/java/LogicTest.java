import com.zaruba.entity.Expression;
import com.zaruba.exception.ExpressionFormatException;
import com.zaruba.repository.impl.ExpressionRepositoryImpl;
import com.zaruba.service.impl.ExpressionServiceImpl;
import com.zaruba.util.Logic;
import com.zaruba.util.Validation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static com.zaruba.util.Constants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;


public class LogicTest {

    final EntityManagerFactory FACTORY = Persistence.createEntityManagerFactory("primary");
    final EntityManager MANAGER = FACTORY.createEntityManager();

    Logic logic;

    @BeforeEach
    public void run() {
        logic = new Logic(new ExpressionServiceImpl(new ExpressionRepositoryImpl(MANAGER)));
    }


    @ParameterizedTest
    @MethodSource("expressionsWithParenthesis")
    void validateParenthesis(String string) {
        Throwable exception = assertThrows(ExpressionFormatException.class,
                () -> new Validation().validateParenthesis(string));
        assertEquals("Not valid expression with parenthesis", exception.getMessage());
    }

    static Stream<Arguments> expressionsWithParenthesis() {
        return Stream.of(
                arguments("20.25+(55.3-3))+(200+20)/(4*-5)"),
                arguments("(20.25+((55.3-3)+(200+20)/(4*-5)"),
                arguments("(20.25+(((55.3-3)+(200+20)/(4*-5)"),
                arguments("20.25+)(+(55.3-3)+(200+20)/(4*-5)")
        );
    }

    @Test
    public void validParenthesis() {
        assertDoesNotThrow(() -> new Validation().validateParenthesis("20.25+(55.3-3)+(200+20)/(4*-5)"));
    }

    @ParameterizedTest
    @MethodSource("expressionsWithOperators")
    void validateOperators(String expression) {
        Throwable exception = assertThrows(ExpressionFormatException.class,
                () -> new Validation().validateOperators(new Validation().getAllValues(expression,
                        Pattern.compile(REGEXP_NUMBER + OR + REGEXP_ALL_OPERATOR).matcher(expression))));
        assertEquals("Not valid expression with operators", exception.getMessage());
    }

    static Stream<Arguments> expressionsWithOperators() {
        return Stream.of(
                arguments("20.25++(55.3-3)+(200+20)/(4*5)"),
                arguments("20.25--(55.3-3)+(200+20)/(4*5)"),
                arguments("20.25+(55.3-3)+(200+20)//(4*5)"),
                arguments("20.25+(55.3-3)+(200+20)/(4*/5)"),
                arguments("20.25+(55.3-3)+(200+20)/(4**5)")
        );
    }

    @Test
    public void validOperatorPlusMinus() {
        assertDoesNotThrow(() -> new Validation().validateParenthesis("20.25+(55.3+-3)+(200+20)/(4*5)"));
    }

    @Test
    public void validOperatorsWithMinus() {
        assertDoesNotThrow(() -> new Validation().validateParenthesis("20.25+(55.3-3)+(200+20)/(4*-5)"));
    }

    @Test
    public void calculateDivideByZero() {
        Throwable exception = assertThrows(ExpressionFormatException.class,
                () -> logic.createExpression("20.25+(55.3-3)+(200+20)/0"));
        assertEquals("Not valid expression", exception.getMessage());
    }

    @Test
    public void calculateDivideZero() {
        Throwable exception = assertThrows(ExpressionFormatException.class,
                () -> logic.createExpression("20.25+(55.3-3)+0/(4*5)"));
        assertEquals("Not valid expression", exception.getMessage());
    }

    @ParameterizedTest
    @MethodSource("expressionsWithResults")
    void calculateSuccess(String string, Double result) {
        Expression expression = logic.createExpression(string);
        assertEquals(result, expression.getResult());
    }

    static Stream<Arguments> expressionsWithResults() {
        return Stream.of(
                arguments("20.5+(55+3)+(200+20)/(4*-5)", 67.5),
                arguments("20.25+(55.3+-3)+(200+20)/(4*5)", 83.55),
                arguments("(200/20)*-5", -50.0),
                arguments("20-55+3+200+20-9", 179.0),
                arguments("20+(50+10)-200/40", 75.0),
                arguments("20+((50-10)+200)", 260.0),
                arguments("20-(2+1)", 17.0)
        );
    }

    @Test
    public void notValidId() {
        Throwable exception = assertThrows(ExpressionFormatException.class,
                () -> logic.updateExpression("20-(2+1)", 125L));
        assertEquals("No expression with this id", exception.getMessage());
    }
}
