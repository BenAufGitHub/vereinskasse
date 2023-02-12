## How to: Database

1. Have MySQL installed
2. Open a command prompt in the bin directory of MySQL
3. Create an empty database:


    mysql -u [user] -p -e "create database [database_name]";

4. Enter a password

The standard configuration is:

    -u: root
    -p: 1234
    -db: vereinskasse

After configuring your own data, change the login data in 'helpers/Query.java'.

5. Load backup into your database:


    mysql -u [user] -p [database_name] < [path/to/../resources/mysql_backup.sql]
