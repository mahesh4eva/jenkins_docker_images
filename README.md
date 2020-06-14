# jenkins_docker_images
jenkins_docker_images

Setup: 

This article explains in detail how to setup Jenkins from scratch on a docker platform.
You will be able to control the entire setup via a bash script provided.
This article is for those people who are looking to set up a Jenkins with minimal infrastructure available. This just uses Docker and nothing else.
Things we can control while setup.
1.	Creating Docker volumes
2.	Creating a Docker network
3.	Building Jenkins's image locally.
4.	Setting up Security Realm via Groovy Script
5.	Creating Nodes via Groovy Script
6.	Creating a seed job
7.	Building different JNLP nodes (ruby, maven and python)
8.	Attaching these JNLP nodes to the Primary node.
9.	Jenkin's Job DSL scripts to create jobs.
I have taken standard Jenkin Docker images to build this solution, with customization added in the groovy script.
Project is available at https://github.com/mahesh4eva/jenkins_docker_images
Clone the above project and let us go through what is available.
Under directory, Jenkins_docker_images/jenkins/primary you can see the following files:
•	Dockerfile: This is the file Docker utilizes to build the image.
•	plugins.txt: This file contains the list of plugins you want to install in the image.
•	seed_job.xml: Initial seed job which creates actual Jenkin's jobs.
•	customization.groovy: This file has the customization for your Jenkins during building the image.
Customization
Open the customization.groovy file and you have a couple of customization available. If you want any further customization, please add the code here and it will be available for you.
If you want to have more/fewer executors for your primary node, modify the below line at the file and save.
Jenkins.instance.setNumExecutors(5) : Creates 5 executors
 




If you need to modify the JNLP nodes created, add or remove the elements in allNodes array variable.
def allNodes = ["ruby1","ruby2","python1","maven1","maven2","maven3"]
Elements must be unique. The above list will create 6 nodes with name as element.
 








Other settings to create nodes are available in the same script.
If you want to update the user credentials, modify the line below
hudsonRealm.createAccount("admin","admin")
Plugins
You can list all the plugins you need for your Jenkins under plugins.txt file.
This file contains a list with name of the plugin and also optionally you can specify the version of the plugin you need. I would recommend using version numbers with the plugin name.
Seed Job
The purpose of the seed job is to create an initial job which then creates other jobs using the Job DSL plugin. The advantage of using Job DSL scripts is that your jobs will be always pristine and do not need to worry about people modifying the jobs.
Primary Nodes
If you observe, the project contains the Replicas directory which has 3 nodes. In case you need a custom replica node, define here. The current nodes are directly taken from Jenkins repo. They just contain the Dockerfile to build the image locally.
Deployment
In usual practice, you run Jenkin's container and then find the replica node's auth token and start the node containers providing the token. However, the included deploy_jenkins script handles the auth tokens and connecting the nodes for us.
If you open deploy_jenkins script, the first 5 lines are the controls you have to set up the system with your own naming conventions.
Update your names and port numbers as you wish. However, variable REPLICAS must match the REPLICAS groovy customization file.
REPLICAS=(ruby1 ruby2 python1 maven1 maven2 maven3) - deploy_jenkins file
def allNodes = ["ruby1","ruby2","python1","maven1","maven2","maven3"] - customization.groovy file
If you are adding a new replica node in one file, make sure you make a similar change in the other file too.
After you are satisfied with all changes, just execute the script.
docker_images/deploy_jenkins
Jenkins Ready
After successful deployment, you can navigate to http://localhost:8080 and provide username and password (default admin/admin)
You should be able to see all nodes connected and a job with name seed_job exists.
Run the seed_job and observe the sample jobs created.
 

