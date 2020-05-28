package  com.asxsyd92.Modle;

import com.jfinal.plugin.activerecord.IBean;
import com.jfinal.plugin.activerecord.Model;

public  class xRoleApp<M extends xRoleApp<M>> extends Model<M> implements IBean {

        public void setId(java.lang.String id) {
        set("ID", id);
    }

        public java.lang.String getId() {
        return get("ID");
    }
   /* public String ID { get; set; }
    public String ParentID { get; set; }
    public String RoleID { get; set; }
    public String AppID { get; set; }
    public String Title { get; set; }
    public String Params { get; set; }
    public int Sort { get; set; }
    public String Ico { get; set; }
    public int Type { get; set; }

    public String icon { get; set; }

    public String tag { get; set; }

    public int tagtype { get; set; }
    public int openType { get; set; }
    public int maxOpen { get; set; }
    public String Color { get; set; }
    public String extend { get; set; }

    public long Count { get; set; }


    public String id { get { return this.ID; } }


    public String title { get { return this.Title; } }

    public String name { get { return this.Title; } }

    public int identifier { get; set; }

    public List<xRoleApp> children
    {
        get
        {

            string sql = "select * from RoleApp where ParentID='" + this.ID + "' order by Sort";
            return xRoleAppDal.Instance.GetList(sql, null).ToList();


        }

    }*/
    }
