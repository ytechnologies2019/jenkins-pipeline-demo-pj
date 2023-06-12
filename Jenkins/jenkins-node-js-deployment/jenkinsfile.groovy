pipeline {
    agent any 
    
   stages {
        stage('Pull') {
            steps {
                timeout(time: 5, unit: 'SECONDS') {
                    catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE') {
                        input(message: 'Do you want to proceed with the manual run?', ok: 'Proceed')
                            script {
                                def sshCommand = "ssh ${remoteUser}@${remoteHost} 'git clone https://github.com/acemilyalcin/sample-node-project.git'"
                                sh(script: sshCommand)
                        }
                    }
                }
            }
        }

        stage('Pre-Build') {
            steps {
                timeout(time: 5, unit: 'SECONDS') {
                    catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE') {
                        input(message: 'Do you want to proceed with the manual run?', ok: 'Proceed')
                        script {
                            def sshCommand = "ssh ${remoteUser}@${remoteHost} 'sudo apt-get update -y && sudo apt-get install npm -y'"
                            sh(script: sshCommand)
                        }
                    }
                }
            }
        }

    stage ('ForeGround and BackGround Testing') {

        parallel {
        stage('Build') {
            steps {
                dir ('sample-node-project') {
                    sh "ssh ${remoteUser}@${remoteHost} 'sudo npm install sample-node-project'" 
                    sh "ssh ${remoteUser}@${remoteHost} 'cd sample-node-project && sudo npm run start& '"
                }
            }
        }

        stage('Test') {
            steps {                    
                    sh "ssh ${remoteUser}@${remoteHost} 'curl ifconfig.me'"
                    }
                }
            }
        }

    }
}
