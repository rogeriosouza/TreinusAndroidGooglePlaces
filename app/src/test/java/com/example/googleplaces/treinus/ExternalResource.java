package com.example.googleplaces.treinus;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public  abstract class ExternalResource implements TestRule {

    @Override
    public Statement apply(Statement base, Description description) {

        return statement(base);
    }

    private Statement statement(final Statement base) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                before();
                try {
                    base.evaluate();
                } finally {
                    after();
                }
            }
        };
    }

    protected void before() throws Throwable {
        // do nothing
    }


    protected void after() {
        // do nothing
    }
}
