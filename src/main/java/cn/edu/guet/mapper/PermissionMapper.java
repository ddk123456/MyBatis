package cn.edu.guet.mapper;

import cn.edu.guet.bean.Permission;
import cn.edu.guet.bean.SearchPermission;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface PermissionMapper {
    //    List<Permission> searchPermission(Map<String,String> map);
//    List<Permission> searchPermission(@Param("name") String name, @Param("url") String url);
    List<Permission> searchPermission(SearchPermission sp);
    Permission getPermissionById(String id);
}
