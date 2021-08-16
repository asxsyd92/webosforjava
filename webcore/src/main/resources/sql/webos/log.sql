#sql("getLogList")
select  * from log
where  1=1
    #if(title)

    and Title like CONCAT('%',#para(title),'%')
	#end
	#if(receiveid)
    and UserID like CONCAT('%',#para(receiveid),'%')
	#end

	#if(userid)
    and WriteTime >= #para(date1)
	#end
	#if(userid)
    and WriteTime <= #para(date2)
	#end
	#if(type)
    and type = #para(type)
	#end
	#if(ipaddress)
    and IPAddress like CONCAT('%',#para(ipaddress),'%')
	#end


      order by WriteTime DESC
    #end
