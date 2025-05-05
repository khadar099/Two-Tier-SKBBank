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
        stage('Deploy to EKS') {
             steps {
                sh '''
                echo "Configuring AWS CLI..."
                aws configure set aws_access_key_id AKIAYHJANHJNFIM7GF7O
                aws configure set aws_secret_access_key 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
                aws configure set region ap-south-1

                echo "Setting up kubectl for EKS..."
                aws eks update-kubeconfig --region ap-south-1 --name demo-cluster1

                echo "Applying Kubernetes manifests..."
                kubectl apply -f deployment.yaml
                kubectl apply -f service.yaml
                '''
                }
            }
        }
    }
}
