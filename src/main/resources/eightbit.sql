DELIMITER $$
CREATE TRIGGER autoCountDownComment
    BEFORE DELETE ON `core`
    FOR EACH ROW
BEGIN
    DECLARE masterTemp VARCHAR(50);
 		DECLARE regdateTemp datetime;
		DECLARE depthTemp int unsigned;
		DECLARE contentTypeTemp varchar(50);


    SELECT original_author, original_regdate into masterTemp, regdateTemp from `comment` where `author`=OLD.register and `regdate`=OLD.regdate;
    SET depthTemp=OLD.depth;
		SET contentTypeTemp =OLD.contentType;


 		if depthTemp=2 then
 			if contentTypeTemp='free' then
    update `article` set `reply_count`=`reply_count`-1 where `writer`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
    elseif contentTypeTemp='strategy' then
    update `article` set `reply_count`=`reply_count`-1 where `writer`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
    elseif contentTypeTemp='question' then
    update `article` set `reply_count`=`reply_count`-1 where `writer`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
    elseif contentTypeTemp='notice' then
    update `article` set `reply_count`=`reply_count`-1 where `writer`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
    elseif contentTypeTemp='indie' then
    update `game` set `reply_count`=`reply_count`-1 where `developer`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
    elseif contentTypeTemp='official' then
    update `game` set `reply_count`=`reply_count`-1 where `developer`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
    elseif contentTypeTemp='coupone' then
    update `shop` set `reply_count`=`reply_count`-1 where `producer`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
    elseif contentTypeTemp='goods' then
    update `shop` set `reply_count`=`reply_count`-1 where `producer`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
end if;
end if;
END $$
DELIMITER ;

    DELIMITER $$
CREATE TRIGGER autoCountDownFileCount
    AFTER DELETE ON `fileStore`
    FOR EACH ROW
BEGIN
    DECLARE uploaderTemp VARCHAR(50);
		DECLARE regdateTemp datetime;
		DECLARE contentTypeTemp varchar(50);
		DECLARE storeTypeTemp varchar(50);
		DECLARE depthTemp int unsigned;

 		SET uploaderTemp = OLD.uploader;
 		SET regdateTemp = OLD.regdate;
		SET contentTypeTemp = OLD.contentType;
		SET storeTypeTemp=OLD.storeType;
		SET depthTemp=OLD.depth;

         if storeTypeTemp='attach' then
 			if contentTypeTemp='free' then
    update `article` set `attach_count`=`attach_count`-1 where `writer`=uploaderTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
    elseif contentTypeTemp='strategy' then
    update `article` set `attach_count`=`attach_count`-1 where `writer`=uploaderTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
    elseif contentTypeTemp='question' then
    update `article` set `attach_count`=`attach_count`-1 where `writer`=uploaderTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
    elseif contentTypeTemp='notice' then
    update `article` set `attach_count`=`attach_count`-1 where `writer`=uploaderTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
end if;
elseif storeTypeTemp='pcGame' then
update `game` set `pcGameCount`=`pcGameCount`-1 where `developer`=uploaderTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
elseif storeTypeTemp='mobileGame' then
update `game` set `mobileGameCount`=`mobileGameCount`-1 where `developer`=uploaderTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
elseif storeTypeTemp='gameImage' then
update `game` set `imgCount`=`imgCount`-1 where `developer`=uploaderTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
elseif storeTypeTemp='gameBanner' then
update `game` set `bannerCount`=`bannerCount`-1 where `developer`=uploaderTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
end if;

END $$
DELIMITER ;

DELIMITER $$
CREATE TRIGGER autoCountUpFileCount
    AFTER INSERT ON `fileStore`
    FOR EACH ROW
BEGIN
    DECLARE uploaderTemp VARCHAR(50);
		DECLARE regdateTemp datetime;
		DECLARE contentTypeTemp varchar(50);
		DECLARE storeTypeTemp varchar(50);
		DECLARE depthTemp int unsigned;

 		SET uploaderTemp = NEW.uploader;
 		SET regdateTemp = NEW.regdate;
		SET contentTypeTemp = NEW.contentType;
		SET storeTypeTemp=NEW.storeType;
		SET depthTemp=NEW.depth;

         if storeTypeTemp='attach' then
 			if contentTypeTemp='free' then
    update `article` set `attach_count`=`attach_count`+1 where `writer`=uploaderTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
    elseif contentTypeTemp='strategy' then
    update `article` set `attach_count`=`attach_count`+1 where `writer`=uploaderTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
    elseif contentTypeTemp='question' then
    update `article` set `attach_count`=`attach_count`+1 where `writer`=uploaderTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
    elseif contentTypeTemp='notice' then
    update `article` set `attach_count`=`attach_count`+1 where `writer`=uploaderTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
end if;
elseif storeTypeTemp='pcGame' then
update `game` set `pcGameCount`=`pcGameCount`+1 where `developer`=uploaderTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
elseif storeTypeTemp='mobileGame' then
update `game` set `mobileGameCount`=`mobileGameCount`+1 where `developer`=uploaderTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
elseif storeTypeTemp='gameImage' then
update `game` set `imgCount`=`imgCount`+1 where `developer`=uploaderTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
elseif storeTypeTemp='gameBanner' then
update `game` set `bannerCount`=`bannerCount`+1 where `developer`=uploaderTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
end if;

END $$
DELIMITER ;