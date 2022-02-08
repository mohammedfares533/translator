pipeline {

	agent any

	stages {

		stage("Clone & Maven Build") {

			environment {
				JAVA_TOOL_OPTIONS = "-Duser.home=/var/maven"
			}

			agent {
				docker {
					image "maven:3.8.3-openjdk-8"
					args "-v /tmp/maven:/var/maven/.m2 -e MAVEN_CONFIG=/var/maven/.m2"
				}
			}
			steps{

				echo 'Start clone ...'
				git branch: 'master', url: 'https://github.com/mohammedfares533/translator-gateway.git'
				echo 'End clone ...'

				echo 'Start maven build ...'
				sh "mvn clean install -Dmaven.test.skip.exec"
				echo 'Finish maven build ...'
			}
		}

		stage("Stop and remove old build") {
			agent { label 'master' }
			steps {
				echo 'Start stop and remove old build ...'
				sh """
					(docker container stop translator-gateway && docker container rm translator-gateway && docker image rm translator-gateway) || true
				"""
				echo 'End stop and remove old build ...'
			}
		}

		stage("Docker build") {
			agent { label 'master' }
			steps {
				echo 'Start building the application ...'
				echo 'workspace directory'
				sh """
					pwd && docker build -t translator-gateway .
				"""
				echo 'End building the application ...'
			}
		}

		stage("Docker run") {
			agent { label 'master' }
			steps {
				echo 'Start running the application ...'
				sh """
					docker run -p 9025:9025 -e EUREKA_SERVER=http://eureka:8762/eureka -e ACTIVE_PROFILE=dev -d --name translator-gateway translator-gateway
				"""
				echo 'End running the application ...'
			}
		}

		stage("Docker connect build to translator-network") {
			agent { label 'master' }
			steps {
				echo 'Start connect container to translator-network ...'
				sh """
					((docker network create translator-network) || true) && docker network connect translator-network translator-gateway
				"""
				echo 'End connect container to translator-network ...'
			}
		}
	}
}