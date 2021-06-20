package com.webos.socket.user;

import com.webcore.modle.Sysim;
import java.util.List;
public class Group {

        private String avatar;
        private String id;
        private List<Sysim> list;
        private String groupname;
        private String imid;
        private String name;
        private String type;
    private String content;
    public void setContent(String content) {
        this.content = content;
    }
    public String getContent() {
        return content;
    }
        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }
        public String getAvatar() {
            return avatar;
        }

        public void setId(String id) {
            this.id = id;
        }
        public String getId() {
            return id;
        }

        public void setList(List<Sysim> list) {
            this.list = list;
        }
        public List<Sysim> getList() {
            return list;
        }

        public void setGroupname(String groupname) {
            this.groupname = groupname;
        }
        public String getGroupname() {
            return groupname;
        }

        public void setImid(String imid) {
            this.imid = imid;
        }
        public String getImid() {
            return imid;
        }

        public void setName(String name) {
            this.name = name;
        }
        public String getName() {
            return name;
        }

        public void setType(String type) {
            this.type = type;
        }
        public String getType() {
            return type;
        }
}
