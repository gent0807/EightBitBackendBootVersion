use `eightbitdb`;


create table `phoneAuth`(
	phoneNum varchar(200) not null primary key,
    authNum varchar(200) not null
);

create table `emailAuth`(
	email varchar(200) not null primary key,
    authNum varchar(2000) not null
);

create table `role` (
	role varchar(20) not null primary key
);

create table `report`(
	report varchar(20) not null primary key
);

create table `fileStoreType`(
	storeType varchar(50) not null,
    primary key(storeType)
);

create table `contentType`(
	contentType varchar(50) not null,
    primary key(contentType)
);

create table `depth`(
	depth int unsigned not null,
    primary key(depth)
);


create table `user`(
	email varchar(200) not null,
    password varchar(200) not null,
    nickname varchar(50) not null,
    role varchar(10) not null,
    profileImgPath varchar(300) not null,
    point int not null default 0,
    accesstoken varchar(300),
    refreshtoken varchar(300),
    primary key (email),
    unique key (nickname),
    foreign key (role) references role(role)
    on update cascade on delete cascade
);

create table `article`(
	seq int not null auto_increment,
    title varchar(200) not null,
    writer varchar(200) not null,
    content varchar(200) not null,
    regdate datetime not null default current_timestamp,
    updatedate datetime not null default current_timestamp,
    visitcnt int not null default 0,
    likecount int not null default 0,
    reply_count int not null default 0,
    attach_count int unsigned not null default 0,
    abuse_report int not null default 0,
    19_report int not null default 0,
    incoporate_report int not null default 0,
    depth int unsigned not null,
    contentType varchar(50) not null,
    primary key(seq),
    unique key(writer, regdate, contentType, depth),
    foreign key(writer) references `user`(nickname)
    on update cascade on delete cascade,
    foreign key(contentType) references `contentType`(contentType) on update cascade,
    foreign key(depth) references `depth`(depth) on update cascade
);

create table `game`(
	seq int not null auto_increment,
    title varchar(200) not null,
    developer varchar(200) not null,
    content varchar(200) not null,
    regdate datetime not null default current_timestamp,
    updatedate datetime not null default current_timestamp,
    visitcnt int not null default 0,
    likecount int not null default 0,
    reply_count int not null default 0,
    attach_count int unsigned not null default 0,
    abuse_report int not null default 0,
    19_report int not null default 0,
    incoporate_report int not null default 0,
    depth int unsigned not null,
    contentType varchar(50) not null,
    primary key(seq),
    unique key(developer, regdate, contentType, depth),
    foreign key(developer) references `user`(nickname)
    on update cascade on delete cascade,
    foreign key(contentType) references `contentType`(contentType) on update cascade,
    foreign key(depth) references `depth`(depth) on update cascade
);

create table `shop`(
	seq int not null auto_increment,
    title varchar(200) not null,
    producer varchar(200) not null,
    content varchar(200) not null,
    regdate datetime not null default current_timestamp,
    updatedate datetime not null default current_timestamp,
    visitcnt int not null default 0,
    likecount int not null default 0,
    reply_count int not null default 0,
    attach_count int unsigned not null default 0,
    abuse_report int not null default 0,
    19_report int not null default 0,
    incoporate_report int not null default 0,
    depth int unsigned not null,
    contentType varchar(50) not null,
    primary key(seq),
    unique key(producer, regdate, contentType, depth),
    foreign key(producer) references `user`(nickname)
    on update cascade on delete cascade,
    foreign key(contentType) references `contentType`(contentType) on update cascade,
    foreign key(depth) references `depth`(depth) on update cascade
);

create table `comment`(
	seq int not null auto_increment,
    original_author varchar(200) not null,
    original_regdate datetime not null,
    author varchar(200) not null,
    content varchar(200) not null,
    regdate datetime not null default current_timestamp,
    updatedate datetime not null default current_timestamp,
    likecount int not null default 0,
    recomment_count int not null default 0,
    abuse_report int not null default 0,
    19_report int not null default 0,
    incoporate_report int not null default 0,
    depth int unsigned not null,
    contentType varchar(50) not null,
    primary key(seq),
    unique key(author, regdate, contentType, depth),
    foreign key(original_author, original_regdate, contentType) references `article`(writer, regdate, contentType)
    on update cascade on delete cascade,
	foreign key(original_author, original_regdate, contentType) references `game`(developer, regdate, contentType)
    on update cascade on delete cascade,
    foreign key(original_author, original_regdate, contentType) references `shop`(producer, regdate, contentType)
    on update cascade on delete cascade,
    foreign key(original_author, original_regdate, contentType) references `comment`(author, regdate, contentType),
    foreign key(author) references `user`(nickname)
    on update cascade
    on delete cascade,
    foreign key(original_author) references `user`(nickname)
    on update cascade
    on delete cascade,
    foreign key(contentType) references `contentType`(contentType) on update cascade,
    foreign key(depth) references `depth`(depth) on update cascade
);



create table `like`(
	liker varchar(50) not null,
    master varchar(50) not null,
    regdate datetime not null default current_timestamp,
    depth int unsigned not null,
	contentType varchar(50) not null,
    foreign key(liker) references `user`(nickname) on update cascade on delete cascade,
    foreign key(master) references `user`(nickname) on update cascade on delete cascade,
    foreign key(master, regdate, contentType, depth) references `article`(writer, regdate, contentType, depth) on update cascade on delete cascade,
    foreign key(master, regdate, contentType, depth) references `game`(developer, regdate, contentType, depth) on update cascade on delete cascade,
    foreign key(master, regdate, contentType, depth) references `shop`(producer, regdate, contentType, depth) on update cascade on delete cascade,
	foreign key(master, regdate, contentType, depth) references `comment`(author, regdate, contentType, depth) on update cascade on delete cascade,
    foreign key(contentType) references `contentType`(contentType) on update cascade,
	foreign key(depth) references `depth`(depth) on update cascade
);



create table `fileStore`(
	id int not null primary key auto_increment,
	uploader varchar(50) not null,
    regdate datetime not null default current_timestamp,
    storeFilename varchar(200) not null,
    uploadFilename varchar(200) not null,
    depth int unsigned not null,
    contentType varchar(50) not null,
    storeType varchar(50) not null,
    foreign key(uploader) references `user`(nickname) on update cascade on delete cascade,
	foreign key(uploader, regdate, contentType, depth) references `article`(writer, regdate, contentType, depth) on update cascade on delete cascade,
    foreign key(uploader, regdate, contentType, depth) references `game`(developer, regdate, contentType, depth) on update cascade on delete cascade,
    foreign key(uploader, regdate, contentType, depth) references `shop`(producer, regdate, contentType, depth) on update cascade on delete cascade,
	foreign key(uploader, regdate, contentType, depth) references `comment`(author, regdate, contentType, depth) on update cascade on delete cascade,
    foreign key(contentType) references `contentType`(contentType) on update cascade,
    foreign key(storeType) references `fileStoreType`(storeType) on update cascade,
	foreign key(depth) references `depth`(depth) on update cascade
);



create table `reportState`(
	reporter varchar(50) not null,
    master varchar(50) not null,
    regdate datetime not null default current_timestamp,
    report varchar(20) not null,
    depth int unsigned not null,
    contentType varchar(50) not null,
    foreign key(reporter) references `user`(nickname) on update cascade on delete cascade,
    foreign key(master) references `user`(nickname) on update cascade on delete cascade,
    foreign key(master, regdate, contentType, depth) references `article`(writer, regdate, contentType, depth) on update cascade on delete cascade,
    foreign key(master, regdate, contentType, depth) references `game`(developer, regdate, contentType, depth) on update cascade on delete cascade,
    foreign key(master, regdate, contentType, depth) references `shop`(producer, regdate, contentType, depth) on update cascade on delete cascade,
	foreign key(master, regdate, contentType, depth) references `comment`(author, regdate, contentType, depth) on update cascade on delete cascade,
    foreign key(report) references `report`(report),
    foreign key(contentType) references `contentType`(contentType) on update cascade ,
	foreign key(depth) references `depth`(depth) on update cascade
);

create table `read`(
	reader varchar(50) not null,
    master varchar(50) not null,
    regdate datetime not null default current_timestamp,
    readDate datetime not null default current_timestamp,
    contentType varchar(50) not null default 'free',
	foreign key(reader) references `user`(nickname) on update cascade on delete cascade,
    foreign key(master) references `user`(nickname) on update cascade on delete cascade,
    foreign key(master, regdate, contentType) references `article`(writer, regdate, contentType) on update cascade on delete cascade,
    foreign key(master, regdate, contentType) references `game`(developer, regdate, contentType) on update cascade on delete cascade,
    foreign key(master, regdate, contentType) references `shop`(producer, regdate, contentType) on update cascade on delete cascade,
	foreign key(contentType) references `contentType`(contentType) on update cascade
);

create table `tagState`(
	id int unsigned not null primary key auto_increment,
	tagger varchar(50) not null,
    regdate datetime not null default current_timestamp,
    tag varchar(80),
    contentType varchar(50) not null,
    foreign key(tagger) references `user`(nickname) on update cascade on delete cascade,
    foreign key(tagger, regdate, contentType) references `article`(writer, regdate, contentType) on update cascade on delete cascade,
    foreign key(tagger, regdate, contentType) references `game`(developer, regdate, contentType) on update cascade on delete cascade,
    foreign key(tagger, regdate, contentType) references `shop`(producer, regdate, contentType) on update cascade on delete cascade,
    foreign key(tag) references `tag`(tag),
    foreign key(contentType) references `contentType`(contentType) on update cascade
);

DELIMITER $$
	CREATE TRIGGER autoCountUpComment
	AFTER  INSERT ON `comment`
	FOR EACH ROW
	BEGIN
		DECLARE masterTemp VARCHAR(50);
		DECLARE regdateTemp datetime;
        DECLARE depthTemp int unsigned;
        DECLARE contentTypeTemp varchar(50);


		SET masterTemp = NEW.original_author;
		SET regdateTemp = NEW.original_regdate;
        SET depthTemp=NEW.depth;
        SET contentTypeTemp =NEW.contentType;

		if depthTemp=2 then
			if contentTypeTemp='free' then
				update `article` set `reply_count`=`reply_count`+1 where `writer`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
			elseif contentTypeTemp='strategy' then
				update `article` set `reply_count`=`reply_count`+1 where `writer`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
			elseif contentTypeTemp='question' then
				update `article` set `reply_count`=`reply_count`+1 where `writer`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
			elseif contentTypeTemp='notice' then
				update `article` set `reply_count`=`reply_count`+1 where `writer`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
			elseif contentTypeTemp='indie' then
				update `game` set `reply_count`=`reply_count`+1 where `developer`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
			elseif contentTypeTemp='official' then
				update `game` set `reply_count`=`reply_count`+1 where `developer`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
			elseif contentTypeTemp='coupone' then
				update `shop` set `reply_count`=`reply_count`+1 where `producer`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
			elseif contentTypeTemp='goods' then
				update `shop` set `reply_count`=`reply_count`+1 where `producer`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
            end if;
		elseif depthTemp=3 then
			update `comment` set `recomment_count`=`recomment_count`+1 where `author`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp and `depth`=2;
		end if;


	END $$
DELIMITER ;

DELIMITER $$
	CREATE TRIGGER autoCountDownComment
	AFTER DELETE ON `comment`
	FOR EACH ROW
	BEGIN
		DECLARE masterTemp VARCHAR(50);
		DECLARE regdateTemp datetime;
        DECLARE depthTemp int unsigned;
        DECLARE contentTypeTemp varchar(50);


		SET masterTemp = OLD.original_author;
		SET regdateTemp = OLD.original_regdate;
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
		elseif depthTemp=3 then
			update `comment` set `recomment_count`=`recomment_count`-1 where `author`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp and `depth`=2;
		end if;


	END $$
DELIMITER ;

DELIMITER $$
	CREATE TRIGGER autoCountUpLike
	AFTER  INSERT ON `like`
	FOR EACH ROW
	BEGIN
		DECLARE likerTemp VARCHAR(50);
        DECLARE masterTemp VARCHAR(50);
		DECLARE regdateTemp datetime;
        DECLARE depthTemp int unsigned;
        DECLARE contentTypeTemp VARCHAR(50);

		SET likerTemp = NEW.liker;
        SET masterTemp = NEW.master;
		SET regdateTemp = NEW.regdate;
        SET depthTemp=NEW.depth;
        SET contentTypeTemp=NEW.contentType;

		if depth=1 then
			if contentTypeTemp='free' then
				update `article` set `likecount`=`likecount`+1 where `writer`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
			elseif contentTypeTemp='strategy' then
				update `article` set `likecount`=`likecount`+1 where `writer`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
			elseif contentTypeTemp='question' then
				update `article` set `likecount`=`likecount`+1 where `writer`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
			elseif contentTypeTemp='notice' then
				update `article` set `likecount`=`likecount`+1 where `writer`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
			elseif contentTypeTemp='indie' then
				update `game` set `likecount`=`likecount`+1 where `developer`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
			elseif contentTypeTemp='official' then
				update `game` set `likecount`=`likecount`+1 where `developer`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
			elseif contentTypeTemp='coupone' then
				update `shop` set `likecount`=`likecount`+1 where `producer`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
			elseif contentTypeTemp='goods' then
				update `shop` set `likecount`=`likecount`+1 where `producer`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
            end if;
		elseif depth=2 then
			update `comment` set `likecount`=`likecount`+1 where `author`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp and `depth`=2;
		end if;
	END $$
DELIMITER ;


DELIMITER $$
	CREATE TRIGGER autoCountDownLike
	AFTER  DELETE ON `like`
	FOR EACH ROW
	BEGIN
		DECLARE likerTemp VARCHAR(50);
        DECLARE masterTemp VARCHAR(50);
		DECLARE regdateTemp datetime;
        DECLARE depthTemp int unsigned;
        DECLARE contentTypeTemp VARCHAR(50);

		SET likerTemp = OLD.liker;
        SET masterTemp = OLD.master;
		SET regdateTemp = OLD.regdate;
        SET depthTemp=OLD.depth;
        SET contentTypeTemp=OLD.contentType;

		if depth=1 then
			if contentTypeTemp='free' then
				update `article` set `likecount`=`likecount`-1 where `writer`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
			elseif contentTypeTemp='strategy' then
				update `article` set `likecount`=`likecount`-1 where `writer`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
			elseif contentTypeTemp='question' then
				update `article` set `likecount`=`likecount`-1 where `writer`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
			elseif contentTypeTemp='notice' then
				update `article` set `likecount`=`likecount`-1 where `writer`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
			elseif contentTypeTemp='indie' then
				update `game` set `likecount`=`likecount`-1 where `developer`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
			elseif contentTypeTemp='official' then
				update `game` set `likecount`=`likecount`-1 where `developer`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
			elseif contentTypeTemp='coupone' then
				update `shop` set `likecount`=`likecount`-1 where `producer`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
			elseif contentTypeTemp='goods' then
				update `shop` set `likecount`=`likecount`-1 where `producer`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
            end if;
		elseif depth=2 then
			update `comment` set `likecount`=`likecount`-1 where `author`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp and `depth`=2;
		end if;
	END $$
DELIMITER ;


DELIMITER $$
	CREATE TRIGGER autoCountUpFileAttach
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
			elseif contentTypeTemp='indie' then
				update `game` set `attach_count`=`attach_count`+1 where `developer`=uploaderTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
			elseif contentTypeTemp='official' then
				update `game` set `attach_count`=`attach_count`+1 where `developer`=uploaderTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
			elseif contentTypeTemp='coupone' then
				update `shop` set `attach_count`=`attach_count`+1 where `producer`=uploaderTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
			elseif contentTypeTemp='goods' then
				update `shop` set `attach_count`=`attach_count`+1 where `producer`=uploaderTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
            end if;
		end if;

	END $$
DELIMITER ;


DELIMITER $$
	CREATE TRIGGER autoCountDownFileAttach
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

		if storeType='attach' then
			if contentTypeTemp='free' then
				update `article` set `attach_count`=`attach_count`-1 where `writer`=uploaderTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
			elseif contentTypeTemp='strategy' then
				update `article` set `attach_count`=`attach_count`-1 where `writer`=uploaderTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
			elseif contentTypeTemp='question' then
				update `article` set `attach_count`=`attach_count`-1 where `writer`=uploaderTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
			elseif contentTypeTemp='notice' then
				update `article` set `attach_count`=`attach_count`-1 where `writer`=uploaderTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
			elseif contentTypeTemp='indie' then
				update `game` set `attach_count`=`attach_count`-1 where `developer`=uploaderTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
			elseif contentTypeTemp='official' then
				update `game` set `attach_count`=`attach_count`-1 where `developer`=uploaderTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
			elseif contentTypeTemp='coupone' then
				update `shop` set `attach_count`=`attach_count`-1 where `producer`=uploaderTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
			elseif contentTypeTemp='goods' then
				update `shop` set `attach_count`=`attach_count`-1 where `producer`=uploaderTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
            end if;
		end if;

	END $$
DELIMITER ;


SELECT * FROM `reportState`;

DELIMITER $$
	CREATE TRIGGER autoCountUpReport
	AFTER INSERT ON `reportState`
	FOR EACH ROW
	BEGIN
		DECLARE masterTemp VARCHAR(50);
		DECLARE regdateTemp datetime;
        DECLARE reportTemp VARCHAR(50);
        DECLARE depthTemp varchar(50);
        DECLARE contentTypeTemp varchar(50);

		SET masterTemp = NEW.master;
		SET regdateTemp = NEW.regdate;
        SET reportTemp= NEW.report;
        SET depthTemp=NEW.depth;
        SET contentTypeTemp=NEW.contentType;

        if depth=1 then
			if contentTypeTemp='free' then
				if reportTemp='abuse' then
					update `article` set `abuse_report`=`abuse_report`+1 where `writer`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
				elseif reportTemp='lewd' then
					update `article` set `19_report`=`19_report`+1 where `writer`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
				elseif reportTemp='incoporate' then
					update `article` set `incoporate_report`=`incoporate_report`+1 where `writer`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
				end if;
			elseif contentTypeTemp='strategy' then
				if reportTemp='abuse' then
					update `article` set `abuse_report`=`abuse_report`+1 where `writer`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
				elseif reportTemp='lewd' then
					update `article` set `19_report`=`19_report`+1 where `writer`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
				elseif reportTemp='incoporate' then
					update `article` set `incoporate_report`=`incoporate_report`+1 where `writer`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
				end if;
			elseif contentTypeTemp='question' then
				if reportTemp='abuse' then
					update `article` set `abuse_report`=`abuse_report`+1 where `writer`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
				elseif reportTemp='lewd' then
					update `article` set `19_report`=`19_report`+1 where `writer`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
				elseif reportTemp='incoporate' then
					update `article` set `incoporate_report`=`incoporate_report`+1 where `writer`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
				end if;
			elseif contentTypeTemp='notice' then
				if reportTemp='abuse' then
					update `article` set `abuse_report`=`abuse_report`+1 where `writer`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
				elseif reportTemp='lewd' then
					update `article` set `19_report`=`19_report`+1 where `writer`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
				elseif reportTemp='incoporate' then
					update `article` set `incoporate_report`=`incoporate_report`+1 where `writer`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
				end if;
			elseif contentTypeTemp='indie' then
				if reportTemp='abuse' then
					update `game` set `abuse_report`=`abuse_report`+1 where `writer`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
				elseif reportTemp='lewd' then
					update `game` set `19_report`=`19_report`+1 where `writer`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
				elseif reportTemp='incoporate' then
					update `game` set `incoporate_report`=`incoporate_report`+1 where `writer`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
				end if;
			elseif contentTypeTemp='official' then
				if reportTemp='abuse' then
					update `game` set `abuse_report`=`abuse_report`+1 where `writer`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
				elseif reportTemp='lewd' then
					update `game` set `19_report`=`19_report`+1 where `writer`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
				elseif reportTemp='incoporate' then
					update `game` set `incoporate_report`=`incoporate_report`+1 where `writer`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
				end if;
			elseif contentTypeTemp='coupone' then
				if reportTemp='abuse' then
					update `shop` set `abuse_report`=`abuse_report`+1 where `writer`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
				elseif reportTemp='lewd' then
					update `shop` set `19_report`=`19_report`+1 where `writer`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
				elseif reportTemp='incoporate' then
					update `shop` set `incoporate_report`=`incoporate_report`+1 where `writer`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
				end if;
			elseif contentTypeTemp='goods' then
				if reportTemp='abuse' then
					update `shop` set `abuse_report`=`abuse_report`+1 where `writer`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
				elseif reportTemp='lewd' then
					update `shop` set `19_report`=`19_report`+1 where `writer`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
				elseif reportTemp='incoporate' then
					update `shop` set `incoporate_report`=`incoporate_report`+1 where `writer`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
				end if;
			end if;
		elseif depth=2 then
			if reportTemp='abuse' then
					update `comment` set `abuse_report`=`abuse_report`+1 where `author`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp and `depth`=2;
				elseif reportTemp='lewd' then
					update `comment` set `19_report`=`19_report`+1 where `author`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp and `depth`=2;
				elseif reportTemp='incoporate' then
					update `comment` set `incoporate_report`=`incoporate_report`+1 where `author`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp and `depth`=2;
				end if;
		elseif depth=3 then
			if reportTemp='abuse' then
					update `comment` set `abuse_report`=`abuse_report`+1 where `author`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp and `depth`=3;
				elseif reportTemp='lewd' then
					update `comment` set `19_report`=`19_report`+1 where `author`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp and `depth`=3;
				elseif reportTemp='incoporate' then
					update `comment` set `incoporate_report`=`incoporate_report`+1 where `author`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp and `depth`=3;
				end if;
		end if;




	END $$
DELIMITER ;

DELIMITER $$
	CREATE TRIGGER autoCountDownReport
	AFTER DELETE ON `reportState`
	FOR EACH ROW
	BEGIN
		DECLARE masterTemp VARCHAR(50);
		DECLARE regdateTemp datetime;
        DECLARE reportTemp VARCHAR(50);
        DECLARE depthTemp varchar(50);
        DECLARE contentTypeTemp varchar(50);

		SET masterTemp = OLD.master;
		SET regdateTemp = OLD.regdate;
        SET reportTemp= OLD.report;
        SET depthTemp=OLD.depth;
        SET contentTypeTemp=OLD.contentType;

        if depth=1 then
			if contentTypeTemp='free' then
				if reportTemp='abuse' then
					update `article` set `abuse_report`=`abuse_report`-1 where `writer`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
				elseif reportTemp='lewd' then
					update `article` set `19_report`=`19_report`-1 where `writer`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
				elseif reportTemp='incoporate' then
					update `article` set `incoporate_report`=`incoporate_report`-1 where `writer`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
				end if;
			elseif contentTypeTemp='strategy' then
				if reportTemp='abuse' then
					update `article` set `abuse_report`=`abuse_report`-1 where `writer`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
				elseif reportTemp='lewd' then
					update `article` set `19_report`=`19_report`-1 where `writer`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
				elseif reportTemp='incoporate' then
					update `article` set `incoporate_report`=`incoporate_report`-1 where `writer`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
				end if;
			elseif contentTypeTemp='question' then
				if reportTemp='abuse' then
					update `article` set `abuse_report`=`abuse_report`-1 where `writer`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
				elseif reportTemp='lewd' then
					update `article` set `19_report`=`19_report`-1 where `writer`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
				elseif reportTemp='incoporate' then
					update `article` set `incoporate_report`=`incoporate_report`-1 where `writer`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
				end if;
			elseif contentTypeTemp='notice' then
				if reportTemp='abuse' then
					update `article` set `abuse_report`=`abuse_report`-1 where `writer`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
				elseif reportTemp='lewd' then
					update `article` set `19_report`=`19_report`-1 where `writer`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
				elseif reportTemp='incoporate' then
					update `article` set `incoporate_report`=`incoporate_report`-1 where `writer`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
				end if;
			elseif contentTypeTemp='indie' then
				if reportTemp='abuse' then
					update `game` set `abuse_report`=`abuse_report`-1 where `writer`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
				elseif reportTemp='lewd' then
					update `game` set `19_report`=`19_report`-1 where `writer`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
				elseif reportTemp='incoporate' then
					update `game` set `incoporate_report`=`incoporate_report`-1 where `writer`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
				end if;
			elseif contentTypeTemp='official' then
				if reportTemp='abuse' then
					update `game` set `abuse_report`=`abuse_report`-1 where `writer`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
				elseif reportTemp='lewd' then
					update `game` set `19_report`=`19_report`-1 where `writer`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
				elseif reportTemp='incoporate' then
					update `game` set `incoporate_report`=`incoporate_report`-1 where `writer`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
				end if;
			elseif contentTypeTemp='coupone' then
				if reportTemp='abuse' then
					update `shop` set `abuse_report`=`abuse_report`-1 where `writer`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
				elseif reportTemp='lewd' then
					update `shop` set `19_report`=`19_report`-1 where `writer`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
				elseif reportTemp='incoporate' then
					update `shop` set `incoporate_report`=`incoporate_report`-1 where `writer`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
				end if;
			elseif contentTypeTemp='goods' then
				if reportTemp='abuse' then
					update `shop` set `abuse_report`=`abuse_report`-1 where `writer`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
				elseif reportTemp='lewd' then
					update `shop` set `19_report`=`19_report`-1 where `writer`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
				elseif reportTemp='incoporate' then
					update `shop` set `incoporate_report`=`incoporate_report`-1 where `writer`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp;
				end if;
			end if;
		elseif depth=2 then
			if reportTemp='abuse' then
					update `comment` set `abuse_report`=`abuse_report`-1 where `author`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp and `depth`=2;
				elseif reportTemp='lewd' then
					update `comment` set `19_report`=`19_report`-1 where `author`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp and `depth`=2;
				elseif reportTemp='incoporate' then
					update `comment` set `incoporate_report`=`incoporate_report`-1 where `author`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp and `depth`=2;
				end if;
		elseif depth=3 then
			if reportTemp='abuse' then
					update `comment` set `abuse_report`=`abuse_report`-1 where `author`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp and `depth`=3;
				elseif reportTemp='lewd' then
					update `comment` set `19_report`=`19_report`-1 where `author`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp and `depth`=3;
				elseif reportTemp='incoporate' then
					update `comment` set `incoporate_report`=`incoporate_report`-1 where `author`=masterTemp and `regdate`=regdateTemp and `contentType`=contentTypeTemp and `depth`=3;
				end if;
		end if;




	END $$
DELIMITER ;










