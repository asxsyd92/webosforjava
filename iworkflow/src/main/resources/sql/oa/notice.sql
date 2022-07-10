#sql("GetNotecePage")
select  * from notice
where  1=1
    #if(users)

    and Users  like CONCAT('%',#para(users),'%') or Users is null
	#end
	 #if(type)
    and NoticeType #para(type)
	#end
		 #if(userid)
        and UserID #para(userid)
    	#end
 #if(title)
    and Title like CONCAT('%',#para(title),'%')
	#end
order by Top,AddTime DESC

    #end