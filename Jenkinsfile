pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh "mvn clean package"
            }
        }

        stage('Deploy to Tomcat') {
            steps {
                script {
                    def tomcatUrl = 'http://localhost:8085'
                    def tomcatUser = 'tomcat'
                    def tomcatPassword = 'tomcat'
                    def war = sh(returnStdout: true, script: 'find . -name "*.war"').trim()

                    sh "curl -v --user ${tomcatUser}:${tomcatPassword} --upload-file ${war} '${tomcatUrl}/manager/text/deploy?path=/vinyl-app'"
                }
            }
        }
    }

    post {
        success {
            echo 'Success!'
        }
        failure {
            echo 'Fail!'
        }
    }
}
