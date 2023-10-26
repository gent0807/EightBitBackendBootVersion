package com.eightbit.impl.article;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Primary
@PropertySource("classpath:upload.properties")
public class QuestionArticleServiceImpl {
}
