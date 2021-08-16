#sql("getusers")
        select DISTINCT  t.*,r.Title  as RoleName, ur.OrganizeID from  Users t,Role r,UsersRelation ur ,Organize o
        where t.identifier=r.identifier and t.ID = ur.UserID and o.ID = ur.OrganizeID
    #if(title)
		and name like ('%'|| #para(title) ||'%')

	#end
	   #if(organizeid)
		and ur.OrganizeID =#para(organizeid)

	#end

#end
#sql("getusersapp")
select * from roleapp where 1=1
    #if(parentid)
     and ParentID  =#para(parentid)
     #end
order by Sort

#end