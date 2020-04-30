def exec(cmd) {
    if (Boolean.valueOf(env.UNIX)) {
        sh cmd
    }
    else {
        bat cmd
   }
}

pipeline {
   agent any

   environment {
        COMPOSE_FILE = "docker-compose.yml"
        UNIX = isUnix()
    }

    tools {
        //git 'git'
        jdk 'jdk-8'
        maven 'apache-maven-3.6.3'
        //docker 'docker'
    }

   stages {
        stage('Cloning from Github repo') {
            steps {
                echo 'Cloning..'
                // Get some code from a GitHub repository
                // git 'https://github.com/richkmeli/Richkware-Manager-Server.git'
                git branch: "spring", url: 'https://github.com/richkmeli/Richkware-Manager-Server.git'
            }
        }
         stage("Deploy env pre-build") {
            steps {
                exec("docker-compose up -d db")
                //waitUntilServicesReady
            }
        }
        stage('Build') {
            steps {
                echo 'Building..'

                // Run Maven on a Unix agent. use sh
                exec("mvn clean install")

            }
            post {
                // If Maven was able to run the tests, even if some of the test
                // failed, record the test results and archive the jar file.
                success {
                    junit '**/target/surefire-reports/TEST-*.xml'
                    archiveArtifacts 'target/*.war'
                }

            }
        }
        stage("Deploy env post-build") {
            steps {
                exec("docker-compose up -d web")
                //waitUntilServicesReady
            }
        }
        stage('Test') {
            steps {
                echo 'Testing...'
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying....'
            }
        }
   }
}

