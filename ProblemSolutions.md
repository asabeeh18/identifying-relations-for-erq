#Java
1. Run `rpm -qa | grep jdk`
2. Take each line of the above result and do:
    `rpm -e -nodeps line`
3. Then install java `rpm -Uvh jdk-whatever.rpm`

#Hadoop
1. Will give problem when starting dfs.
    1. Open hosts file `/etc/hosts` add your pc name `M12-##` at the end of line which has 127.0.0.0 or localhost  
1. Run `jps` to see if everything ran properly else see logs.

#Virtuoso
1. Strictly open the shell in Virtuoso root directory
2. Install `lib-something.so.rpm` The rpm package is in your PC
3. odbc.ini might need to be changed in `/etc` . Refer Tembes/Dhruvs PC
2. Run isql by `bin/isql 1111 dba dba` 

#Static IP
-
#Remote
1. Some software with a small,weird 3 letter name in Start -> Internet
2. Enter that ip and password in other pc with any viewer and *poof*