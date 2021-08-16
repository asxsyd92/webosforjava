#sql("WorkFlowList")
select  * from workflow
where  Status!=4
    #if(createuserid)

    and CreateUserID = #para(createuserid)
	#end
	 #if(type)
    and Type #para(type)
	#end
 #if(title)
    and Name like CONCAT('%',#para(title),'%')
	#end
order by CreateDate DESC

    #end