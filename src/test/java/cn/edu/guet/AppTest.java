package cn.edu.guet;

import static org.junit.Assert.assertTrue;

import cn.edu.guet.bean.Permission;
import cn.edu.guet.bean.Role;
import cn.edu.guet.bean.SearchPermission;
import cn.edu.guet.bean.User;
import cn.edu.guet.mapper.PermissionMapper;
import cn.edu.guet.mapper.RoleMapper;
import cn.edu.guet.mapper.UserMapper;
import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Unit test for simple App.
 */
public class AppTest {
    SqlSession sqlSession;
    SqlSessionFactory sqlSessionFactory;

    @Before
    public void init() throws ClassNotFoundException {
        InputStream in = Class.forName("cn.edu.guet.AppTest")
                .getResourceAsStream("/mybatis-config.xml");
        SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
        sqlSessionFactory = builder.build(in);
        sqlSession = sqlSessionFactory.openSession();// 相当于以前的Connection
    }

    @Test
    public void testSaveUser() {

        User user = new User();
        user.setUserId("e1217c5d18dc41aa966e37ab8fdd9003");
        user.setUsername("lirongyou");
        user.setNickName("黎荣右");
        user.setPassword("abdee3243239acde2334dddeeeff");
        user.setSalt("abdee3243239acde2334dddeeeff");

        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        userMapper.saveUser(user);
    }

    @Test
    public void testGetAllUser() {
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        List<User> userList = userMapper.getAllUser();

        System.out.println(userList);
    }

    @Test
    public void testGetUserByName() {
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        User user = userMapper.getUserByName("qsx");
        System.out.println("结果：" + user);
    }

    @Test
    public void testDeleteUserById() {
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);// JDK动态代理，生成的代理对象，相当于曾经的UserDaoImpl
        int num = userMapper.deleteUserById("2111");
        System.out.println("删除了：" + num + "条记录");
    }

    @Test
    public void testUpdateUser() {
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        User user = userMapper.getUserByName("qsx");
        user.setNickName("覃世想");
        int a = userMapper.updateUser(user);
        System.out.println("更新了：" + a + "条记录");
    }

    @Test
    public void testLogin() {
        RoleMapper roleMapper = sqlSession.getMapper(RoleMapper.class);
        Role role = roleMapper.getRoleById("67426c770519471384fa87aa0fabbc45");
        System.out.println(role);
    }

    @Test
    public void testMultiParam() {
        PermissionMapper permissionMapper = sqlSession.getMapper(PermissionMapper.class);

        Map<String, String> map = new HashMap<>();
        map.put("name", "列表");
        map.put("url", "role");

        SearchPermission searchPermission = new SearchPermission();
        searchPermission.setName("列表");
//        searchPermission.setUrl("role");
        List<Permission> permissionList = permissionMapper.searchPermission(searchPermission);
        System.out.println(permissionList);

    }

    @Test
    public void testSelfJoin() {
        PermissionMapper permissionMapper = sqlSession.getMapper(PermissionMapper.class);
        Permission permission = permissionMapper.getPermissionById("e44ed8d606464a068361c690b6f2874c");
        System.out.println(permission.getName());
    }

    @Test
    public void testLevel1Cache() {
        SqlSession sqlSession1 = sqlSessionFactory.openSession();
        UserMapper userMapper = sqlSession1.getMapper(UserMapper.class);//代理对象，相当于之前的UserDaoImpl类的对象，JDK的动态代理
        User user = userMapper.getUserById("3772a1cd5c8943e999a58b53f96413c6");
        System.out.println("第1次查询：" + user.getNickName());


        sqlSession1.clearCache();//清空session缓存

        UserMapper userMapper1 = sqlSession1.getMapper(UserMapper.class);
        User user1 = userMapper1.getUserById("3772a1cd5c8943e999a58b53f96413c6");
        System.out.println("第2次查询：" + user1.getNickName());

        // 会执行几次SQL语句呢？
    }
    @Test
    public void testLevel2Cache(){
        /*
        Mapper级别（跨session）
        1、自己实现cache接口
        2、在XXXMapper中使用cache标签来使用二级缓存
         */
        SqlSession sqlSession1 = sqlSessionFactory.openSession();
        UserMapper userMapper = sqlSession1.getMapper(UserMapper.class);//代理对象，相当于之前的UserDaoImpl类的对象，JDK的动态代理
        User user = userMapper.getUserById("3772a1cd5c8943e999a58b53f96413c6");
        System.out.println("第1次查询：" + user.getNickName());

        sqlSession1.close();

        SqlSession sqlSession2=sqlSessionFactory.openSession();
        UserMapper userMapper1 = sqlSession2.getMapper(UserMapper.class);
        User user1 = userMapper1.getUserById("3772a1cd5c8943e999a58b53f96413c6");
        System.out.println("第2次查询：" + user1.getNickName());

    }

    @After
    public void destroy() {
        // sqlSession.commit();// 提交
        sqlSession.close();// 关闭连接
    }
}
