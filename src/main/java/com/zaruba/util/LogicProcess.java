package com.zaruba.util;

import com.zaruba.exception.ExpressionFormatException;
import com.zaruba.repository.impl.ExpressionRepositoryImpl;
import com.zaruba.service.ExpressionService;
import com.zaruba.service.impl.ExpressionServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Scanner;

public class LogicProcess {

    private static EntityManagerFactory FACTORY;
    private static EntityManager MANAGER;

    private final Logger logger = LoggerFactory.getLogger(LogicProcess.class);

    public void execute() {

        try (Scanner scanner = new Scanner(System.in)) {

            FACTORY = Persistence.createEntityManagerFactory("primary");
            MANAGER = FACTORY.createEntityManager();
            ExpressionService expressionService = new ExpressionServiceImpl(new ExpressionRepositoryImpl(MANAGER));

            Logic logic = new Logic(expressionService);

            System.out.println("Enter expression : ");
            logic.createExpression(scanner.nextLine());

            System.out.println("Find all expressions :");
            System.out.println(logic.findAllExpression());

            System.out.println("Enter id expression to modify :");
            Long id = scanner.nextLong();

            System.out.println("Enter expression to modify");
            String expression = scanner.next();
            System.out.println("Modify result:");
            System.out.println(logic.updateExpression(expression, id));

            System.out.println("Enter result to find: ");
            System.out.println(expressionService.findEqualResult(scanner.nextDouble()));

            System.out.println("Enter result to find expression with less than :");
            System.out.println(expressionService.findLessResults(scanner.nextDouble()));

            System.out.println("Enter result to find expression with greater than :");
            System.out.println(expressionService.findGreaterResults(scanner.nextDouble()));

        } catch (ExpressionFormatException exception) {
            logger.error("Program failed : ", exception);
            System.out.println(exception.getMessage());
        } catch (Exception exception) {
            logger.error("Program failed : ", exception);
            System.out.println("Something went wrong");
        } finally {
            MANAGER.close();
            FACTORY.close();
        }
    }


}
