import jenkins.model.*
import hudson.model.Node.Mode
import hudson.slaves.*
import hudson.security.*
import javaposse.jobdsl.plugin.GlobalJobDslSecurityConfiguration

// Master node executors
Jenkins.instance.setNumExecutors(5)

// Creating nodes
def allNodes = ["ruby1","ruby2","python1","maven1","maven2","maven3"]
String agentHome = "/var/jenkins_home"
String agentExecutors = "2"

allNodes.each {
  DumbSlave dumb = new DumbSlave(it,  // Agent name, usually matches the host computer's machine name
          it,                         // Agent description
          agentHome,                  // Workspace on the agent's computer
          agentExecutors,             // Number of executors
          Mode.EXCLUSIVE,             // "Usage" field, EXCLUSIVE is "only tied to node", NORMAL is "any"
          it,                         // Labels
          new JNLPLauncher(true),     // Launch strategy, JNLP is the Java Web Start setting services use
          RetentionStrategy.INSTANCE) // Is the "Availability" field and INSTANCE means "Always"
  Jenkins.instance.addNode(dumb)
  println "Agent '$it' created with $agentExecutors executors and home '$agentHome'"
  File file = new File("/var/jenkins_home/"+it+".txt")
  file.write jenkins.model.Jenkins.getInstance().getComputer(it).getJnlpMac()
  println it+ " : "+jenkins.model.Jenkins.getInstance().getComputer(it).getJnlpMac()
}

// Disable Job DSL script approval
GlobalConfiguration.all().get(GlobalJobDslSecurityConfiguration.class).useScriptSecurity=false
GlobalConfiguration.all().get(GlobalJobDslSecurityConfiguration.class).save()

// Enable Security Realm
def instance = Jenkins.getInstance()
def hudsonRealm = new HudsonPrivateSecurityRealm(false)
hudsonRealm.createAccount("admin","admin")
instance.setSecurityRealm(hudsonRealm)
def strategy = new hudson.security.FullControlOnceLoggedInAuthorizationStrategy()
strategy.setAllowAnonymousRead(false)
instance.setAuthorizationStrategy(strategy)
instance.save()

