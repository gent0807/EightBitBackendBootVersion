package com.eightbit.persistence.game;

import com.eightbit.entity.article.Article;
import com.eightbit.entity.game.Game;
import com.eightbit.entity.read.Read;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class GameRepository {

    private final SqlSessionTemplate mybatis;


    public List<Game> getGameList(String contentType){
        return mybatis.selectList("GameMyBatisDAO.getGameList", contentType);
    }

    public List<Game> getDeveloperGames(Game game){
        return mybatis.selectList("GameMyBatisDAO.getDeveloperGames", game);
    }

    public Game getGame(Read read) {

        if (!read.getReader().isEmpty()) {

            Integer userArticleViewCount = mybatis.selectOne("ReadMyBatisDAO.getView", read);

            if (userArticleViewCount.intValue() > 0) {
                mybatis.update("ReadMyBatisDAO.updateView", read);
            } else if (userArticleViewCount.intValue() == 0) {
                mybatis.insert("ReadMyBatisDAO.insertView", read);
            }
        }

        mybatis.update("GameMyBatisDAO.updateGameVisitCnt", read);

        return mybatis.selectOne("GameMyBatisDAO.getGame", read);
    }

    public void modifyGame(Game game) {
        mybatis.update("GameMyBatisDAO.updateGame", game);
    }

    public Game registerGame(Game game){
        mybatis.update("UserMyBatisDAO.updatePointByArticle", game);
        mybatis.insert("GameMyBatisDAO.insertGame", game);
        return  mybatis.selectOne("GameMyBatisDAO.findDeveloperAndRegdate",mybatis.selectOne("GameMyBatisDAO.selectSeqOfDeveloper", game));
    }

    public boolean removeGame(Game game){
        mybatis.delete("GameMyBatisDAO.deleteGame", game);
        return true;
    }
}
