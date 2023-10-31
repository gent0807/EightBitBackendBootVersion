package com.eightbit.controller.game;


import com.eightbit.entity.article.Article;
import com.eightbit.entity.game.Game;
import com.eightbit.entity.read.Read;
import com.eightbit.impl.token.TokenManager;
import com.eightbit.persistence.article.ArticleRepository;
import com.eightbit.persistence.game.GameRepository;
import com.eightbit.util.file.FolderAndFileManger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/Games/*")
@RequiredArgsConstructor
@Slf4j
@Primary
public class GameController {
    private final GameRepository gameRepository;

    private final TokenManager tokenManager;

    private final FolderAndFileManger folderAndFileManger;

    @GetMapping(value = "/games")
    public ResponseEntity<List<Game>> getGameList(@RequestParam String contentType){
        return ResponseEntity.ok().body(gameRepository.getGameList(contentType));
    }

    @GetMapping(value="/games/official")
    public ResponseEntity<List<String>> getOfficialGames(){
        return ResponseEntity.ok().body(gameRepository.getOfficialGameList());
    }

    @GetMapping(value="/game")
    public ResponseEntity<Game> getGame(@RequestParam String viewer, @RequestParam String developer, @RequestParam String regdate,
                                              @RequestParam String contentType, Read read){
        read.setReader(viewer);
        read.setMaster(developer);
        read.setRegdate(regdate);
        read.setContentType(contentType);
        return ResponseEntity.ok().body(gameRepository.getGame(read));
    }

    @GetMapping(value="/developer/games")
    public ResponseEntity<List<Game>> getDeveloperGames(@RequestParam String developer, @RequestParam String contentType, Game game){
        game.setDeveloper(developer);
        game.setContentType(contentType);
        return ResponseEntity.ok().body(gameRepository.getDeveloperGames(game));
    }


    @PostMapping(value = "/game")
    @Transactional
    public ResponseEntity<Game> insertGame(HttpServletRequest request, String token, @RequestBody Game game){

        if(tokenManager.checkAccessToken(request, token, game.getDeveloper())){
            return ResponseEntity.ok().body(gameRepository.registerGame(game));
        }
        return ResponseEntity.status(HttpStatus.resolve(403)).body(null);
    }

    @PatchMapping(value="/game/{contentType}/{developer}/{regdate}")
    @Transactional
    public void updateGame(HttpServletRequest request, String token, @PathVariable String contentType, @PathVariable String developer, @PathVariable String regdate, @RequestBody Game game){
        if(tokenManager.checkAccessToken(request,token, developer)){
            game.setDeveloper(developer);
            game.setRegdate(regdate);
            game.setContentType(contentType);
            gameRepository.modifyGame(game);
        }
    }

    @DeleteMapping(value = "/game/{contentType}/{developer}/{regdate}/{role}")
    @Transactional
    public void deleteArticle(HttpServletRequest request, String token,
                              @PathVariable("developer") String developer, @PathVariable("regdate") String regdate,
                              @PathVariable("role") String role, @PathVariable("contentType") String contentType, Game game){
        if(tokenManager.checkAccessToken(request,  token, developer)|| role.equals("ADMIN")){
            game.setDeveloper(developer);
            game.setRegdate(regdate);
            game.setContentType(contentType);
            gameRepository.removeGame(game);
            folderAndFileManger.removeFilesAndFolder(game.getDeveloper(), game.getRegdate(),contentType, 1);
        }
    }

}
