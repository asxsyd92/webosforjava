在此统一管理所有应用业务的sql脚本
#namespace("webos-article")
#include("webos/a_article.sql")
#end
用户表
#namespace("webos-users")
#include("webos/users.sql")
#end
字典
#namespace("webos-dictionary")
#include("webos/dictionary.sql")
#end
公用task表
#namespace("webos-task")
#include("webos/task.sql")
#end
公用表
#namespace("webos-commom")
#include("webos/commom.sql")
#end

公用表
#namespace("webos-log")
#include("webos/log.sql")
#end

