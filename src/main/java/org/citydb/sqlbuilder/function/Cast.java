package org.citydb.sqlbuilder.function;

import org.citydb.sqlbuilder.common.Expression;
import org.citydb.sqlbuilder.common.SqlVisitor;
import org.citydb.sqlbuilder.query.Selection;
import org.citydb.sqlbuilder.schema.ColumnExpression;

import java.util.Objects;
import java.util.Optional;

public class Cast implements ColumnExpression, Selection<Cast> {
    private final Expression expression;
    private final String targetType;
    private String alias;

    private Cast(Expression expression, String targetType) {
        this.expression = Objects.requireNonNull(expression, "The expression must not be null.");
        this.targetType = Objects.requireNonNull(targetType, "The target type must not be null.");
    }

    public static Cast of(Expression expression, String targetType) {
        return new Cast(expression, targetType);
    }

    public Expression getExpression() {
        return expression;
    }

    public String getTargetType() {
        return targetType;
    }

    @Override
    public Optional<String> getAlias() {
        return Optional.ofNullable(alias);
    }

    @Override
    public Cast as(String alias) {
        this.alias = alias;
        return this;
    }

    @Override
    public void accept(SqlVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return toSql();
    }
}
