#!/bin/bash
MYPATH=/casp-portal-webapps

jvmUnitSize=`cat $MYPATH/mark/jvmUnitSize`
if [ ! -n $jvmUnitSize ]; then
  jvmUnitSize=256
fi

extOpts=`cat $MYPATH/mark/extOpts`

nohup /usr/local/jdk1.8.0_241/bin/java -Xms$(($jvmUnitSize*1))m -Xmx$(($jvmUnitSize*1))m $extOpts -Dfile.encoding=utf-8 -Duser.timezone=GMT+08 -Dlog4j2.formatMsgNoLookups=true -Djava.net.useSystemProxies=true  -Djava.security.egd=file:/dev/./urandom -Dloader.path=/opt/casp-portal-webapps/cards/custom,/casp-portal/lib,/opt/casp-portal-webapps/templates/custom  -jar /casp-portal-webapps/casp-portal-webapps-*.jar > /dev/null 2>&1 &
pid=$!
echo $pid > $MYPATH/mark/tpid
# 记录本次脚本启动时间
echo `date +%s` > $MYPATH/mark/startTime
# 重启计数器文件
if [ -f $MYPATH/mark/register ]; then
   register=`cat $MYPATH/mark/register`
   register=$[$register + 1]
   echo $register > $MYPATH/mark/register
else
   echo 1 > $MYPATH/mark/register
fi