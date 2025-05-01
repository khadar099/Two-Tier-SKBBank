pipeline {
    
    agent any 
    
    stages {
        stage('Git Checkout'){
            steps{
                script{
                    git branch: 'develop', url: 'https://github.com/khadar099/Two-Tier-SKBBank.git'
                    }
                }
            }
        stage('Maven build') {
            
            steps {
                
                script{
                    
                    sh 'mvn clean install'
                }
            }
        }
        stage('test') {
            steps {
                sh 'mvn test'
            }
        }
        stage('Docker image  build stage') {
            steps {
                sh 'docker image build -t skbbank:v.$BUILD_NUMBER .'
            }
        }
        stage('Tag docker image') {
            steps {
                sh 'docker image tag skbbank:v.$BUILD_NUMBER khadar3099/skbbank:v.$BUILD_NUMBER'
                }
        }
       stage ('push docker image to  dockerhub') {
            steps {
                script {
                   withCredentials([string(credentialsId: 'dockerhub-password', variable: 'dockerhub_psd')]) {
                        sh '''
                        docker login -u khadar3099 -p ${dockerhub_psd}
                        docker image push khadar3099/skbbank:v.$BUILD_NUMBER
                        docker rmi skbbank:v.$BUILD_NUMBER
                        docker rmi khadar3099/skbbank:v.$BUILD_NUMBER
                        ''' 
                        }
                    }
                }
            }
        stage('deploy docker image') {
            steps {
                 sh '''
                 docker ps -q -f name=skbbank-container && docker stop skbbank-container && docker rm skbbank-container || echo "skbbank-container Container not found or already stopped."
                 docker run -d -p 8082:8082 --name skbbank-container khadar3099/skbbank:v.$BUILD_NUMBER
                 '''
            }
        }
        post {
        success {
            emailext(
                subject: "SUCCESS: Job '${env.JOB_NAME} [#${env.BUILD_NUMBER}]'",
                body: "Good news! The build was successful.\n\nCheck it here: ${env.BUILD_URL}",
                to: 'your_email@example.com'
            )
        }
        failure {
            emailext(
                subject: "FAILURE: Job '${env.JOB_NAME} [#${env.BUILD_NUMBER}]'",
                body: "Unfortunately, the build failed.\n\nCheck details here: ${env.BUILD_URL}",
                to: 'your_email@example.com'
            )
            }
        }
    }
}
