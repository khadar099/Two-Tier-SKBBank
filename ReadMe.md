🧩 Prerequisites
Install Required Plugins:

Email Extension Plugin: Provides advanced email notification features.

Mailer Plugin: Handles basic email notifications.​
Jenkins Plugins
+1
Medium
+1

Configure SMTP Settings:

Navigate to Manage Jenkins > Configure System.

In the Extended E-mail Notification section:

SMTP Server: For Gmail, use smtp.gmail.com.

Use SMTP Authentication: Check this box and provide your email credentials.

Use SSL: Check this box.

SMTP Port: Set to 465.

Default Recipients: Enter the email addresses to receive notifications.

Default Subject and Default Content: Customize as needed.

Click Apply and Save.​
edureka.co
+1
Medium
+1

🛠️ Modify Your Jenkins Pipeline
Add a post section at the end of your pipeline to handle email notifications:

groovy
Copy
Edit
pipeline {
    agent any

    stages {
        // Your existing stages...
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
Replace 'your_email@example.com' with the actual recipient's email address.
