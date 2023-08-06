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
        		sh 'cp backend/ASAF/build/libs/*.jar /home/ubuntu/'

        // 백그라운드에서 앱 실행
        		sh 'sudo nohup java -jar build/libs/ASAF-0.0.1-SNAPSHOT.jar &'
   		 }
	}
    }
}
