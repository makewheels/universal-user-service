tar zxvf /home/admin/${PIPELINE_NAME}/package.tgz -C /home/admin/${PIPELINE_NAME}/

chmod a+x /home/admin/${PIPELINE_NAME}/user-service/deploy.sh
sh /home/admin/${PIPELINE_NAME}/user-service/deploy.sh restart
