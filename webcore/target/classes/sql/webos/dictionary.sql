#sql("getpage")
     select  * from dictionary
     where 1=1


         #if(parentid)

    and ParentID  =#para(parentid)
	#end
    #if(title)

    and title like CONCAT('%',#para(title),'%')
	#end
	 #if(code)
    and Code =#para(code)
	#end

	order by Sort DESC

#end