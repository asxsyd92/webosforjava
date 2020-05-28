#sql("getnew")
	select * from a_article
	where
	1=1
	#if(type)
		and Classid = #para(type)
	#end
		#if(title)
		and title like '% #para(title) %'
	#end
	order by Adddate DESC
#end


#sql("taobaosearch")
     select  *from t_map_dataitem order by rand() desc LIMIT 10
#end