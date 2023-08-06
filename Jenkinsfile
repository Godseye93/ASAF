pipeline {
    agent any

    tools {
        jdk 'Java'
        gradle 'Gradle'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh 'gradle clean build'
            }
        }

        stage('Test') {
            steps {
                sh 'gradle test'
            }
        }

        stage('Deploy') {
            steps {
                // 서버에 파일을 전송할 때는 SSH를 사용할 수 있습니다.
                sshagent(['your-ssh-credentials']) {
                    sh 'scp S09P12D103/backend/ASAF/build/libs/*.jar ssafy@i9d103.p.ssafy.io:/home/ubuntu/'
                }

                // 원격 서버에서 JAR 파일을 실행합니다.
                sshagent(['your-ssh-credentials']) {
                    sh 'ssh ssafy@i9d103.p.ssafy.io "nohup java -jar ASAF-0.0.1-SNAPSHOT.jar &"'
                }
            }
        }
    }
}
