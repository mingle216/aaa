#!/bin/bash

####################### 模块容器启动脚本 #############################################
# 由于后续产品需要在不同的操作系统中进行安装部署。
# 不同操作系统对nobody用户的定义是不同的，因此需要在容器启动时指定对应的目录权限，防止后续其他操作系统中无法启动问题
# add by zhangjian 01116211
# date: 2021-11-01
###########################################################################################

# 初始化环境信息
function initEnv(){
  # 创建需要的目录
  mkdir -p /opt/settings/license
  mkdir -p /casp-portal/lib
  mkdir -p /opt/logs/casp-portal-webapps
  mkdir -p /opt/casp-portal-webapps/cards/custom
  mkdir -p /opt/casp-portal-webapps/templates/custom
  mkdir -p /opt/casp-portal-webapps/upLoadFiles

  mkdir -p $MYPATH/mark

  # 修改目录权限
  chmod -R 777 /opt/settings
  chown nobody:nobody /casp-portal/lib -R
  chown nobody:nobody /opt/logs/casp-portal-webapps -R
  chown nobody:nobody /opt/casp-portal-webapps/cards/custom -R
  chown nobody:nobody /opt/casp-portal-webapps/templates/custom -R
  chown nobody:nobody /opt/casp-portal-webapps/upLoadFiles -R

  chown nobody:nobody $MYPATH -R
}

# 预留JDK扩展能力，比如加载证书的扩展点，允许执行外部的指定的shell脚本
function loadExtJdkTool(){
  mkdir -p /opt/jdktools
  cd /opt/jdktools
  # TODO 预留，需要考虑一个安全的方案
  properties="properties"
	certPwdStr=""
	for file in `ls $1` #注意此处这是两个反引号，表示运行系统命令
	do
	 if [ -d $file ] #注意此处之间一定要加上空格，否则会报错
	 then
	 loadExtJdkTool $file
	 else
		if [[ ${file##*.} = $properties ]]
		 then
		 echo /opt/jdktools/$1"/"$file
		 certPwdStr=$(cat /opt/jdktools/$1"/"$file | grep certPwd | awk -F'=' '{ print $2 }' | sed s/[[:space:]]//g)
		 certName=$(cat /opt/jdktools/$1"/"$file | grep certName | awk -F'=' '{ print $2 }' | sed s/[[:space:]]//g)
		 echo $certPwdStr
		 echo $certName
		 echo y | keytool -import -keystore "/usr/local/jdk1.8.0_241/jre/lib/security/cacerts" -file /opt/jdktools/$1"/"$certName -alias $certName -storepass $certPwdStr
		 fi
	 fi
	done
}

# 启动java服务
function startJavaComponent() {
    echo $(date "+%Y-%m-%d %H:%M:%S") "begin to start casp-portal"
    # 将环境变量中的内存设置到对应文件中
    if [ ! -n $jvmUnitSize ]; then  
      echo 256 > $MYPATH/mark/jvmUnitSize
    else  
      echo $jvmUnitSize > $MYPATH/mark/jvmUnitSize
    fi    
    # 增加高级启动参数 add by zhangjian 2021-12-17
    echo $JAVA_EXT_OPTS > $MYPATH/mark/extOpts

    chmod 777 $MYPATH/start.sh
    sudo -u nobody sh -c $MYPATH/start.sh
}


# 检查java组件是否已经启动
function checkJavaHealthy() {
    if [ -f $MYPATH/mark/tpid ]; then
      pid=`cat $MYPATH/mark/tpid`
      num=`ps -ef | grep -w $pid | grep -v grep | wc -l`;
      if [ $num -gt 0 ]; then
         return 0
      else
         # 当前时间是否离本次启动时间超过1分钟，如果已经超过，则进行重启
         beginTime=`cat $MYPATH/mark/startTime`
         nowTime=`date +%s`
         if [ `expr $nowTime - $beginTime` -gt 60 ]; then
            echo $(date "+%Y-%m-%d %H:%M:%S") "casp-portal启动已经超过1分钟，本次启动失败，请检查/opt/minos/logs/casp-portal-webapps中的相关日志"
            return 1
         fi
      fi
    else
      return 1
    fi
}

# 检查容器是否正常启动成功，如果
function checkComponentHealthy() {
    while true
    do
       sleep 1
       checkJavaHealthy
       javaHealthy=$?
       if [ $javaHealthy -ne 0 ]; then
          echo $(date "+%Y-%m-%d %H:%M:%S") "casp-portal组件启动失败"
          break
       fi
    done
}

# 函数的入口类
function main() {
  initEnv 
  loadExtJdkTool
  startJavaComponent
  checkComponentHealthy
}

dir=`dirname $0`
dir=`cd $dir; pwd`

main $dir
