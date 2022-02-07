package com.cerebra.translator.config;

import org.hibernate.dialect.SQLServer2012Dialect;

import java.sql.Types;

public class UnicodeSQLServerDialect extends SQLServer2012Dialect {

    public UnicodeSQLServerDialect() {
        super();
        // Use Unicode Characters
        registerColumnType(Types.VARCHAR, 255, "nvarchar($l)");
        registerColumnType(Types.CHAR, "nchar(1)");
        registerColumnType(Types.CLOB, "nvarchar(max)");

        // Microsoft SQL Server 2000 supports bigint and bit
        registerColumnType(Types.BIGINT, "bigint");
        registerColumnType(Types.BIT, "bit");
    }
}
