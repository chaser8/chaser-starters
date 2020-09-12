CREATE TABLE IF NOT EXISTS file_info
(
    FILE_ID      varchar(50)  NOT NULL COMMENT '文件标识',
    BIZ_TYPE     varchar(20)  COMMENT '业务类型',
    BIZ_ID       varchar(50)  COMMENT '业务编码',
    FILE_TYPE    varchar(255) NOT NULL COMMENT '文件类型（image/jpg, image/png, video/mp4, xsl,doc等)',
    FILE_NAME    varchar(255) NOT NULL COMMENT '原始文件名称',
    FILE_PATH    varchar(255) NOT NULL COMMENT '文件相对路径',
    FILE_SIZE    bigint(20)   NOT NULL COMMENT '文件大小 单位 kb',
    STATUS       char(4)      NOT NULL COMMENT '状态：1000有效，2000无效',
    STATUS_DATE  datetime     NOT NULL COMMENT '状态时间',
    CREATE_DATE  datetime     NOT NULL COMMENT '创建时间',
    CREATE_STAFF varchar(50) DEFAULT NULL COMMENT '创建人',
    PRIMARY KEY (FILE_ID)
);