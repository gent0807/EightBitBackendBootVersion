package com.eightbit.controller.comment.article;

import com.eightbit.impl.token.TokenManager;
import com.eightbit.persistence.comment.article.FreeCommentRepository;
import com.eightbit.util.file.FolderAndFileManger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("*")
@RestController
@RequestMapping(value = "/Comments/free/*")
@RequiredArgsConstructor
@Slf4j
public class StrategyCommentController {
    private final StrategyCommentRepository strategyCommentRepository;

    private final TokenManager tokenManager;

    private final FolderAndFileManger folderAndFileManger;
}
