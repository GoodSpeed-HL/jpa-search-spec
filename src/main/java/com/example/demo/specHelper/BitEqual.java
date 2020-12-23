package com.example.demo.specHelper;

import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.utils.Converter;
import net.kaczmarzyk.spring.data.jpa.utils.QueryContext;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class BitEqual<T> extends Equal<T> {

    private Converter converter;

    public BitEqual(QueryContext queryContext, String path, String[] httpParamValues, Converter converter) {
        super(queryContext, path, httpParamValues, converter);
        this.converter = converter;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        Class<?> typeOnPath = path(root).getJavaType();
        if(expectedValue.charAt(0) == '!'){
            expectedValue = expectedValue.substring(1, expectedValue.length());
            return cb.notEqual(path(root), this.converter.convert(expectedValue, typeOnPath));
        }
        else{
            return cb.equal(path(root), this.converter.convert(expectedValue, typeOnPath));
        }

    }
}
