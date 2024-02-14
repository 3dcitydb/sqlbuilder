module org.citydb.sqlbuilder {
    requires transitive java.sql;

    exports org.citydb.sqlbuilder;
    exports org.citydb.sqlbuilder.common;
    exports org.citydb.sqlbuilder.function;
    exports org.citydb.sqlbuilder.join;
    exports org.citydb.sqlbuilder.literal;
    exports org.citydb.sqlbuilder.operation;
    exports org.citydb.sqlbuilder.query;
    exports org.citydb.sqlbuilder.schema;
    exports org.citydb.sqlbuilder.update;
    exports org.citydb.sqlbuilder.util;
}