package com.aliyun.kms.secretsmanager;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

public class C3p0DataSourceTest {
    @Test
    public void testC3p0DataSource() throws SQLException {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        Connection connection = dataSource.getConnection();
        System.out.println(connection);
    }
}
