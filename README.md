easydao
=======

Easydao是简单易用的轻量级DAO(Data Access Object)框架，它集成了Hibernate实体维护和Mybaits SQL分离的两大优势，提供了非入侵式API，可以与Hibernate、SpringJdbc等数据库框架很好的集成 
。
****
###　　　　　　　　　　　　Author:王伟
###　　　　　　　　　 E-mail:ww20081120@126.com

===========================

##<a name="index"/>目录
* [特征](#style)
* [使用说明](#read)
* [开发者指南](#developer)

 ###Easydao具有以下特征:
 1.O/R mapping不用设置xml，零配置便于维护  
 2.不需要了解JDBC的知识  
 3.SQL语句和java代码的分离  
 4.可以自动生成SQL语句  
 5.接口和实现分离，不用写持久层代码，用户只需写接口，以及某些接口方法对应的sql 它会通过动态代理自动生成实现类  
 6.支持自动事务处理和手动事务处理  
 7.MiniDao整合了Hibernate+mybatis的两大优势，支持实体维护和SQL分离  
 8.SQL支持脚本语言  
 9.可以无缝集成Hibernate、Spring等第三方框架，也可以单独部署运行，适应性强。  
###接口和SQL文件对应目录  
#####接口文件[EmpDao.java]
```java
  @DAO
public interface EmpDao {

    @Sql("select * from emp")
    List<Map<String,Object>> selectAll();
    
    @Sql("select * from emp where empno = :empno")
    Map<String,Object> selectOne(@Param("empno") int empno);
    
    @Sql(value="select * from emp where deptno = :dept.deptno", bean=Emp.class)
    List<Emp> selectDeptEmp(@Param("deptno")Dept dept, @Param(Param.pageIndex)int pageIndex,@Param(Param.pageSize)int pageSize);
    
    @Sql(bean = Emp.class)
    List<Emp> queryEmp(@Param("dept") Dept dept);

    @Sql("select count(*) from emp")
    int listCount(ResultTransformer transformer);
}
```
####SQL文件[EmpDao_queryEmp.sql]  
```java
   select * from emp where   
   1=1   
   #if($dept)   
   and deptno=$dept.deptno   
   #end   
```
####测试代码  
```java
  @RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
    "classpath:/META-INF/spring/*.xml"
})
@Transactional
public class EmpService {

    @Resource
    private EmpDao empDao;

    @Test
    public void test() {
        try {
            Dept dept = new Dept();
            dept.setDeptno(30);

            System.out.println("------------------");
            System.out.println(empDao.queryEmp(dept).size());
            System.out.println("------------------");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
 }
```
