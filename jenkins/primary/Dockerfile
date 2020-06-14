FROM jenkins/jenkins:lts
MAINTAINER mahesh4eva@gmail.com

COPY customization.groovy /usr/share/jenkins/ref/init.groovy.d/customization.groovy
COPY plugins.txt /usr/share/jenkins/ref/
COPY seed_job.xml /usr/share/jenkins/ref/jobs/seed_job/config.xml

RUN /usr/local/bin/install-plugins.sh < /usr/share/jenkins/ref/plugins.txt
