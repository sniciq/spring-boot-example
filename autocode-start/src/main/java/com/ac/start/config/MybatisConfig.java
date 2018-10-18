package com.ac.start.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "com.ac.dao.mapper.demo", sqlSessionFactoryRef = "sqlSessionFactoryDB")
public class MybatisConfig {

    @Bean(name = "sqlSessionFactoryDB")
    public SqlSessionFactory sqlSessionFactoryDB(
            @Qualifier("dataSourceDB") DataSource dataSource,
            @Value("classpath:com/ac/dao/mapper/demo/**/*.xml") Resource[] resources
    ) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setMapperLocations(resources);
        return sqlSessionFactoryBean.getObject();
    }

    @Bean(name = "sqlSessionTemplateDB")
    public SqlSessionTemplate sqlSessionTemplateDB(@Qualifier("sqlSessionFactoryDB") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
