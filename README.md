# Online Chat System

A online based chat application built for android platform. Easy to impliment and easy code understandablity

  - Send and recieve messages.
  - Connect with anyone on database.
  - Easy search users.


## Online database and server management

Upload all files of the public_html folder to your webserver.
  - Change the data of the connection.php file as per your database and location.
  - Don't forget to change the timezone in connection.php or it will take the Indian Standard Time.
### Database:

Create a database. Then create a table named 'user_table'. This is the only table required to be created for the app.
###### Use the following format for the table:

The table must have the below columns with the same datatype.



| Column Name | Datatype |
| ------ | ------ |
| id | int[11] auto increment/primary key |
| Name | varchar[1000] |
| Email | varchar[1000] |
| Password | varchar[1000] |
| chat_table | varchar[1000] |


 -  https://www.php.net/manual/en/timezones.php for more details on php time zones.
