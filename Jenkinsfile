pipeline {
    environment {
        registry = "visheshawasthi/football-service"
        registryCredential = 'DockerHub'
    }
    agent any
    stages {
        stage('Checkout and Build') {
            steps {
                git 'https://github.com/vishesh-awasthi/football-service.git'
                sh "mvn -DskipTests clean install"
            }
        }
        stage('Build Docker Image and Push') {
            steps {
                script {
                    docker.withRegistry('', registryCredential) {
                        def image = docker.build registry
                        image.push()
                    }
                }
            }
        }
    }
}