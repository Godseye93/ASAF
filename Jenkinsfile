pipeline {
    agent any

    tools {
        jdk 'JAVA_HOME'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build and Test') {
            steps {
                dir('backend/ASAF') {
                    // gradlew 파일에 실행 권한 부여
                    sh 'chmod +x gradlew'
                    
                    sh './gradlew clean build -x test' // 'gradlew' 대신 './gradlew' 사용
                }
            }
        }

        stage('Deploy') {
       steps {
              sh 'sudo cp backend/ASAF/build/libs/*.jar /home/ubuntu/'
 // Change to target directory before running the jar file
// 앱 종료
sh 'sudo pkill -f ASAF-0.0.1-SNAPSHOT || true'

        // 백그라운드에서 앱 실행
            sh 'nohup sudo java -jar /home/ubuntu/ASAF-0.0.1-SNAPSHOT.jar &'

          }
       
   }
    }
}