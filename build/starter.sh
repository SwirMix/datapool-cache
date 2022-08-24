work_dir="/home/ponchick/Desktop/datapool-service/service-source/build"
export DATAPOOL_WORKDIR=${work_dir}
rm -r ${DATAPOOL_WORKDIR}/ignite/
java \
-Dspring.config.location=${DATAPOOL_WORKDIR}/application.yml \
-jar ${DATAPOOL_WORKDIR}/http-datapool-service-1.0-SNAPSHOT.jar