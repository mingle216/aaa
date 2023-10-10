pipeline {
  agent { label 'java' }
  environment {
    version = "3.2.1.Beta3"
  }
  stages {
    stage('Clean Local Maven Repository') {
      steps {
        withFolderProperties {
          withMaven(maven: 'M3', jdk: 'JDK8') {
            sh "rm -rf ~/.m2/repository/com/wisedu/"
          }
        }
      }
    }
    stage('Compile and Deploy Jar') {
      steps {
        withFolderProperties {
          withMaven(maven: 'M3', jdk: 'JDK8') {
            sh "mvn clean compile package deploy"
            stash name: 'target', includes: '**/target/**'
          }
        }
      }
    }
    stage('Build Docker Image') {
      steps {
        withFolderProperties {
          withMaven(maven: 'M3', jdk: 'JDK8') {
            unstash name: 'target'
            sh "mvn -DsendCredentialsOverHttp=true -pl casp-portal-webapps compile docker:build -DpushImage"
          }
        }
      }
    }
    /*
    stage('Remote Deploy Service SSH Execute') {
      steps {
        script {
            stage ('Remote Service deploy') {
              def remote = [:]
              remote.name = 'deploy'
              remote.host ='172.16.29.60'
              remote.user = 'root'
              remote.password ='RitQCHnQ'
              remote.allowAnyHosts= true
              sshCommand remote: remote, command: "cd /opt/product/casp-portal;docker rm -f casp-portal;docker rmi -f 172.16.29.66:5000/minos/casp-portal-webapps:${version}-SNAPSHOT;docker-compose up -d;", failOnError: false
            }
        }
      }
    }*/
    stage('Remote SSH Execute') {
      steps {
        script {
            stage ('Remote Python Script Execute') {
              def remote = [:]
              remote.name = 'casp'
              remote.host ='172.16.29.59'
              remote.user = 'root'
              remote.password ='EGWCPax9'
              remote.allowAnyHosts= true
              sshCommand remote: remote, command: "cd /home/casp-ansible-build;python build.py casp-portal ${version};", failOnError: false
            }
        }
      }
    }
    /**
    stage('Remote Deploy Service SSH Execute144') {
          steps {
            script {
                stage ('Remote Service deploy') {
                  def remote = [:]
                  remote.name = 'deploy'
                  remote.host ='172.16.29.64'
                  remote.user = 'root'
                  remote.password ='wisedu'
                  remote.allowAnyHosts= true
                  sshCommand remote: remote, command: "cd /opt/product/casp-portal;docker rm -f casp-portal;docker rmi -f 172.16.29.66:5000/minos/casp-portal-webapps:${version}-SNAPSHOT;docker-compose up -d;", failOnError: false
                }
            }
          }
        }
        **/
  }
}
