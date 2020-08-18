# 后端使用说明
##### 本项目是diamond_doc前后端分离的后端项目，为旨在提供接口供前端调用，以完善网站功能。
## 配置

### 数据库建表如下

```mysql
use diamond;
create table users(
	id int primary key not null auto_increment,
    user_name varchar(50) unique not null,
    password varchar(30) not null,
    token char(36),
    avatar_url varchar(255),
    sex char(2),
    birthday date,
    telephone char(11),
    email varchar(30),
    team_ids varchar(1024)
);
create table docs(
	id int primary key not null auto_increment,
	title varchar(50) not null,
    content text not null,
    creator int not null,
    create_time datetime,
    update_time datetime,
    team_id int,
    comment_count int default 0,
    collect_count int default 0,
    authority int not null,
    updated_id int,
    deleted int default 0,
    edited int default 0
);
create table views(
	id int primary key not null auto_increment,
    viewer_id int not null,
    doc_id int not null,
    doc_title varchar(50),
    latest_time datetime not null
);
create table favourites(
	id int primary key not null auto_increment,
    collector_id int not null,
    doc_id int not null,
    doc_title varchar(50),
    collect_time datetime not null
);
create table team(
	id int primary key not null auto_increment,
    team_name varchar(30) not null,
    basic_information text not null,
    creator int not null,
    members varchar(1024),
    create_time datetime
);
create table comments(
	id int primary key not null auto_increment,
    content text not null,
    doc_id int not null,
    commentator int not null,
    commentator_name varchar(50),
    comment_time datetime,
    like_count int default 0,
    avatar_url varchar(255)
);
create table notification(
	id int primary key not null auto_increment,
   	notifier int,
    receiver int,
    type int,
    create_time datetime,
    status int,
    outer_id int,
    outer_title varchar(50),
    completed int
);
```

### 配置信息

application.properties配置文件中

diamond为数据库名，请在本地Mysql数据库新建该数据库，username以及password根据你们本地mysql配置的账号密码来

运行时启动DiamondApplication


## 接口文档

#### 登录、注册、退出

| interface |   path    | method | parameter |                          return                          |
| :-------: | :-------: | :----: | :-------: | :------------------------------------------------------: |
|   注册    | /register |  post  |  LogDTO   | ResultDTO{type=200:正常,else:异常},message中含有异常信息 |
|   登录    |  /login   |  post  |  LogDTO   | ResultDTO{type=200:正常,else:异常},message中含有异常信息 |
|   退出    |  /logout  |  get   |    IP     |                  String{"您已退出登录"}                  |

path中都省略了localhost:8080

#### 用户信息修改

| interface |     path     | method |   parameter    |  return   |
| :-------: | :----------: | :----: | :------------: | :-------: |
| 更改信息  | /information |  POST  | InformationDTO | ResultDTO |
| 查看信息  | /information/{userName} |  get   |                |  USers  |

可以尝试在session里面直接获取user

#### 工作台

| interface |           path           | method | parameter |         return          |
| :-------: | :----------------------: | :----: | :-------: | :---------------------: |
| 最近浏览  |     /views/{userId}      |  GET   |           |      List< Views >      |
| 收藏文档  | /collect/{page}/{userId} |  GET   |           |   Page< Favourites >    |
| 自己创建  |  /mydoc/{page}/{userId}  |  GET   |           |      Page< Docs >       |
| 加入团队  |     /myteam/{userId}     |  GET   |           |  List< TeamCheckDTO >   |
|  回收站   |    /recycle/{userId}     |  GET   |           |  List< DeleteDocDTO >   |
| 消息中心  |  /notification/{userId}  |  GET   |           | List< NotificationDTO > |
| 一键清空  |   /deletedAll/{userId}   |  GET   |           |        ResultDTO        |
| 逐个删除  |   /deletedOne/{docId}    |  GET   |           |        ResultDTO        |

Page可以传回所包含的数据，总页数，总记录数，当前页面，是否有上一页或下一页

#### Notification的字段解释

| type |         消息类型         | outer_id | outer_title |
| :--: | :----------------------: | :------: | :---------: |
|  1   |         团队邀请         | team_id  |  team_name  |
|  2   |         申请加入         | team_id  |  team_name  |
|  3   |         自己退出         | team_id  |  team_name  |
|  4   |         踢出团队         | team_id  |  team_name  |
|  5   |         评论信息         |  doc_id  |  doc_title  |
|  6   |         团队解散         | team_id  | team_title  |
|  7   |         同意邀请         | team_id  | team_title  |
|  8   |         同意申请         | team_id  | team_title  |
|  9   |         拒绝邀请         | team_id  | team_title  |
|  10  |         拒绝申请         | team_id  | team_title  |
|  11  | 退出团队管理员接受的通知 | team_id  | team_title  |

status{0：未读；1：已读}

#### 文档功能

|  interface   |              path              | method |  parameter   |                          return                          |
| :----------: | :----------------------------: | :----: | :----------: | :------------------------------------------------------: |
|   创建文档   |         /doc/{userId}          |  PSOT  | DocCreateDTO | ResultDTO{type=200:正常,else:异常},message中含有异常信息 |
|   修改文档   |       /doc/{id}/{userId}       |  POST  | DocCreateDTO | ResultDTO{type=200:正常,else:异常},message中含有异常信息 |
|   查看文档   |       /doc/{id}/{title}        |  GET   |              |                       DocReturnDTO                       |
|   收藏文档   | /collect/{id}/{title}/{userId} |  GET   |              |                        ResultDTO                         |
|   取消收藏   |     /cancel/{id}/{userId}      |  GET   |              |                        ResultDTO                         |
|   删除文档   |          /delete/{id}          | DELETE |              |                        ResultDTO                         |
|   恢复文档   |         /recovery/{id}         |  PUT   |              |                        ResultDTO                         |
| 更改文档状态 |          /doc/{docId}          |  GET   |              |                        ResultDTO                         |

该表中传的所有id皆为对应文档的id

#### 团队功能

|  interface   |                 path                 | method  |   parameter   |                          return                          |
| :----------: | :----------------------------------: | :-----: | :-----------: | :------------------------------------------------------: |
|   创建团队   |            /team/{userId}            |  POST   | TeamCreateDTO | ResultDTO{type=200:正常,else:异常},message中含有异常信息 |
|   解散团队   |           /deleteTeam/{id}           | DELETED |               |                        ResultDTO                         |
|   查看信息   |       /team/{teamId}/{userId}        |   GET   |               |                      TeamReturnDTO                       |
|   加入团队   |       /join/{teamId}/{userId}        |   GET   |               |                        ResultDTO                         |
|   退出团队   |      /quit/{teamId}/{userName}       |   GET   |               |                        ResultDTO                         |
|   设置权限   |     /authority/{docId}/{weight}      |   GET   |               |                        ResultDTO                         |
|   搜索团队   |          /search/{teamName}          |   GET   |               |                  List< TeamReturnDTO >                   |
| 修改文档权限 | /modifyAuthority/{docId}/{authority} |   GET   |               |                        ResultDTO                         |

加入分为主动加入和邀请加入，若管理员或者被邀请者同意，则调用该接口将该用户写入成员列

退出分为自动退出和被踢出团队，通过检验传过来的userName与当前登录者是否一致进行区分

#### 通知功能

| interface |                 path                  | method | parameter |  return   |
| :-------: | :-----------------------------------: | :----: | :-------: | :-------: |
|   邀请    |      /invite/{teamId}/{userName}      |  GET   |           | ResultDTO |
|   申请    |      /apply/{teamId}/{userName}       |  GET   |           | ResultDTO |
| 拒绝邀请  | /refuseInvitation/{teamId}/{userName} |  GET   |           | ResultDTO |
| 拒绝申请  |   /refuseApply/{teamId}/{userName}    |  GET   |           | ResultDTO |

#### 评论功能

| interface |           path           | method | parameter  |  return   |
| :-------: | :----------------------: | :----: | :--------: | :-------: |
| 评论文档  |    /comment/{userId}     |  POST  | CommentDTO | ResultDTO |
| 点赞评论  | /commentLike/{commentId} |  GET   |            | ResultDTO |

#### 图片上传

| interface |   path    | method | parameter | return  |
| :-------: | :-------: | :----: | :-------: | :-----: |
| 图片上传  | /addImage |  POST  | imageData | FileDTO |
imageData为前端参数的名称