#sql("getwaitlist")
select  * from workflowtask
where  Status in(0,1) and ReceiveID=#para(receiveid)
    #if(title)

    and title like CONCAT('%',#para(title),'%')
	#end
	 #if(type)
    and FlowID like CONCAT('%',#para(type),'%')
	#end

order by ReceiveTime DESC
 #end

#sql("completedlist")
select  * from workflowtask
where  Status in(2,3,4,5,6) and ReceiveID=#para(receiveid)
    #if(title)

    and title like CONCAT('%',#para(title),'%')
	#end
	#if(type)
    and flowid like CONCAT('%',#para(flowid),'%')
	#end
	#if(user)
    and SenderID like CONCAT('%',#para(user),'%')
	#end
	#if(date1)
    and ReceiveTime >#para(date1)
	#end
	#if(date2)
    and ReceiveTime >#para(date2)
	#end
order by ReceiveTime DESC

    #end

    #sql("gettasklist")
select *from workflowtask where flowID=#para(flowID)
                            and stepID=#para(stepID)
                            and groupID=#para(groupID)

    #end