nohup java -jar -Dspring.profiles.active=product \
/home/admin/universal-user-service/user-service/target/user-service-1.0.0.jar \
>> /home/admin/universal-user-service/user-service/logs/start.log 2>&1 &