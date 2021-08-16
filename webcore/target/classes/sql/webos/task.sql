
#sql("gettask")
     select  * from commontask
     where 1=1
    #if(title)

    and title like CONCAT('%',#para(title),'%')
	#end
	 #if(type)
    and Classid like CONCAT('%',#para(type),'%')
	#end

	order by AddTime DESC

#end