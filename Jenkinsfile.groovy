pipeline {
    agent any
    options {
        disableConcurrentBuilds()
    }
    stages {
        stage('Build and deploy artifact'){
            agent {
                docker {
                    image 'blinfo/maven:3.6.1-jdk-11'
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
