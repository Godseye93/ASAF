pipeline {
    agent any

    tools {
        jdk 'JDK 11'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                dir('backend/ASAF') {
                    // gradlew 파일에 실행 권한 부여
                    sh 'chmod +x gradlew'
                    
                    sh './gradlew clean build' // 'gradlew' 대신 './gradlew' 사용
                }
            }
        }

        stage('Test') {
            steps {
                dir('backend/ASAF') {
                    // gradlew 파일에 실행 권한 부여
                    sh 'chmod +x gradlew'
                    
                    sh './gradlew test' // 'gradlew' 대신 './gradlew' 사용
                }
            }
        }

        stage('Deploy') {
            steps {
                sshagent(['jenkin-ssh']) {
                    sh 'scp S09P12D103/backend/ASAF/build/libs/*.jar ssafy@i9d103.p.ssafy.io:/home/ubuntu/' // 'bat' 대신 'sh' 명령어 사용
                }

                sshagent(['jenkin-ssh']) {
                    sh 'ssh ssafy@i9d103.p.ssafy.io "nohup java -jar ASAF-0.0.1-SNAPSHOT.jar &"' // 'bat' 대신 'sh' 명령어 사용
                }
            }
        }
    }
}
