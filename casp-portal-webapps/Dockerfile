FROM 172.16.29.66:5000/minos-jdk-base-docker:v3.0.1

COPY ./target/*.jar ./start.sh ./container.sh ./arthas.sh /casp-portal-webapps/

ENV jvmUnitSize=256 \
    JAVA_EXT_OPTS=$JAVA_EXT_OPTS \
    MYPATH=/casp-portal-webapps
    
WORKDIR $MYPATH

RUN chmod 755 /casp-portal-webapps/container.sh

CMD ["/casp-portal-webapps/container.sh"]