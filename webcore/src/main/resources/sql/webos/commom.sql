
#sql("getcommonlist")
     select  * from $table$
     where 1=1
	#if(type)
		and Classid like CONCAT('%',#para(type),'%')
	#end
	#if(title)
		and title like CONCAT('%',#para(title),'%')
	#end


#end