pipeline {
    agent any
    options {
        disableConcurrentBuilds()
    }
    stages {
        stage('Build and deploy artifact'){
            agent {
                docker {
                    image 'docker.repos.blinfo.se/blinfo/maven:3.8.5-jdk-17'
                    args '-u root'
                }
            }
            steps {
                sh "mvn clean package deploy -DskipITs"
            }
        }
    }
    post {
        failure {
            emailext body: '${DEFAULT_CONTENT}', recipientProviders: [[$class: 'DevelopersRecipientProvider'], [$class: 'RequesterRecipientProvider']], subject: '${DEFAULT_SUBJECT}'
        }  
    }
}
