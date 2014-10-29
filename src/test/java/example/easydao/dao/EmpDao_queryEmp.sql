select * from emp where
1=1
#if($dept)
and deptno=$dept.deptno
#end