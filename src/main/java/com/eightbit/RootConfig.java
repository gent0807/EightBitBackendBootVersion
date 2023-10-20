package com.eightbit;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
@ComponentScan
@PropertySource("classpath:database.properties")
public class RootConfig {

        @Autowired
        Environment env;

        @Autowired
        ApplicationContext applicationContext;

        @Bean
        public BCryptPasswordEncoder encoder() {
            return new BCryptPasswordEncoder();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
            return PasswordEncoderFactories.createDelegatingPasswordEncoder();
        }

//        @Bean
//        public DataSource dataSource(){
//        BasicDataSource basicDataSource=new BasicDataSource();
//            basicDataSource.setDriverClassName(env.getProperty("jdbc.driver"));
//            basicDataSource.setUrl(env.getProperty("jdbc.url"));
//            basicDataSource.setUsername(env.getProperty("jdbc.username"));
//            basicDataSource.setPassword(env.getProperty("jdbc.password"));
//
//            return  basicDataSource;
//    }
//        @Bean
//        public JdbcTemplate jdbcTemplate(){
//            JdbcTemplate jdbcTemplate=new JdbcTemplate();
//            jdbcTemplate.setDataSource(dataSource());
//            return jdbcTemplate;
//        }

//        @Bean(name="txManager")
//        public DataSourceTransactionManager txManager(){
//            return new DataSourceTransactionManager(dataSource());
//        }



        @Bean
        public DataSource hikariDataSource(){
            HikariConfig hikariConfig=new HikariConfig();
            hikariConfig.setDriverClassName(env.getProperty("jdbc.driver"));
            hikariConfig.setJdbcUrl(env.getProperty("jdbc.url"));
            hikariConfig.setUsername(env.getProperty("jdbc.username"));
            hikariConfig.setPassword(env.getProperty("jdbc.password"));

            HikariDataSource dataSource=new HikariDataSource(hikariConfig); //DataSource 객체 생성
            return  dataSource;
        }



        @Bean
        public SqlSessionFactory sqlSessionFactory() throws Exception{
            SqlSessionFactoryBean sqlSessionFactory= new SqlSessionFactoryBean();
            sqlSessionFactory.setDataSource(hikariDataSource());
            sqlSessionFactory.setConfigLocation(applicationContext.getResource("classpath:mybatis-config.xml"));
            sqlSessionFactory.setMapperLocations(applicationContext.getResources("classpath:/mappings/*.xml"));
            return (SqlSessionFactory) sqlSessionFactory.getObject();
        }

        @Bean
        public SqlSessionTemplate sqlSessionTemplate() throws Exception
        {
            SqlSessionTemplate sqlSessionTemplate=new SqlSessionTemplate(sqlSessionFactory());
            return sqlSessionTemplate;
        }



}
