#sql("getnew")
	select * from a_article
	#for(x : kv)
     #(for.first ? "where": "and")
     #if(x.key=="type")
     	 Classid like CONCAT('%',#para(value),'%')
     #end
    #if(x.key=="title")
     	 title like CONCAT('%',#para(value),'%')
     #end
--      #(x.key) #para(x.value)

--   	 #end
-- 	where
-- 	1=1
-- 	#if(type)
-- 		and Classid = #para(type)
-- 	#end
-- 		#if(title)
-- 		and title like '% #para(title) %'
-- 	#end
    order by rand()
#end

#sql("getrandnews")
select  *from a_article order by rand() desc LIMIT 5
#end
#sql("taobaosearch")
     select  *from t_map_dataitem order by rand() desc LIMIT 10
#end
#sql("t_map_dataitem")
select  * from t_map_dataitem order by rand()
#end

#sql("getdownload")
select  * from download
where 1=1
    #if(title)

  and title like CONCAT('%',#para(title),'%')
    #end

    #if(type)
     and Classid = #para(type)
    #end

order by views DESC
    #end