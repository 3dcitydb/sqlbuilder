package org.citydb.sqlbuilder.expression;

import java.util.List;

public class LiteralSelectExpression implements SubQueryExpression {
    private final String select;

    public LiteralSelectExpression(String select) {
        this.select = select;
    }

    @Override
    public void getInvolvedPlaceHolders(List<PlaceHolder<?>> statements) {
        // nothing to do
    }

    @Override
    public String toString() {
        return select;
    }
}
