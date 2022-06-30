package com.zaruba.repository.impl;

import com.zaruba.entity.Expression;
import com.zaruba.exception.ExpressionFormatException;
import com.zaruba.repository.ExpressionRepository;

import javax.persistence.EntityManager;
import java.util.List;

public class ExpressionRepositoryImpl implements ExpressionRepository {

    private final EntityManager manager;

    public ExpressionRepositoryImpl(EntityManager entityManager) {
        this.manager = entityManager;
    }

    @Override
    public void save(Expression expression) {
        manager.getTransaction().begin();
        manager.persist(expression);
        manager.getTransaction().commit();
    }

    @Override
    public void update(Expression expression) {
        manager.getTransaction().begin();
        manager.merge(expression);
        manager.getTransaction().commit();
    }

    @Override
    public List<Expression> findAll() {
        return manager.createQuery("select e from Expression e", Expression.class).getResultList();
    }

    @Override
    public Expression findResultById(Long id) {
        return manager.createQuery("select e from Expression e where e.id = :id", Expression.class)
                .setParameter("id", id).getResultList().stream()
                .findFirst()
                .orElseThrow(() -> new ExpressionFormatException("No expression with this id"));
    }

    @Override
    public List<Expression> findEqualResult(Double condition) {
        return manager.createQuery("select e from Expression e where e.result = :filter", Expression.class)
                .setParameter("filter", condition)
                .getResultList();
    }

    @Override
    public List<Expression> findLessResults(Double condition) {
        return manager.createQuery("select e from Expression e where e.result < :filter", Expression.class)
                .setParameter("filter", condition)
                .getResultList();
    }

    @Override
    public List<Expression> findGreaterResults(Double condition) {
        return manager.createQuery("select e from Expression e where e.result > :filter", Expression.class)
                .setParameter("filter", condition)
                .getResultList();
    }


}
