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