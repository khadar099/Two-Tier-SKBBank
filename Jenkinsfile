pipeline {
    agent any

    stages {
        stage('Git Checkout') {
            steps {
                script {
                    git branch: 'sending-email', url: 'https://github.com/khadar099/Two-Tier-SKBBank.git'
                }
            }
        }

        stage('Maven Build') {
            steps {
                script {
                    sh 'mvn clean install'
                }
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }

        stage('Docker Image Build') {
            steps {
                sh 'docker image build -t skbbank:v.$BUILD_NUMBER .'
            }
        }

        stage('Tag Docker Image') {
            steps {
                sh 'docker image tag skbbank:v.$BUILD_NUMBER khadar3099/skbbank:v.$BUILD_NUMBER'
            }
        }

        stage('Push Docker Image to DockerHub') {
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

        stage('Deploy Docker Image') {
            steps {
                sh '''
                    docker ps -q -f name=skbbank-container && docker stop skbbank-container && docker rm skbbank-container || echo "skbbank-container Container not found or already stopped."
                    docker run -d -p 8082:8082 --name skbbank-container khadar3099/skbbank:v.$BUILD_NUMBER
                '''
            }
        }
    }

    post {
        success {
            emailext(
                subject: "SUCCESS: Job '${env.JOB_NAME} [#${env.BUILD_NUMBER}]'",
                body: """<p>Good news! The build was successful.</p>
                         <p>Project: ${env.JOB_NAME}</p>
                         <p>Build Number: ${env.BUILD_NUMBER}</p>
                         <p>Build URL: <a href="${env.BUILD_URL}">${env.BUILD_URL}</a></p>""",
                mimeType: 'text/html',
                to: 'khadar3099@gmail.com'
            )
        }

        failure {
            emailext(
                subject: "FAILURE: Job '${env.JOB_NAME} [#${env.BUILD_NUMBER}]'",
                body: """<p>Unfortunately, the build failed.</p>
                         <p>Project: ${env.JOB_NAME}</p>
                         <p>Build Number: ${env.BUILD_NUMBER}</p>
                         <p>Build URL: <a href="${env.BUILD_URL}">${env.BUILD_URL}</a></p>""",
                mimeType: 'text/html',
                to: 'khadar3099@gmail.com'
            )
        }
    }
}
