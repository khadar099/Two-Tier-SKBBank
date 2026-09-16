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

**Steps to follow**
**1. Install required plugins**

Email Extension Plugin: Provides advanced email notification features.

Mailer Plugin: Handles basic email notifications.​

**2. First open port 465 in the jenkins server**
![image](https://github.com/user-attachments/assets/62f859a5-7308-4477-9799-37270b8ef219)

3. Create password in gmail
   Go to Manage your google account in gmail
   ![image](https://github.com/user-attachments/assets/4e6050fb-b5de-4b85-abdd-e06365590869)

   Then click on security

   ![image](https://github.com/user-attachments/assets/12eaf093-908a-41d1-b799-ee3f26a88ec9)

   click on two step verification

   



