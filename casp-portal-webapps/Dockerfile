FROM 172.16.29.66:5000/minos-jdk-base-docker:v1.0.0
COPY ./target/*.jar /casp-portal-webapps/
COPY ./start.sh /casp-portal-webapps/start.sh
ENV MYPATH /casp-portal-webapps
WORKDIR $MYPATH
ENV jvmUnitSize 256
RUN mkdir -p /casp-portal/lib

RUN chown -R nobody:nobody /casp-portal
RUN chown -R nobody:nobody /casp-portal-webapps
RUN chmod 755 /casp-portal-webapps/start.sh
USER nobody
CMD ["/casp-portal-webapps/start.sh"]
